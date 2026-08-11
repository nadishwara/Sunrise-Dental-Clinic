package DAO;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nadis
 */
public class TreatmentDAO {

    public Map<String, Object> getTreatmentByAppointmentId(int appointmentId) {
        Map<String, Object> data = new HashMap<>();

        String sql = "SELECT tr.treatment_id, tr.patient_user_id, tr.treatment_name, tr.tooth_number, tr.clinical_notes, "
                + "tc.status AS tooth_status, tc.notes AS tooth_notes, "
                + "dx.xray_type, dx.file_path AS xray_file_path "
                + "FROM treatment_records tr "
                + "LEFT JOIN patient_tooth_chart tc "
                + "  ON tr.patient_user_id = tc.patient_user_id "
                + "  AND CAST(NULLIF(tr.tooth_number, '') AS UNSIGNED) = tc.tooth_number "
                + "LEFT JOIN dental_xrays dx ON tr.treatment_id = dx.treatment_id "
                + "WHERE tr.appointment_id = ? "
                + "ORDER BY tr.treatment_id DESC LIMIT 1";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int treatmentId = rs.getInt("treatment_id");
                    data.put("treatmentId", treatmentId);
                    data.put("patientUserId", rs.getInt("patient_user_id"));
                    data.put("treatmentName", rs.getString("treatment_name"));
                    data.put("toothNumber", rs.getString("tooth_number"));
                    data.put("clinicalNotes", rs.getString("clinical_notes"));
                    data.put("toothStatus", rs.getString("tooth_status"));
                    data.put("toothNotes", rs.getString("tooth_notes"));
                    data.put("xrayType", rs.getString("xray_type"));
                    data.put("xrayFilePath", rs.getString("xray_file_path"));
                    data.put("prescriptions", getPrescriptionsByTreatmentId(treatmentId, conn));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    private List<Object[]> getPrescriptionsByTreatmentId(int treatmentId, Connection conn) throws SQLException {
        List<Object[]> prescriptions = new ArrayList<>();
        String sql = "SELECT medication_name, dosage, duration, instructions FROM prescriptions WHERE treatment_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, treatmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prescriptions.add(new Object[]{
                        rs.getString("medication_name"),
                        rs.getString("dosage"),
                        rs.getString("duration"),
                        rs.getString("instructions")
                    });
                }
            }
        }
        return prescriptions;
    }

    public boolean saveFullTreatmentRecord(
            int appointmentId,
            int patientUserId,
            int dentistUserId,
            String treatmentName,
            String toothNumberStr,
            String clinicalNotes,
            int toothNoInt,
            String toothStatus,
            String toothNotes,
            List<Object[]> prescriptionsList,
            String xrayType,
            String xrayFilePath
    ) {
        Connection conn = null;

        String insertTreatmentSql = "INSERT INTO treatment_records "
                + "(appointment_id, patient_user_id, dentist_user_id, treatment_name, tooth_number, clinical_notes) "
                + "VALUES (?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE treatment_name = VALUES(treatment_name), tooth_number = VALUES(tooth_number), clinical_notes = VALUES(clinical_notes)";

        String updateAppointmentSql = "UPDATE appointments SET status = 'COMPLETED' WHERE appointment_id = ?";

        String upsertToothChartSql = "INSERT INTO patient_tooth_chart "
                + "(patient_user_id, tooth_number, status, notes) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE status = VALUES(status), notes = VALUES(notes)";

        String deleteOldPrescriptionsSql = "DELETE FROM prescriptions WHERE treatment_id = ?";

        String insertPrescriptionSql = "INSERT INTO prescriptions "
                + "(treatment_id, medication_name, dosage, duration, instructions) VALUES (?, ?, ?, ?, ?)";

        String insertXraySql = "INSERT INTO dental_xrays "
                + "(patient_user_id, treatment_id, xray_type, file_path, taken_at) VALUES (?, ?, ?, ?, NOW()) "
                + "ON DUPLICATE KEY UPDATE xray_type = VALUES(xray_type), file_path = VALUES(file_path)";

        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            int generatedTreatmentId = -1;

            try (PreparedStatement psTreatment = conn.prepareStatement(insertTreatmentSql, Statement.RETURN_GENERATED_KEYS)) {
                psTreatment.setInt(1, appointmentId);
                psTreatment.setInt(2, patientUserId);
                psTreatment.setInt(3, dentistUserId);
                psTreatment.setString(4, treatmentName);
                psTreatment.setString(5, toothNumberStr);
                psTreatment.setString(6, clinicalNotes);
                psTreatment.executeUpdate();

                try (ResultSet rsKeys = psTreatment.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        generatedTreatmentId = rsKeys.getInt(1);
                    }
                }
            }

            if (generatedTreatmentId == -1) {
                String selectTreatmentIdSql = "SELECT treatment_id FROM treatment_records WHERE appointment_id = ?";
                try (PreparedStatement psSelect = conn.prepareStatement(selectTreatmentIdSql)) {
                    psSelect.setInt(1, appointmentId);
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (rs.next()) {
                            generatedTreatmentId = rs.getInt("treatment_id");
                        }
                    }
                }
            }

            if (generatedTreatmentId == -1) {
                throw new SQLException("Failed to retrieve treatment_id.");
            }

            try (PreparedStatement psAppUpdate = conn.prepareStatement(updateAppointmentSql)) {
                psAppUpdate.setInt(1, appointmentId);
                psAppUpdate.executeUpdate();
            }

            if (toothNoInt > 0) {
                try (PreparedStatement psTooth = conn.prepareStatement(upsertToothChartSql)) {
                    psTooth.setInt(1, patientUserId);
                    psTooth.setInt(2, toothNoInt);
                    psTooth.setString(3, toothStatus);
                    psTooth.setString(4, toothNotes);
                    psTooth.executeUpdate();
                }
            }

            if (prescriptionsList != null) {
                try (PreparedStatement psDelete = conn.prepareStatement(deleteOldPrescriptionsSql)) {
                    psDelete.setInt(1, generatedTreatmentId);
                    psDelete.executeUpdate();
                }

                if (!prescriptionsList.isEmpty()) {
                    try (PreparedStatement psPrescription = conn.prepareStatement(insertPrescriptionSql)) {
                        for (Object[] row : prescriptionsList) {
                            psPrescription.setInt(1, generatedTreatmentId);
                            psPrescription.setString(2, row[0].toString());
                            psPrescription.setString(3, row[1] != null ? row[1].toString() : "");
                            psPrescription.setString(4, row[2] != null ? row[2].toString() : "");
                            psPrescription.setString(5, row[3] != null ? row[3].toString() : "");
                            psPrescription.addBatch();
                        }
                        psPrescription.executeBatch();
                    }
                }
            }

            if (xrayFilePath != null && !xrayFilePath.trim().isEmpty()) {
                try (PreparedStatement psXray = conn.prepareStatement(insertXraySql)) {
                    psXray.setInt(1, patientUserId);
                    psXray.setInt(2, generatedTreatmentId);
                    psXray.setString(3, xrayType);
                    psXray.setString(4, xrayFilePath);
                    psXray.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

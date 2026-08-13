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

public class TreatmentDAO {

    public Map<String, Object> getTreatmentByAppointmentId(int appointmentId) {
        Map<String, Object> data = new HashMap<>();
        String sql = "SELECT * FROM treatment_records WHERE appointment_id = ? ORDER BY treatment_id DESC LIMIT 1";

        try (Connection conn = DBConnection.getInstance().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int treatmentId = rs.getInt("treatment_id");
                    data.put("treatmentId", treatmentId);
                    data.put("patientUserId", rs.getInt("patient_user_id"));
                    data.put("treatmentName", rs.getString("treatment_name"));
                    data.put("toothNumber", rs.getString("tooth_number"));
                    data.put("clinicalNotes", rs.getString("clinical_notes"));
                    data.put("serviceCost", rs.getDouble("service_cost"));
                    // prescriptions කොටස ඉවත් කර හිස් List එකක් පවරයි
                    data.put("prescriptions", new ArrayList<Object[]>());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
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
                + "(appointment_id, patient_user_id, dentist_user_id, treatment_name, tooth_number, clinical_notes, service_cost) "
                + "VALUES (?, ?, ?, ?, ?, ?, COALESCE((SELECT base_price FROM treatment_services WHERE service_name = ? LIMIT 1), 0.00))";

        String updateAppointmentSql = "UPDATE appointments SET status = 'COMPLETED' WHERE appointment_id = ?";

        String upsertToothChartSql = "INSERT INTO patient_tooth_chart "
                + "(patient_user_id, tooth_number, status, notes) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE status = VALUES(status), notes = VALUES(notes)";

        String insertXraySql = "INSERT INTO dental_xrays "
                + "(patient_user_id, treatment_id, xray_type, file_path, xray_cost, taken_at) VALUES (?, ?, ?, ?, "
                + "CASE "
                + "  WHEN ? = 'Periapical X-Ray' THEN 1000.00 "
                + "  WHEN ? = 'Bitewing X-Ray' THEN 1200.00 "
                + "  WHEN ? = 'Panoramic X-Ray (OPG)' THEN 3500.00 "
                + "  WHEN ? = 'Occlusal X-Ray' THEN 1500.00 "
                + "  WHEN ? = 'Cephalometric X-Ray' THEN 4000.00 "
                + "  WHEN ? = 'Cone Beam Computed Tomography (CBCT)' THEN 8000.00 "
                + "  ELSE 1000.00 "
                + "END, NOW())";

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
                psTreatment.setString(7, treatmentName);
                psTreatment.executeUpdate();

                try (ResultSet rsKeys = psTreatment.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        generatedTreatmentId = rsKeys.getInt(1);
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

            if (xrayFilePath != null && !xrayFilePath.trim().isEmpty()) {
                try (PreparedStatement psXray = conn.prepareStatement(insertXraySql)) {
                    psXray.setInt(1, patientUserId);
                    psXray.setInt(2, generatedTreatmentId);
                    psXray.setString(3, xrayType);
                    psXray.setString(4, xrayFilePath);
                    psXray.setString(5, xrayType);
                    psXray.setString(6, xrayType);
                    psXray.setString(7, xrayType);
                    psXray.setString(8, xrayType);
                    psXray.setString(9, xrayType);
                    psXray.setString(10, xrayType);
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
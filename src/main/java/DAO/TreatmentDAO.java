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
        String sql = "SELECT t.*, p.full_name AS patient_name, p.email AS patient_email, " +
                     "p.phone AS patient_phone, p.dob AS patient_dob, p.address AS patient_address " +
                     "FROM treatment_records t " +
                     "LEFT JOIN appointments a ON t.appointment_id = a.appointment_id " +
                     "LEFT JOIN patients p ON a.patient_id = p.patient_id " +
                     "WHERE t.appointment_id = ? ORDER BY t.treatment_id DESC LIMIT 1";

        try (Connection conn = DBConnection.getInstance().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int treatmentId = rs.getInt("treatment_id");
                    int patientUserId = rs.getInt("patient_user_id");
                    String toothNumberStr = rs.getString("tooth_number");

                    data.put("treatmentId", treatmentId);
                    data.put("patientUserId", patientUserId);
                    data.put("treatmentName", rs.getString("treatment_name"));
                    data.put("toothNumber", toothNumberStr);
                    data.put("clinicalNotes", rs.getString("clinical_notes"));
                    data.put("serviceCost", rs.getDouble("service_cost"));

                    data.put("patientName", rs.getString("patient_name"));
                    data.put("patientEmail", rs.getString("patient_email"));
                    data.put("patientPhone", rs.getString("patient_phone"));
                    data.put("patientDob", rs.getDate("patient_dob"));
                    data.put("patientAddress", rs.getString("patient_address"));

                    List<Object[]> prescriptions = new ArrayList<>();
                    String prescSql = "SELECT medication_name, dosage, duration, instructions FROM prescriptions WHERE treatment_id = ?";
                    try (PreparedStatement psPresc = conn.prepareStatement(prescSql)) {
                        psPresc.setInt(1, treatmentId);
                        try (ResultSet rsPresc = psPresc.executeQuery()) {
                            while (rsPresc.next()) {
                                prescriptions.add(new Object[]{
                                    rsPresc.getString("medication_name"),
                                    rsPresc.getString("dosage"),
                                    rsPresc.getString("duration"),
                                    rsPresc.getString("instructions")
                                });
                            }
                        }
                    }
                    data.put("prescriptions", prescriptions);

                    if (toothNumberStr != null && !toothNumberStr.isEmpty()) {
                        try {
                            int toothNo = Integer.parseInt(toothNumberStr);
                            String toothSql = "SELECT status, notes FROM patient_tooth_chart WHERE patient_user_id = ? AND tooth_number = ?";
                            try (PreparedStatement psTooth = conn.prepareStatement(toothSql)) {
                                psTooth.setInt(1, patientUserId);
                                psTooth.setInt(2, toothNo);
                                try (ResultSet rsTooth = psTooth.executeQuery()) {
                                    if (rsTooth.next()) {
                                        data.put("toothStatus", rsTooth.getString("status"));
                                        data.put("toothNotes", rsTooth.getString("notes"));
                                    }
                                }
                            }
                        } catch (NumberFormatException ignored) {}
                    }

                    String xraySql = "SELECT xray_type, file_path FROM dental_xrays WHERE treatment_id = ? ORDER BY xray_id DESC LIMIT 1";
                    try (PreparedStatement psXray = conn.prepareStatement(xraySql)) {
                        psXray.setInt(1, treatmentId);
                        try (ResultSet rsXray = psXray.executeQuery()) {
                            if (rsXray.next()) {
                                data.put("xrayType", rsXray.getString("xray_type"));
                                data.put("xrayFilePath", rsXray.getString("file_path"));
                            }
                        }
                    }
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

        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            int existingTreatmentId = -1;
            String checkExistingSql = "SELECT treatment_id FROM treatment_records WHERE appointment_id = ?";
            try (PreparedStatement psCheck = conn.prepareStatement(checkExistingSql)) {
                psCheck.setInt(1, appointmentId);
                try (ResultSet rsCheck = psCheck.executeQuery()) {
                    if (rsCheck.next()) {
                        existingTreatmentId = rsCheck.getInt("treatment_id");
                    }
                }
            }

            int resolvedUserId = patientUserId;
            if (resolvedUserId <= 0) {
                String fetchUserIdSql = "SELECT patient_id AS valid_user_id FROM appointments WHERE appointment_id = ?";
                try (PreparedStatement psFetchUser = conn.prepareStatement(fetchUserIdSql)) {
                    psFetchUser.setInt(1, appointmentId);
                    try (ResultSet rsUser = psFetchUser.executeQuery()) {
                        if (rsUser.next() && rsUser.getInt("valid_user_id") > 0) {
                            resolvedUserId = rsUser.getInt("valid_user_id");
                        }
                    }
                }
            }

            int generatedTreatmentId = -1;

            if (existingTreatmentId != -1) {
                generatedTreatmentId = existingTreatmentId;
                String updateTreatmentSql = "UPDATE treatment_records SET "
                        + "patient_user_id = ?, dentist_user_id = ?, treatment_name = ?, tooth_number = ?, clinical_notes = ?, "
                        + "service_cost = COALESCE((SELECT base_price FROM treatment_services WHERE service_name = ? LIMIT 1), 0.00) "
                        + "WHERE treatment_id = ?";
                
                try (PreparedStatement psUpdate = conn.prepareStatement(updateTreatmentSql)) {
                    if (resolvedUserId > 0) {
                        psUpdate.setInt(1, resolvedUserId);
                    } else {
                        psUpdate.setNull(1, java.sql.Types.INTEGER);
                    }
                    psUpdate.setInt(2, dentistUserId);
                    psUpdate.setString(3, treatmentName);
                    psUpdate.setString(4, toothNumberStr);
                    psUpdate.setString(5, clinicalNotes);
                    psUpdate.setString(6, treatmentName);
                    psUpdate.setInt(7, generatedTreatmentId);
                    psUpdate.executeUpdate();
                }

                String deleteOldPresc = "DELETE FROM prescriptions WHERE treatment_id = ?";
                try (PreparedStatement psDelPresc = conn.prepareStatement(deleteOldPresc)) {
                    psDelPresc.setInt(1, generatedTreatmentId);
                    psDelPresc.executeUpdate();
                }

            } else {
                String insertTreatmentSql = "INSERT INTO treatment_records "
                        + "(appointment_id, patient_user_id, dentist_user_id, treatment_name, tooth_number, clinical_notes, service_cost) "
                        + "VALUES (?, ?, ?, ?, ?, ?, COALESCE((SELECT base_price FROM treatment_services WHERE service_name = ? LIMIT 1), 0.00))";

                try (PreparedStatement psTreatment = conn.prepareStatement(insertTreatmentSql, Statement.RETURN_GENERATED_KEYS)) {
                    psTreatment.setInt(1, appointmentId);
                    if (resolvedUserId > 0) {
                        psTreatment.setInt(2, resolvedUserId);
                    } else {
                        psTreatment.setNull(2, java.sql.Types.INTEGER);
                    }
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
            }

            if (generatedTreatmentId == -1) {
                throw new SQLException("Failed to retrieve or generate treatment_id.");
            }

            String updateAppointmentSql = "UPDATE appointments SET status = 'COMPLETED' WHERE appointment_id = ?";
            try (PreparedStatement psAppUpdate = conn.prepareStatement(updateAppointmentSql)) {
                psAppUpdate.setInt(1, appointmentId);
                psAppUpdate.executeUpdate();
            }

            String fetchManualPatientSql = "SELECT patient_id FROM appointments WHERE appointment_id = ?";
            int actualPatientId = -1;
            try (PreparedStatement psGetPat = conn.prepareStatement(fetchManualPatientSql)) {
                psGetPat.setInt(1, appointmentId);
                try (ResultSet rsPat = psGetPat.executeQuery()) {
                    if (rsPat.next()) {
                        actualPatientId = rsPat.getInt("patient_id");
                    }
                }
            }

            if (toothNoInt > 0 && actualPatientId > 0) {
                String checkPatientSql = "SELECT patient_id FROM patients WHERE patient_id = ?";
                boolean patientExists = false;
                try (PreparedStatement psCheckPat = conn.prepareStatement(checkPatientSql)) {
                    psCheckPat.setInt(1, actualPatientId);
                    try (ResultSet rsCheckPat = psCheckPat.executeQuery()) {
                        if (rsCheckPat.next()) {
                            patientExists = true;
                        }
                    }
                }

                if (!patientExists) {
                    String checkUserSql = "SELECT user_id FROM users WHERE user_id = ?";
                    try (PreparedStatement psCheckUser = conn.prepareStatement(checkUserSql)) {
                        psCheckUser.setInt(1, actualPatientId);
                        try (ResultSet rsCheckUser = psCheckUser.executeQuery()) {
                            if (rsCheckUser.next()) {
                                patientExists = true;
                            }
                        }
                    }
                }

                if (patientExists) {
                    String upsertToothChartSql = "INSERT INTO patient_tooth_chart "
                            + "(patient_user_id, tooth_number, status, notes) VALUES (?, ?, ?, ?) "
                            + "ON DUPLICATE KEY UPDATE status = VALUES(status), notes = VALUES(notes)";
                    try (PreparedStatement psTooth = conn.prepareStatement(upsertToothChartSql)) {
                        psTooth.setInt(1, actualPatientId);
                        psTooth.setInt(2, toothNoInt);
                        psTooth.setString(3, toothStatus);
                        psTooth.setString(4, toothNotes);
                        psTooth.executeUpdate();
                    }
                }
            }

            if (prescriptionsList != null && !prescriptionsList.isEmpty()) {
                String insertPrescriptionSql = "INSERT INTO prescriptions "
                        + "(treatment_id, medication_name, dosage, duration, instructions) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement psPresc = conn.prepareStatement(insertPrescriptionSql)) {
                    for (Object[] presc : prescriptionsList) {
                        psPresc.setInt(1, generatedTreatmentId);
                        psPresc.setString(2, presc[0] != null ? presc[0].toString() : "");
                        psPresc.setString(3, presc[1] != null ? presc[1].toString() : "");
                        psPresc.setString(4, presc[2] != null ? presc[2].toString() : "");
                        psPresc.setString(5, presc.length > 3 && presc[3] != null ? presc[3].toString() : "");
                        psPresc.addBatch();
                    }
                    psPresc.executeBatch();
                }
            }

            if (xrayFilePath != null && !xrayFilePath.trim().isEmpty()) {
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
                try (PreparedStatement psXray = conn.prepareStatement(insertXraySql)) {
                    if (resolvedUserId > 0) {
                        psXray.setInt(1, resolvedUserId);
                    } else {
                        psXray.setNull(1, java.sql.Types.INTEGER);
                    }
                    psXray.setInt(2, generatedTreatmentId);
                    psXray.setString(3, xrayType);
                    psXray.setString(5, xrayFilePath); 
                }

                String safeXraySql = "INSERT INTO dental_xrays (patient_user_id, treatment_id, xray_type, file_path, xray_cost, taken_at) "
                        + "VALUES (?, ?, ?, ?, "
                        + "CASE ? "
                        + "  WHEN 'Periapical X-Ray' THEN 1000.00 "
                        + "  WHEN 'Bitewing X-Ray' THEN 1200.00 "
                        + "  WHEN 'Panoramic X-Ray (OPG)' THEN 3500.00 "
                        + "  WHEN 'Occlusal X-Ray' THEN 1500.00 "
                        + "  WHEN 'Cephalometric X-Ray' THEN 4000.00 "
                        + "  WHEN 'Cone Beam Computed Tomography (CBCT)' THEN 8000.00 "
                        + "  ELSE 1000.00 "
                        + "END, NOW())";
                
                try (PreparedStatement psXray = conn.prepareStatement(safeXraySql)) {
                    if (resolvedUserId > 0) {
                        psXray.setInt(1, resolvedUserId);
                    } else {
                        psXray.setNull(1, java.sql.Types.INTEGER);
                    }
                    psXray.setInt(2, generatedTreatmentId);
                    psXray.setString(3, xrayType);
                    psXray.setString(4, xrayFilePath);
                    psXray.setString(5, xrayType);
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
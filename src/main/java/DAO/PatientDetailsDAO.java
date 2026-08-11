package DAO;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class PatientDetailsDAO {

    public DefaultTableModel getPatientMedicalHistoryModel(int patientUserId) {
        String[] columnNames = {
            "Date & Time",
            "Treatment Name",
            "Treatment Doc Name",
            "Tooth No.",
            "Attending Dentist",
            "Clinical Notes"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String sql = "SELECT "
                + "  tr.treated_at, "
                + "  tr.treatment_name, "
                + "  dx.xray_type, "
                + "  tr.tooth_number, "
                + "  d.username AS dentist_name, "
                + "  tr.clinical_notes "
                + "FROM treatment_records tr "
                + "INNER JOIN appointments a ON tr.appointment_id = a.appointment_id "
                + "INNER JOIN users d ON tr.dentist_user_id = d.user_id "
                + "LEFT JOIN dental_xrays dx ON tr.treatment_id = dx.treatment_id "
                + "WHERE tr.patient_user_id = ? AND a.status = 'COMPLETED' "
                + "ORDER BY tr.treated_at DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String docType = rs.getString("xray_type");
                String docName = (docType != null && !docType.isEmpty()) ? docType : "No Document";

                String toothNo = rs.getString("tooth_number");
                if (toothNo == null || toothNo.trim().isEmpty() || toothNo.contains("--")) {
                    toothNo = "N/A";
                }

                Object[] row = {
                    rs.getTimestamp("treated_at"),
                    rs.getString("treatment_name"),
                    docName,
                    toothNo,
                    "Dr. " + rs.getString("dentist_name"),
                    rs.getString("clinical_notes") != null ? rs.getString("clinical_notes") : ""
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }

    public int getPatientIdByAppointmentDetails(int dentistId, String appointmentDate, String formattedOrRawTime, String patientName) {
        int patientId = -1;

        String query = "SELECT a.patient_id "
                + "FROM appointments a "
                + "LEFT JOIN users u ON a.patient_id = u.user_id "
                + "WHERE a.dentist_id = ? "
                + "  AND a.appointment_date = ? "
                + "  AND (u.username = ? OR CONCAT('Unknown (ID:', a.patient_id, ')') = ?) "
                + "LIMIT 1";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, dentistId);
            pstmt.setString(2, appointmentDate);
            pstmt.setString(3, patientName);
            pstmt.setString(4, patientName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    patientId = rs.getInt("patient_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patientId;
    }

    public java.util.Map<String, String> getPatientPersonalDetails(int patientUserId) {
        java.util.Map<String, String> details = new java.util.HashMap<>();

        String sql = "SELECT username, email, contact_no, address, created_at, status "
                + "FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientUserId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    details.put("name", rs.getString("username"));
                    details.put("email", rs.getString("email"));

                    String contact = rs.getString("contact_no");
                    details.put("phone", contact != null ? contact : "N/A");

                    details.put("gender_dob", "N/A");

                    String address = rs.getString("address");
                    details.put("address", address != null ? address : "N/A");

                    details.put("created_at", rs.getString("created_at"));
                    details.put("status", rs.getString("status"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }
}

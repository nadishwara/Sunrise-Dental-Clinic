/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import config.DBConnection;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import model.Appointment;

/**
 *
 * @author nadis
 */
public class AppointmentDAO {

    public boolean scheduleAppointment(int requestId, int patientUserId, int dentistUserId, int receptionistUserId, String date, String timeSlot) {

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            int validPatientId = patientUserId;
            if (validPatientId <= 0) {
                String fetchPatientSql = "SELECT patient_user_id FROM appointment_requests WHERE request_id = ?";
                try (PreparedStatement psFetch = conn.prepareStatement(fetchPatientSql)) {
                    psFetch.setInt(1, requestId);
                    try (ResultSet rs = psFetch.executeQuery()) {
                        if (rs.next()) {
                            validPatientId = rs.getInt("patient_user_id");
                        }
                    }
                }
            }

            String insertSql = "INSERT INTO appointments "
                    + "(custom_appointment_id, request_id, patient_id, dentist_id, receptionist_id, appointment_date, appointment_time, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, 'SCHEDULED')";

            String updateRequestSql = "UPDATE appointment_requests SET status = 'CONFIRMED' WHERE request_id = ?";

            String customAppId = "APP-" + (System.currentTimeMillis() % 100000);

            try (PreparedStatement ps1 = conn.prepareStatement(insertSql)) {
                ps1.setString(1, customAppId);
                ps1.setInt(2, requestId);
                ps1.setInt(3, validPatientId);
                ps1.setInt(4, dentistUserId);
                ps1.setInt(5, receptionistUserId);
                ps1.setString(6, date);

                String formattedTime = convertSlotToTime(timeSlot);
                ps1.setString(7, formattedTime);

                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(updateRequestSql)) {
                ps2.setInt(1, requestId);
                ps2.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private String convertSlotToTime(String slot) {
        if (slot != null && slot.contains("-")) {
            String startTime = slot.split("-")[0].trim();
            try {
                java.text.SimpleDateFormat displayFormat = new java.text.SimpleDateFormat("hh:mm a");
                java.text.SimpleDateFormat parseFormat = new java.text.SimpleDateFormat("HH:mm:ss");
                java.util.Date date = displayFormat.parse(startTime);
                return parseFormat.format(date);
            } catch (Exception e) {
                return "09:00:00";
            }
        }
        return "09:00:00";
    }

    public List<Object[]> getActiveDentists() {
        List<Object[]> dentists = new ArrayList<>();
        String query = "SELECT user_id, username FROM users WHERE role = 'DENTIST' AND status = 'ACTIVE'";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pst = conn.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                dentists.add(new Object[]{rs.getInt("user_id"), rs.getString("username")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dentists;
    }

    public List<Object[]> getAllAppointmentsForTable() {
        List<Object[]> appointments = new ArrayList<>();
        String query = "SELECT a.appointment_id, p.full_name AS patient_name, p.address, "
                + "p.phone AS contact_no, p.email, u_doc.username AS dentist_name, "
                + "a.treatment_type, a.appointment_date, a.appointment_time "
                + "FROM appointments a "
                + "JOIN patients p ON a.patient_id = p.patient_id "
                + "JOIN users u_doc ON a.dentist_id = u_doc.user_id "
                + "ORDER BY a.appointment_id DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pst = conn.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                appointments.add(new Object[]{
                    rs.getInt("appointment_id"),
                    rs.getString("patient_name"),
                    rs.getString("address"),
                    rs.getString("contact_no"),
                    rs.getString("email"),
                    rs.getString("dentist_name"),
                    rs.getString("treatment_type"),
                    rs.getDate("appointment_date"),
                    rs.getString("appointment_time")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public boolean saveOrUpdateBooking(int appointmentId, String name, String email, String contact, String address,
                                   int dentistId, String treatment, String formattedDate, String time) {
    Connection conn = null;
    try {
        conn = DBConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        int patientId = 0;
        String checkPatientQuery = "SELECT patient_id FROM patients WHERE full_name = ? OR phone = ? LIMIT 1";
        try (PreparedStatement pstCheck = conn.prepareStatement(checkPatientQuery)) {
            pstCheck.setString(1, name);
            pstCheck.setString(2, contact);
            try (ResultSet rsCheck = pstCheck.executeQuery()) {
                if (rsCheck.next()) {
                    patientId = rsCheck.getInt("patient_id");
                }
            }
        }

        if (patientId == 0) {
            String patientQuery = "INSERT INTO patients (full_name, email, phone, address) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstPatient = conn.prepareStatement(patientQuery, Statement.RETURN_GENERATED_KEYS)) {
                pstPatient.setString(1, name);
                pstPatient.setString(2, email);
                pstPatient.setString(3, contact);
                pstPatient.setString(4, address);
                pstPatient.executeUpdate();

                try (ResultSet rsKeys = pstPatient.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        patientId = rsKeys.getInt(1);
                    }
                }
            }
        } else {
            String updatePatientQuery = "UPDATE patients SET full_name = ?, email = ?, phone = ?, address = ? WHERE patient_id = ?";
            try (PreparedStatement pstUpdate = conn.prepareStatement(updatePatientQuery)) {
                pstUpdate.setString(1, name);
                pstUpdate.setString(2, email);
                pstUpdate.setString(3, contact);
                pstUpdate.setString(4, address);
                pstUpdate.setInt(5, patientId);
                pstUpdate.executeUpdate();
            }
        }

        if (patientId <= 0) {
            throw new SQLException("Failed to resolve or create a valid patient.");
        }

        if (appointmentId > 0) {
            String updateAppQuery = "UPDATE appointments SET patient_id = ?, dentist_id = ?, appointment_date = ?, appointment_time = ?, treatment_type = ? WHERE appointment_id = ?";
            try (PreparedStatement pstApp = conn.prepareStatement(updateAppQuery)) {
                pstApp.setInt(1, patientId);
                pstApp.setInt(2, dentistId);
                pstApp.setString(3, formattedDate);
                pstApp.setString(4, time);
                pstApp.setString(5, treatment);
                pstApp.setInt(6, appointmentId);
                pstApp.executeUpdate();
            }
        } else {
            String customAppId = "APT" + (System.currentTimeMillis() % 100000);
            String appQuery = "INSERT INTO appointments (custom_appointment_id, patient_id, dentist_id, receptionist_id, appointment_date, appointment_time, treatment_type, status) "
                    + "VALUES (?, ?, ?, 10, ?, ?, ?, 'SCHEDULED')";

            try (PreparedStatement pstApp = conn.prepareStatement(appQuery)) {
                pstApp.setString(1, customAppId);
                pstApp.setInt(2, patientId);
                pstApp.setInt(3, dentistId);
                pstApp.setString(4, formattedDate);
                pstApp.setString(5, time);
                pstApp.setString(6, treatment);
                pstApp.executeUpdate();
            }
        }

        conn.commit();
        return true;

    } catch (Exception e) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (Exception ex) {
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

    public boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointmentId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Appointment> getMedicalHistoryByPatientId(int patientUserId) {
        List<Appointment> list = new ArrayList<>();
        String query = "SELECT a.appointment_id, a.appointment_date, a.appointment_time, "
                + "COALESCE(t.treatment_name, 'General Consultation') AS treatment_name, "
                + "CONCAT('Dr. ', u.username) AS dentist_name "
                + "FROM appointments a "
                + "LEFT JOIN treatment_records t ON a.appointment_id = t.appointment_id "
                + "LEFT JOIN users u ON a.dentist_id = u.user_id "
                + "WHERE a.patient_id = ? AND a.status = 'COMPLETED' "
                + "ORDER BY a.appointment_date DESC, a.appointment_time DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, patientUserId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Appointment app = new Appointment();
                    app.setAppointmentId(rs.getInt("appointment_id"));
                    app.setAppointmentDate(rs.getDate("appointment_date"));
                    app.setAppointmentTime(rs.getString("appointment_time"));
                    app.setTreatmentType(rs.getString("treatment_name"));
                    app.setDentistName(rs.getString("dentist_name"));

                    list.add(app);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getScheduledAppointmentsByDentist(int dentistUserId) {
    List<Object[]> list = new ArrayList<>();
    String sql = "SELECT a.appointment_id, a.patient_id, "
            + "COALESCE(p.full_name, u.username, CONCAT('Unknown (ID:', a.patient_id, ')')) AS patient_name, "
            + "COALESCE(a.treatment_type, 'General Examination') AS treatment_type, "
            + "a.appointment_date, a.appointment_time "
            + "FROM appointments a "
            + "LEFT JOIN patients p ON a.patient_id = p.patient_id "
            + "LEFT JOIN users u ON a.patient_id = u.user_id "
            + "WHERE a.dentist_id = ? AND a.status IN ('SCHEDULED', 'COMPLETED', 'ACCEPTED') "
            + "ORDER BY a.appointment_date DESC, a.appointment_time DESC";

    try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, dentistUserId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("appointment_id"),
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getString("treatment_type"),
                    rs.getDate("appointment_date") != null ? rs.getDate("appointment_date").toString() : "",
                    rs.getString("appointment_time")
                });
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

    private String formatTimeSlot(String rawTime) {
        if (rawTime == null || rawTime.trim().isEmpty()) {
            return "N/A";
        }

        String timeToParse = rawTime.contains("-") ? rawTime.split("-")[0].trim() : rawTime.trim();

        try {
            SimpleDateFormat sdf24Sec = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");
            Date date = sdf24Sec.parse(timeToParse);
            return sdf12.format(date);
        } catch (Exception ignored) {
        }

        try {
            SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");
            Date date = sdf24.parse(timeToParse);
            return sdf12.format(date);
        } catch (Exception ignored) {
        }

        try {
            SimpleDateFormat sdf12Input = new SimpleDateFormat("hh:mm a");
            Date date = sdf12Input.parse(timeToParse);
            return sdf12Input.format(date);
        } catch (Exception ignored) {
        }

        return timeToParse;
    }

    public List<Object[]> getDailyAppointmentsByDentistAndDate(int dentistUserId, String selectedDate) {
        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT a.appointment_time, "
                + "COALESCE(u.username, CONCAT('Unknown (ID:', a.patient_id, ')')) AS patient_name, "
                + "COALESCE(a.treatment_type, 'General Examination') AS treatment_type, "
                + "COALESCE(u.contact_no, 'N/A') AS phone, "
                + "a.status "
                + "FROM appointments a "
                + "LEFT JOIN users u ON a.patient_id = u.user_id "
                + "WHERE a.dentist_id = ? AND a.appointment_date = ? "
                + "ORDER BY a.appointment_time ASC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dentistUserId);
            ps.setString(2, selectedDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String formattedTime = formatTimeSlot(rs.getString("appointment_time"));

                    list.add(new Object[]{
                        formattedTime,
                        rs.getString("patient_name"),
                        rs.getString("treatment_type"),
                        rs.getString("phone"),
                        rs.getString("status")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getAppointmentIdByDetails(int dentistId, String dateStr, String timeStr, String patientName) {
        int apptId = 0;
        String sql = "SELECT a.appointment_id FROM appointments a "
                + "JOIN users u ON a.patient_id = u.user_id "
                + "WHERE a.dentist_id = ? AND a.appointment_date = ? AND u.username = ? LIMIT 1";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dentistId);
            ps.setString(2, dateStr);
            ps.setString(3, patientName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    apptId = rs.getInt("appointment_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apptId;
    }
}

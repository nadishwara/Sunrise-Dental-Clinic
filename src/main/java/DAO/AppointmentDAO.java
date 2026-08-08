/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Appointment;

/**
 *
 * @author nadis
 */
public class AppointmentDAO {

    public boolean scheduleAppointment(int requestId, int patientUserId, int dentistUserId, int receptionistUserId, String date, String timeSlot) {

        String insertSql = "INSERT INTO appointments "
                + "(custom_appointment_id, request_id, patient_id, dentist_id, receptionist_id, appointment_date, appointment_time, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 'SCHEDULED')";

        String updateRequestSql = "UPDATE appointment_requests SET status = 'CONFIRMED' WHERE request_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            String customAppId = "APP-" + System.currentTimeMillis() % 100000;

            try (PreparedStatement ps1 = conn.prepareStatement(insertSql)) {
                ps1.setString(1, customAppId);
                ps1.setInt(2, requestId);
                ps1.setInt(3, patientUserId);
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
        String query = "SELECT a.appointment_id, u_pat.username AS patient_name, u_pat.address, "
                + "u_pat.contact_no, u_pat.whatsapp_no, u_doc.username AS dentist_name, "
                + "a.treatment_type, a.appointment_date, a.appointment_time "
                + "FROM appointments a "
                + "JOIN users u_pat ON a.patient_id = u_pat.user_id "
                + "JOIN users u_doc ON a.dentist_id = u_doc.user_id "
                + "ORDER BY a.appointment_id DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pst = conn.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                appointments.add(new Object[]{
                    rs.getInt("appointment_id"),
                    rs.getString("patient_name"),
                    rs.getString("address"),
                    rs.getString("contact_no"),
                    rs.getString("whatsapp_no"),
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

    public boolean saveDirectBooking(String name, String address, String contact, String whatsapp,
            int dentistId, String treatment, String formattedDate, String time) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            int patientUserId = 0;
            String generatedEmail = name.toLowerCase().replaceAll("\\s+", "") + "@patient.com";

            String checkUserQuery = "SELECT user_id FROM users WHERE username = ? OR contact_no = ? LIMIT 1";
            PreparedStatement pstCheck = conn.prepareStatement(checkUserQuery);
            pstCheck.setString(1, name);
            pstCheck.setString(2, contact);
            ResultSet rsCheck = pstCheck.executeQuery();

            if (rsCheck.next()) {
                patientUserId = rsCheck.getInt("user_id");
            } else {
                String customId = "PTN" + (System.currentTimeMillis() % 100000);
                String userQuery = "INSERT INTO users (custom_id, username, email, contact_no, whatsapp_no, address, password_hash, role, status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, 'default123', 'PATIENT', 'ACTIVE')";

                PreparedStatement pstUser = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
                pstUser.setString(1, customId);
                pstUser.setString(2, name);
                pstUser.setString(3, generatedEmail);
                pstUser.setString(4, contact);
                pstUser.setString(5, whatsapp);
                pstUser.setString(6, address);
                pstUser.executeUpdate();

                ResultSet rsKeys = pstUser.getGeneratedKeys();
                if (rsKeys.next()) {
                    patientUserId = rsKeys.getInt(1);
                }
            }

            String customAppId = "APT" + (System.currentTimeMillis() % 100000);
            String appQuery = "INSERT INTO appointments (custom_appointment_id, patient_id, dentist_id, receptionist_id, appointment_date, appointment_time, treatment_type, status) "
                    + "VALUES (?, ?, ?, 10, ?, ?, ?, 'SCHEDULED')";

            PreparedStatement pstApp = conn.prepareStatement(appQuery);
            pstApp.setString(1, customAppId);
            pstApp.setInt(2, patientUserId);
            pstApp.setInt(3, dentistId);
            pstApp.setString(4, formattedDate);
            pstApp.setString(5, time);
            pstApp.setString(6, treatment);
            pstApp.executeUpdate();

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
        String query = "SELECT a.appointment_id, a.patient_id, a.dentist_id, "
                + "u_pat.username AS patient_name, u_pat.address, u_pat.contact_no, u_pat.whatsapp_no, "
                + "u_doc.username AS dentist_name, a.treatment_type, a.appointment_date, a.appointment_time "
                + "FROM appointments a "
                + "JOIN users u_pat ON a.patient_id = u_pat.user_id "
                + "JOIN users u_doc ON a.dentist_id = u_doc.user_id "
                + "WHERE a.patient_id = ? "
                + "ORDER BY a.appointment_date DESC, a.appointment_time DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, patientUserId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Appointment app = new Appointment(
                            rs.getInt("appointment_id"),
                            rs.getInt("patient_id"),
                            rs.getInt("dentist_id"),
                            rs.getString("patient_name"),
                            rs.getString("address"),
                            rs.getString("contact_no"),
                            rs.getString("whatsapp_no"),
                            rs.getString("dentist_name"),
                            rs.getString("treatment_type"),
                            rs.getDate("appointment_date"),
                            rs.getString("appointment_time")
                    );
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
    String query = "SELECT a.appointment_id, a.patient_id, u_pat.username AS patient_name, "
            + "a.treatment_type, a.appointment_date, a.appointment_time "
            + "FROM appointments a "
            + "JOIN users u_pat ON a.patient_id = u_pat.user_id "
            + "WHERE a.dentist_id = ? AND a.status = 'SCHEDULED' "
            + "ORDER BY a.appointment_date ASC, a.appointment_time ASC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, dentistUserId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getString("patient_name"),
                        rs.getString("treatment_type"),
                        rs.getDate("appointment_date"),
                        rs.getString("appointment_time")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

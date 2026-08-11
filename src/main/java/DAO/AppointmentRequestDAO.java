/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import config.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Model_AppointmentRequest;

/**
 *
 * @author nadis
 */
public class AppointmentRequestDAO {

    public List<Model_AppointmentRequest> getAllRequests() {
        List<Model_AppointmentRequest> list = new ArrayList<>();

        String sql = "SELECT ar.request_id, ar.patient_user_id, ar.patient_custom_id, u.username AS patient_name, u.email AS patient_email, "
                + "ar.preferred_date, ar.preferred_time_slot, ar.notes, ar.status "
                + "FROM appointment_requests ar "
                + "INNER JOIN users u ON ar.patient_user_id = u.user_id "
                + "ORDER BY ar.created_at DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Model_AppointmentRequest req = new Model_AppointmentRequest();
                req.setRequestId(rs.getInt("request_id"));
                req.setPatientUserId(rs.getInt("patient_user_id"));
                req.setPatientCustomId(rs.getString("patient_custom_id"));
                req.setPatientName(rs.getString("patient_name"));
                req.setPatientEmail(rs.getString("patient_email"));
                req.setPreferredDate(rs.getDate("preferred_date"));
                req.setPreferredTimeSlot(rs.getString("preferred_time_slot"));
                req.setDentistName("Any Dentist");
                req.setNotes(rs.getString("notes"));
                req.setStatus(rs.getString("status"));
                list.add(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(int requestId, String newStatus) {
        String sql = "UPDATE appointment_requests SET status = ? WHERE request_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Model_AppointmentRequest> getRequestsByPatientUserId(int patientUserId) {
        List<Model_AppointmentRequest> list = new ArrayList<>();
        String sql = "SELECT request_id, patient_custom_id, preferred_date, preferred_time_slot, notes, status "
                + "FROM appointment_requests WHERE patient_user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientUserId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Model_AppointmentRequest req = new Model_AppointmentRequest();
                    req.setRequestId(rs.getInt("request_id"));
                    req.setPatientCustomId(rs.getString("patient_custom_id"));
                    req.setPreferredDate(rs.getDate("preferred_date"));
                    req.setPreferredTimeSlot(rs.getString("preferred_time_slot"));
                    req.setNotes(rs.getString("notes"));
                    req.setStatus(rs.getString("status"));
                    list.add(req);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean createRequest(int patientUserId, String patientCustomId, Date preferredDate, String preferredTimeSlot, String notes, String status) {
        String sql = "INSERT INTO appointment_requests (patient_user_id, patient_custom_id, preferred_date, preferred_time_slot, notes, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientUserId);
            stmt.setString(2, patientCustomId);
            stmt.setDate(3, preferredDate);
            stmt.setString(4, preferredTimeSlot);
            stmt.setString(5, notes);
            stmt.setString(6, status);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

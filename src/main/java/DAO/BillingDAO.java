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
import java.util.ArrayList;
import java.util.List;
import model.BillingDetails;

/**
 *
 * @author nadis
 */
public class BillingDAO {

    public boolean processAndGenerateBill(int appointmentId, int patientUserId, int receptionistUserId,
            double consultationFee, double otherCharges, double discount) {

        String fetchCostsSql = "SELECT "
                + " (SELECT COALESCE(SUM(service_cost), 0) FROM treatment_records WHERE appointment_id = ?) AS total_treatments, "
                + " (SELECT COALESCE(SUM(dx.xray_cost), 0) FROM dental_xrays dx "
                + "  JOIN treatment_records tr ON dx.treatment_id = tr.treatment_id WHERE tr.appointment_id = ?) AS total_xrays";

        String insertBillSql = "INSERT INTO billing (custom_bill_id, appointment_id, patient_user_id, receptionist_user_id, "
                + "consultation_fee, total_treatment_cost, total_xray_cost, other_charges, discount, net_amount) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false); // Transaction Safety

            double totalTreatmentCost = 0;
            double totalXrayCost = 0;

            try (PreparedStatement psFetch = conn.prepareStatement(fetchCostsSql)) {
                psFetch.setInt(1, appointmentId);
                psFetch.setInt(2, appointmentId);
                ResultSet rs = psFetch.executeQuery();
                if (rs.next()) {
                    totalTreatmentCost = rs.getDouble("total_treatments");
                    totalXrayCost = rs.getDouble("total_xrays");
                }
            }

            double netAmount = (consultationFee + totalTreatmentCost + totalXrayCost + otherCharges) - discount;
            String customBillId = "INV-" + (System.currentTimeMillis() % 100000);

            try (PreparedStatement psInsert = conn.prepareStatement(insertBillSql)) {
                psInsert.setString(1, customBillId);
                psInsert.setInt(2, appointmentId);
                psInsert.setInt(3, patientUserId);
                psInsert.setInt(4, receptionistUserId);
                psInsert.setDouble(5, consultationFee);
                psInsert.setDouble(6, totalTreatmentCost);
                psInsert.setDouble(7, totalXrayCost);
                psInsert.setDouble(8, otherCharges);
                psInsert.setDouble(9, discount);
                psInsert.setDouble(10, netAmount);

                psInsert.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> getAllPendingAppointmentsForTable() {
        List<Object[]> tableData = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.custom_appointment_id, u_pat.username AS patient_name, "
                   + "u_pat.custom_id AS patient_custom_id, a.appointment_date "
                   + "FROM appointments a "
                   + "JOIN users u_pat ON a.patient_id = u_pat.user_id "
                   + "ORDER BY a.appointment_id DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tableData.add(new Object[]{
                    rs.getInt("appointment_id"),
                    rs.getString("custom_appointment_id"),
                    rs.getString("patient_name"),
                    rs.getString("patient_custom_id"),
                    rs.getString("appointment_date")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableData;
    }

    public BillingDetails getBillingDetailsByAppointmentId(int appointmentId) {
        BillingDetails details = null;

        String sql = "SELECT a.appointment_id, a.custom_appointment_id, a.appointment_date, "
                + "u_pat.user_id AS patient_user_id, u_pat.custom_id AS patient_custom_id, u_pat.username AS patient_name, u_pat.contact_no, "
                + "u_doc.username AS doctor_name "
                + "FROM appointments a "
                + "JOIN users u_pat ON a.patient_id = u_pat.user_id "
                + "JOIN users u_doc ON a.dentist_id = u_doc.user_id "
                + "WHERE a.appointment_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                details = new BillingDetails();
                details.setAppointmentId(rs.getInt("appointment_id"));
                details.setCustomAppointmentId(rs.getString("custom_appointment_id"));
                details.setPatientUserId(rs.getInt("patient_user_id"));
                details.setPatientCustomId(rs.getString("patient_custom_id"));
                details.setPatientName(rs.getString("patient_name"));
                details.setPatientPhone(rs.getString("contact_no"));
                details.setDoctorName(rs.getString("doctor_name"));
                details.setAppointmentDate(rs.getString("appointment_date"));

                loadTreatmentData(conn, details);
                loadXrayData(conn, details);
                loadPrescriptionData(conn, details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public BillingDetails getBillingDetailsBySearchQuery(String query) {
        BillingDetails details = null;

        String sql = "SELECT a.appointment_id, a.custom_appointment_id, a.appointment_date, "
            + "u_pat.user_id AS patient_user_id, u_pat.custom_id AS patient_custom_id, u_pat.username AS patient_name, u_pat.contact_no, "
            + "u_doc.username AS doctor_name "
            + "FROM appointments a "
            + "JOIN users u_pat ON a.patient_id = u_pat.user_id "
            + "JOIN users u_doc ON a.dentist_id = u_doc.user_id "
            + "WHERE a.custom_appointment_id = ? OR u_pat.custom_id = ? OR u_pat.username LIKE ? "
            + "ORDER BY a.appointment_id DESC LIMIT 1";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, "%" + query + "%");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                details = new BillingDetails();
                details.setAppointmentId(rs.getInt("appointment_id"));
                details.setCustomAppointmentId(rs.getString("custom_appointment_id"));
                details.setPatientUserId(rs.getInt("patient_user_id"));
                details.setPatientCustomId(rs.getString("patient_custom_id"));
                details.setPatientName(rs.getString("patient_name"));
                details.setPatientPhone(rs.getString("contact_no"));
                details.setDoctorName(rs.getString("doctor_name"));
                details.setAppointmentDate(rs.getString("appointment_date"));

                loadTreatmentData(conn, details);
                loadXrayData(conn, details);
                loadPrescriptionData(conn, details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    private void loadTreatmentData(Connection conn, BillingDetails details) throws SQLException {
        String sql = "SELECT treatment_name, service_cost, clinical_notes FROM treatment_records WHERE appointment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, details.getAppointmentId());
            ResultSet rs = ps.executeQuery();
            double totalCost = 0;
            StringBuilder notes = new StringBuilder();

            while (rs.next()) {
                String tName = rs.getString("treatment_name");
                double cost = rs.getDouble("service_cost");
                details.getTreatments().add(tName + " (Rs. " + cost + ")");
                totalCost += cost;

                String note = rs.getString("clinical_notes");
                if (note != null && !note.isEmpty()) {
                    notes.append(note).append("\n");
                }
            }
            details.setTotalTreatmentCost(totalCost);
            details.setClinicalNotes(notes.toString());
        }
    }

    private void loadXrayData(Connection conn, BillingDetails details) throws SQLException {
        String sql = "SELECT dx.xray_type, dx.xray_cost FROM dental_xrays dx "
                + "JOIN treatment_records tr ON dx.treatment_id = tr.treatment_id "
                + "WHERE tr.appointment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, details.getAppointmentId());
            ResultSet rs = ps.executeQuery();
            double totalCost = 0;
            while (rs.next()) {
                details.getXrays().add(rs.getString("xray_type"));
                totalCost += rs.getDouble("xray_cost");
            }
            details.setTotalXrayCost(totalCost);
        }
    }

    private void loadPrescriptionData(Connection conn, BillingDetails details) throws SQLException {
        String sql = "SELECT p.medication_name, p.dosage, p.duration FROM prescriptions p "
                + "JOIN treatment_records tr ON p.treatment_id = tr.treatment_id "
                + "WHERE tr.appointment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, details.getAppointmentId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                details.getPrescriptions().add(rs.getString("medication_name") + " - "
                        + rs.getString("dosage") + " (" + rs.getString("duration") + ")");
            }
        }
    }
}
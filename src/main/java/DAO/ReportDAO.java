/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nadis
 */
public class ReportDAO {

    public Map<String, Double> getRevenueReport(String type) {
        Map<String, Double> revenueMap = new HashMap<>();
        String groupBy = "DATE(created_at)";

        if ("WEEKLY".equalsIgnoreCase(type)) {
            groupBy = "YEARWEEK(created_at, 1)";
        } else if ("MONTHLY".equalsIgnoreCase(type)) {
            groupBy = "DATE_FORMAT(created_at, '%Y-%m')";
        }

        String sql = "SELECT " + groupBy + " AS period, SUM(net_amount) AS total_revenue "
                + "FROM billing WHERE payment_status = 'PAID' OR payment_status = 'PENDING' "
                + "GROUP BY period ORDER BY period DESC LIMIT 30";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                revenueMap.put(rs.getString("period"), rs.getDouble("total_revenue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenueMap;
    }

    public Map<String, Integer> getAppointmentStatusSummary() {
        Map<String, Integer> summaryMap = new HashMap<>();
        String sql = "SELECT status, COUNT(*) AS count FROM appointments GROUP BY status";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                summaryMap.put(rs.getString("status"), rs.getInt("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return summaryMap;
    }

    public List<Object[]> getTopTreatments() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT treatment_name, COUNT(*) AS total_count, SUM(service_cost) AS total_earned "
                + "FROM treatment_records GROUP BY treatment_name ORDER BY total_count DESC LIMIT 10";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("treatment_name"),
                    rs.getInt("total_count"),
                    rs.getDouble("total_earned")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, Integer> getNewPatientStats() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT DATE_FORMAT(created_at, '%Y-%m') AS month, COUNT(*) AS new_patients "
                + "FROM users WHERE role = 'PATIENT' GROUP BY month ORDER BY month DESC LIMIT 12";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                stats.put(rs.getString("month"), rs.getInt("new_patients"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    public double getTotalRevenue() {
        String sql = "SELECT SUM(net_amount) AS total FROM billing WHERE payment_status = 'PAID'";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getActiveStaffCount() {
        String sql = "SELECT COUNT(*) AS count FROM staff WHERE status = 'ACTIVE'";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayAppointmentsCount() {
        String sql = "SELECT COUNT(*) AS count FROM appointments WHERE DATE(appointment_date) = CURDATE()";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalPatientsCount() {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE role = 'PATIENT'";
        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public DefaultTableModel getDetailedBillAndTreatmentReport() {
        String[] columnNames = {"Bill ID", "Patient Name", "Treatment Name", "Service Cost", "Consultation", "Net Amount", "Status", "Date"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0);

        String sql = "SELECT b.custom_bill_id, u.username AS patient_name, "
                + "IFNULL(t.treatment_name, 'N/A') AS treatment_name, "
                + "IFNULL(t.service_cost, 0.00) AS service_cost, "
                + "b.consultation_fee, b.net_amount, b.payment_status, b.created_at "
                + "FROM billing b "
                + "JOIN users u ON b.patient_user_id = u.user_id "
                + "LEFT JOIN appointments a ON b.appointment_id = a.appointment_id "
                + "LEFT JOIN treatment_records t ON a.appointment_id = t.appointment_id "
                + "ORDER BY b.created_at DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("custom_bill_id"),
                    rs.getString("patient_name"),
                    rs.getString("treatment_name"),
                    rs.getDouble("service_cost"),
                    rs.getDouble("consultation_fee"),
                    rs.getDouble("net_amount"),
                    rs.getString("payment_status"),
                    rs.getTimestamp("created_at")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public DefaultTableModel getReceptionistExportData() {
        String[] columnNames = {"Receptionist ID", "Receptionist Name", "Appointment ID", "Appointment Date", "Patient Name", "Dentist Name", "Bill ID", "Net Amount (LKR)", "Payment Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String sql = "SELECT r.custom_id AS receptionist_id, r.username AS receptionist_name, "
                + "a.custom_appointment_id, a.appointment_date, p.username AS patient_name, "
                + "d.username AS dentist_name, IFNULL(b.custom_bill_id, 'N/A') AS bill_id, "
                + "IFNULL(b.net_amount, 0.00) AS net_amount, IFNULL(b.payment_status, 'N/A') AS payment_status "
                + "FROM users r "
                + "LEFT JOIN appointments a ON r.user_id = a.receptionist_id "
                + "LEFT JOIN users p ON a.patient_id = p.user_id "
                + "LEFT JOIN users d ON a.dentist_id = d.user_id "
                + "LEFT JOIN billing b ON a.appointment_id = b.appointment_id "
                + "WHERE r.role = 'RECEPTIONIST' "
                + "ORDER BY a.created_at DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("receptionist_id"),
                    rs.getString("receptionist_name"),
                    rs.getString("custom_appointment_id"),
                    rs.getDate("appointment_date"),
                    rs.getString("patient_name"),
                    rs.getString("dentist_name"),
                    rs.getString("bill_id"),
                    rs.getDouble("net_amount"),
                    rs.getString("payment_status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public DefaultTableModel getDentistExportData() {
        String[] columnNames = {"Dentist ID", "Dentist Name", "Appointment ID", "Patient Name", "Appointment Date", "Treatment Name", "Tooth No", "Service Cost (LKR)", "Clinical Notes"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String sql = "SELECT d.custom_id AS dentist_id, d.username AS dentist_name, "
                + "a.custom_appointment_id, p.username AS patient_name, a.appointment_date, "
                + "IFNULL(t.treatment_name, 'N/A') AS treatment_name, IFNULL(t.tooth_number, 'N/A') AS tooth_number, "
                + "IFNULL(t.service_cost, 0.00) AS service_cost, IFNULL(t.clinical_notes, '') AS clinical_notes "
                + "FROM users d "
                + "LEFT JOIN appointments a ON d.user_id = a.dentist_id "
                + "LEFT JOIN users p ON a.patient_id = p.user_id "
                + "LEFT JOIN treatment_records t ON a.appointment_id = t.appointment_id "
                + "WHERE d.role = 'DENTIST' "
                + "ORDER BY a.appointment_date DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("dentist_id"),
                    rs.getString("dentist_name"),
                    rs.getString("custom_appointment_id"),
                    rs.getString("patient_name"),
                    rs.getDate("appointment_date"),
                    rs.getString("treatment_name"),
                    rs.getString("tooth_number"),
                    rs.getDouble("service_cost"),
                    rs.getString("clinical_notes")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public DefaultTableModel getPatientExportData() {
        String[] columnNames = {"Patient ID", "Patient Name", "Email", "Contact", "Appointment ID", "Appointment Date", "Treatment Name", "Bill Amount (LKR)", "Payment Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String sql = "SELECT u.custom_id AS patient_id, u.username AS patient_name, u.email, "
                + "IFNULL(u.contact_no, 'N/A') AS contact_no, IFNULL(a.custom_appointment_id, 'N/A') AS appointment_id, "
                + "a.appointment_date, IFNULL(t.treatment_name, 'N/A') AS treatment_name, "
                + "IFNULL(b.net_amount, 0.00) AS net_amount, IFNULL(b.payment_status, 'N/A') AS payment_status "
                + "FROM users u "
                + "LEFT JOIN appointments a ON u.user_id = a.patient_id "
                + "LEFT JOIN treatment_records t ON a.appointment_id = t.appointment_id "
                + "LEFT JOIN billing b ON a.appointment_id = b.appointment_id "
                + "WHERE u.role = 'PATIENT' "
                + "ORDER BY u.created_at DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("patient_id"),
                    rs.getString("patient_name"),
                    rs.getString("email"),
                    rs.getString("contact_no"),
                    rs.getString("appointment_id"),
                    rs.getDate("appointment_date"),
                    rs.getString("treatment_name"),
                    rs.getDouble("net_amount"),
                    rs.getString("payment_status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}

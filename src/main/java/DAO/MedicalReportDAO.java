package DAO;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.MedicalReportModel;

public class MedicalReportDAO {

    public MedicalReportModel getMedicalReportByAppointmentId(String appointmentId) {
        MedicalReportModel report = null;
        System.out.println("Searching for Appointment ID: " + appointmentId);

        String sql = "SELECT "
                + "a.appointment_date AS date, "
                + "a.appointment_time AS time, "
                + "st.full_name AS dentist_name, "
                + "a.treatment_type, "
                + "tr.treatment_name, "
                + "tr.clinical_notes, "
                + "NULL AS remarks, "
                + "tr.tooth_number, "
                + "NULL AS affected_area, "
                + "x.xray_type, "
                + "tr.service_cost "
                + "FROM appointments a "
                + "JOIN staff st ON a.dentist_id = st.user_id "
                + "LEFT JOIN treatment_records tr ON a.appointment_id = tr.appointment_id "
                + "LEFT JOIN dental_xrays x ON tr.treatment_id = x.treatment_id "
                + "WHERE a.appointment_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, appointmentId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                report = new MedicalReportModel();
                report.setAppointmentDate(rs.getString("date"));
                report.setAppointmentTime(rs.getString("time"));
                report.setDentistName(rs.getString("dentist_name"));
                report.setTreatmentType(rs.getString("treatment_type"));
                report.setTreatmentName(rs.getString("treatment_name"));
                report.setClinicalNotes(rs.getString("clinical_notes"));
                report.setRemarks(rs.getString("remarks"));
                report.setToothNumber(rs.getString("tooth_number"));
                report.setAffectedArea(rs.getString("affected_area"));
                report.setXRayType(rs.getString("xray_type"));
                report.setServiceCost(rs.getString("service_cost"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return report;
    }

    public String getXRayPathByAppointmentId(int appointmentId) {
        String filePath = null;
        String sql = "SELECT x.file_path FROM dental_xrays x "
                + "JOIN treatment_records tr ON x.treatment_id = tr.treatment_id "
                + "WHERE tr.appointment_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, appointmentId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                filePath = rs.getString("file_path");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }
}

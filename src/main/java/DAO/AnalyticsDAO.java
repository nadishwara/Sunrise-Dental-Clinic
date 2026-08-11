package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import config.DBConnection;

public class AnalyticsDAO {

    public int getTotalPatientsToday() {
        int count = 0;
        String todayDate = LocalDate.now().toString();
        String query = "SELECT COUNT(*) FROM appointments WHERE appointment_date = ?";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, todayDate);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public Map<String, Integer> getTreatmentTypeDistribution() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String query = "SELECT COALESCE(NULLIF(TRIM(treatment_type), ''), 'General / Unspecified') AS treatment, "
                + "COUNT(*) AS total FROM appointments GROUP BY treatment";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String treatmentName = rs.getString("treatment");
                int total = rs.getInt("total");

                if (treatmentName == null) {
                    treatmentName = "General / Unspecified";
                }

                data.put(treatmentName, total);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map<String, Integer> getWeeklyPatientCount() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String query = "SELECT DAYNAME(appointment_date) as day_name, COUNT(*) as total "
                + "FROM appointments WHERE appointment_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) "
                + "GROUP BY DAYNAME(appointment_date), appointment_date ORDER BY appointment_date ASC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                data.put(rs.getString("day_name"), rs.getInt("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    
}

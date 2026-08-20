package DAO;

import at.favre.lib.crypto.bcrypt.BCrypt;
import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.Staff;
import model.User;
import util.IdGenerator;

public class StaffDAO {

    public boolean registerStaff(User user, Staff staff) {
        if (user == null || staff == null) {
            System.err.println("Staff Registration Error: User or Staff object is null.");
            return false;
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
            user.getEmail() == null || user.getEmail().trim().isEmpty() ||
            user.getPassword() == null || user.getPassword().isEmpty() ||
            staff.getFullName() == null || staff.getFullName().trim().isEmpty()) {
            System.err.println("Staff Registration Error: Required fields are empty.");
            return false;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.isUsernameExists(user.getUsername())) {
            System.err.println("Staff Registration Error: Username already taken.");
            return false;
        }

        if (userDAO.isEmailExists(user.getEmail())) {
            System.err.println("Staff Registration Error: Email already exists.");
            return false;
        }

        String insertUserSql = "INSERT INTO users (username, email, password_hash, role, custom_id, status) VALUES (?, ?, ?, ?, ?, ?)";
        String updateCustomIdSql = "UPDATE users SET custom_id = ? WHERE user_id = ?";
        String insertStaffSql = "INSERT INTO staff (user_id, custom_staff_id, full_name, contact_no, specialization) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            int generatedUserId = -1;
            String tempCustomId = "TEMP-" + UUID.randomUUID().toString().substring(0, 8);

            String hashedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());

            try (PreparedStatement userStmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, user.getUsername().trim());
                userStmt.setString(2, user.getEmail().trim());
                userStmt.setString(3, hashedPassword);
                userStmt.setString(4, user.getRole());
                userStmt.setString(5, tempCustomId);
                userStmt.setString(6, user.getStatus() != null ? user.getStatus() : "ACTIVE");

                int affectedStaff = userStmt.executeUpdate();
                if (affectedStaff == 0) {
                    conn.rollback();
                    return false;
                }
                try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedUserId = generatedKeys.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            String customId = IdGenerator.generateCustomId(user.getRole(), generatedUserId);

            try (PreparedStatement updateStmt = conn.prepareStatement(updateCustomIdSql)) {
                updateStmt.setString(1, customId);
                updateStmt.setInt(2, generatedUserId);
                updateStmt.executeUpdate();
            }

            try (PreparedStatement staffStmt = conn.prepareStatement(insertStaffSql)) {
                staffStmt.setInt(1, generatedUserId);
                staffStmt.setString(2, customId);
                staffStmt.setString(3, staff.getFullName().trim());
                staffStmt.setString(4, staff.getContactNo() != null ? staff.getContactNo().trim() : null);
                staffStmt.setString(5, (staff.getSpecialization() != null && !staff.getSpecialization().trim().isEmpty()) 
                        ? staff.getSpecialization().trim() : "General");

                staffStmt.executeUpdate();
            }

            conn.commit();
            user.setUserId(generatedUserId);
            user.setCustomId(customId);
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Staff Register SQL Error: " + e.getMessage());
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

    public List<Staff> getActiveDentist() {
        List<Staff> dentistList = new ArrayList<>();
        String sql = "SELECT s.staff_id, s.user_id, s.custom_staff_id, s.full_name, s.contact_no, s.specialization "
                + "FROM staff s "
                + "INNER JOIN users u ON s.user_id = u.user_id "
                + "WHERE UPPER(u.role) = 'DENTIST' "
                + "AND (u.status IS NULL OR UPPER(u.status) = 'ACTIVE' OR u.status = '1')";

        try (Connection conn = DBConnection.getInstance().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffId(rs.getInt("staff_id"));
                staff.setUserId(rs.getInt("user_id"));
                staff.setCustomStaffId(rs.getString("custom_staff_id"));
                staff.setFullName(rs.getString("full_name"));
                staff.setContactNo(rs.getString("contact_no"));
                staff.setSpecialization(rs.getString("specialization"));
                dentistList.add(staff);
            }

        } catch (SQLException e) {
            System.err.println("Error loading active dentists: " + e.getMessage());
            e.printStackTrace();
        }
        return dentistList;
    }
}

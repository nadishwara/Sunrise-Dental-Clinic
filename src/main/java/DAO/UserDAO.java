package DAO;

import at.favre.lib.crypto.bcrypt.BCrypt;
import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import model.User;
import util.IdGenerator;

public class UserDAO {

    public boolean isUsernameExists(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        String sql = "SELECT user_id FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailExistsForOtherUser(String email, int currentUserId) {
        if (email == null || email.trim().isEmpty()) return false;
        String sql = "SELECT user_id FROM users WHERE email = ? AND user_id != ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim());
            stmt.setInt(2, currentUserId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUser(User user) {
        if (user == null || user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            System.err.println("Registration Error: Invalid input data (Null values detected).");
            return false;
        }

        if (isUsernameExists(user.getUsername())) {
            System.err.println("Registration Error: Username already taken.");
            return false;
        }

        if (isEmailExists(user.getEmail())) {
            System.err.println("Registration Error: Email already registered.");
            return false;
        }

        String sql = "INSERT INTO users (custom_id, username, email, password_hash, role, status) VALUES (?, ?, ?, ?, ?, ?)";
        String updateCustomIdSql = "UPDATE users SET custom_id = ? WHERE user_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            int generatedUserId = -1;
            String tempCustomId = "TEMP-" + UUID.randomUUID().toString().substring(0, 8);

            String hashedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, tempCustomId);
                stmt.setString(2, user.getUsername().trim());
                stmt.setString(3, user.getEmail().trim());
                stmt.setString(4, hashedPassword);
                stmt.setString(5, user.getRole());
                stmt.setString(6, user.getStatus() != null ? user.getStatus() : "ACTIVE");

                int rows = stmt.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedUserId = rs.getInt(1);
                    }
                }
            }

            if (generatedUserId == -1) {
                conn.rollback();
                return false;
            }

            String customId = IdGenerator.generateCustomId(user.getRole(), generatedUserId);

            try (PreparedStatement updateStmt = conn.prepareStatement(updateCustomIdSql)) {
                updateStmt.setString(1, customId);
                updateStmt.setInt(2, generatedUserId);
                updateStmt.executeUpdate();
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
            System.err.println("Registration SQL Error: " + e.getMessage());
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

    public User authenticateUser(String email, String plainPassword) {
        if (email == null || email.trim().isEmpty() || plainPassword == null || plainPassword.isEmpty()) {
            return null;
        }

        String sql = "SELECT u.*, s.staff_id, s.full_name, "
                   + "COALESCE(u.contact_no, s.contact_no) AS final_contact_no "
                   + "FROM users u "
                   + "LEFT JOIN staff s ON u.user_id = s.user_id "
                   + "WHERE u.email = ?";

        User user = null;

        try (Connection conn = DBConnection.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password_hash");
                    boolean isPasswordValid = false;

                    if (storedHashedPassword != null && storedHashedPassword.startsWith("$2a$")) {
                        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), storedHashedPassword);
                        isPasswordValid = result.verified;
                    } else {
                        isPasswordValid = plainPassword.equals(storedHashedPassword);
                    }

                    if (isPasswordValid) {
                        user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setCustomId(rs.getString("custom_id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setRole(rs.getString("role"));
                        user.setStatus(rs.getString("status"));

                        user.setContactNo(rs.getString("final_contact_no"));
                        user.setWhatsappNo(rs.getString("whatsapp_no"));
                        user.setAddress(rs.getString("address"));
                        
                        String dbImg = rs.getString("profile_image");
                        System.out.println("DEBUG [authenticateUser] - DB Profile Image Path: " + dbImg);
                        user.setProfileImage(dbImg);

                        int staffId = rs.getInt("staff_id");
                        if (!rs.wasNull()) {
                            user.setStaffId(staffId);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    public User getUserById(int userId) {
        if (userId <= 0) return null;

        String sql = "SELECT u.*, s.full_name, "
                + "COALESCE(u.contact_no, s.contact_no) AS final_contact_no, "
                + "COALESCE(u.address, s.address) AS address "
                + "FROM users u "
                + "LEFT JOIN staff s ON u.user_id = s.user_id "
                + "WHERE u.user_id = ?";
        User user = null;

        try (Connection conn = DBConnection.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setCustomId(rs.getString("custom_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));

                    user.setContactNo(rs.getString("final_contact_no"));
                    user.setWhatsappNo(rs.getString("whatsapp_no"));
                    user.setAddress(rs.getString("address"));
                    
                    // --- DEBUG PRINT ---
                    String dbImg = rs.getString("profile_image");
                    System.out.println("DEBUG [getUserById] - DB Profile Image Path: " + dbImg);
                    user.setProfileImage(dbImg);
                    // ------------------
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean updateUserProfile(User user) {
        if (user == null || user.getUserId() <= 0) {
            return false;
        }

        String sql = "UPDATE users SET username = ?, contact_no = ?, whatsapp_no = ?, address = ?, profile_image = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername() != null ? user.getUsername().trim() : "");
            stmt.setString(2, user.getContactNo());
            stmt.setString(3, user.getWhatsappNo());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getProfileImage());
            stmt.setInt(6, user.getUserId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
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

    public boolean registerUser(User user) {
        // SQL Statement (Parameters 6ක් පමණි)
        String sql = "INSERT INTO users (custom_id, username, email, password_hash, role, status) VALUES (?, ?, ?, ?, ?, ?)";
        String updateCustomIdSql = "UPDATE users SET custom_id = ? WHERE user_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            int generatedUserId = -1;
            String tempCustomId = "TEMP-" + UUID.randomUUID().toString().substring(0, 8);
            
            // Password Hash කිරීම
            String hashedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
            
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, tempCustomId);
                stmt.setString(2, user.getUsername());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, hashedPassword); // Fix: Positional parameter 4
                stmt.setString(5, user.getRole());       // Fix: Positional parameter 5
                stmt.setString(6, user.getStatus() != null ? user.getStatus() : "ACTIVE"); // Fix: Positional parameter 6

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
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Registration SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close(); 
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    public User authenticateUser(String email, String plainPassword) {
        String sql = "SELECT u.*, s.staff_id, s.full_name FROM users u " +
                     "LEFT JOIN staff s ON u.user_id = s.user_id " +
                     "WHERE u.email = ?";
        User user = null;

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password_hash");
                    boolean isPasswordValid = false;

                    if (storedHashedPassword != null && storedHashedPassword.startsWith("$2a$")) {
                        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), storedHashedPassword);
                        isPasswordValid = result.verified;
                    } else {
                        // Old plaintext passwords support (If needed during dev)
                        isPasswordValid = plainPassword.equals(storedHashedPassword);
                    }

                    if (isPasswordValid) {
                        user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setCustomId(rs.getString("custom_id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setRole(rs.getString("role"));

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
}
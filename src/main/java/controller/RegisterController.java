/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.StaffDAO;
import DAO.UserDAO;
import model.Staff;
import model.User;

/**
 *
 * @author nadis
 */
public class RegisterController {
    private UserDAO userDAO;
    private StaffDAO staffDAO;

    public RegisterController() {
        this.userDAO = new UserDAO();
        this.staffDAO = new StaffDAO();
    }

    public String registerUser(String username, String email, String password, String confirmPassword, String role, String contactNo) {
        if (username == null || username.trim().isEmpty()) {
            return "Username is required";
        }
        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }
        if (role == null || role.trim().isEmpty()) {
            return "Role is required";
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            return "Invalid email format";
        }

        if (password.length() < 6) {
            return "Password must contain at least 6 characters";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }

        if (("DENTIST".equalsIgnoreCase(role) || "RECEPTIONIST".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)) 
                && (contactNo == null || contactNo.trim().isEmpty())) {
            return "Contact number is required for staff roles";
        }

        if (userDAO.isEmailExists(email)) {
            return "Email already registered";
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim());
        user.setPassword(password);
        user.setRole(role.toUpperCase());
        user.setStatus("ACTIVE");

        boolean isSaved;

        if ("PATIENT".equalsIgnoreCase(role)) {
            user.setContactNo(contactNo);
            isSaved = userDAO.registerUser(user);
        } else {
            Staff staff = new Staff();
            staff.setFullName(username.trim());
            staff.setContactNo(contactNo != null ? contactNo.trim() : "");
            staff.setSpecialization("General");

            isSaved = staffDAO.registerStaff(user, staff);
        }

        return isSaved ? "REGISTRATION_SUCCESS" : "DATABASE_ERROR";
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.UserDAO;
import model.User;

/**
 *
 * @author nadis
 */
public class LoginController {
    private UserDAO userDAO;
    
    public LoginController() {
        this.userDAO = new UserDAO();
    }

    public String login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return "Username is required";
        }
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }
        if (email.trim().length() < 3) {
            return "Username must contain at least 3 characters";
        }
        if (password.length() < 5) {
            return "Password must contain at least 5 characters";
        }

        User user = userDAO.authenticateUser(email, password);

        if (user != null) {
            return "LOGIN_SUCCESS";
        } else {
            return "INVALID_CREDENTIALS";
        }
    }
}

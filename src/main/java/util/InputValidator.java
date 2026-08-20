/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.regex.Pattern;

/**
 *
 * @author nadis
 */
public class InputValidator {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+(?: [a-zA-Z0-9_]+)*$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private static final Pattern CONTACT_NO_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidContactNumber(String contactNo) {
        return contactNo != null && CONTACT_NO_PATTERN.matcher(contactNo.trim()).matches();
    }

    public static boolean isValidFullName(String fullName) {
        return fullName != null && NAME_PATTERN.matcher(fullName.trim()).matches();
    }

    public static String sanitizeInput(String input) {
        if (input == null) return "";
        // Basic sanitization to prevent script/HTML injection
        return input.trim().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}

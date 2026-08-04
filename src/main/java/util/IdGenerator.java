/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author nadis
 */
public class IdGenerator {
    public static String generateCustomId(String role, int numericId) {
        String prefix;
        switch (role.toUpperCase()) {
            case "DENTIST":
                prefix = "DEN";
                break;
            case "RECEPTIONIST":
                prefix = "REP";
                break;
            case "PATIENT":
                prefix = "PTN";
                break;
            case "ADMIN":
                prefix = "ADM";
                break;
            default:
                prefix = "USR";
                break;
        }
        return String.format("%s%03d", prefix, numericId);
    }
}

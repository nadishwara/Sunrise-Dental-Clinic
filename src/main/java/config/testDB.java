/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;

/**
 *
 * @author nadis
 */
public class testDB {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("DATABASE CONNECTED SUCCESSFULLY!");
            }
        } catch (Exception e) {
            System.out.println("CONNECTION FAILED: " + e.getMessage());
        }
    }
}

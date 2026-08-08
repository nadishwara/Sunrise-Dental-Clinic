/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mavenproject1;

import javax.swing.UIManager;
import view.loginF;

/**
 *
 * @author nadis
 */
public class Mavenproject1 {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    for (UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            javax.swing.UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(loginF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }

                loginF loginWindow = new loginF();
                loginWindow.setVisible(true);
            }
        });
//        try {
//            com.formdev.flatlaf.FlatDarkLaf.setup();
//            javax.swing.UIManager.put("Window.roundedCorners", true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        java.awt.EventQueue.invokeLater(() -> {
//            new loginF().setVisible(true);
//        });
    }
}

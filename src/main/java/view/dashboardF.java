/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.User;

/**
 *
 * @author nadis
 */
public class dashboardF extends javax.swing.JFrame {

    /**
     * Creates new form dashboardF
     */
    private User loggedInUser;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public dashboardF() {
        initComponents();
        makeCornersRounded();
        init();
    }

    public dashboardF(User user) {
        this.loggedInUser = user;
        initComponents();
        makeCornersRounded();
        init();
    }
    
    private void makeCornersRounded() {
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
            }
        });
    }

    private void init() {
        setLocationRelativeTo(null);
        menu1.initMoving(this);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);

        panelBorder1.setLayout(new java.awt.BorderLayout());
        panelBorder1.add(menu1, java.awt.BorderLayout.WEST);
        panelBorder1.add(mainPanel, java.awt.BorderLayout.CENTER);

        initViews();

        menu1.addEventMenuSelected(new swing.EventMenuSelected() {
            @Override
            public void selected(int index) {
                switch (index) {
                    case 0: 
                        cardLayout.show(mainPanel, "HOME");
                        break;
                    case 1:
                        cardLayout.show(mainPanel, "PROFILE");
                        break;
                    case 2:
                        if (loggedInUser != null && "DOCTOR".equalsIgnoreCase(loggedInUser.getRole())) {
                            cardLayout.show(mainPanel, "PATIENTS_DOCTOR");
                        } else {
                            cardLayout.show(mainPanel, "PATIENTS_GENERAL");
                        }
                        break;
                    case 5: 
                        int opt = JOptionPane.showConfirmDialog(
                                null, 
                                "Are you sure you want to Logout?", 
                                "Logout Confirmation", 
                                JOptionPane.YES_NO_OPTION
                        );
                        if (opt == JOptionPane.YES_OPTION) {
                            new loginF().setVisible(true);
                            dispose();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        cardLayout.show(mainPanel, "HOME");
        
        if (loggedInUser != null) {
        String username = loggedInUser.getUsername();
        String role = loggedInUser.getRole(); 
        
        menu1.setUserProfile(username, role);
    } else {
        menu1.setUserProfile("Guest User", "N/A");
    }
    }

    /**
     * Dashboard Center Panel එකට අවශ්‍ය Views / Forms එකතු කරන ස්ථානය
     */
    private void initViews() {
        // Dummy Views (පසුව ඔබේ නියම JPanel Classes වලින් මේවා replace කරන්න)
        
        // Home Panel
        JPanel homeView = new JPanel();
        homeView.setOpaque(false);
        JLabel lblHome = new JLabel("Welcome to Dashboard Home!");
        lblHome.setForeground(Color.WHITE);
        homeView.add(lblHome);

        // Profile Panel
        JPanel profileView = new JPanel();
        profileView.setOpaque(false);
        String username = (loggedInUser != null) ? loggedInUser.getUsername() : "Guest";
        JLabel lblProfile = new JLabel("User Profile: " + username);
        lblProfile.setForeground(Color.WHITE);
        profileView.add(lblProfile);

        // Patients Panel (Doctor)
        JPanel doctorPatientView = new JPanel();
        doctorPatientView.setOpaque(false);
        JLabel lblDoc = new JLabel("Doctor View: Assigned Patient Records");
        lblDoc.setForeground(Color.WHITE);
        doctorPatientView.add(lblDoc);

        // Patients Panel (General/Receptionist)
        JPanel generalPatientView = new JPanel();
        generalPatientView.setOpaque(false);
        JLabel lblGen = new JLabel("General View: All Patient Records");
        lblGen.setForeground(Color.WHITE);
        generalPatientView.add(lblGen);

        // Cards mainPanel එකට Register කිරීම
        mainPanel.add(homeView, "HOME");
        mainPanel.add(profileView, "PROFILE");
        mainPanel.add(doctorPatientView, "PATIENTS_DOCTOR");
        mainPanel.add(generalPatientView, "PATIENTS_GENERAL");
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorder1 = new swing.PanelBorder();
        menu1 = new view.components.Menu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 792, Short.MAX_VALUE))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu1, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(dashboardF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dashboardF().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private view.components.Menu menu1;
    private swing.PanelBorder panelBorder1;
    // End of variables declaration//GEN-END:variables
}

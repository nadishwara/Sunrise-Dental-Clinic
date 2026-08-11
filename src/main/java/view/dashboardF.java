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
import view.dentistViews.ManagePatients;
import view.forms.AnalyticsForm;
import view.forms.MedicalHistory;
import view.forms.PatientRequestForm;
import view.forms.WelcomePage;

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
    private PatientRequestForm patientRequestView;
    private view.forms.UserProfile userProfileView;
    private view.forms.NewBookings newBookingsView;
    private view.forms.WelcomePage newWelcomePage;
    private view.forms.MedicalHistory MedicalHistory;
    private view.forms.AnalyticsForm analyticsFormView;
    private view.dentistViews.DailyAppointment dailyAppointmentView;

    private view.dentistViews.ManagePatients managePatientView;

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
        if (user != null) {
            menu1.setUserProfile(user);
        }
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

        patientRequestView = new view.forms.PatientRequestForm();

        initViews();

        menu1.addEventMenuSelected(new swing.EventMenuSelected() {
            @Override
            public void selected(int index) {
                User currentUser = menu1.getLoggedInUser() != null ? menu1.getLoggedInUser() : loggedInUser;
                String role = (currentUser != null && currentUser.getRole() != null) ? currentUser.getRole() : "";

                boolean isReceptionist = "RECEPTIONIST".equalsIgnoreCase(role.trim());
                boolean isPatient = "PATIENT".equalsIgnoreCase(role.trim());
                boolean isDentist = "DENTIST".equalsIgnoreCase(role.trim()) || "DOCTOR".equalsIgnoreCase(role.trim());

//                int logoutIndex = isReceptionist ? 5 : (isPatient ? 4 : 4);
                int logoutIndex = isReceptionist ? 5 : 4;

                if (index == logoutIndex) {
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
                    return; 
                }

                if (index == 0) {
                    if (isReceptionist || isDentist) {
                        cardLayout.show(mainPanel, "ANALYTICS_HOME");
                    } else {
                        cardLayout.show(mainPanel, "HOME");
                    }
                } else if (index == 1) {
                    cardLayout.show(mainPanel, "PROFILE");
                } else if (index == 2) {
                    if (isReceptionist) {
                        cardLayout.show(mainPanel, "NEW_BOOKING");
                    } else if (isPatient) {
                        cardLayout.show(mainPanel, "MEDICAL_HISTORY");
                    } else if (isDentist) {
                        // Switch to ManagePatients panel
                        cardLayout.show(mainPanel, "MANAGE_PATIENTS");
                    } else {
                        cardLayout.show(mainPanel, "PATIENTS_GENERAL");
                    }
                } else if (isReceptionist && index == 3) {
                    patientRequestView.loadTableData();
                    cardLayout.show(mainPanel, "PATIENT_REQUEST");
                } else if (isPatient && index == 3) {
                    cardLayout.show(mainPanel, "BILLING_PAYMENTS");
                } else if (isDentist) {
                    if (dailyAppointmentView != null) {
                        dailyAppointmentView.loadDailySchedule();
                    }
                    cardLayout.show(mainPanel, "DAILY_SCHEDULE");
                } else if (index == logoutIndex) {
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
                }
            }
        });

        String role = (loggedInUser != null && loggedInUser.getRole() != null) ? loggedInUser.getRole() : "";
        if ("RECEPTIONIST".equalsIgnoreCase(role.trim()) || "DENTIST".equalsIgnoreCase(role.trim()) || "DOCTOR".equalsIgnoreCase(role.trim())) {
            cardLayout.show(mainPanel, "ANALYTICS_HOME");
        } else {
            cardLayout.show(mainPanel, "HOME");
        }

        if (loggedInUser != null) {
            String username = loggedInUser.getUsername();
            String userRole = loggedInUser.getRole();
            menu1.setUserProfile(username, userRole);
        } else {
            menu1.setUserProfile("Guest User", "N/A");
        }
    }

    private void initViews() {
        JPanel homeView = new JPanel();
        homeView.setOpaque(false);
        JLabel lblHome = new JLabel("Welcome to Dashboard Home!");
        lblHome.setForeground(Color.WHITE);
        homeView.add(lblHome);

        userProfileView = new view.forms.UserProfile();
        if (loggedInUser != null) {
            userProfileView.setUserData(loggedInUser);
        }

        int dentistId = (loggedInUser != null) ? loggedInUser.getUserId() : 0;

        try {
            managePatientView = new ManagePatients(dentistId);
        } catch (Exception e) {
            managePatientView = new ManagePatients();
        }

        JPanel doctorPatientView = new JPanel();
        doctorPatientView.setOpaque(false);
        JLabel lblDoc = new JLabel("Doctor View: Assigned Patient Records");
        lblDoc.setForeground(Color.WHITE);
        doctorPatientView.add(lblDoc);

        JPanel generalPatientView = new JPanel();
        generalPatientView.setOpaque(false);
        JLabel lblGen = new JLabel("General View: All Patient Records");
        lblGen.setForeground(Color.WHITE);
        generalPatientView.add(lblGen);

        patientRequestView = new view.forms.PatientRequestForm();

        newBookingsView = new view.forms.NewBookings();
        String currentRole = (loggedInUser != null && loggedInUser.getRole() != null) ? loggedInUser.getRole() : "";
        newWelcomePage = new WelcomePage(loggedInUser);
        MedicalHistory = new MedicalHistory(loggedInUser);
        analyticsFormView = new AnalyticsForm();

        managePatientView = new ManagePatients(dentistId);
        dailyAppointmentView = new view.dentistViews.DailyAppointment(dentistId);

        javax.swing.JScrollPane newBookingsScrollPane = new javax.swing.JScrollPane(newBookingsView);
        newBookingsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        newBookingsScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        newBookingsScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        newBookingsScrollPane.setBorder(null);

        mainPanel.add(newWelcomePage, "HOME");
        mainPanel.add(analyticsFormView, "ANALYTICS_HOME");
        mainPanel.add(userProfileView, "PROFILE");
        mainPanel.add(managePatientView, "MANAGE_PATIENTS");
        mainPanel.add(dailyAppointmentView, "DAILY_SCHEDULE");
        mainPanel.add(doctorPatientView, "PATIENTS_DOCTOR");
        mainPanel.add(generalPatientView, "PATIENTS_GENERAL");
        mainPanel.add(newBookingsScrollPane, "NEW_BOOKING");
        mainPanel.add(patientRequestView, "PATIENT_REQUEST");
        mainPanel.add(MedicalHistory, "MEDICAL_HISTORY");
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
                .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 871, Short.MAX_VALUE))
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

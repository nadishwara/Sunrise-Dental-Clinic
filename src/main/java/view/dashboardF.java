/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.User;
import view.adminView.reportAnalysis;
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
    private view.forms.PatientBilling patientBillingView;
    private reportAnalysis reportAnalysisView;
    private view.adminView.UserManagement userManagementView;

    private view.dentistViews.ManagePatients managePatientView;

    public dashboardF() {
        initComponents();
        setResizable(true);
        makeCornersRounded();
        init();
    }

    public dashboardF(User user) {
        this.loggedInUser = user;
        initComponents();
        setResizable(true);
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
                if ((getExtendedState() & MAXIMIZED_BOTH) == 0) {
                    setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
                } else {
                    setShape(null);
                }
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
                User currentUser = menu1.getLoggedInUser() != null ? menu1.getLoggedInUser() : loggedInUser;
                String role = (currentUser != null && currentUser.getRole() != null) ? currentUser.getRole() : "";

                boolean isAdmin = "ADMIN".equalsIgnoreCase(role.trim());
                boolean isReceptionist = "RECEPTIONIST".equalsIgnoreCase(role.trim());
                boolean isPatient = "PATIENT".equalsIgnoreCase(role.trim());
                boolean isDentist = "DENTIST".equalsIgnoreCase(role.trim()) || "DOCTOR".equalsIgnoreCase(role.trim());

                int logoutIndex = isReceptionist ? 5 : (isAdmin ? 4 : 4);

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
                    if (isAdmin || isReceptionist || isDentist) {
                        cardLayout.show(mainPanel, "ANALYTICS_HOME");
                    } else {
                        cardLayout.show(mainPanel, "HOME");
                    }
                } else if (index == 1) {
                    if (currentUser != null && userProfileView != null) {
                        userProfileView.setUserData(currentUser);
                    }
                    cardLayout.show(mainPanel, "PROFILE");
                } else if (index == 2) {
                    if (isAdmin) {
                        cardLayout.show(mainPanel, "REPORT_ANALYSIS");
                    } else if (isReceptionist) {
                        cardLayout.show(mainPanel, "NEW_BOOKING");
                    } else if (isPatient) {
                        cardLayout.show(mainPanel, "MEDICAL_HISTORY");
                    } else if (isDentist) {
                        cardLayout.show(mainPanel, "MANAGE_PATIENTS");
                    } else {
                        cardLayout.show(mainPanel, "PATIENTS_GENERAL");
                    }
                } else if (index == 3) {
                    if (isAdmin) {
                        if (userManagementView != null) {
                            userManagementView.loadStaffTableData();
                        }
                        cardLayout.show(mainPanel, "USER_MANAGEMENT");
                    } else if (isReceptionist) {
                        if (patientRequestView != null) {
                            patientRequestView.loadTableData();
                        }
                        cardLayout.show(mainPanel, "PATIENT_REQUEST");
                    } else if (isPatient) {
                        cardLayout.show(mainPanel, "BILLING_PAYMENTS");
                    } else if (isDentist) {
                        if (dailyAppointmentView != null) {
                            dailyAppointmentView.loadDailySchedule();
                        }
                        cardLayout.show(mainPanel, "DAILY_SCHEDULE");
                    }
                } else if (index == 4 && isReceptionist) {
                    cardLayout.show(mainPanel, "PATIENT_BILLING");
                }
               
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        // Default view when dashboard opens
        String role = (loggedInUser != null && loggedInUser.getRole() != null) ? loggedInUser.getRole() : "";
        if ("ADMIN".equalsIgnoreCase(role.trim()) || "RECEPTIONIST".equalsIgnoreCase(role.trim()) || "DENTIST".equalsIgnoreCase(role.trim()) || "DOCTOR".equalsIgnoreCase(role.trim())) {
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
        userProfileView = new view.forms.UserProfile();
        if (loggedInUser != null) {
            userProfileView.setUserData(loggedInUser);
        }

        int dentistId = (loggedInUser != null) ? loggedInUser.getUserId() : 0;

        try {
            managePatientView = new ManagePatients(dentistId);
        } catch (Exception e) {
            System.err.println("ManagePatients load error: " + e.getMessage());
        }

        try {
            reportAnalysisView = new reportAnalysis();
        } catch (Exception e) {
            System.err.println("reportAnalysis load error: " + e.getMessage());
        }

        try {
            dailyAppointmentView = new view.dentistViews.DailyAppointment(dentistId);
        } catch (Exception e) {
            System.err.println("DailyAppointment load error: " + e.getMessage());
        }

        patientRequestView = new view.forms.PatientRequestForm();
        newBookingsView = new view.forms.NewBookings();
        newWelcomePage = new WelcomePage(loggedInUser);
        MedicalHistory = new MedicalHistory(loggedInUser);
        analyticsFormView = new AnalyticsForm();
        patientBillingView = new view.forms.PatientBilling();

        JPanel doctorPatientView = new JPanel();
        doctorPatientView.setOpaque(false);

        JPanel generalPatientView = new JPanel();
        generalPatientView.setOpaque(false);

        JScrollPane newBookingsScrollPane = new javax.swing.JScrollPane(newBookingsView);
        newBookingsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        newBookingsScrollPane.setBorder(null);

        if (newWelcomePage != null) {
            mainPanel.add(newWelcomePage, "HOME");
        }
        if (analyticsFormView != null) {
            mainPanel.add(analyticsFormView, "ANALYTICS_HOME");
        }
        if (reportAnalysisView != null) {
            JScrollPane reportScrollPane = new JScrollPane(reportAnalysisView);
            reportScrollPane.getVerticalScrollBar().setUnitIncrement(16);
            reportScrollPane.setBorder(null);
            reportScrollPane.setOpaque(false);
            reportScrollPane.getViewport().setOpaque(false);

            mainPanel.add(reportScrollPane, "REPORT_ANALYSIS");
        }
        if (userProfileView != null) {
            mainPanel.add(userProfileView, "PROFILE");
        }
        if (managePatientView != null) {
            mainPanel.add(managePatientView, "MANAGE_PATIENTS");
        }
        if (dailyAppointmentView != null) {
            mainPanel.add(dailyAppointmentView, "DAILY_SCHEDULE");
        }
        if (doctorPatientView != null) {
            mainPanel.add(doctorPatientView, "PATIENTS_DOCTOR");
        }
        if (generalPatientView != null) {
            mainPanel.add(generalPatientView, "PATIENTS_GENERAL");
        }
        if (newBookingsScrollPane != null) {
            mainPanel.add(newBookingsScrollPane, "NEW_BOOKING");
        }
        if (patientRequestView != null) {
            mainPanel.add(patientRequestView, "PATIENT_REQUEST");
        }
        if (MedicalHistory != null) {
            mainPanel.add(MedicalHistory, "MEDICAL_HISTORY");
        }
        if (patientBillingView != null) {
            mainPanel.add(patientBillingView, "PATIENT_BILLING");
        }
        userManagementView = new view.adminView.UserManagement();
            if (userManagementView != null) {
                mainPanel.add(userManagementView, "USER_MANAGEMENT");
            }
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
        controlButtons1 = new swing.ControlButtons();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(796, 796, 796)
                .addComponent(controlButtons1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu1, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(controlButtons1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private swing.ControlButtons controlButtons1;
    private view.components.Menu menu1;
    private swing.PanelBorder panelBorder1;
    // End of variables declaration//GEN-END:variables
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.forms;

import DAO.AppointmentDAO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import model.User;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.Appointment;
import javax.swing.Box;

/**
 *
 * @author nadis
 */
public class MedicalHistory extends javax.swing.JPanel {

    private User loggedInUser;
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private JPanel containerPanel;
    private JScrollPane jScrollPane1;

    public MedicalHistory() {
        initComponents();
        setupDynamicScrollPane();
    }

    public MedicalHistory(User user) {
        initComponents();
        this.loggedInUser = user;
        setupDynamicScrollPane();
        loadPatientMedicalHistory();
    }

    private void setupDynamicScrollPane() {
        // Create dynamic container panel for rows
        containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(new java.awt.Color(228, 239, 232));

        // Wrap containerPanel inside a JScrollPane
        jScrollPane1 = new JScrollPane(containerPanel);
        jScrollPane1.setBorder(null);
        jScrollPane1.setBackground(new java.awt.Color(228, 239, 232));
        jScrollPane1.getViewport().setBackground(new java.awt.Color(228, 239, 232));

        // Remove static roundedPanel1 placeholder and add jScrollPane1 dynamically to jPanel1
        jPanel1.remove(roundedPanel1);

        javax.swing.GroupLayout jPanel1Layout = (javax.swing.GroupLayout) jPanel1.getLayout();
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel2)
                        .addGap(48, 48, 48)
                        .addComponent(jLabel4)
                        .addGap(49, 49, 49)
                        .addComponent(jLabel3)
                        .addGap(55, 55, 55)
                        .addComponent(jLabel5)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel6)
                        .addGap(132, 132, 132)
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(66, 66, 66)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    public void loadPatientMedicalHistory() {
        if (loggedInUser != null) {
            List<Appointment> historyList = appointmentDAO.getMedicalHistoryByPatientId(loggedInUser.getUserId());
            containerPanel.removeAll();

            if (historyList != null && !historyList.isEmpty()) {
                for (Appointment app : historyList) {
                    JPanel rowPanel = createHistoryRow(
                        app.getAppointmentDate() != null ? app.getAppointmentDate().toString() : "N/A",
                        app.getAppointmentTime() != null ? app.getAppointmentTime() : "N/A",
                        app.getTreatmentType() != null ? app.getTreatmentType() : "General Consultation",
                        loggedInUser.getUsername()!= null ? loggedInUser.getUsername(): "N/A",
                        app.getDentistName() != null ? app.getDentistName() : "N/A",
                        app
                    );
                    containerPanel.add(rowPanel);
                    containerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between rows
                }
            } else {
                JPanel emptyPanel = createHistoryRow("N/A", "N/A", "No Records Found", "N/A", "-", null);
                containerPanel.add(emptyPanel);
            }

            containerPanel.revalidate();
            containerPanel.repaint();
        }
    }

    private JPanel createHistoryRow(String date, String time, String type, String patientName, String doctorName, Appointment app) {
        JPanel panel = new swing.roundedPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));
        panel.setMaximumSize(new Dimension(830, 55));
        panel.setPreferredSize(new Dimension(830, 55));

        JLabel dLabel = new JLabel(date);
        dLabel.setPreferredSize(new Dimension(75, 20));
        
        JLabel tLabel = new JLabel(time);
        tLabel.setPreferredSize(new Dimension(65, 20));
        
        JLabel tyLabel = new JLabel(type);
        tyLabel.setPreferredSize(new Dimension(90, 20));
        
        JLabel pLabel = new JLabel(patientName);
        pLabel.setPreferredSize(new Dimension(110, 20));

        JLabel docLabel = new JLabel(doctorName);
        docLabel.setPreferredSize(new Dimension(120, 20));
        
        JButton localPrintButton = new JButton("Print");
        localPrintButton.setBackground(new java.awt.Color(51, 51, 255));
        localPrintButton.setForeground(Color.WHITE);
        
        JButton localViewButton = new JButton("View");
        localViewButton.setBackground(new java.awt.Color(153, 255, 102));
        localViewButton.setForeground(Color.WHITE);

        if (app != null) {
            localPrintButton.addActionListener(e -> {
                // Add print logic using app.getAppointmentId()
            });
            localViewButton.addActionListener(e -> {
                // Add view logic using app.getAppointmentId()
            });
        } else {
            localPrintButton.setEnabled(false);
            localViewButton.setEnabled(false);
        }

        panel.add(dLabel);
        panel.add(tLabel);
        panel.add(tyLabel);
        panel.add(pLabel);
        panel.add(docLabel);
        panel.add(localPrintButton);
        panel.add(localViewButton);

        return panel;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        roundedPanel1 = new swing.roundedPanel();
        dateLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        assigndoctorLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        patientNameLabel = new javax.swing.JLabel();
        printButton = new javax.swing.JButton();
        viewButton = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(228, 239, 232));

        jLabel1.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 36)); // NOI18N
        jLabel1.setText("Medical History");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("Date");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Type");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Time");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setText("Patient Name");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText("Assign Doctor");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel7.setText("Action");

        dateLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        dateLabel.setText("Date");
        dateLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dateLabelMouseClicked(evt);
            }
        });

        timeLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        timeLabel.setText("Time");
        timeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                timeLabelMouseClicked(evt);
            }
        });

        assigndoctorLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        assigndoctorLabel.setText("Assign Doctor");
        assigndoctorLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                assigndoctorLabelMouseClicked(evt);
            }
        });

        typeLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        typeLabel.setText("Type");
        typeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                typeLabelMouseClicked(evt);
            }
        });

        patientNameLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        patientNameLabel.setText("Patient Name");
        patientNameLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                patientNameLabelMouseClicked(evt);
            }
        });

        printButton.setBackground(new java.awt.Color(51, 51, 255));
        printButton.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        printButton.setForeground(new java.awt.Color(255, 255, 255));
        printButton.setText("Print");
        printButton.setToolTipText("");
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        viewButton.setBackground(new java.awt.Color(153, 255, 102));
        viewButton.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        viewButton.setForeground(new java.awt.Color(255, 255, 255));
        viewButton.setText("View");
        viewButton.setToolTipText("");
        viewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundedPanel1Layout = new javax.swing.GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(dateLabel)
                .addGap(46, 46, 46)
                .addComponent(timeLabel)
                .addGap(77, 77, 77)
                .addComponent(typeLabel)
                .addGap(75, 75, 75)
                .addComponent(patientNameLabel)
                .addGap(74, 74, 74)
                .addComponent(assigndoctorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                .addComponent(printButton)
                .addGap(18, 18, 18)
                .addComponent(viewButton)
                .addGap(18, 18, 18))
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(dateLabel)
                        .addComponent(timeLabel)
                        .addComponent(patientNameLabel)
                        .addComponent(assigndoctorLabel)
                        .addComponent(typeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(printButton)
                        .addComponent(viewButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel2)
                        .addGap(48, 48, 48)
                        .addComponent(jLabel4)
                        .addGap(49, 49, 49)
                        .addComponent(jLabel3)
                        .addGap(55, 55, 55)
                        .addComponent(jLabel5)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel6)
                        .addGap(132, 132, 132)
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(66, 66, 66)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(401, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_viewButtonActionPerformed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_printButtonActionPerformed

    private void patientNameLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_patientNameLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_patientNameLabelMouseClicked

    private void typeLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_typeLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_typeLabelMouseClicked

    private void assigndoctorLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_assigndoctorLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_assigndoctorLabelMouseClicked

    private void timeLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_timeLabelMouseClicked

    private void dateLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_dateLabelMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel assigndoctorLabel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel patientNameLabel;
    private javax.swing.JButton printButton;
    private swing.roundedPanel roundedPanel1;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JButton viewButton;
    // End of variables declaration//GEN-END:variables
}

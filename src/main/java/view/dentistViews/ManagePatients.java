/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.dentistViews;

import DAO.AppointmentDAO;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author nadis
 */
public class ManagePatients extends javax.swing.JPanel {

    /**
     * Creates new form ManagePatients
     */
    private int loggedInDentistId;
    private AppointmentDAO appointmentDAO;

    public interface TreatmentSaveListener {

        void onTreatmentSaved();
    }

    public ManagePatients() {
        initComponents();
        searchbar1.attachToTable(jTable1);
        loadScheduledAppointments();
    }

    public ManagePatients(int dentistUserId) {
        initComponents();
        this.loggedInDentistId = dentistUserId;
        this.appointmentDAO = new AppointmentDAO();
        loadScheduledAppointments();
        searchbar1.attachToTable(jTable1);
    }

    private void loadScheduledAppointments() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        model.setColumnIdentifiers(new String[]{
            "Appointment ID", "Patient ID", "Patient Name", "Treatment Type", "Date", "Time"
        });

        List<Object[]> appointments = appointmentDAO.getScheduledAppointmentsByDentist(loggedInDentistId);

        if (appointments != null) {
            for (Object[] row : appointments) {
                model.addRow(row);
            }
        }
//        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);

        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setWidth(0);

        jTable1.getColumnModel().getColumn(1).setMinWidth(0);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(1).setWidth(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        searchbar1 = new view.components.Searchbar();
        editButton = new javax.swing.JButton();

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Appointment ID", "Patient ID", "Patient Name", "Treatment Type", "Date", "Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 24)); // NOI18N
        jLabel1.setText("Manage Patient Treatment Records");

        jButton1.setBackground(new java.awt.Color(0, 204, 51));
        jButton1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add Treatment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        editButton.setBackground(new java.awt.Color(204, 51, 0));
        editButton.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        editButton.setForeground(new java.awt.Color(255, 255, 255));
        editButton.setText("Edit Treatment");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 775, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(searchbar1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(editButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(233, 233, 233)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchbar1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(editButton)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
                .addGap(103, 103, 103))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a patient row from the table first!",
                    "No Patient Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int modelRow = jTable1.convertRowIndexToModel(selectedRow);
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        int appId = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
        int patientId = Integer.parseInt(model.getValueAt(modelRow, 1).toString());
        String patientName = model.getValueAt(modelRow, 2).toString();
        String treatment = model.getValueAt(modelRow, 3).toString();
        String appDate = model.getValueAt(modelRow, 4).toString();

        String dentistName = "Dr. Dentist";

        TreatmenyF treatmentFrame = new TreatmenyF(
                appId,
                patientId,
                patientName,
                treatment,
                loggedInDentistId,
                dentistName,
                appDate
        );
        treatmentFrame.setTreatmentSaveListener(new TreatmentSaveListener() {
            @Override
            public void onTreatmentSaved() {
                loadScheduledAppointments();
            }
        });

        treatmentFrame.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a patient record from the table to edit!",
                    "No Record Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int modelRow = jTable1.convertRowIndexToModel(selectedRow);
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        int appId = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
        int patientId = Integer.parseInt(model.getValueAt(modelRow, 1).toString());
        String patientName = model.getValueAt(modelRow, 2).toString();
        String treatment = model.getValueAt(modelRow, 3).toString();
        String appDate = model.getValueAt(modelRow, 4).toString();

        String dentistName = "Dr. Dentist";

        TreatmenyF treatmentFrame = new TreatmenyF(
                appId,
                patientId,
                patientName,
                treatment,
                loggedInDentistId,
                dentistName,
                appDate
        );

        treatmentFrame.setTreatmentSaveListener(new TreatmentSaveListener() {
            @Override
            public void onTreatmentSaved() {
                loadScheduledAppointments();
            }
        });

        treatmentFrame.setVisible(true);
    }//GEN-LAST:event_editButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton editButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private view.components.Searchbar searchbar1;
    // End of variables declaration//GEN-END:variables
}

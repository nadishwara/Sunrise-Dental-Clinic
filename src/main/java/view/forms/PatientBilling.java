/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.forms;

import DAO.BillingDAO;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.BillingDetails;

/**
 *
 * @author nadis
 */
public class PatientBilling extends javax.swing.JPanel {

    private BillingDAO billingDAO;
    private BillingDetails currentBillingDetails;
    private int currentLoggedInReceptionistId = 1;

    public PatientBilling() {
        initComponents();
        billingDAO = new BillingDAO();
        addCalculationListeners();
        loadTableData();
        addTableSelectionListener();

        jScrollPane3.setViewportView(jPanel3);

        jPanel3.setPreferredSize(new java.awt.Dimension(1420, 1600));

        jScrollPane3.getVerticalScrollBar().setUnitIncrement(25);

        jPanel3.revalidate();
        jScrollPane3.revalidate();
        jScrollPane3.repaint();
    }

    private void loadTableData() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        java.util.List<Object[]> rows = billingDAO.getAllPendingAppointmentsForTable();
        for (Object[] row : rows) {
            model.addRow(row);
        }

        searchbar1.attachToTable(jTable1);
    }

    private void addTableSelectionListener() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = jTable1.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = jTable1.convertRowIndexToModel(selectedRow);

                    Object apptIdObj = jTable1.getModel().getValueAt(modelRow, 0);
                    if (apptIdObj != null) {
                        int appointmentId = Integer.parseInt(apptIdObj.toString());
                        loadBillingDetailsById(appointmentId);
                    }
                }
            }
        });
    }

    private void loadBillingDetailsById(int appointmentId) {
        currentBillingDetails = billingDAO.getBillingDetailsByAppointmentId(appointmentId);

        if (currentBillingDetails != null) {
            patintId.setText(currentBillingDetails.getPatientCustomId());
            patintNameLabel1.setText(currentBillingDetails.getPatientName());
            patintContact.setText(currentBillingDetails.getPatientPhone());
            appintmentIDLable.setText(currentBillingDetails.getCustomAppointmentId());
            dateLabel.setText(currentBillingDetails.getAppointmentDate());
            doctorNameLabel.setText("Dr. " + currentBillingDetails.getDoctorName());

            populateLists();

            txtTreatmentCost.setText(String.format("%.2f", currentBillingDetails.getTotalTreatmentCost()));
            txtXrayCost.setText(String.format("%.2f", currentBillingDetails.getTotalXrayCost()));

            calculateTotalCost();
        }
    }

    private void performSearch() {
        String searchText = searchbar1.getSearchTextField().getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.");
            return;
        }
        currentBillingDetails = billingDAO.getBillingDetailsBySearchQuery(searchText);

        if (currentBillingDetails != null) {
            patintId.setText(currentBillingDetails.getPatientCustomId());
            patintNameLabel1.setText(currentBillingDetails.getPatientName());
            patintContact.setText(currentBillingDetails.getPatientPhone());
            appintmentIDLable.setText(currentBillingDetails.getCustomAppointmentId());
            dateLabel.setText(currentBillingDetails.getAppointmentDate());
            doctorNameLabel.setText("Dr. " + currentBillingDetails.getDoctorName());

            populateLists();

            txtTreatmentCost.setText(String.format("%.2f", currentBillingDetails.getTotalTreatmentCost()));
            txtXrayCost.setText(String.format("%.2f", currentBillingDetails.getTotalXrayCost()));

            calculateTotalCost();

        } else {
            JOptionPane.showMessageDialog(this, "No details found!", "Not Found", JOptionPane.WARNING_MESSAGE);
            clearFields();
        }
    }

    private void populateLists() {
        StringBuilder treatmentBuilder = new StringBuilder();
        for (String t : currentBillingDetails.getTreatments()) {
            treatmentBuilder.append(t).append("\n");
        }

        StringBuilder xrayBuilder = new StringBuilder();
        for (String x : currentBillingDetails.getXrays()) {
            xrayBuilder.append(x).append("\n");
        }

        txtTreatments.setText(treatmentBuilder.toString());
        txtXrays.setText(xrayBuilder.toString());
    }

    private void addCalculationListeners() {
        KeyAdapter calcAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateTotalCost();
            }
        };

        txtConsultationFee.addKeyListener(calcAdapter);
        txtOtherCharges.addKeyListener(calcAdapter);
        txtDiscount.addKeyListener(calcAdapter);
    }

    private void calculateTotalCost() {
        try {
            double treatmentCost = currentBillingDetails != null ? currentBillingDetails.getTotalTreatmentCost() : 0.0;
            double xrayCost = currentBillingDetails != null ? currentBillingDetails.getTotalXrayCost() : 0.0;

            double consultationFee = parseDouble(txtConsultationFee.getText());
            double otherCharges = parseDouble(txtOtherCharges.getText());
            double discount = parseDouble(txtDiscount.getText());

            double netAmount = (consultationFee + treatmentCost + xrayCost + otherCharges) - discount;

            lblNetAmount.setText(String.format("Rs. %.2f", netAmount));
        } catch (Exception e) {
            lblNetAmount.setText("Rs. 0.00");
        }
    }

    private double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void saveAndGenerateBill() {
        if (currentBillingDetails == null) {
            JOptionPane.showMessageDialog(this, "Please search for a patient or appointment first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int appointmentId = currentBillingDetails.getAppointmentId();
        int patientUserId = currentBillingDetails.getPatientUserId();

        double consultationFee = parseDouble(txtConsultationFee.getText());
        double otherCharges = parseDouble(txtOtherCharges.getText());
        double discount = parseDouble(txtDiscount.getText());

        boolean isSuccess = billingDAO.processAndGenerateBill(
                appointmentId,
                patientUserId,
                currentLoggedInReceptionistId,
                consultationFee,
                otherCharges,
                discount
        );

        if (isSuccess) {
            JOptionPane.showMessageDialog(this, "Bill saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save the bill.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        currentBillingDetails = null;
        patintId.setText("-");
        patintNameLabel1.setText("-");
        patintContact.setText("-");
        appintmentIDLable.setText("-");
        dateLabel.setText("-");
        doctorNameLabel.setText("-");

        txtConsultationFee.setText("");
        txtOtherCharges.setText("");
        txtDiscount.setText("");
        txtTreatmentCost.setText("0.00");
        txtXrayCost.setText("0.00");
        lblNetAmount.setText("Rs. 0.00");
        lblNetAmount.setText("Rs. 0.00");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane3.getVerticalScrollBar().setUnitIncrement(20);
        jPanel3 = new javax.swing.JPanel();
        roundedPanel1 = new swing.roundedPanel();
        searchbar1 = new view.components.Searchbar();
        jLabel2 = new javax.swing.JLabel();
        roundedPanel2 = new swing.roundedPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        patintNameLabel1 = new javax.swing.JLabel();
        patintId = new javax.swing.JLabel();
        patintContact = new javax.swing.JLabel();
        patintAge = new javax.swing.JLabel();
        appintmentIDLable = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        doctorNameLabel = new javax.swing.JLabel();
        roundedPanel3 = new swing.roundedPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtTreatments = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtXrays = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        prescriptionLabel = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        clinicNoteTextArea = new javax.swing.JTextArea();
        roundedPanel4 = new swing.roundedPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtConsultationFee = new javax.swing.JLabel();
        txtXrayCost = new javax.swing.JLabel();
        txtTreatmentCost = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        lblNetAmount = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel46 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel47 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        printBillButton1 = new javax.swing.JButton();
        pdfDownloadButton = new javax.swing.JButton();
        txtOtherCharges = new javax.swing.JLabel();
        txtDiscount = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jScrollPane2.setViewportView(jTextPane1);

        searchbar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchbar1KeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI Historic", 1, 18)); // NOI18N
        jLabel2.setText("Billing & Invoice Management");

        javax.swing.GroupLayout roundedPanel1Layout = new javax.swing.GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(searchbar1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
            .addGroup(roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(roundedPanel1Layout.createSequentialGroup()
                    .addGap(39, 39, 39)
                    .addComponent(jLabel2)
                    .addContainerGap(147, Short.MAX_VALUE)))
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel1Layout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addComponent(searchbar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
            .addGroup(roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(roundedPanel1Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jLabel2)
                    .addContainerGap(63, Short.MAX_VALUE)))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel1.setText("Patient & Doctor Info");

        jLabel3.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel3.setText("Name:");

        jLabel4.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel4.setText("Patient Details:");

        jLabel5.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel5.setText("Patient ID:");

        jLabel6.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel6.setText("Phone:");

        jLabel7.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel7.setText("Age:");

        jLabel8.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel8.setText("Appointment Info:");

        jLabel9.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel9.setText("Appt ID:");

        jLabel10.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel10.setText("Date:");

        jLabel11.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel11.setText("Doctor:");

        patintNameLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        patintNameLabel1.setText("patintName");

        patintId.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        patintId.setText("Id");

        patintContact.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        patintContact.setText("No");

        patintAge.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        patintAge.setText("age");

        appintmentIDLable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        appintmentIDLable.setText("Id");

        dateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dateLabel.setText("date");

        doctorNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        doctorNameLabel.setText("name");

        javax.swing.GroupLayout roundedPanel2Layout = new javax.swing.GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(roundedPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(roundedPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(appintmentIDLable))
                                        .addGroup(roundedPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel11)
                                            .addGap(39, 39, 39)
                                            .addComponent(doctorNameLabel)
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(roundedPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel10)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(dateLabel)))
                                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel1)
                                            .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(patintNameLabel1)
                                                .addGroup(roundedPanel2Layout.createSequentialGroup()
                                                    .addGap(3, 3, 3)
                                                    .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(patintAge)
                                                        .addComponent(patintContact)
                                                        .addComponent(patintId))))))))
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)))
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel8)))
                .addContainerGap(74, Short.MAX_VALUE))
            .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(roundedPanel2Layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addComponent(jLabel4)
                    .addContainerGap(191, Short.MAX_VALUE)))
        );
        roundedPanel2Layout.setVerticalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(47, 47, 47)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7))
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addComponent(patintNameLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(patintId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(patintContact)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(patintAge)))
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(appintmentIDLable))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(dateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(doctorNameLabel))
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(roundedPanel2Layout.createSequentialGroup()
                    .addGap(65, 65, 65)
                    .addComponent(jLabel4)
                    .addContainerGap(206, Short.MAX_VALUE)))
        );

        jLabel12.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel12.setText("Clinical Summary");

        jLabel13.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel13.setText("Treatments Received:");

        jLabel14.setText("Dental Examination");

        jLabel15.setText("Tooth #25");

        jLabel16.setText("____________________");

        txtTreatments.setText("number");

        jLabel18.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel18.setText("X-Rays Taken:");

        jLabel19.setText("Amoxicillin 500mg (5 Days)");

        jLabel20.setText("_______________");

        txtXrays.setText("details");

        jLabel22.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel22.setText("Prescriptions Issued:");

        jLabel23.setText("Periapical X-Ray");

        jLabel24.setText("_____");

        prescriptionLabel.setText("Price");

        jLabel26.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel26.setText("Doctor's Clinical Notes:");

        clinicNoteTextArea.setColumns(20);
        clinicNoteTextArea.setRows(5);
        jScrollPane1.setViewportView(clinicNoteTextArea);

        javax.swing.GroupLayout roundedPanel3Layout = new javax.swing.GroupLayout(roundedPanel3);
        roundedPanel3.setLayout(roundedPanel3Layout);
        roundedPanel3Layout.setHorizontalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel3Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16))
                            .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addComponent(jLabel12))))
                    .addGroup(roundedPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel13)
                            .addComponent(jLabel22)
                            .addComponent(jLabel26)))
                    .addGroup(roundedPanel3Layout.createSequentialGroup()
                        .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addComponent(jLabel20))
                            .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(prescriptionLabel))
                            .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtTreatments)
                                    .addComponent(txtXrays)))))
                    .addGroup(roundedPanel3Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(roundedPanel3Layout.createSequentialGroup()
                    .addGap(45, 45, 45)
                    .addComponent(jLabel23)
                    .addContainerGap(165, Short.MAX_VALUE)))
        );
        roundedPanel3Layout.setVerticalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(txtTreatments))
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtXrays))
                .addGap(30, 30, 30)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel24)
                    .addComponent(prescriptionLabel))
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
            .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(roundedPanel3Layout.createSequentialGroup()
                    .addGap(170, 170, 170)
                    .addComponent(jLabel23)
                    .addContainerGap(224, Short.MAX_VALUE)))
        );

        jLabel27.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel27.setText("Invoice Calculation");

        jLabel28.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel28.setText("Billing Breakdown:");

        jLabel29.setText("Fee 1");

        jLabel30.setText("Treatment cost");

        jLabel31.setText("X-ray Fee 3");

        jLabel32.setText("Sub Total:");

        txtConsultationFee.setText("Price");

        txtXrayCost.setText("Price");

        txtTreatmentCost.setText("Price");

        jLabel36.setText("Price");

        jLabel37.setText("____________________");

        jLabel38.setText("________");

        jLabel39.setText("_____________");

        jLabel40.setText("____________________");

        jLabel41.setText("Other Charges:");

        jLabel42.setText("Discount (-):");

        jLabel43.setText("TOTAL:");

        lblNetAmount.setText("total");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblNetAmount)
                .addGap(12, 12, 12))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(lblNetAmount))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel44.setText("Payment Method:");

        jCheckBox1.setText("Cash");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/payment-method (1).png"))); // NOI18N

        jCheckBox2.setText("Card");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/atm-card.png"))); // NOI18N

        printBillButton1.setBackground(new java.awt.Color(0, 204, 0));
        printBillButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        printBillButton1.setForeground(new java.awt.Color(255, 255, 255));
        printBillButton1.setText("Print Bill");
        printBillButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBillButton1ActionPerformed(evt);
            }
        });

        pdfDownloadButton.setBackground(new java.awt.Color(0, 0, 255));
        pdfDownloadButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        pdfDownloadButton.setForeground(new java.awt.Color(255, 255, 255));
        pdfDownloadButton.setText("Downloa PDF");
        pdfDownloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdfDownloadButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(pdfDownloadButton)
                .addGap(18, 18, 18)
                .addComponent(printBillButton1)
                .addGap(16, 16, 16))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printBillButton1)
                    .addComponent(pdfDownloadButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtOtherCharges.setText("Price");

        txtDiscount.setText("Price");

        jLabel49.setText("_____________");

        jLabel50.setText("_____________");

        javax.swing.GroupLayout roundedPanel4Layout = new javax.swing.GroupLayout(roundedPanel4);
        roundedPanel4.setLayout(roundedPanel4Layout);
        roundedPanel4Layout.setHorizontalGroup(
            roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel27)
                        .addGap(40, 40, 40)))
                .addGap(48, 48, 48))
            .addGroup(roundedPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox2)
                .addGap(65, 65, 65))
            .addGroup(roundedPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(roundedPanel4Layout.createSequentialGroup()
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                        .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel31)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel30)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel29)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel37))))
                            .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28)
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel32)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel41)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel42)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18)
                        .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTreatmentCost)
                            .addComponent(jLabel36)
                            .addComponent(txtXrayCost)
                            .addComponent(txtConsultationFee)
                            .addComponent(txtOtherCharges)
                            .addComponent(txtDiscount)))
                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel44)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        roundedPanel4Layout.setVerticalGroup(
            roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel27)
                .addGap(18, 18, 18)
                .addComponent(jLabel28)
                .addGap(18, 18, 18)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtTreatmentCost)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtXrayCost)
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtConsultationFee)
                    .addComponent(jLabel40))
                .addGap(18, 18, 18)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txtOtherCharges)
                    .addComponent(jLabel49))
                .addGap(18, 18, 18)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(txtDiscount)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addGap(18, 18, 18)
                        .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox1)
                            .addComponent(jCheckBox2)))
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 683, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 262, Short.MAX_VALUE)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Appt ID", "Appt Custom ID", "Patient Name", "Patient Custom ID", "Date"
            }
        ));
        jScrollPane4.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(roundedPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(roundedPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(roundedPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(731, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(roundedPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(roundedPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(roundedPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(263, 263, 263))
        );

        jScrollPane3.setViewportView(jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 32, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1130, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        if (jCheckBox1.isSelected()) {
            jCheckBox2.setSelected(false);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
        if (jCheckBox2.isSelected()) {
            jCheckBox1.setSelected(false);
        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void printBillButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBillButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_printBillButton1ActionPerformed

    private void pdfDownloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdfDownloadButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pdfDownloadButtonActionPerformed

    private void searchbar1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchbar1KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            performSearch();
        }
    }//GEN-LAST:event_searchbar1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel appintmentIDLable;
    private javax.swing.JTextArea clinicNoteTextArea;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel doctorNameLabel;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel lblNetAmount;
    private javax.swing.JLabel patintAge;
    private javax.swing.JLabel patintContact;
    private javax.swing.JLabel patintId;
    private javax.swing.JLabel patintNameLabel1;
    private javax.swing.JButton pdfDownloadButton;
    private javax.swing.JLabel prescriptionLabel;
    private javax.swing.JButton printBillButton1;
    private swing.roundedPanel roundedPanel1;
    private swing.roundedPanel roundedPanel2;
    private swing.roundedPanel roundedPanel3;
    private swing.roundedPanel roundedPanel4;
    private view.components.Searchbar searchbar1;
    private javax.swing.JLabel txtConsultationFee;
    private javax.swing.JLabel txtDiscount;
    private javax.swing.JLabel txtOtherCharges;
    private javax.swing.JLabel txtTreatmentCost;
    private javax.swing.JLabel txtTreatments;
    private javax.swing.JLabel txtXrayCost;
    private javax.swing.JLabel txtXrays;
    // End of variables declaration//GEN-END:variables
}

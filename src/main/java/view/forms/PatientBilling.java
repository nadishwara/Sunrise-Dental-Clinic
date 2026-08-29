/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.forms;

import DAO.BillingDAO;
import model.BillingDetails;

import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

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
        DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        java.util.List<Object[]> rows = billingDAO.getAllPendingAppointmentsForTable();
        for (Object[] row : rows) {
            model.addRow(row);
        }
        searchbar1.attachToTable(jTable1);
        
        System.out.println("Total rows loaded in JTable: " + model.getRowCount());
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

            // Populate editable financial fields if a bill already exists
            txtConsultationFee.setText(currentBillingDetails.getConsultationFee() > 0 ? String.format("%.2f", currentBillingDetails.getConsultationFee()) : "");
            txtOtherCharges.setText(currentBillingDetails.getOtherCharges() > 0 ? String.format("%.2f", currentBillingDetails.getOtherCharges()) : "");
            txtDiscount.setText(currentBillingDetails.getDiscount() > 0 ? String.format("%.2f", currentBillingDetails.getDiscount()) : "");

            calculateTotalCost();
        }
    }

    private void performSearch() {
        String searchText = searchbar1.getSearchTextField().getText().trim();
        if (searchText.isEmpty()) {
            loadTableData();
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

            txtConsultationFee.setText(currentBillingDetails.getConsultationFee() > 0 ? String.format("%.2f", currentBillingDetails.getConsultationFee()) : "");
            txtOtherCharges.setText(currentBillingDetails.getOtherCharges() > 0 ? String.format("%.2f", currentBillingDetails.getOtherCharges()) : "");
            txtDiscount.setText(currentBillingDetails.getDiscount() > 0 ? String.format("%.2f", currentBillingDetails.getDiscount()) : "");

            calculateTotalCost();

        } else {
            JOptionPane.showMessageDialog(this, "No details found!", "Not Found", JOptionPane.WARNING_MESSAGE);
            loadTableData();
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

        StringBuilder prescriptionBuilder = new StringBuilder();
        for (String p : currentBillingDetails.getPrescriptions()) {
            prescriptionBuilder.append(p).append("\n");
        }

        txtTreatments.setText(treatmentBuilder.toString());
        txtXrays.setText(xrayBuilder.toString());
        clinicNoteTextArea.setText(currentBillingDetails.getClinicalNotes() != null ? currentBillingDetails.getClinicalNotes() : "");
        prescriptionLabel.setText("<html>" + prescriptionBuilder.toString().replaceAll("\n", "<br/>") + "</html>");
    }

    private void addCalculationListeners() {
        javax.swing.event.DocumentListener docListener = new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calculateTotalCost();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calculateTotalCost();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calculateTotalCost();
            }
        };

        txtConsultationFee.getDocument().addDocumentListener(docListener);
        txtOtherCharges.getDocument().addDocumentListener(docListener);
        txtDiscount.getDocument().addDocumentListener(docListener);
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

    public PatientBilling(int appointmentId) {
        this();
        displayBillForAppointment(appointmentId);
    }

    /**
     * Public method to load and display bill details dynamically.
     */
    public void displayBillForAppointment(int appointmentId) {
        loadBillingDetailsById(appointmentId);
        jScrollPane3.getVerticalScrollBar().setValue(0);
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
            JOptionPane.showMessageDialog(this, "Bill generated and saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadTableData();
            displayBillForAppointment(appointmentId);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save the bill.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        currentBillingDetails = null;
        patintId.setText("-");
        patintNameLabel1.setText("-");
        patintContact.setText("-");
        patintAge.setText("-");
        appintmentIDLable.setText("-");
        dateLabel.setText("-");
        doctorNameLabel.setText("-");

        txtConsultationFee.setText("");
        txtOtherCharges.setText("");
        txtDiscount.setText("");
        txtTreatmentCost.setText("0.00");
        txtXrayCost.setText("0.00");
        lblNetAmount.setText("Rs. 0.00");
        txtTreatments.setText("");
        txtXrays.setText("");
        clinicNoteTextArea.setText("");
        jTable1.clearSelection();
    }

    private String getPatientEmailByAppointmentId(int appointmentId) {
        String email = null;
        String sql = "SELECT COALESCE(p.email, u.email) AS email "
                + "FROM appointments a "
                + "LEFT JOIN patients p ON a.patient_id = p.patient_id "
                + "LEFT JOIN users u ON a.patient_id = u.user_id "
                + "WHERE a.appointment_id = ?";

        try (java.sql.Connection conn = config.DBConnection.getInstance().getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    email = rs.getString("email");
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return email;
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
        txtTreatments = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtXrays = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
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
        txtXrayCost = new javax.swing.JLabel();
        txtTreatmentCost = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
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
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        txtOtherCharges = new javax.swing.JTextField();
        txtDiscount = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txtConsultationFee = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        saveUpdateBillButton = new javax.swing.JButton();
        printBillButton1 = new javax.swing.JButton();
        mailButton = new javax.swing.JButton();

        jScrollPane2.setViewportView(jTextPane1);

        setBackground(new java.awt.Color(255, 255, 255));

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

        jLabel15.setText("Tooth :");

        txtTreatments.setText("number");

        jLabel18.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel18.setText("X-Rays Taken:");

        jLabel19.setText("Amoxicillin 500mg (5 Days)");

        jLabel20.setText("_______________");

        txtXrays.setText("details");

        jLabel22.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel22.setText("Prescriptions Issued:");

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
                        .addGap(86, 86, 86)
                        .addComponent(jLabel12))
                    .addGroup(roundedPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel26)
                            .addComponent(jLabel22)
                            .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtXrays))
                            .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(roundedPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(prescriptionLabel))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(roundedPanel3Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(roundedPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addComponent(txtTreatments)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        roundedPanel3Layout.setVerticalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtTreatments))
                .addGap(40, 40, 40)
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtXrays)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel24)
                    .addComponent(prescriptionLabel))
                .addGap(2, 2, 2)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLabel27.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jLabel27.setText("Invoice Calculation");

        jLabel28.setFont(new java.awt.Font("Segoe UI Historic", 0, 12)); // NOI18N
        jLabel28.setText("Billing Breakdown:");

        jLabel29.setText("Fee 1");

        jLabel30.setText("Treatment cost");

        jLabel31.setText("X-ray Fee 3");

        txtXrayCost.setText("Price");

        txtTreatmentCost.setText("Price");

        jLabel36.setText("Price");

        jLabel37.setText("____________________");

        jLabel38.setText("________");

        jLabel39.setText("_____________");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 209, Short.MAX_VALUE)
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

        jLabel49.setText("_____________");

        jLabel50.setText("_____________");

        txtOtherCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOtherChargesActionPerformed(evt);
            }
        });

        txtDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiscountActionPerformed(evt);
            }
        });

        jLabel45.setText("Consultation Fee");

        jLabel51.setText("_____________");

        txtConsultationFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtConsultationFeeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundedPanel4Layout = new javax.swing.GroupLayout(roundedPanel4);
        roundedPanel4.setLayout(roundedPanel4Layout);
        roundedPanel4Layout.setHorizontalGroup(
            roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel4Layout.createSequentialGroup()
                .addContainerGap(120, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addGap(88, 88, 88))
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
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                                .addComponent(jLabel37)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTreatmentCost)
                            .addComponent(jLabel36)
                            .addComponent(txtXrayCost)))
                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel41)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel42)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtOtherCharges)
                                    .addComponent(txtDiscount)))
                            .addGroup(roundedPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel45)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtConsultationFee))))
                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel44))
                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(jLabel51)
                    .addComponent(txtConsultationFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel49)
                    .addComponent(txtOtherCharges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(jLabel50)
                    .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addGap(18, 18, 18)
                        .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox1)
                            .addComponent(jCheckBox2)))
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(50, 50, 50))
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
                "Appt ID", "Appt Custom ID", "Patient Name", "Patient Custom ID", "Date", "Status"
            }
        ));
        jScrollPane4.setViewportView(jTable1);

        saveUpdateBillButton.setBackground(new java.awt.Color(0, 0, 255));
        saveUpdateBillButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        saveUpdateBillButton.setForeground(new java.awt.Color(255, 255, 255));
        saveUpdateBillButton.setText("Update Bill");
        saveUpdateBillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveUpdateBillButtonActionPerformed(evt);
            }
        });

        printBillButton1.setBackground(new java.awt.Color(0, 204, 0));
        printBillButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        printBillButton1.setForeground(new java.awt.Color(255, 255, 255));
        printBillButton1.setText("Print Bill");
        printBillButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBillButton1ActionPerformed(evt);
            }
        });

        mailButton.setBackground(new java.awt.Color(153, 51, 0));
        mailButton.setFont(new java.awt.Font("Segoe UI Historic", 1, 18)); // NOI18N
        mailButton.setForeground(new java.awt.Color(255, 255, 255));
        mailButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/email.png"))); // NOI18N
        mailButton.setText("Mail Bill");
        mailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mailButtonActionPerformed(evt);
            }
        });

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
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(roundedPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(49, 49, 49)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(mailButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(saveUpdateBillButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(27, 27, 27)
                                        .addComponent(printBillButton1)))))
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
                                .addComponent(roundedPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(saveUpdateBillButton)
                                    .addComponent(printBillButton1))
                                .addGap(10, 10, 10)
                                .addComponent(mailButton))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(roundedPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(roundedPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(239, 239, 239))
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

    private void searchbar1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchbar1KeyReleased
        // TODO add your handling code here:
        String searchText = searchbar1.getSearchTextField().getText().trim();
        if (searchText.isEmpty()) {
            loadTableData();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            performSearch();
        }
    }//GEN-LAST:event_searchbar1KeyReleased

    private void txtConsultationFeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtConsultationFeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtConsultationFeeActionPerformed

    private void txtDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiscountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountActionPerformed

    private void txtOtherChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOtherChargesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOtherChargesActionPerformed

    private void saveUpdateBillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveUpdateBillButtonActionPerformed
        // TODO add your handling code here:
        if (currentBillingDetails == null) {
            JOptionPane.showMessageDialog(this, "Please select or search for an appointment first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int appointmentId = currentBillingDetails.getAppointmentId();

        int receptionistUserId = currentLoggedInReceptionistId;

        double consultationFee = parseDouble(txtConsultationFee.getText());
        double otherCharges = parseDouble(txtOtherCharges.getText());
        double discount = parseDouble(txtDiscount.getText());

        boolean isSuccess = billingDAO.updateBill(appointmentId, receptionistUserId, consultationFee, otherCharges, discount);

        if (isSuccess) {
            JOptionPane.showMessageDialog(this, "Bill updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadTableData();
            displayBillForAppointment(appointmentId);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update the bill.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveUpdateBillButtonActionPerformed

    private void printBillButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBillButton1ActionPerformed
        // TODO add your handling code here:
        if (currentBillingDetails == null) {
            JOptionPane.showMessageDialog(this, "Please search or select a patient bill first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Bill as PDF");
        fileChooser.setSelectedFile(new File("Invoice_" + appintmentIDLable.getText() + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
                document.open();

                String apptId = appintmentIDLable.getText();
                String patientName = patintNameLabel1.getText();
                String patientId = patintId.getText();
                String phone = patintContact.getText();
                String date = dateLabel.getText();
                String doctor = doctorNameLabel.getText();
                String netTotal = lblNetAmount.getText();

                Paragraph title = new Paragraph("DENTAL CLINIC INVOICE");
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("-------------------------------------------------------------------------------------------------"));

                document.add(new Paragraph("Appointment ID : " + apptId));
                document.add(new Paragraph("Patient Name   : " + patientName));
                document.add(new Paragraph("Patient ID     : " + patientId));
                document.add(new Paragraph("Contact Number : " + phone));
                document.add(new Paragraph("Date           : " + date));
                document.add(new Paragraph("Doctor         : " + doctor));
                document.add(new Paragraph("-------------------------------------------------------------------------------------------------"));

                document.add(new Paragraph("Treatment Cost : " + txtTreatmentCost.getText()));
                document.add(new Paragraph("X-Ray Cost     : " + txtXrayCost.getText()));
                document.add(new Paragraph("Consultation   : " + txtConsultationFee.getText()));
                document.add(new Paragraph("Other Charges  : " + txtOtherCharges.getText()));
                document.add(new Paragraph("Discount       : " + txtDiscount.getText()));
                document.add(new Paragraph("-------------------------------------------------------------------------------------------------"));

                Paragraph total = new Paragraph("Net Amount     : " + netTotal);
                total.setAlignment(Element.ALIGN_RIGHT);
                document.add(total);

                document.close();

                JOptionPane.showMessageDialog(this, "PDF Downloaded Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(fileToSave);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error generating PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_printBillButton1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
        if (jCheckBox2.isSelected()) {
            jCheckBox1.setSelected(false);
        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        if (jCheckBox1.isSelected()) {
            jCheckBox2.setSelected(false);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void mailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mailButtonActionPerformed
        // TODO add your handling code here:
        try {
            int selectedRow = jTable1.getSelectedRow();

            if (selectedRow == -1) {
                javax.swing.JOptionPane.showMessageDialog(this, "Please select an appointment/bill from the table first!", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            int appointmentId = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());

            BillingDAO billingDAO = new BillingDAO();
            BillingDetails details = billingDAO.getBillingDetailsByAppointmentId(appointmentId);

            if (details == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Bill details not found!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            String defaultEmail = getPatientEmailByAppointmentId(appointmentId);
            if (defaultEmail == null || defaultEmail.equals("N/A")) {
                defaultEmail = "";
            }
            String patientEmail = (String) javax.swing.JOptionPane.showInputDialog(
                    this,
                    "Confirm or enter the patient's email address for Bill ID: " + details.getCustomBillId(),
                    "Send Bill via Email",
                    javax.swing.JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    defaultEmail
            );
            if (patientEmail == null) {
                return;
            }

            patientEmail = patientEmail.trim();
            if (patientEmail.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Email address cannot be empty!", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean emailSent = util.EmailSender.sendBillEmail(
                    patientEmail,
                    details.getPatientName(),
                    details.getCustomBillId(),
                    details.getCustomAppointmentId(),
                    details.getAppointmentDate(),
                    details.getDoctorName(),
                    details.getConsultationFee(),
                    details.getTotalTreatmentCost(),
                    details.getTotalXrayCost(),
                    details.getOtherCharges(),
                    details.getDiscount(),
                    details.getNetAmount(),
                    details.getPaymentStatus(),
                    details.getTreatments()
            );

            if (emailSent) {
                javax.swing.JOptionPane.showMessageDialog(this, "Bill successfully emailed to " + patientEmail, "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Failed to send email. Check console for details.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid Appointment ID format.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_mailButtonActionPerformed


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
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel lblNetAmount;
    private javax.swing.JButton mailButton;
    private javax.swing.JLabel patintAge;
    private javax.swing.JLabel patintContact;
    private javax.swing.JLabel patintId;
    private javax.swing.JLabel patintNameLabel1;
    private javax.swing.JLabel prescriptionLabel;
    private javax.swing.JButton printBillButton1;
    private swing.roundedPanel roundedPanel1;
    private swing.roundedPanel roundedPanel2;
    private swing.roundedPanel roundedPanel3;
    private swing.roundedPanel roundedPanel4;
    private javax.swing.JButton saveUpdateBillButton;
    private view.components.Searchbar searchbar1;
    private javax.swing.JTextField txtConsultationFee;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtOtherCharges;
    private javax.swing.JLabel txtTreatmentCost;
    private javax.swing.JLabel txtTreatments;
    private javax.swing.JLabel txtXrayCost;
    private javax.swing.JLabel txtXrays;
    // End of variables declaration//GEN-END:variables
}

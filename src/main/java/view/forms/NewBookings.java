package view.forms;

import DAO.AppointmentDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class NewBookings extends javax.swing.JPanel {

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final Map<String, Integer> dentistMap = new HashMap<>();

    public NewBookings() {
        initComponents();
        clearFields();
        loadDentists();
        loadAppointmentsTable();
    }

    private void loadDentists() {
        dentistComboBox1.removeAllItems();
        dentistMap.clear();
        dentistComboBox1.addItem("-- Select Dentist --");

        List<Object[]> dentists = appointmentDAO.getActiveDentists();
        for (Object[] dentist : dentists) {
            int id = (int) dentist[0];
            String name = (String) dentist[1];
            dentistComboBox1.addItem(name);
            dentistMap.put(name, id);
        }
    }

    private void loadAppointmentsTable() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Appointment ID", "Patient Name", "Address", "Contact No",
                    "Whatsapp No", "Assign Dentist", "Treatment Type", "Date", "Time"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(model);

        List<Object[]> list = appointmentDAO.getAllAppointmentsForTable();
        for (Object[] row : list) {
            model.addRow(row);
        }
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setWidth(0);
    }

    private void clearFields() {
        nameField1.setText("");
        addressField2.setText("");
        contactField.setText("");
        whatsappField.setText("");
        dentistComboBox1.setSelectedIndex(0);
        treatmentComboBox2.setSelectedIndex(0);
        timeComboBox3.setSelectedIndex(0);
        jDateChooser1.setDate(new Date());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        nameField1 = new javax.swing.JTextField();
        addressField2 = new javax.swing.JTextField();
        contactField = new javax.swing.JTextField();
        whatsappField = new javax.swing.JTextField();
        dentistComboBox1 = new javax.swing.JComboBox<>();
        treatmentComboBox2 = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        timeComboBox3 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        button1 = new java.awt.Button();
        button2 = new java.awt.Button();

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Schedule New Appointment");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Patient Name");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Address");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Contact number");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Whatsapp number");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Assign Dentist name ");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Treatment type");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Appointment Date");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Time");

        nameField1.setText("jTextField1");
        nameField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameField1ActionPerformed(evt);
            }
        });

        addressField2.setText("jTextField1");
        addressField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressField2ActionPerformed(evt);
            }
        });

        contactField.setText("jTextField1");
        contactField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactFieldActionPerformed(evt);
            }
        });

        whatsappField.setText("jTextField1");
        whatsappField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                whatsappFieldActionPerformed(evt);
            }
        });

        dentistComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        dentistComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dentistComboBox1ActionPerformed(evt);
            }
        });

        treatmentComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "General & Preventive Care", "Restorative Dentistry", "Prosthodontics & Cosmetic", "Orthodontics & Periodontics" }));
        treatmentComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                treatmentComboBox2ActionPerformed(evt);
            }
        });

        jDateChooser1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateChooser1MouseClicked(evt);
            }
        });

        timeComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "08:30 AM - 09:30 AM", "09:30 AM - 10:30 AM", "10:30 AM - 11:30 AM", "11:30 AM - 12:30 PM", "01:30 PM - 02:30 PM", "02:30 PM - 03:30 PM", "03:30 PM - 04:30 PM", "04:30 PM - 05:30 PM", "05:30 PM - 06:30 PM", "06:30 PM - 07:30 PM" }));
        timeComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeComboBox3ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Patient Name", "Address", "Contact No", "Whatsapp No", "Assign Dentist", "Treatment Type", "Date", "Time"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        button1.setBackground(new java.awt.Color(51, 255, 0));
        button1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setLabel("Save");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        button2.setBackground(new java.awt.Color(255, 51, 0));
        button2.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        button2.setForeground(new java.awt.Color(255, 255, 255));
        button2.setLabel("Delete");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(62, 62, 62))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(103, 103, 103))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(40, 40, 40))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(20, 20, 20))
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(48, 48, 48))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(20, 20, 20))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(130, 130, 130)))
                        .addGap(84, 84, 84)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(whatsappField, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                            .addComponent(contactField, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                            .addComponent(addressField2, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                            .addComponent(nameField1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                            .addComponent(dentistComboBox1, 0, 306, Short.MAX_VALUE)
                            .addComponent(treatmentComboBox2, 0, 306, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                            .addComponent(timeComboBox3, 0, 306, Short.MAX_VALUE))
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(button2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(19, 19, 19))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(188, 188, 188)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                        .addGap(238, 238, 238)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(300, 300, 300))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(nameField1)))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(addressField2)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(contactField)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(whatsappField)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(dentistComboBox1)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(treatmentComboBox2)))
                                .addGap(17, 17, 17))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(3, 3, 3)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(timeComboBox3)
                                        .addGap(3, 3, 3))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(button2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(314, 314, 314))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nameField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameField1ActionPerformed

    private void addressField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addressField2ActionPerformed

    private void contactFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contactFieldActionPerformed

    private void whatsappFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_whatsappFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_whatsappFieldActionPerformed

    private void dentistComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dentistComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dentistComboBox1ActionPerformed

    private void treatmentComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_treatmentComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_treatmentComboBox2ActionPerformed

    private void jDateChooser1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateChooser1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser1MouseClicked

    private void timeComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_timeComboBox3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            nameField1.setText(model.getValueAt(selectedRow, 1) != null ? model.getValueAt(selectedRow, 1).toString() : "");
            addressField2.setText(model.getValueAt(selectedRow, 2) != null ? model.getValueAt(selectedRow, 2).toString() : "");
            contactField.setText(model.getValueAt(selectedRow, 3) != null ? model.getValueAt(selectedRow, 3).toString() : "");
            whatsappField.setText(model.getValueAt(selectedRow, 4) != null ? model.getValueAt(selectedRow, 4).toString() : "");

            if (model.getValueAt(selectedRow, 5) != null) {
                dentistComboBox1.setSelectedItem(model.getValueAt(selectedRow, 5).toString());
            }

            if (model.getValueAt(selectedRow, 6) != null) {
                treatmentComboBox2.setSelectedItem(model.getValueAt(selectedRow, 6).toString());
            }

            if (model.getValueAt(selectedRow, 7) != null) {
                jDateChooser1.setDate((Date) model.getValueAt(selectedRow, 7));
            }

            if (model.getValueAt(selectedRow, 8) != null) {
                timeComboBox3.setSelectedItem(model.getValueAt(selectedRow, 8).toString());
            }
        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
        String name = nameField1.getText().trim();
        String address = addressField2.getText().trim();
        String contact = contactField.getText().trim();
        String whatsapp = whatsappField.getText().trim();
        String selectedDentist = (String) dentistComboBox1.getSelectedItem();
        String treatment = (String) treatmentComboBox2.getSelectedItem();
        Date selectedDate = jDateChooser1.getDate();
        String timeSlot = (String) timeComboBox3.getSelectedItem();

        if (name.isEmpty() || contact.isEmpty() || selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields (Name, Contact, Date).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dentistComboBox1.getSelectedIndex() == 0 || !dentistMap.containsKey(selectedDentist)) {
            JOptionPane.showMessageDialog(this, "Please select a valid Dentist.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int dentistId = dentistMap.get(selectedDentist);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(selectedDate);
        boolean success = appointmentDAO.saveDirectBooking(name, address, contact, whatsapp, dentistId, treatment, formattedDate, timeSlot);

        if (success) {
            JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadAppointmentsTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to schedule appointment. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_button1ActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an appointment from the table to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this appointment?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                int appointmentId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

                boolean success = appointmentDAO.deleteAppointment(appointmentId);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Appointment deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    clearFields();
                    loadAppointmentsTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to delete appointment. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error processing deletion: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_button2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressField2;
    private java.awt.Button button1;
    private java.awt.Button button2;
    private javax.swing.JTextField contactField;
    private javax.swing.JComboBox<String> dentistComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField nameField1;
    private javax.swing.JComboBox<String> timeComboBox3;
    private javax.swing.JComboBox<String> treatmentComboBox2;
    private javax.swing.JTextField whatsappField;
    // End of variables declaration//GEN-END:variables
}

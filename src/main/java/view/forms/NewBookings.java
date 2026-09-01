package view.forms;

import DAO.AppointmentDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class NewBookings extends javax.swing.JPanel {

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final Map<String, Integer> dentistMap = new HashMap<>();
    private TableRowSorter<DefaultTableModel> rowSorter;

    public NewBookings() {
        initComponents();
        clearFields();
        loadDentists();
        loadAppointmentsTable();
        initSearchListener();
        this.setPreferredSize(new java.awt.Dimension(750, 950));
    }
    
    private void initSearchListener() {
        if (searchbar1 != null && searchbar1.getSearchTextField() != null) {
            searchbar1.getSearchTextField().addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    searchbar1KeyReleased(evt);
                }
            });
        }
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
        rowSorter = new TableRowSorter<>(model);
        jTable1.setRowSorter(rowSorter);
    }

    private void clearFields() {
        nameField1.setText("");
        addressField.setText("");
        emailaddressField1.setText("");
        contactField.setText("");
        dentistComboBox1.setSelectedIndex(0);
        treatmentComboBox2.setSelectedIndex(0);
        timeComboBox3.setSelectedIndex(0);
        jDateChooser1.setDate(new Date());
        jTable1.clearSelection();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        nameField1 = new javax.swing.JTextField();
        addressField = new javax.swing.JTextField();
        contactField = new javax.swing.JTextField();
        dentistComboBox1 = new javax.swing.JComboBox<>();
        treatmentComboBox2 = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        timeComboBox3 = new javax.swing.JComboBox<>();
        button1 = new java.awt.Button();
        button2 = new java.awt.Button();
        mailScheduleAppointmentbutton = new java.awt.Button();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        emailaddressField1 = new javax.swing.JTextField();
        searchbar1 = new view.components.Searchbar();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Schedule New Appointment");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Patient Name");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Email Address");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Contact number");

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

        addressField.setText("jTextField1");
        addressField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressFieldActionPerformed(evt);
            }
        });

        contactField.setText("jTextField1");
        contactField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactFieldActionPerformed(evt);
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

        mailScheduleAppointmentbutton.setBackground(new java.awt.Color(0, 0, 204));
        mailScheduleAppointmentbutton.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        mailScheduleAppointmentbutton.setForeground(new java.awt.Color(255, 255, 255));
        mailScheduleAppointmentbutton.setLabel("Mail Schedule");
        mailScheduleAppointmentbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mailScheduleAppointmentbuttonActionPerformed(evt);
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
        jScrollPane2.setViewportView(jTable1);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Address");

        emailaddressField1.setText("jTextField1");
        emailaddressField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailaddressField1ActionPerformed(evt);
            }
        });

        searchbar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchbar1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 661, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(237, 237, 237)
                        .addComponent(jLabel4)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(searchbar1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(50, 50, 50))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(contactField, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dentistComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(treatmentComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(timeComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nameField1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(mailScheduleAppointmentbutton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(emailaddressField1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(20, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel4)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailaddressField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(contactField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(mailScheduleAppointmentbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dentistComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(treatmentComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timeComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(searchbar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nameField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameField1ActionPerformed

    private void addressFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addressFieldActionPerformed

    private void contactFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contactFieldActionPerformed

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
            int modelRow = jTable1.convertRowIndexToModel(selectedRow);
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            nameField1.setText(model.getValueAt(modelRow, 1) != null ? model.getValueAt(modelRow, 1).toString() : "");
            addressField.setText(model.getValueAt(modelRow, 2) != null ? model.getValueAt(modelRow, 2).toString() : "");
            contactField.setText(model.getValueAt(modelRow, 3) != null ? model.getValueAt(modelRow, 3).toString() : "");
            emailaddressField1.setText(model.getValueAt(modelRow, 4) != null ? model.getValueAt(modelRow, 4).toString() : "");

            if (model.getValueAt(modelRow, 5) != null) {
                dentistComboBox1.setSelectedItem(model.getValueAt(modelRow, 5).toString());
            }

            if (model.getValueAt(modelRow, 6) != null) {
                treatmentComboBox2.setSelectedItem(model.getValueAt(modelRow, 6).toString());
            }

            if (model.getValueAt(modelRow, 7) != null) {
                try {
                    Object dateObj = model.getValueAt(modelRow, 7);
                    if (dateObj instanceof Date) {
                        jDateChooser1.setDate((Date) dateObj);
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        jDateChooser1.setDate(sdf.parse(dateObj.toString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (model.getValueAt(modelRow, 8) != null) {
                timeComboBox3.setSelectedItem(model.getValueAt(modelRow, 8).toString());
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:                                       
        String name = nameField1.getText().trim();
        String email = emailaddressField1.getText().trim();
        String address = addressField.getText().trim();
        String contact = contactField.getText().trim();
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

        int selectedRow = jTable1.getSelectedRow();
        int appointmentId = 0;

        if (selectedRow != -1) {
            try {
                appointmentId = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
            } catch (Exception e) {
                appointmentId = 0;
            }
        }

        boolean success = appointmentDAO.saveOrUpdateBooking(
                appointmentId, name, email, contact, address, dentistId, treatment, formattedDate, timeSlot
        );

        if (success) {
            String msg = (appointmentId > 0) ? "Appointment updated successfully!" : "Appointment scheduled successfully!";
            JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadAppointmentsTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save appointment. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void mailScheduleAppointmentbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mailScheduleAppointmentbuttonActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an appointment from the table to send mail.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        String patientName = model.getValueAt(selectedRow, 1).toString();
        String dentist = model.getValueAt(selectedRow, 5).toString();
        String treatment = model.getValueAt(selectedRow, 6).toString();
        String date = model.getValueAt(selectedRow, 7).toString();
        String time = model.getValueAt(selectedRow, 8).toString();

        String recipientEmail = JOptionPane.showInputDialog(this,
                "Enter Patient Email Address for " + patientName + ":",
                "Send Email Confirmation", JOptionPane.PLAIN_MESSAGE);
        if (recipientEmail != null && !recipientEmail.trim().isEmpty()) {

            new Thread(() -> {
                boolean success = util.EmailSender.sendAppointmentEmail(
                        recipientEmail.trim(), patientName, dentist, treatment, date, time
                );

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Appointment details sent to " + recipientEmail + " successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to send email. Please check your network connection or email setup.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }).start();
        }
    }//GEN-LAST:event_mailScheduleAppointmentbuttonActionPerformed

    private void emailaddressField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailaddressField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailaddressField1ActionPerformed

    private void searchbar1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchbar1KeyReleased
        // TODO add your handling code here:
        if (searchbar1 != null && searchbar1.getSearchTextField() != null) {
            String searchText = searchbar1.getSearchTextField().getText().trim();
            if (rowSorter != null) {
                if (searchText.isEmpty()) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, 0, 1));
                }
            }
        }
    }//GEN-LAST:event_searchbar1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressField;
    private java.awt.Button button1;
    private java.awt.Button button2;
    private javax.swing.JTextField contactField;
    private javax.swing.JComboBox<String> dentistComboBox1;
    private javax.swing.JTextField emailaddressField1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private java.awt.Button mailScheduleAppointmentbutton;
    private javax.swing.JTextField nameField1;
    private view.components.Searchbar searchbar1;
    private javax.swing.JComboBox<String> timeComboBox3;
    private javax.swing.JComboBox<String> treatmentComboBox2;
    // End of variables declaration//GEN-END:variables
}

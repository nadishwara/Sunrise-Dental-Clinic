/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.forms;

import DAO.AppointmentRequestDAO;
import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.io.File;
import javax.swing.table.DefaultTableModel;
import model.Model_AppointmentRequest;
import model.User;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import swing.RoundedImageIconLabel;

/**
 *
 * @author nadis
 */
public class UserProfile extends javax.swing.JPanel {

    /**
     * Creates new form UserProfile
     */
    private User currentUser;
    private RoundedImageIconLabel roundedProfileLabel;

    public UserProfile() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));

        roundedProfileLabel = new RoundedImageIconLabel();
        roundedProfileLabel.setBounds(jLabel1.getBounds());
        roundedProfileLabel.setSize(jLabel1.getSize());

        Container parent = jLabel1.getParent();
        if (parent != null) {
            parent.remove(jLabel1);
            parent.add(roundedProfileLabel);
            parent.revalidate();
            parent.repaint();
        }
    }

    public void setUserData(User user) {
        this.currentUser = user;
        setBackground(new Color(0, 0, 0, 0));
        loadDataToUI();
        this.revalidate();
        this.repaint();
    }

    private void loadDataToUI() {
        if (currentUser == null) {
            return;
        }

        usernameLable.setText(currentUser.getUsername());
        userRoleLable.setText(currentUser.getRole());
        emailLable.setText("Email: " + (currentUser.getEmail() != null ? currentUser.getEmail() : "N/A"));
        statusLabel.setText("Status: " + (currentUser.getStatus() != null ? currentUser.getStatus() : "ACTIVE"));

        String contact_no = currentUser.getContactNo();
        String whatsapp = currentUser.getWhatsappNo();
        String address = currentUser.getAddress();

        contactLabel.setText("Contact: " + (contact_no != null && !contact_no.trim().isEmpty() ? contact_no : "N/A"));
        whatsappLabel.setText("WhatsApp: " + (whatsapp != null && !whatsapp.trim().isEmpty() ? whatsapp : "N/A"));
        addressLabel.setText("Address: " + (address != null && !address.trim().isEmpty() ? address : "N/A"));

        String imgPath = currentUser.getProfileImage();
        System.out.println("DEBUG [UserProfile] - Current User Profile Image Path: " + imgPath);

        if (imgPath != null && !imgPath.trim().isEmpty()) {
            try {
                File imgFile = new File(imgPath);
                if (!imgFile.exists()) {
                    imgFile = new File(System.getProperty("user.dir"), imgPath);
                }

                System.out.println("DEBUG [UserProfile] - File Absolute Path: " + imgFile.getAbsolutePath());
                System.out.println("DEBUG [UserProfile] - File Exists?: " + imgFile.exists());

                if (imgFile.exists()) {
                    ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());

                    int width = roundedProfileLabel.getWidth();
                    int height = roundedProfileLabel.getHeight();

                    if (width <= 0 || height <= 0) {
                        width = jLabel1.getWidth() > 0 ? jLabel1.getWidth() : 120;
                        height = jLabel1.getHeight() > 0 ? jLabel1.getHeight() : 120;
                    }

                    Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    roundedProfileLabel.setIcon(new ImageIcon(img));
                    roundedProfileLabel.revalidate();
                    roundedProfileLabel.repaint();

                    System.out.println("DEBUG [UserProfile] - Image loaded and UI refreshed successfully!");
                } else {
                    System.out.println("DEBUG [UserProfile] - ERROR: Image file does not exist at this path!");
                    roundedProfileLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/profile.png")));
                }
            } catch (Exception e) {
                System.out.println("DEBUG [UserProfile] - EXCEPTION:");
                e.printStackTrace();
            }
        } else {
            System.out.println("DEBUG [UserProfile] - Profile image path is null or empty.");
            roundedProfileLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/profile.png")));
        }

        String role = currentUser.getRole().toUpperCase();
        if ("PATIENT".equals(role) || "RECEPTIONIST".equals(role) || "DENTIST".equals(role)) {
            jScrollPane1.setVisible(true);
            loadRoleBasedTableData(role);
        } else {
            jScrollPane1.setVisible(false);
        }
    }

    private void loadRoleBasedTableData(String role) {
        AppointmentRequestDAO dao = new AppointmentRequestDAO();
        DefaultTableModel model;

        if ("PATIENT".equals(role)) {
            String[] columnNames = {"Req ID", "Custom ID", "Date", "Slot", "Status"};
            model = new DefaultTableModel(columnNames, 0);
            List<Model_AppointmentRequest> list = dao.getRequestsByPatientUserId(currentUser.getUserId());
            for (Model_AppointmentRequest req : list) {
                model.addRow(new Object[]{
                    req.getRequestId(),
                    req.getPatientCustomId(),
                    req.getPreferredDate(),
                    req.getPreferredTimeSlot(),
                    req.getStatus()
                });
            }
        } else if ("RECEPTIONIST".equals(role)) {
            String[] columnNames = {"Appt ID", "Date", "Time", "Treatment Type", "Status"};
            model = new DefaultTableModel(columnNames, 0);
            List<Object[]> list = dao.getAppointmentsByReceptionistId(currentUser.getUserId());
            for (Object[] row : list) {
                model.addRow(row);
            }
        } else if ("DENTIST".equals(role)) {
            String[] columnNames = {"Treatment ID", "Appt ID", "Treatment Name", "Tooth No", "Cost"};
            model = new DefaultTableModel(columnNames, 0);
            List<Object[]> list = dao.getTreatmentsByDentistUserId(currentUser.getUserId());
            for (Object[] row : list) {
                model.addRow(row);
            }
        } else {
            model = new DefaultTableModel(new String[]{"Info"}, 0);
        }

        jTable1.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        usernameLable = new javax.swing.JLabel();
        userRoleLable = new javax.swing.JLabel();
        emailLable = new javax.swing.JLabel();
        contactLabel = new javax.swing.JLabel();
        whatsappLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        editButton = new javax.swing.JButton();
        addressLabel = new javax.swing.JLabel();

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/profile.png"))); // NOI18N

        usernameLable.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        usernameLable.setText("Username");
        usernameLable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usernameLableMouseClicked(evt);
            }
        });

        userRoleLable.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        userRoleLable.setText("role");
        userRoleLable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userRoleLableMouseClicked(evt);
            }
        });

        emailLable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        emailLable.setText("email");
        emailLable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emailLableMouseClicked(evt);
            }
        });

        contactLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        contactLabel.setText("Contact");
        contactLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contactLabelMouseClicked(evt);
            }
        });

        whatsappLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        whatsappLabel.setText("Whatsapp");
        whatsappLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                whatsappLabelMouseClicked(evt);
            }
        });

        statusLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        statusLabel.setText("Status");
        statusLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                statusLabelMouseClicked(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        editButton.setBackground(new java.awt.Color(0, 0, 255));
        editButton.setForeground(new java.awt.Color(255, 255, 255));
        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/edit (1).png"))); // NOI18N
        editButton.setText("edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        addressLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        addressLabel.setText("address");
        addressLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addressLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userRoleLable)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(usernameLable)
                                .addGap(10, 10, 10)
                                .addComponent(editButton))))
                    .addComponent(statusLabel)
                    .addComponent(whatsappLabel)
                    .addComponent(contactLabel)
                    .addComponent(emailLable)
                    .addComponent(addressLabel))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(usernameLable)
                            .addComponent(editButton))
                        .addGap(10, 10, 10)
                        .addComponent(userRoleLable))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(emailLable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contactLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(whatsappLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addressLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusLabel)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "No user loaded!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserProfileUpdateF updateFrame = new UserProfileUpdateF(this);
        updateFrame.setUser(currentUser);
        updateFrame.setVisible(true);
        updateFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_editButtonActionPerformed

    private void usernameLableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usernameLableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameLableMouseClicked

    private void userRoleLableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userRoleLableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_userRoleLableMouseClicked

    private void emailLableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailLableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_emailLableMouseClicked

    private void contactLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_contactLabelMouseClicked

    private void whatsappLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_whatsappLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_whatsappLabelMouseClicked

    private void statusLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statusLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_statusLabelMouseClicked

    private void addressLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addressLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_addressLabelMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addressLabel;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel emailLable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel userRoleLable;
    private javax.swing.JLabel usernameLable;
    private javax.swing.JLabel whatsappLabel;
    // End of variables declaration//GEN-END:variables
}

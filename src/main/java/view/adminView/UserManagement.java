/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.adminView;

import DAO.UserDAO;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.User;

/**
 *
 * @author nadis
 */
public class UserManagement extends javax.swing.JPanel {

    /**
     * Creates new form UserManagement
     */
    public UserManagement() {
        initComponents();
        loadStaffTableData();
        initSearchListener();   
    }
    
    private void initSearchListener() {
        if (searchbar2 != null && searchbar2.getSearchTextField() != null) {
            searchbar2.getSearchTextField().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    performSearch();
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    performSearch();
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    performSearch();
                }
            });
        }
    }
    
    public void loadStaffTableData() {
        DefaultTableModel model = (DefaultTableModel) userRequestTable.getModel();
        model.setRowCount(0);

        UserDAO userDAO = new UserDAO();
        List<User> staffList = userDAO.getAllStaffUsers();

        System.out.println("DEBUG: Loaded all staff users. Total count = " + staffList.size());

        for (User user : staffList) {
            model.addRow(new Object[]{
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus() 
            });
        }
    }
    
    private void approveSelectedUser() {
        int selectedRow = userRequestTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff user to approve.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = Integer.parseInt(userRequestTable.getValueAt(selectedRow, 0).toString());
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUserStatus(userId, "ACTIVE");

        if (success) {
            JOptionPane.showMessageDialog(this, "Staff account approved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadStaffTableData(); 
        } else {
            JOptionPane.showMessageDialog(this, "Failed to approve staff account.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rejectSelectedUser() {
        int selectedRow = userRequestTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff user to reject.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to reject this staff account?", "Confirm Rejection", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int userId = Integer.parseInt(userRequestTable.getValueAt(selectedRow, 0).toString());
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.updateUserStatus(userId, "REJECTED");

            if (success) {
                JOptionPane.showMessageDialog(this, "Staff account rejected.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStaffTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to reject staff account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void performSearch() {
        String searchText = "";
        if (searchbar2 != null && searchbar2.getSearchTextField() != null) {
            searchText = searchbar2.getSearchTextField().getText().trim();
        }
        
//        System.out.println("DEBUG: performSearch called with keyword: [" + searchText + "]");
        
        DefaultTableModel model = (DefaultTableModel) userRequestTable.getModel();
        model.setRowCount(0);

        UserDAO userDAO = new UserDAO();
        List<User> staffList;

        if (searchText.isEmpty()) {
            staffList = userDAO.getAllStaffUsers(); 
        } else {
            staffList = userDAO.searchStaffUsers(searchText);
        }

//        System.out.println("DEBUG: Search results found = " + staffList.size());

        for (User user : staffList) {
            model.addRow(new Object[]{
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
            });
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

        searchbar1 = new view.components.Searchbar();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userRequestTable = new javax.swing.JTable();
        rejectButton = new javax.swing.JButton();
        approveButton1 = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        searchbar2 = new view.components.Searchbar();

        searchbar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchbar1KeyReleased(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Pending User Registration Approvals");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/userClock.png"))); // NOI18N

        userRequestTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "User ID", "Username", "Email", "Role", "Status"
            }
        ));
        jScrollPane1.setViewportView(userRequestTable);

        rejectButton.setBackground(new java.awt.Color(153, 0, 0));
        rejectButton.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        rejectButton.setForeground(new java.awt.Color(255, 255, 255));
        rejectButton.setText("Reject Account");
        rejectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rejectButtonActionPerformed(evt);
            }
        });

        approveButton1.setBackground(new java.awt.Color(0, 0, 153));
        approveButton1.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        approveButton1.setForeground(new java.awt.Color(255, 255, 255));
        approveButton1.setText("Approve Account");
        approveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                approveButton1ActionPerformed(evt);
            }
        });

        refreshButton.setBackground(new java.awt.Color(0, 153, 0));
        refreshButton.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        refreshButton.setForeground(new java.awt.Color(255, 255, 255));
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        searchbar2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchbar2KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(301, 301, 301))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(rejectButton)
                        .addGap(20, 20, 20)
                        .addComponent(approveButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(searchbar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel1)))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(refreshButton)
                    .addComponent(searchbar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(approveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rejectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchbar1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchbar1KeyReleased
        // TODO add your handling code here:
//        performSearch();
    }//GEN-LAST:event_searchbar1KeyReleased

    private void rejectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejectButtonActionPerformed
        // TODO add your handling code here:
        rejectSelectedUser();
    }//GEN-LAST:event_rejectButtonActionPerformed

    private void approveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_approveButton1ActionPerformed
        // TODO add your handling code here:
        approveSelectedUser();
    }//GEN-LAST:event_approveButton1ActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        loadStaffTableData();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void searchbar2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchbar2KeyReleased
        // TODO add your handling code here:
        performSearch();
    }//GEN-LAST:event_searchbar2KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton approveButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton rejectButton;
    private view.components.Searchbar searchbar1;
    private view.components.Searchbar searchbar2;
    private javax.swing.JTable userRequestTable;
    // End of variables declaration//GEN-END:variables
}

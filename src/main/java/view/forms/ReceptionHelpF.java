/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view.forms;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
/**
 *
 * @author nadis
 */
public class ReceptionHelpF extends javax.swing.JFrame {

    /**
     * Creates new form ReceptionHelpF
     */
    
    private JScrollPane scrollPane;
    
    // Track expansion state and which section is currently active
    private int activeSection = 0;
    
    /**
     * Creates new form ReceptionHelpF
     */
    public ReceptionHelpF() {
        initComponents();
        initExpandableFeature();
        makeCornersRounded();
    }
    
    private void initExpandableFeature() {
        jLabel1.setVisible(false);
        headingLabel.setVisible(false);
        stepsLabel.setVisible(false);

        scrollPane = new JScrollPane(jPanel1);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Set frame content to scrollPane
        getContentPane().removeAll();
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void toggleContent(int sectionId) {
        if (activeSection == sectionId) {
            activeSection = 0;
            jLabel1.setVisible(false);
            headingLabel.setVisible(false);
            stepsLabel.setVisible(false);
        } else {
            activeSection = sectionId;
            jLabel1.setVisible(true);
            headingLabel.setVisible(true);
            stepsLabel.setVisible(true);

            if (sectionId == 1) {
                headingLabel.setText("Schedule New Appointment.");
                jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/screen shots/Screenshot 2026-08-18 150149.png")));
                stepsLabel.setText("<html>" +
                    "1. Patient Name: Enter the patient's full name into the text field.<br>" +
                    "2. Address: Provide the residential address of the patient.<br>" +
                    "3. Contact number: Enter the primary phone number for communication.<br>" +
                    "4. Whatsapp number: Type the WhatsApp-enabled contact number.<br>" +
                    "5. Assign Dentist name: Select the appropriate dentist from the dropdown menu.<br>" +
                    "6. Treatment type: Choose the required dental treatment category.<br>" +
                    "7. Appointment Date: Pick the preferred date using the calendar/date field.<br>" +
                    "8. Time: Select the available time slot from the dropdown options.<br>" +
                    "9. Save: Click the green 'Save' button to register the appointment into the system." +
                    "</html>");
            } else if (sectionId == 2) {
                headingLabel.setText("Manage Patients.");
                jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/screen shots/image 2.png")));
                stepsLabel.setText("<html>" +
                    "1. Search Patient: Type the name or ID in the search bar.<br>" +
                    "2. View Details: Click on the patient record to view history.<br>" +
                    "3. Update Info: Modify contact details or address if needed.<br>" +
                    "4. Save Changes: Click update to store the modifications." +
                    "</html>");
            } else if (sectionId == 3) {
                headingLabel.setText("Billing & Invoices.");
                jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/screen shots/image 3.png")));
                stepsLabel.setText("<html>" +
                    "1. Select Appointment: Choose the completed session from the list.<br>" +
                    "2. Add Charges: Include consultation and treatment costs.<br>" +
                    "3. Apply Discounts: Enter promo codes or concessions if applicable.<br>" +
                    "4. Print Receipt: Click print to generate the bill." +
                    "</html>");
            }
        }

        jPanel1.revalidate();
        jPanel1.repaint();
    }
    
    private void makeCornersRounded() {
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
            }
        });
    }

//    private void toggleContent() {
//        isExpanded = !isExpanded;
//        if (isExpanded) {
//            jPanel1.add(dynamicContentPanel);
//            dynamicContentPanel.setVisible(true);
//        } else {
//            dynamicContentPanel.setVisible(false);
//            jPanel1.remove(dynamicContentPanel);
//        }
//        jPanel1.revalidate();
//        jPanel1.repaint();
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jMini = new javax.swing.JLabel();
        roundedPanel1 = new swing.roundedPanel();
        jLabel3 = new javax.swing.JLabel();
        arroeDownButton = new javax.swing.JButton();
        headingLabel = new javax.swing.JLabel();
        stepsLabel = new javax.swing.JLabel();
        roundedPanel2 = new swing.roundedPanel();
        jLabel4 = new javax.swing.JLabel();
        arroeDownButton1 = new javax.swing.JButton();
        roundedPanel3 = new swing.roundedPanel();
        jLabel6 = new javax.swing.JLabel();
        arroeDownButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(30, 109, 211));
        jPanel1.setForeground(new java.awt.Color(38, 140, 187));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/close (2).png"))); // NOI18N
        jLabel2.setToolTipText("");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jMini.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/minus.png"))); // NOI18N
        jMini.setToolTipText("");
        jMini.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMiniMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Lucida Fax", 0, 18)); // NOI18N
        jLabel3.setText("Schedule New Appointment.");

        arroeDownButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/down.png"))); // NOI18N
        arroeDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arroeDownButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundedPanel1Layout = new javax.swing.GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(arroeDownButton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(arroeDownButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        headingLabel.setFont(new java.awt.Font("Lucida Fax", 0, 18)); // NOI18N
        headingLabel.setForeground(new java.awt.Color(255, 255, 255));
        headingLabel.setText("Schedule New Appointment.");

        stepsLabel.setFont(new java.awt.Font("Lucida Fax", 0, 18)); // NOI18N
        stepsLabel.setForeground(new java.awt.Color(255, 255, 255));
        stepsLabel.setText("steps");

        jLabel4.setFont(new java.awt.Font("Lucida Fax", 0, 18)); // NOI18N
        jLabel4.setText("Manage Patients");

        arroeDownButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/down.png"))); // NOI18N
        arroeDownButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arroeDownButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundedPanel2Layout = new javax.swing.GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(arroeDownButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        roundedPanel2Layout.setVerticalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(arroeDownButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Lucida Fax", 0, 18)); // NOI18N
        jLabel6.setText("Billin Management");

        arroeDownButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/down.png"))); // NOI18N
        arroeDownButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arroeDownButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundedPanel3Layout = new javax.swing.GroupLayout(roundedPanel3);
        roundedPanel3.setLayout(roundedPanel3Layout);
        roundedPanel3Layout.setHorizontalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(arroeDownButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        roundedPanel3Layout.setVerticalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(arroeDownButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("jLabel5");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMini))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(headingLabel)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(stepsLabel))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(roundedPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(roundedPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jLabel1)))
                .addContainerGap(236, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jMini))
                .addGap(67, 67, 67)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roundedPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(roundedPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addComponent(headingLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stepsLabel)
                .addContainerGap(477, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jMiniMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMiniMouseClicked
        // TODO add your handling code here:
        this.setState(javax.swing.JFrame.ICONIFIED);
    }//GEN-LAST:event_jMiniMouseClicked

    private void arroeDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arroeDownButtonActionPerformed
        // TODO add your handling code here:
        toggleContent(1);
    }//GEN-LAST:event_arroeDownButtonActionPerformed

    private void arroeDownButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arroeDownButton1ActionPerformed
        // TODO add your handling code here:
        toggleContent(2);
    }//GEN-LAST:event_arroeDownButton1ActionPerformed

    private void arroeDownButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arroeDownButton2ActionPerformed
        // TODO add your handling code here:
        toggleContent(3);
    }//GEN-LAST:event_arroeDownButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReceptionHelpF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReceptionHelpF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReceptionHelpF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReceptionHelpF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReceptionHelpF().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton arroeDownButton;
    private javax.swing.JButton arroeDownButton1;
    private javax.swing.JButton arroeDownButton2;
    private javax.swing.JLabel headingLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jMini;
    private javax.swing.JPanel jPanel1;
    private swing.roundedPanel roundedPanel1;
    private swing.roundedPanel roundedPanel2;
    private swing.roundedPanel roundedPanel3;
    private javax.swing.JLabel stepsLabel;
    // End of variables declaration//GEN-END:variables
}

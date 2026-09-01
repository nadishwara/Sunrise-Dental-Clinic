/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view.forms;

import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author nadis
 */
public class ReceptionHelpF extends javax.swing.JFrame {

    private JScrollPane scrollPane;
    private int mouseX, mouseY;
    private int activeSection = 1;

    /**
     * Creates new form ReceptionHelpF
     */
    public ReceptionHelpF() {
        initComponents();
        initExpandableFeature();
        toggleContent(1);
        makeCornersRounded();
        initWindowDragging();
    }
    
    private void initWindowDragging() {
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mouseX = evt.getX();
                mouseY = evt.getY();
            }
        });

        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                setLocation(evt.getXOnScreen() - mouseX, evt.getYOnScreen() - mouseY);
            }
        });
    }
    
    private void initExpandableFeature() {
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
        activeSection = sectionId;
        
        // Ensure labels are visible
        jLabel1.setVisible(true);
        headingLabel.setVisible(true);
        stepsLabel.setVisible(true);

        if (sectionId == 1) {
            headingLabel.setText("Schedule New Appointment");
            try {
                java.net.URL imgUrl = getClass().getResource("/screen shots/Screenshot 2026-08-18 150149.png");
                if (imgUrl != null) {
                    jLabel1.setIcon(new javax.swing.ImageIcon(imgUrl));
                } else {
                    jLabel1.setIcon(null); // Prevents crash if image is missing
                }
            } catch (Exception e) {
                jLabel1.setIcon(null);
            }
            
            stepsLabel.setText("<html><body style='width: 400px; font-family: Segoe UI; font-size: 13px; color: #FFFFFF;'>" +
                "<b>Step 1. Patient Name:</b> Enter the patient's full name into the text field.<br>" +
                "<b>Step 2. Address:</b> Provide the residential address of the patient.<br>" +
                "<b>Step 3. Contact number:</b> Enter the primary phone number for communication.<br>" +
                "<b>Step 4. Whatsapp number:</b> Type the WhatsApp-enabled contact number.<br>" +
                "<b>Step 5. Assign Dentist name:</b> Select the appropriate dentist from the dropdown menu.<br>" +
                "<b>Step 6. Treatment type:</b> Choose the required dental treatment category.<br>" +
                "<b>Step 7. Appointment Date:</b> Pick the preferred date using the calendar/date field.<br>" +
                "<b>Step 8. Time:</b> Select the available time slot from the dropdown options.<br>" +
                "<b>Step 9. Save:</b> Click the green 'Save' button to register the appointment into the system." +
                "</body></html>");
                
        } else if (sectionId == 2) {
            headingLabel.setText("Manage Patient Requests");
            try {
                java.net.URL imgUrl = getClass().getResource("/screen shots/image 5.png");
                if (imgUrl != null) {
                    jLabel1.setIcon(new javax.swing.ImageIcon(imgUrl));
                } else {
                    jLabel1.setIcon(null);
                }
            } catch (Exception e) {
                jLabel1.setIcon(null);
            }
            
            stepsLabel.setText("<html><body style='width: 400px; font-family: Segoe UI; font-size: 13px; color: #FFFFFF;'>" +
                "<b>Step 1:</b> Click Patient Request from the side bar.<br>" +
                "<b>Step 2:</b> Review the patient requests list in the table with details.<br>" +
                "<b>Step 3:</b> To process a request, select the specific patient row from the table.<br>" +
                "&nbsp;&nbsp;&bull; <i>Confirm Request:</i> Click to approve a pending request.<br>" +
                "&nbsp;&nbsp;&bull; <i>Reject Request:</i> Click to decline the request.<br>" +
                "<b>Step 4:</b> Click 'Schedule Appointment' to open the appointment window." +
                "</body></html>");
                
        } else if (sectionId == 3) {
            headingLabel.setText("Billing & Invoices Management");
            try {
                java.net.URL imgUrl = getClass().getResource("/screen shots/image 3.png");
                if (imgUrl != null) {
                    jLabel1.setIcon(new javax.swing.ImageIcon(imgUrl));
                } else {
                    jLabel1.setIcon(null);
                }
            } catch (Exception e) {
                jLabel1.setIcon(null);
            }
            
            stepsLabel.setText("<html><body style='width: 400px; font-family: Segoe UI; font-size: 13px; color: #FFFFFF;'>" +
                "<b>Step 1:</b> Click Billing from the side bar to open the module.<br>" +
                "<b>Step 2:</b> Use the search bar or select an appointment record from the table.<br>" +
                "<b>Step 3:</b> Review populated patient details and clinical notes.<br>" +
                "<b>Step 4:</b> Input charges and apply any discounts (-).<br>" +
                "<b>Step 5:</b> Choose either 'Cash' or 'Card' as the payment option.<br>" +
                "<b>Step 6:</b> Click 'Update Bill' or 'Print Bill' to generate the receipt." +
                "</body></html>");
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
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });

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
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                .addGap(20, 20, 20)
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

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    }

    private void jMiniMouseClicked(java.awt.event.MouseEvent evt) {
        this.setState(javax.swing.JFrame.ICONIFIED);
    }

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {
        this.dispose();
    }

    private void arroeDownButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        toggleContent(2);
    }

    private void arroeDownButtonActionPerformed(java.awt.event.ActionEvent evt) {
        toggleContent(1);
    }

    private void arroeDownButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        toggleContent(3);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReceptionHelpF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

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
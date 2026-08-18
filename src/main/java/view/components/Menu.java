package view.components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import model.Model_Menu;
import model.User;
import swing.EventMenuSelected;
import view.forms.ReceptionHelpF;

public class Menu extends javax.swing.JPanel {

    private String userRole = "";
    private User loggedInUser;

    public Menu() {
        initComponents();
        setOpaque(false);
        listMenu1.setOpaque(false);
        jPanel1.setOpaque(false);
        userLable.setForeground(Color.WHITE);
        roleLabel.setForeground(new Color(220, 220, 220));

        listMenu1.setPreferredSize(null);

        jPanel1.setPreferredSize(new java.awt.Dimension(225, 80));
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 90));

        revalidate();
        repaint();
    }

    public void initMenu(String role) {
        this.userRole = (role != null) ? role : "";

        listMenu1.clear();

        listMenu1.addItem(new Model_Menu("1", "Dashboard", Model_Menu.MenuType.MENU));
        listMenu1.addItem(new Model_Menu("2", "User Profile", Model_Menu.MenuType.MENU));

        if ("RECEPTIONIST".equalsIgnoreCase(userRole)) {
            listMenu1.addItem(new Model_Menu("3", "Manage Patients", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("4", "Patient Request", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("5", "Billing", Model_Menu.MenuType.MENU));
        } else if ("PATIENT".equalsIgnoreCase(userRole)) {
            listMenu1.addItem(new Model_Menu("3", "Medical History", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("4", "Billing & Payments", Model_Menu.MenuType.MENU));
        } else {
            listMenu1.addItem(new Model_Menu("3", "Manage Patients", Model_Menu.MenuType.MENU));
            listMenu1.addItem(new Model_Menu("4", "Daily Appointments", Model_Menu.MenuType.MENU));
        }

        listMenu1.addItem(new Model_Menu("10", "Logout", Model_Menu.MenuType.MENU));
        listMenu1.addItem(new Model_Menu("", "", Model_Menu.MenuType.EMPTY));
    }

    public void setUserProfile(String username, String role) {
        if (userLable != null) {
            userLable.setText(username != null ? username : "User");
        }
        if (roleLabel != null) {
            roleLabel.setText(role != null ? role : "");
        }

        initMenu(role);
    }

    public void addEventMenuSelected(swing.EventMenuSelected event) {
        listMenu1.addEventMenuSelected(event);
    }

    public void setUserProfile(User user) {
        this.loggedInUser = user;
        if (user != null) {
            userLable.setText(user.getUsername() != null ? user.getUsername() : "Guest");
            roleLabel.setText(user.getRole() != null ? user.getRole() : "User");

            initMenu(user.getRole());
        }

        revalidate();
        repaint();
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMoving = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        listMenu1 = new swing.ListMenu<>();
        jPanel1 = new javax.swing.JPanel();
        userLable = new javax.swing.JLabel();
        roleLabel = new javax.swing.JLabel();
        helpLabel = new javax.swing.JLabel();

        setOpaque(false);

        panelMoving.setBackground(new java.awt.Color(255, 255, 255));
        panelMoving.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imageLogo.png"))); // NOI18N
        jLabel2.setText("DASHBOARD");

        javax.swing.GroupLayout panelMovingLayout = new javax.swing.GroupLayout(panelMoving);
        panelMoving.setLayout(panelMovingLayout);
        panelMovingLayout.setHorizontalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMovingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelMovingLayout.setVerticalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMovingLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        userLable.setFont(new java.awt.Font("Segoe UI Historic", 1, 18)); // NOI18N
        userLable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/user.png"))); // NOI18N
        userLable.setText("Username");

        roleLabel.setText("role");

        helpLabel.setForeground(new java.awt.Color(255, 255, 255));
        helpLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/help.png"))); // NOI18N
        helpLabel.setText("help...");
        helpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                helpLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userLable, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(helpLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(roleLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userLable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roleLabel)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMoving, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(listMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMoving, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(200, 200, 200)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void helpLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_helpLabelMouseClicked
        // TODO add your handling code here:
        ReceptionHelpF helpFrame = new ReceptionHelpF();
        helpFrame.setLocationRelativeTo(null);
        helpFrame.setVisible(true);
    }//GEN-LAST:event_helpLabelMouseClicked

    @Override
    protected void paintChildren(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0, Color.decode("#1CB5E0"), 0, getHeight(), Color.decode("#000046"));
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.fillRect(getWidth() - 20, 0, getWidth(), getHeight());
//        super.paintComponents(grphcs); 
        super.paintChildren(grphcs);
    }

    private int x;
    private int y;

    public void initMoving(JFrame fram) {
        panelMoving.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                x = me.getX();
                y = me.getY();
            }

        });
        panelMoving.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                fram.setLocation(me.getXOnScreen() - x, me.getYOnScreen() - y);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel helpLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private swing.ListMenu<String> listMenu1;
    private javax.swing.JPanel panelMoving;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JLabel userLable;
    // End of variables declaration//GEN-END:variables
}

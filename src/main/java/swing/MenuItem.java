
package swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import model.Model_Menu;


public class MenuItem extends javax.swing.JPanel {

   private boolean selected;
    private Model_Menu data;

    public MenuItem() {
        initComponents();
        setOpaque(false);
    }

    public MenuItem(Model_Menu data) {
        this.data = data;
        initComponents();
        setOpaque(false);
        IbName.setForeground(new java.awt.Color(255, 255, 255));
        initData();
    }

    public void setMenuData(Model_Menu data) {
        this.data = data;
        initData();
    }

    private void initData() {
        if (data != null) {
            if (data.getType() == Model_Menu.MenuType.MENU) {
                IbIcon.setIcon(data.toIcon());
                IbName.setText(data.getName());
            } else if (data.getType() == Model_Menu.MenuType.TITLE) {
                IbIcon.setText(data.getName());
                IbIcon.setFont(new Font("sansserif", Font.BOLD, 12));
                IbName.setVisible(false);
            } else {
                IbName.setText(" ");
            }
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        IbIcon = new javax.swing.JLabel();
        IbName = new javax.swing.JLabel();

        IbName.setText("Menu Item");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(IbIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(IbName)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IbName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IbIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        if (selected) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 80));
            g2.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 5, 5);
        }
        super.paintComponent(grphcs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IbIcon;
    private javax.swing.JLabel IbName;
    // End of variables declaration//GEN-END:variables
}

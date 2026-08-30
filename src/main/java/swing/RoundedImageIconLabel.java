/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

/**
 *
 * @author nadis
 */
public class RoundedImageIconLabel extends JLabel{

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, width, height);
        g2.setClip(circle);
        
        Icon icon = getIcon();
        if (icon != null) {
            Image img = ((ImageIcon) icon).getImage();
            g2.drawImage(img, 0, 0, width, height, this);
        }
        
        g2.dispose();
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author pseudo
 */
public class CardPane extends JLayeredPane {
    
    public int layer;
    private JPanel contentPane;
    private GlassPane glassPane;
    
    public CardPane(){
        super();
        contentPane = new JPanel();
        contentPane.setVisible(true);
        contentPane.setBounds(0, 0, getWidth(), getHeight());
        add(contentPane, new Integer(0));
        
        glassPane = new GlassPane();
        glassPane.setVisible(true);
        glassPane.setBounds(0, 0, getWidth(), getHeight());
        add(glassPane, new Integer(1));
        
        addComponentListener(new ComponentAdapter() {   
            public void componentResized(ComponentEvent e)
            {
                Dimension newSize = ((CardPane)e.getSource()).getSize();
                contentPane.setSize(newSize);
                glassPane.setSize(newSize);
            }
        });
        layer = 0;
    }
    
    public CardPane(int l){
        super();
        contentPane = new JPanel();
        contentPane.setVisible(true);
        contentPane.setSize(getWidth(), getHeight());
        add(contentPane, new Integer(0));
        
        glassPane = new GlassPane();
        glassPane.setVisible(true);
        glassPane.setSize(getWidth(), getHeight());
        add(glassPane, new Integer(1));
        addComponentListener(new ComponentAdapter() {   
            public void componentResized(ComponentEvent e)
            {
                Dimension newSize = ((CardPane)e.getSource()).getSize();
                contentPane.setSize(newSize);
                glassPane.setSize(newSize);
            }
        });
        layer = l;
    }
    
    public void addMouseListener(MouseListener l){
        glassPane.addMouseListener(l);
    }
    
    public MouseListener[] getMouseListeners(){
        return glassPane.getMouseListeners(); 
    }
    
    public void removeMouseListener(MouseListener l) {
        glassPane.removeMouseListener(l);
    }
    
    public void setToolTipText(String s) {
        glassPane.setToolTipText(s);
    }
    
    public void setLayout(LayoutManager l) {
        if (contentPane == null)
            super.setLayout(l);
        else
            contentPane.setLayout(l);
    }
    
    public Component add(Component c) {
        return contentPane.add(c);
    }
    
    public void removeAll() {
        contentPane.removeAll();
    }
    
    public void setBackground(Color c){
        contentPane.setBackground(c);
    }
    
    public void setBorder(Border b){
        contentPane.setBorder(b);
    }
}

class GlassPane extends JComponent {}
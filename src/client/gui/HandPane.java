/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JLayeredPane;

/**
 *
 * @author pseudo
 */
public class HandPane extends JLayeredPane {
    
    public void layAll(ArrayList<MemoryPanel> comps){
        removeAll();
        if(comps == null || comps.size() == 0)
            return;
        int num = comps.size();
        int cardWidth = comps.get(0).getWidth();
        int size = getWidth();
        int xShift = 0;
        if ((size / num) < cardWidth) {
            xShift = (size - cardWidth)/(num - 1);
        } else {
            xShift = cardWidth + 5;
        }
        setLayout(null);
        for (int i = 0; i < num; i++) {
            MemoryPanel panel = comps.get(i);
            panel.setBounds(i * xShift, 5, panel.getWidth(), panel.getHeight());
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    System.out.println("Mouse Entered");
                    setLayer(panel, highestLayer()+1);
                    revalidate();
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    System.out.println("Mouse Exited");
                    setLayer(panel, panel.layer);
                    revalidate();
                    repaint();
                }
            });
            add(panel, new Integer(i));
        }
    }
    
    public void add(MemoryPanel p){
        Component[] comps = getComponents();
        ArrayList<MemoryPanel> panels = new ArrayList<MemoryPanel>(Arrays.asList((MemoryPanel[])comps));
        panels.add(p);
        layAll(panels);
    }
    
    public void remove (MemoryPanel p){
        remove(p);
        Component[] comps = getComponents();
        ArrayList<MemoryPanel> panels = new ArrayList<MemoryPanel>(Arrays.asList((MemoryPanel[])comps));
        layAll(panels);
    }
}

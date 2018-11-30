/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import cards.Card;
import helpers.Debug;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;

/**
 *
 * @author pseudo
 */
public class OverlapPane extends JLayeredPane {
    
    private boolean vertical;
    
    public OverlapPane(boolean vert){
        vertical = vert;
        setLayout(null);
        setBorder(new LineBorder(Color.BLACK, 1));
        addComponentListener(new ComponentAdapter() {   
            public void componentResized(ComponentEvent e)
            {
                Component[] comps = getComponents();
                ArrayList<CardPane> panels = new ArrayList<>();
                for (Component c : comps){
                    if (c instanceof CardPane)
                        panels.add((CardPane)c);
                }
                layAll(panels);
            }
        });
    }
    
    public void layAll(ArrayList<CardPane> comps){
        removeAll();
        if(comps == null || comps.size() == 0){
            revalidate();
            repaint(); 
            return;
        }
        
        int num, cardWidth, cardHeight, width, height, shift; 
        num = comps.size();
        height = getHeight() - 10;
        width = getWidth() - 10;
        if (height <= 0){
            height = getMaximumSize().height;
        }
        if (width <= 0){
            width = getMaximumSize().width;
        }
        
        if (vertical) {
            cardWidth = width;
            cardHeight = (int) (Card.RATIO * cardWidth);
            shift = 0;
            if ((height / num) < cardHeight + 5 && num > 1)
                shift = (height - cardHeight)/(num - 1);
            else
                shift = cardHeight + 5;            
        } else {
            cardHeight = height; 
            cardWidth = (int) (cardHeight / Card.RATIO);
            shift = 0;
            if ((width / num) < cardWidth + 5 && num > 1) {
                shift = (width - cardWidth)/(num - 1);
            } else {
                shift = cardWidth + 5;
            }
        }
        
        Debug.println("Displaying in OverlapPane.\n"
                    + "num: " + num + "\n"
                    + "cardWidth: " + cardWidth + "\n"
                    + "cardHeight: " + cardHeight + "\n"
                    + "width: " + width + "\n"
                    + "height: " + height + "\n"
                    + "shift: " + shift);
        
        setLayout(null);
        for (int i = 0; i < num; i++) {
            CardPane panel = comps.get(i);
            panel.layer = i;
            if (vertical)
                panel.setBounds(5, 5 + i * shift, cardWidth, cardHeight);
            else
                panel.setBounds(5 + i * shift, 5, cardWidth, cardHeight);
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
        revalidate();
        repaint();
    }
    
    public void add(CardPane p){
        Component[] comps = getComponents();
        ArrayList<CardPane> panels = new ArrayList<>();
        for (Component c : comps){
            if (c instanceof CardPane)
                panels.add((CardPane)c);
        }
        panels.add(p);
        layAll(panels);
    }
    
    public void remove (CardPane p){
        remove(p);
        Component[] comps = getComponents();
        ArrayList<CardPane> panels = new ArrayList<>();
        for (Component c : comps){
            if (c instanceof CardPane)
                panels.add((CardPane)c);
        }
        layAll(panels);
    }
}


package client.gui.components;

import static card.Card.RATIO;
import static java.awt.Color.BLACK;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;

/**
 *
 * @author pseudo
 */
public class OverlapPane extends JLayeredPane {
    
    private final boolean vertical;
    
    /**
     *
     * @param vert
     */
    public OverlapPane(boolean vert){
        vertical = vert;
        setLayout(null);
        setBorder(new LineBorder(BLACK, 1));
        addComponentListener(new ComponentAdapter() {   
            @Override
            public void componentResized(ComponentEvent e)
            {
                Component[] comps = getComponents();
                ArrayList<CardPane> panels = new ArrayList<>();
                for (Component comp : comps) {
                    if (comp instanceof CardPane) {
                        panels.add(0, (CardPane) (comp));
                    }
                }
                layAll(panels);
            }
        });
    }
    
    /**
     *
     * @param comps
     */
    public void layAll(ArrayList<CardPane> comps){
        removeAll();
        if(comps == null || comps.isEmpty()){
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
            cardHeight = (int) (RATIO * cardWidth);
            shift = 0;
            if ((height / num) < cardHeight + 5 && num > 1)
                shift = (height - cardHeight)/(num - 1);
            else
                shift = cardHeight + 5;            
        } else {
            cardHeight = height; 
            cardWidth = (int) (cardHeight / RATIO);
            shift = 0;
            if ((width / num) < cardWidth + 5 && num > 1) {
                shift = (width - cardWidth)/(num - 1);
            } else {
                shift = cardWidth + 5;
            }
        }
        
        /*
        Debug.println("Displaying in OverlapPane.\n"
                    + "num: " + num + "\n"
                    + "cardWidth: " + cardWidth + "\n"
                    + "cardHeight: " + cardHeight + "\n"
                    + "width: " + width + "\n"
                    + "height: " + height + "\n"
                    + "shift: " + shift);
        */
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
                    //Debug.println("Mouse Entered");
                    setLayer(panel, highestLayer()+1);
                    revalidate();
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    //Debug.println("Mouse Exited");
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
    
    /**
     *
     * @param p
     */
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
    
    /**
     *
     * @param p
     */
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

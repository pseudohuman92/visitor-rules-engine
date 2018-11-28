/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;

import cards.Card;
import static cards.Card.RATIO;
import game.ClientGame;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author pseudo
 */
public abstract class Activation extends Card {

    public Activation(Item c, String text) {
        super(c, text);
        creator = c;
    }

    @Override
    public boolean canPlay(ClientGame game) { return false; }
    
    public void updatePanel() {
        getPanel().removeAll();
        getPanel().setLayout(new MigLayout("wrap 1"));
        getPanel().setPreferredSize(new Dimension(150, (int) (150 * RATIO)));
        getPanel().add(new JLabel("<html>" + name + "'s Activation" + "</html>"));
        try {
            getPanel().add(new JLabel(new ImageIcon(ImageIO.read(new File(image)).getScaledInstance(100, -1, 0))));
        } catch (IOException ex) {
        }
        getPanel().add(new JLabel(type.toString()));
        JLabel textLabel = new JLabel("<html>"
                + String.format(text, (Object[]) Arrays.stream(values).
                        mapToObj(String::valueOf).toArray(String[]::new)) + "</html>");
        getPanel().add(textLabel);
        drawBorders();
        setToolTip();
        getPanel().setBackground(getColor());
        getPanel().setVisible(true);
        getPanel().revalidate();
        getPanel().repaint();
    }
}

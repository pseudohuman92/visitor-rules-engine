
package card.types;

import card.Card;
import static card.Card.RATIO;
import client.Client;
import game.ClientGame;
import game.Game;
import helpers.Hashmap;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import static javax.imageio.ImageIO.read;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

/**
 * Abstract class for the Tome card type.
 * @author pseudo
 */
public abstract class Tome extends Card {
    
    /**
     *
     * @param name
     * @param text
     * @param image
     * @param owner
     */
    public Tome(String name, String text, String image, String owner) {
        super(name, 0, new Hashmap<>(), text, "assets/tome.png", owner);
    }

    @Override
    public boolean canPlay(ClientGame game){ return false; }
    @Override
    public void resolve(Game game){}
    
    @Override
    public abstract void study(Client client);
    
    @Override
    public void updatePanel() {
        getPanel().removeAll();
        getPanel().setLayout(new MigLayout("wrap 1"));
        getPanel().setPreferredSize(new Dimension(150, (int) (150 * RATIO)));
        getPanel().add(new JLabel("<html>" + name + "</html>"));
        try {
            getPanel().add(new JLabel(new ImageIcon(read(new File(image)).getScaledInstance(100, -1, 0))));
        } catch (IOException ex) {
        }
        getPanel().add(new JLabel("Tome"));
        JLabel textLabel = new JLabel("<html>" + text + "</html>");
        getPanel().add(textLabel);

        drawBorders();
        setToolTip();
        getPanel().setBackground(getColor());
        getPanel().setVisible(true);
        getPanel().revalidate();
        getPanel().repaint();
    }
    
}
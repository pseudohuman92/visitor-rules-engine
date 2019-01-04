
package card.types;

import card.Card;
import static card.Card.RATIO;
import enums.Knowledge;
import static enums.Type.ACTION;
import game.ClientGame;
import helpers.Hashmap;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import static javax.imageio.ImageIO.read;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author pseudo
 */
public abstract class Action extends Card {
    
    /**
     *
     * @param name
     * @param cost
     * @param knowledge
     * @param text
     * @param image
     * @param owner
     */
    public Action(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String image, String owner) {
        super(name, cost, knowledge, text, "assets/action.png", ACTION, owner);
    }

    @Override
    public boolean canPlay(ClientGame game){ 
        return (game.player.energy >= cost)
               && game.player.hasKnowledge(knowledge);
    }
    
    @Override
    public void updatePanel() {
        getPanel().removeAll();
        getPanel().setLayout(new MigLayout("wrap 1"));
        getPanel().setPreferredSize(new Dimension(150, (int) (150 * RATIO)));
        getPanel().add(new JLabel(cost + " " + getKnowledgeString()));
        getPanel().add(new JLabel("<html>" + name + "</html>"));
        try {
            getPanel().add(new JLabel(new ImageIcon(read(new File(image)).getScaledInstance(100, -1, 0))));
        } catch (IOException ex) {
        }
        getPanel().add(new JLabel(type.toString()));
        JLabel textLabel = new JLabel("<html>" + text + "</html>");
        getPanel().add(textLabel);
        
        drawCounters();
        drawBorders();
        setToolTip();
        getPanel().setBackground(getColor());
        getPanel().setVisible(true);
        getPanel().revalidate();
        getPanel().repaint();
    }

}

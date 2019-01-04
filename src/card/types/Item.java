
package card.types;

import card.Card;
import card.properties.Activatable;
import static card.Card.RATIO;
import client.Client;
import enums.Knowledge;
import static enums.Phase.MAIN;
import static enums.Type.ITEM;
import game.ClientGame;
import game.Game;
import helpers.Hashmap;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import static javax.imageio.ImageIO.read;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import network.Message;

/**
 *
 * @author pseudo
 */
public abstract class Item extends Card implements Activatable {
    
    /**
     *
     * @param name
     * @param cost
     * @param knowledge
     * @param text
     * @param image
     * @param owner
     */
    public Item(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String image, String owner) {
        super(name, cost, knowledge, text, "assets/item.png", ITEM, owner);
    }
    
    @Override
    public void resolve(Game game) {
        depleted = true;
        game.players.get(owner).items.add(this);
    }
    
    @Override
    public boolean canPlay(ClientGame game){ 
        return (game.player.energy >= cost)
               && game.player.hasKnowledge(knowledge)
               && game.turnPlayer.equals(owner)
               && game.phase == MAIN;
    }
    
    // Called by the client to see if this item can be activated

    /**
     *
     * @param game
     * @return
     */
    @Override
    public boolean canActivate(ClientGame game){ return false; }

    // Called by the client when you choose to activate this item

    /**
     *
     * @param client
     */
    @Override
    public void activate(Client client){
        client.gameConnection.send(Message.activate(client.game.uuid, client.username, uuid, new ArrayList<>()));
    }
    
    /**
     *
     * @param client
     * @param targets
     */
    @Override
    public void activate(Client client, ArrayList<Serializable> targets){
        client.gameConnection.send(Message.activate(client.game.uuid, client.username, uuid, targets));
    }
    
    // Called by the server when you activated this item

    /**
     *
     * @param game
     */
    @Override
    public void activate(Game game){}
    
    
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
        JLabel textLabel = new JLabel("<html>"+ text + "</html>");
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

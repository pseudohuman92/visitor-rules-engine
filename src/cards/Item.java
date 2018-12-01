/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;

import static cards.Card.RATIO;
import client.Client;
import enums.Knowledge;
import enums.Type;
import game.ClientGame;
import game.Game;
import game.Phase;
import helpers.Hashmap;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

import network.Message;

/**
 *
 * @author pseudo
 */
public abstract class Item extends Card {
    
    public Item(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String image, String owner) {
        super(name, cost, knowledge, text, "assets/item.png", Type.ITEM, owner);
    }
    
    public Item(Item c){
        super (c);
    }
    
    public Item(Item c, String text){
        super (c, text);
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
               && game.stack.isEmpty()
               && game.phase == Phase.MAIN;
    }
    
    // Called by the client to see if this item can be activated
    public boolean canActivate(ClientGame game){ return false; }

    // Called by the client when you choose to activate this item
    public void activate(Client client){
        client.gameConnection.send(Message.activate(client.game.uuid, client.username, uuid, new ArrayList<>()));
    }
    
    public void activate(Client client, ArrayList<Serializable> targets){
        client.gameConnection.send(Message.activate(client.game.uuid, client.username, uuid, targets));
    }
    
    // Called by the server when you activated this item
    public void activate(Game game){
        game.addToStack(getActivation());
        game.activePlayer = game.getOpponentName(game.activePlayer);
    }
    
    //Called at the start of your turn
    public Activation getPlayerStartTrigger(){ return null; }
    
    public Activation getActivation(){ return null; }
    
    public void updatePanel() {
        getPanel().removeAll();
        getPanel().setLayout(new MigLayout("wrap 1"));
        getPanel().setPreferredSize(new Dimension(150, (int) (150 * RATIO)));
        getPanel().add(new JLabel(cost + " " + getKnowledgeString()));
        getPanel().add(new JLabel("<html>" + name + "</html>"));
        try {
            getPanel().add(new JLabel(new ImageIcon(ImageIO.read(new File(image)).getScaledInstance(100, -1, 0))));
        } catch (IOException ex) {
        }
        getPanel().add(new JLabel(type.toString()));
        JLabel textLabel = new JLabel("<html>"
                + String.format(text, (Object[]) Arrays.stream(values).
                        mapToObj(String::valueOf).toArray(String[]::new)) + "</html>");
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

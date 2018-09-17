/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set1.items;

import cards.Activation;
import cards.Card;
import cards.Item;
import client.Client;
import enums.Counter;
import game.ClientGame;
import game.Game;
import game.Player;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import set1.items.activations.SpareBatteryAddCounterActivation;
import set1.items.activations.SpareBatteryGainEnergyActivation;

/**
 *
 * @author pseudo
 */
public class SpareBattery extends Item {
    public SpareBattery (String owner){
        super("Spare Battery", 1, new HashMap<>(), 
                "X, Deplete: Remove all charge counters then place X charge counters on ~<br><br>"
                + "Destroy: Gain X energy where X = # of charge counters on ~", 
                "item.png", owner);
    }
    
    @Override
    public boolean canActivate(ClientGame game){ 
        return true; 
    }

    @Override
    public void activate(Client client) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem;
        if (!depleted) {
            menuItem = new JMenuItem("Add charge counters.");
            menu.add(menuItem);
            menuItem.addActionListener((ActionEvent event) -> {
                client.gameArea.getXValue(this::activate1Helper, (cli, x) -> {return x <= cli.game.player.energy;});
            });
        }
        
        menuItem = new JMenuItem("Destroy: Gain energy");
        menu.add(menuItem);
        menuItem.addActionListener((ActionEvent event) -> {
            ArrayList<Serializable> data = new ArrayList<>();
            data.add(2);
            super.activate(client, data);
        });
        menu.setVisible(true);
        menu.show(panel, 5, 5);
    }

    public void activate1Helper(Client client, int x) {
        ArrayList<Serializable> data = new ArrayList<>();
        data.add(1);
        data.add(x);
        super.activate(client, data);
    }
  
    @Override
    public void activate (Game game){
        if (((int)supplimentaryData.get(0)) == 1){
            depleted = true;
            game.players.get(owner).energy -= (int)supplimentaryData.get(1);
        } else {
            game.destroy(uuid);
        }
        super.activate(game);
    }
    

    @Override
    public Activation getActivation(){
        if (((int)supplimentaryData.get(0)) == 1){
            return new SpareBatteryAddCounterActivation(this); 
        } else {
            return new SpareBatteryGainEnergyActivation(this); 
        }
    }
}

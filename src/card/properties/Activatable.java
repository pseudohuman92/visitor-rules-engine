/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card.properties;

import client.Client;
import game.ClientGame;
import game.Game;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author pseudo
 */
public interface Activatable {
    
        // Called by the client to see if this item can be activated

    /**
     *
     * @param game
     * @return
     */
    public boolean canActivate(ClientGame game);

    // Called by the client when you choose to activate this item

    /**
     *
     * @param client
     */
    public void activate(Client client);
    
    /**
     *
     * @param client
     * @param targets
     */
    public void activate(Client client, ArrayList<Serializable> targets);
    
    // Called by the server when you activated this item

    /**
     *
     * @param game
     */
    public void activate(Game game);
}

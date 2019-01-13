/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card.properties;

import client.Client;
import game.ClientGame;
import game.Game;
/**
 * Interface for cards that has an activating ability.
 * @author pseudo
 */
public interface Activatable {
    
    /**
     * CALLER: Client<br>
     * BEHAVIOR: return true if item can be activated.
     * @param game
     * @return
     */
    public boolean canActivate(ClientGame game);
    
    /**
     * CALLER: Client<br>
     * BEHAVIOR: Execute required logic to activate the card.
     * @param client
     * @param targets
     */
    public void activate(Client client);

    /**
     * CALLER: Server<br>
     * BEHAVIOR: Execute effects of the activation.
     * @param game
     */
    public void activate(Game game);
}

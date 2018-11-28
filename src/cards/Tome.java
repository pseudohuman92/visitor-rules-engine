/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;

import client.Client;
import enums.Knowledge;
import enums.Type;
import game.ClientGame;
import game.Game;
import java.util.HashMap;

/**
 *
 * @author pseudo
 */
public abstract class Tome extends Card {
    
    public Tome(String name, String text, String image, String owner) {
        super(name, 0, new HashMap<>(), text, image, Type.TOME, owner);
    }

    public boolean canPlay(ClientGame game){ return false; }
    public void resolve(Game game){}
    
    public abstract void playAsSource(Client client);
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;

import cards.Card;
import game.ClientGame;

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
}

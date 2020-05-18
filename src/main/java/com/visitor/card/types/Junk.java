/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types;

import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;

/**
 * @author pseudo
 */
public class Junk extends Card {

    public Junk(Game g, String owner) {
        super(g, "Junk", new Hashmap<>(), CardType.Junk, "Junk can't be played or studied.", owner);
    }

}

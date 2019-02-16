/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.cards;

import com.ccg.ancientaliens.card.types.Action;
import com.ccg.ancientaliens.game.Game;
import com.ccg.ancientaliens.protocol.Types.Knowledge;
import helpers.Hashmap;

/**
 *
 * @author pseudo
 */
public class TestCard extends Action {

    public TestCard(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, String image, String owner) {
        super(name, cost, knowledge, text, image, owner);
    }

    @Override
    public void resolve(Game game) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import com.visitor.protocol.Types;
import static com.visitor.protocol.Types.Knowledge.BLACK;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Pilfer extends Spell {
    
    String targetPlayer;
    
    public Pilfer(String owner) {
        super("Pilfer", 0, new Hashmap(BLACK, 1), "Target player loots X.", owner);
    }
    

    @Override
    protected void beforePlay(Game game) {
        int x = game.selectX(controller, game.getPlayer(controller).energy);
        UUID targetPlayerId = game.selectPlayers(controller, Predicates::any, 1, false).get(0);
        targetPlayer = game.getUsername(targetPlayerId);
        cost = x;
        game.spendEnergy(controller, cost);
        text = targetPlayer + " loots " + x;
        game.addToStack(this);
    }
    
    @Override
    protected void duringResolve (Game game){
        game.loot(targetPlayer, cost);
        text = "Target player loots X.";
        cost = 0;
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        return super.toCardMessage().setCost("X");
    }
}

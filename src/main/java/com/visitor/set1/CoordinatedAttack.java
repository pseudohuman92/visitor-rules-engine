/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.set1;

import com.visitor.card.types.Ritual;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.GREEN;

/**
 *
 * @author pseudo
 */
public class CoordinatedAttack extends Ritual {
    
    int x;
    
    public CoordinatedAttack(String owner) {
        super("Coordinated Attack", 5, new Hashmap(GREEN, 3), 
        "Deal X damage to your opponent and each damageable they control.\n" +
        "X = 1+ num of allies you control.", owner);
    }

    @Override
    protected void duringResolve (Game game){
        x = game.getAllFrom(controller, PLAY, Predicates::isAlly).size()+1;
        game.dealDamage(id, game.getOpponentUid(controller), x);
        game.getAllFrom(game.getOpponentName(controller), PLAY, Predicates::isDamageable).forEach(
            c->{
                game.dealDamage(id, c.id, x);
            });
    }
}


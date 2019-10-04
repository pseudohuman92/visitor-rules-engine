/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.sets.set1;

import com.visitor.card.types.Spell;
import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.BOTH_PLAY;
import static com.visitor.game.Game.Zone.HAND;
import com.visitor.helpers.Hashmap;
import com.visitor.helpers.Predicates;
import static com.visitor.protocol.Types.Knowledge.BLUE;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Confuzzleinator
 */
public class ScrapShield extends Spell {
    ArrayList<UUID> j;
    
    public ScrapShield(String owner) {
        super("Scrap Shield", 2, new Hashmap(BLUE, 1),
                "Additional Cost - Discard a Junk\n"
                        + "  Target gains Shield 4", owner);
    }
    
    @Override
    public boolean canPlay(Game game) {
        return super.canPlay(game) && 
                game.hasIn(controller, HAND, Predicates::isJunk, 1);
    }
    
    @Override
    protected void beforePlay(Game game) {
        // Select junk to be discarded
        j = game.selectFromZone(controller, HAND, Predicates::isJunk, 1, false);
        game.discard(controller, j.get(0));
    }
    
    @Override
    protected void duringResolve(Game game) {
        // Select player/card to receive shield
        targets = game.selectFromZoneWithPlayers(controller, BOTH_PLAY, Predicates::isDamageable, Predicates::any, 1, false);
        if(game.isPlayer(targets.get(0))) {
            game.getPlayer(game.getUsername(targets.get(0))).shield += 4;
        } else if(game.isIn(controller, targets.get(0), BOTH_PLAY)) {
            game.getCard(targets.get(0)).shield += 4;
        }
    }
}

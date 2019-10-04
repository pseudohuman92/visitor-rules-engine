/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.card.types.helpers;

import com.visitor.card.properties.Triggering;
import com.visitor.card.types.Card;
import com.visitor.game.Event;
import com.visitor.game.Game;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 *
 * @author pseudo
 */
public class TemporaryEffect extends Card implements Triggering {
    
    BiConsumer<Card, Event> effect;
    Card creator;
    
    public TemporaryEffect(Game game, Card creator, BiConsumer<Card, Event> effect) {
        super("", 0, new Hashmap<>(), "", creator.controller);
        this.effect = effect;
        this.creator = creator;
        this.targets = new Arraylist<>(creator.id);
        game.addTriggeringCard(creator.controller, this);
        
    }

    public TemporaryEffect(Game game, Card creator, BiConsumer<Card, Event> effect, UUID target) {
        super("", 0, new Hashmap<>(), "", creator.controller);
        this.effect = effect;
        this.creator = creator;
        this.targets = new Arraylist<>(creator.id).putIn(target);
        game.addTriggeringCard(creator.controller, this);
    }
    
    public TemporaryEffect(Game game, Card creator, BiConsumer<Card, Event> effect, Arraylist<UUID> targets) {
        super("", 0, new Hashmap<>(), "", creator.controller);
        this.effect = effect;
        this.creator = creator;
        this.targets = new Arraylist<>(creator.id).putAllIn(targets);
        game.addTriggeringCard(creator.controller, this);
    }

    @Override
    public final void checkEvent(Game game, Event event) {
        effect.accept(this, event);
    }

    @Override
    public final boolean canPlay(Game game) {return false;}

    @Override
    protected final void duringResolve(Game game) {}
    
}

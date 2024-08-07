package com.visitor.card.containers;

import com.visitor.card.Card;
import com.visitor.game.Event;
import com.visitor.game.parts.Game;
import com.visitor.helpers.Arraylist;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.commons.lang3.function.TriFunction;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.visitor.helpers.HelperFunctions.runIf;

public class EventChecker implements Consumer<Event> {
    private Predicate<Event> condition;
    private Consumer<Event> effect;
    private final Card card;
    private final Game game;
    private boolean createAbility;
    private String abilityText;

    private EventChecker(Game game, Card card) {
        this.game = game;
        this.card = card;
        this.effect = c -> {};
        condition = event -> true;
    }

    public static EventChecker attackChecker(Game game, Card card, Predicate<Arraylist<Card>>  predicate, Consumer<Arraylist<Card>> effect){
        return new EventChecker(game, card).addTypeCondition(Event.EventType.Attack)
                .addCardListCondition(predicate).setCardListEffect(effect);
    }

    public static EventChecker playChecker(Game game, Card card, Predicate<Card>  predicate, Consumer<Card> effect){
        return new EventChecker(game, card).addTypeCondition(Event.EventType.Play_Card)
                .addCardCondition(predicate).setCardEffect(effect);
    }
    public static EventChecker startOfTurnChecker(Game game, Card card, boolean controllersTurn,  Runnable effect){
        EventChecker ec = new EventChecker(game, card);
        if (controllersTurn) {
            ec.addStartOfControllerTurnCondition();
        } else {
            ec.addStartOfOpponentTurnCondition();
        }
        return ec.setEffect(effect);
    }

    public static EventChecker startOfTurnChecker(Game game, Card card, Runnable effect){
        return new EventChecker(game, card).addTypeCondition(Event.EventType.Turn_Start).setEffect(effect);
    }


    public static EventChecker deathChecker(Game game, Card card, Consumer<Card> effect){
        return new EventChecker(game, card).addTypeCondition(Event.EventType.Death).setCardEffect(effect);
    }

    public static EventChecker selfDeathChecker(Game game, Card card, Runnable effect){
        return new EventChecker(game, card).addTypeCondition(Event.EventType.Death).setCardEffect(c -> {
            if (c.getId().equals(card.getId())){
                effect.run();
             }});
    }

    public static EventChecker damageChecker(Game game, Card card, TriFunction<Card, UUID, Damage, Boolean>  predicate, TriConsumer<Card, UUID, Damage> effect){
        return new EventChecker(game, card).addTypeCondition(Event.EventType.Damage)
                .addDamageCondition(predicate).setDamageEffect(effect);
    }

    public static EventChecker dealDamageChecker(Game game, Card card, TriFunction<Card, UUID, Damage, Boolean>  predicate, TriConsumer<Card, UUID, Damage> effect){
        return damageChecker(game, card, (c, i, d) -> {
            return c.getId().equals(card.getId()) && predicate.apply(c, i, d);
        }, effect);
    }

    public static EventChecker dealCombatDamageChecker(Game game, Card card, TriFunction<Card, UUID, Damage, Boolean>  predicate, TriConsumer<Card, UUID, Damage> effect){
        return dealDamageChecker(game, card, (c, i, d) -> {
            return d.combat && predicate.apply(c, i, d);
        }, effect);
    }

    private EventChecker setCardListEffect(Consumer<Arraylist<Card>> effect) {
        this.effect = event -> effect.accept((Arraylist<Card>) event.data.get(0));
        return this;
    }

    private EventChecker setCardEffect(Consumer<Card> effect) {
        this.effect = event -> effect.accept((Card) event.data.get(0));
        return this;
    }

    private EventChecker setDamageEffect(TriConsumer<Card, UUID, Damage> effect) {
        this.effect = event -> effect.accept((Card) event.data.get(0), (UUID) event.data.get(1), (Damage) event.data.get(2));
        return this;
    }

    private EventChecker setEffect(Runnable effect) {
        this.effect = event -> effect.run();
        return this;
    }

    //Adders
    private  EventChecker addStartOfControllerTurnCondition() {
        return and(event -> event.playersTurnStart(card.controller));
    }

    private  EventChecker addStartOfOpponentTurnCondition() {
        return and(event -> event.playersTurnStart(game.getOpponentId(card.controller)));
    }

    private EventChecker addEndOfControllerTurnCondition() {
        return and(event -> event.playersTurnEnd(card.controller));
    }

    private EventChecker addEndOfOpponentTurnCondition() {
        return and(event -> event.playersTurnEnd(game.getOpponentId(card.controller)));
    }

    private EventChecker addTypeCondition(Event.EventType type) {
        return and(event -> event.type == type);
    }

    //Make sure that card list is the first argument of event.data before using.
    private EventChecker addCardListCondition(Predicate<Arraylist<Card>> predicate) {
        return and(event -> predicate.test((Arraylist<Card>) event.data.get(0)));
    }

    private EventChecker addDamageCondition(TriFunction<Card, UUID, Damage, Boolean> predicate) {
        return and(event -> predicate.apply((Card) event.data.get(0), (UUID) event.data.get(1), (Damage) event.data.get(2)));
    }

    //Make sure that card is the first argument of event.data before using.
    private EventChecker addCardCondition(Predicate<Card> predicate) {
        return and(event -> predicate.test((Card) event.data.get(0)));
    }

    //Make sure that player name is the first argument of event.data before using.
    private EventChecker addPlayerCondition(Predicate<String> predicate) {
        return and(event -> predicate.test((String) event.data.get(0)));
    }

    private EventChecker and(Predicate<Event> predicate) {
        Predicate<Event> oldCondition = condition;
        condition = event -> oldCondition.test(event) && predicate.test(event);
        return this;
    }

    public final void accept(Event event) {
        if (condition.test(event)) {
            if (createAbility) {
                game.addToStack(new AbilityCard(game, card, abilityText, () -> effect.accept(event)));
            } else {
                effect.accept(event);
            }
        }
    }

    public EventChecker createAbility(String abilityText) {
        createAbility = true;
        this.abilityText = abilityText;
        return this;
    }
}

package com.visitor.card.types;

import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.HAND;
import static com.visitor.game.Game.Zone.SCRAPYARD;
import com.visitor.game.Player;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Counter;
import com.visitor.protocol.Types.CounterGroup;
import com.visitor.protocol.Types.Knowledge;
import com.visitor.protocol.Types.KnowledgeGroup;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import java.util.stream.Collectors;

/**
 *
 * @author pseudo
 */
public abstract class Card {

    public UUID id;
    public String name;
    public int cost;
    public Hashmap<Knowledge, Integer> knowledge;
    public String text;
    public Arraylist<String> subtypes;
    public int health;
    public int shield;
    public int reflect;
    
    
    public String owner;
    public String controller;

    public boolean depleted;
    public Hashmap<Counter, Integer> counters;
    public Arraylist<UUID> targets;

    /**
     * This is the default constructor for creating a card.
     * @param name
     * @param cost
     * @param knowledge
     * @param text
     * @param image
     * @param owner
     */
    public Card(String name, int cost, Hashmap<Knowledge, Integer> knowledge,
            String text, String owner) {
        id = randomUUID();
        counters = new Hashmap<>();
        subtypes = new Arraylist<>();
        targets = new Arraylist<>();
        this.name = name;
        this.cost = cost;
        this.knowledge = knowledge;
        this.text = text;
        this.owner = owner;
        this.controller = owner;
        this.depleted = false;
        health = -1;
        shield = 0;
        reflect = 0;
    }
    
    /**
     * Called by client to check if you can play this card in current game state.
     * @param game
     * @return
     */
    public abstract boolean canPlay(Game game);
    
    /**
     * Called by client to check if you can studyCard this card in current game state.
     * Default implementation just checks the game if the controller can studyCard.
 OVERRIDE IF: Card has special conditions to be studied.
     * @param game
     * @return
     */
    public boolean canStudy (Game game){
       return game.canStudy(controller);
    }
    
    protected void beforePlay(Game game){};
    protected void afterPlay(Game game){};
    
    /**
     * Called by server when this card is played.
     * Default behavior is that it deducts the energy cost of the card, 
     * removes it from player's hand and then puts on the stack.
     * OVERRIDE IF: Card has an alternative cost (like X) or a special effect when played.
     * @param game
     */
    public final void play(Game game) {
        beforePlay(game);
        game.spendEnergy(controller, cost);
        game.addToStack(this);
        afterPlay(game);
    }

    /**
     * Called by the server when you choose to studyCard this card.
     * It increases player's maximum energy and adds knowledgePool.
     * OVERRIDE IF: Card has a special effect when studied or card is multicolor.
     * @param game
     */
    public final void study(Game game, boolean regular) {
        Player player = game.getPlayer(controller);
        player.voidPile.add(this);
        player.energy++;
        player.maxEnergy++;
        player.addKnowledge(getKnowledgeType());
        if(regular){
            player.numOfStudiesLeft--;
        }
    }
    
    public void sacrifice(Game game){
        game.extractCard(id);
        game.putTo(controller, this, SCRAPYARD);
    }

    public Hashmap<Knowledge, Integer> getKnowledgeType() {
        Hashmap<Knowledge, Integer> knowledgeType = new Hashmap<>();
        knowledge.forEach((k, i) -> { knowledgeType.putIn(k, 1);});
        return knowledgeType;
    }
    /**
     * This is the function that describes what is the effect of the card when it is resolved.
     * This function contains the business logic of the card effect.
     * @param game
     */
    protected void beforeResolve(Game game){};
    protected abstract void duringResolve(Game game);
    protected void afterResolve(Game game){};
    
    public void resolve(Game game){
        beforeResolve(game);
        duringResolve(game);
        afterResolve(game);
    }
    
    
    /**
     * Function that adds counters to the card.
     * @param name
     * @param count
     */
    public void addCounters(Counter name, int count) {
        counters.merge(name, count, (a, b) -> a + b);
    }
    
    public void removeCounters(Counter name, int count) {
        int k = counters.get(name);
        if (k <= count){
            counters.remove(name);
        } else {
            counters.put(name, k - count);
        }
    }
    
    public int removeAllCounters(Counter name) {
        return counters.remove(name);
    }
    
    public void deplete(){
        depleted = true;
    }
    
    public void resetShields(){
        shield = 0;
        reflect = 0;
    }
    
    public void ready(){
        depleted = false;
    }
    
    public void destroy(Game game){
        clear();
        game.extractCard(this.id);
        game.putTo(controller, this, SCRAPYARD);
    }

    /**
     * Function that clears status flags and supplementary data of the card.
     * 
     */
    public void clear() {
        depleted = false;
        targets = new Arraylist<>();
        shield = 0;
        reflect = 0;
    }
    
    public void returnToHand(Game game){
        clear();
        game.extractCard(this.id);
        game.putTo(controller, this, HAND);
    }
    
    public void copyPropertiesFrom(Card c) {
        id = c.id;
        owner = c.owner;
        controller = c.controller;
        counters = c.counters;
        depleted = c.depleted;
        targets = c.targets;
        health = c.health;
        shield = c.shield;
        reflect = c.reflect;
    }
    
    public boolean isDamageable(){
        return health >= 0;
    }
    
    public void dealDamage(Game game, int count, UUID source) {
        int damage = count;
        if(shield >= damage){
            shield -= damage;
            return;
        }
        damage -= shield;
        shield = 0;
        if(reflect >= damage){
            reflect -= damage;
            game.dealDamage(id, source, damage);
            return;
        }
        int temp = reflect;
        damage -= reflect;
        reflect = 0;
        health = Math.max(0, health - damage);
        game.dealDamage(id, source, temp);
        if(health <= 0){
            game.destroy(source, id);
        }
    }

    public Types.Card.Builder toCardMessage() {
        Types.Card.Builder b = Types.Card.newBuilder()
                .setId(id.toString())
                .setName(name)
                .setDepleted(depleted)
                .setDescription(text)
                .setCost(Integer.toString(cost))
                .setType("Card")
                .addAllSubtypes(subtypes)
                .addAllTargets(targets.parallelStream()
                        .map(c -> { return c.toString(); })
                        .collect(Collectors.toList()));
        counters.forEach((k, i) -> {
            b.addCounters(CounterGroup.newBuilder()
                    .setCounter(k)
                    .setCount(i).build());
        });
        knowledge.forEach((k, i) -> {
            b.addKnowledgeCost(KnowledgeGroup.newBuilder()
                    .setKnowledge(k)
                    .setCount(i).build());
        });
        if(health > -1){
            b.setHealth(health);
        }
        if(shield > 0){
            b.setShield(shield);
        }
        if(reflect > 0){
            b.setReflect(reflect);
        }
        return b;
    }
    
}

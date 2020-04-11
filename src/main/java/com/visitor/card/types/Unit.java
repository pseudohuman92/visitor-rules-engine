
package com.visitor.card.types;

import com.visitor.game.Game;
import static com.visitor.game.Game.Zone.PLAY;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.Hashmap;
import com.visitor.protocol.Types;
import com.visitor.protocol.Types.Knowledge;
import java.util.UUID;

/**
 * Abstract class for the Asset card type.
 * @author pseudo
 */
public abstract class Unit extends Card {
    
    int attack;
    boolean deploying;
    UUID blockedAttacker;
    Arraylist<UUID> blockedBy;
    UUID attackTarget;
        
    public Unit(String name, int cost, Hashmap<Knowledge, Integer> knowledge, String text, int attack, int health, String owner) {
        super(name, cost, knowledge, text, owner);
        this.attack = attack;
        this.health = health;
        blockedBy = new Arraylist<>();
    }
    
    @Override
    protected final void duringResolve(Game game) {
        deploying = true;
        game.putTo(controller, this, PLAY);
    }
    
    /* TODO: create a class Activatible unit and move this there
    protected boolean canActivateAdditional(Game game){return true;};
    @Override
    public final boolean canActivate(Game game) {
        return !depleted && !deploying && canActivateAdditional(game);
    }
    */
    
    protected boolean canAttackAdditional(Game game){return true;};
    public final boolean canAttack(Game game) {
        return !depleted && !deploying && canAttackAdditional(game);
    }
    
    protected boolean canBlockAdditional(Game game, Unit u){return true;};
    public final boolean canBlock(Game game, Unit u) {
        return !depleted && canBlockAdditional(game, u);
    }
    
    public final void setAttacking(UUID target) {
       depleted = true;
       attackTarget = target;
    }
    
    public final void unsetAttacking() {
       attackTarget = null;
       blockedBy.clear();
    }
    
    public final void unsetBlocking() {
       blockedAttacker = null;
    }
    
    public final void setBlocking(UUID u) {
        blockedAttacker = u;
    }
    
    public final void addBlocker(UUID blocker) {
        blockedBy.add(blocker);
    }
    
    @Override
    public void ready(){
        depleted = false;
        deploying = false;
    }
    
    public final void dealAttackDamage(Game game){
        if (blockedBy.isEmpty()) {
            game.dealDamage(id, attackTarget, attack);
        } else {
            if(blockedBy.size() == 1){
                game.dealDamage(id, blockedBy.get(0), attack);
            } else {
                game.assignDamage(id, blockedBy, attack);
            }
        }
    }
    
    public final void dealBlockDamage(Game game){
        if (blockedAttacker != null)
            game.dealDamage(id, blockedAttacker, attack);
    }
    
    @Override
    public boolean canPlay(Game game){ 
        return game.hasEnergy(controller, cost)
               && game.hasKnowledge(controller, knowledge)
               && game.canPlaySlow(controller);
    }
    
    @Override
    public Types.Card.Builder toCardMessage() {
        Types.Card.Builder b =
                super.toCardMessage()
                .setType("Unit")
                .setAttack(attack)
                .setDeploying(deploying);
        if (blockedAttacker != null)
            b.setBlockedAttacker(blockedAttacker.toString());
        if (attackTarget != null)
            b.setAttackTarget(attackTarget.toString());
        return b;
    }
}

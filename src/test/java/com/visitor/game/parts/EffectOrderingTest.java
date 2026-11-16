package com.visitor.game.parts;

import com.visitor.card.Card;
import com.visitor.card.containers.ActivatedAbility;
import com.visitor.card.containers.EventChecker;
import com.visitor.card.types.Asset;
import com.visitor.card.types.Cantrip;
import com.visitor.card.types.Unit;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.protocol.Types;
import com.visitor.sets.base2.ShieldBarrage;
import com.visitor.sets.base2.Stormtrooper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.visitor.game.parts.Base.Zone.*;
import static com.visitor.protocol.Types.Knowledge.YELLOW;
import static org.junit.Assert.*;

public class EffectOrderingTest {
    private GameTestFixture f;
    private final List<String> trace=new ArrayList<>();
    @Before public void setUp() { f=new GameTestFixture(); }
    @After public void tearDown() throws Exception { f.close(); }
    private void resolve() { f.game.pass(f.opponent.getId()); f.game.pass(f.player.getId()); }

    @Test public void cardCostsResolutionPlacementAndAfterEffectsFollowRegistrationOrder() {
        f.player.addEnergy(1,false);
        Card card=f.hand(new OrderedSpell(f.game,f.player.getId(),trace),f.player);
        f.game.playCard(f.player.getId(),card.getId(),new Arraylist<>());
        assertEquals(Arrays.asList("cost-a","cost-b"),trace);
        resolve();
        List<String> expected=new ArrayList<>(Arrays.asList("cost-a","cost-b"));
        for (int i=0;i<8;i++) expected.add("resolve-"+i);
        expected.addAll(Arrays.asList("place","after-a","after-b"));
        assertEquals(expected,trace);
        assertEquals(Discard_Pile,card.zone);
    }
    private static class OrderedSpell extends Cantrip {
        OrderedSpell(Game game,UUID owner,List<String> trace) {
            super(game,"Ordered spell",1,null,"",owner);
            for (String label : Arrays.asList("a","b")) {
                playable.addTargetMultipleCardsOrPlayers(None,Predicates::none,0,0,"cost-"+label,id -> {
                    assertEquals("Printed energy is paid before card additional costs",0,game.getPlayerEnergy(owner));
                    trace.add("cost-"+label);
                },false,true);
            }
            for (int i=0;i<8;i++) {
                final int step=i;
                playable.addResolveEffect(() -> trace.add("resolve-"+step));
            }
            playable.setResolvePlaceCard(() -> { trace.add("place"); game.putTo(owner,this,Discard_Pile); });
            playable.addAfterResolveEffect(() -> trace.add("after-a"));
            playable.addAfterResolveEffect(() -> trace.add("after-b"));
        }
    }

    @Test public void activatedAbilityCostsAndDependentEffectsFollowRegistrationOrder() {
        f.player.addEnergy(1,false);
        Card unit=f.play(new OrderedUnit(f.game,f.player.getId(),trace),f.player);
        List<Types.TargetingAbility> abilities=unit.toCardMessage().getActivateTargetsList();
        assertEquals(Arrays.asList("ordered","other"),Arrays.asList(abilities.get(0).getText(),abilities.get(1).getText()));
        f.game.activateCard(f.player.getId(),unit.getId(),UUID.fromString(abilities.get(0).getId()),new Arraylist<>());
        assertEquals(Arrays.asList("cost-a","cost-b"),trace);
        assertEquals(0,f.player.getEnergy());
        resolve();
        assertEquals(Arrays.asList("cost-a","cost-b","set-three","add-two"),trace);
        assertEquals(5,unit.getAttack());
    }
    private static class OrderedUnit extends Unit {
        OrderedUnit(Game game,UUID owner,List<String> trace) {
            super(game,"Ordered unit",0,null,"",0,1,owner);
            ActivatedAbility ability=new ActivatedAbility(game,this,1,"ordered");
            for (String label : Arrays.asList("a","b")) {
                ability.addAbility("cost-"+label,() -> {
                    assertEquals("Ability additional costs run before printed energy payment",1,game.getPlayerEnergy(owner));
                    trace.add("cost-"+label);
                },true);
            }
            ability.addAbility("set",() -> { setAttack(3); trace.add("set-three"); },false);
            ability.addAbility("add",() -> { addAttackAndHealth(2,0,false); trace.add("add-two"); },false);
            activatable.addActivatedAbility(ability);
            activatable.addActivatedAbility(new ActivatedAbility(game,this,0,"other",() -> {}));
        }
    }

    @Test public void shieldBarrageRemovesShieldsBeforeCalculatingDamage() {
        f.player.addEnergy(1,false); f.player.addKnowledge(new CounterMap<>(YELLOW,1));
        Card ally=f.play(new Stormtrooper(f.game,f.player.getId()),f.player);
        Card enemy=f.play(new Stormtrooper(f.game,f.opponent.getId()),f.opponent);
        enemy.setHealth(10); ally.addShield(4,false);
        Card spell=f.hand(new ShieldBarrage(f.game,f.player.getId()),f.player);
        List<Types.Targeting> groups=spell.toCardMessage().getPlayTargetsList();
        assertEquals("Remove all shields from ally unit.",groups.get(0).getTargetMessage());
        Arraylist<Types.TargetSelection> selections=new Arraylist<>();
        selections.add(Types.TargetSelection.newBuilder().setId(groups.get(0).getId()).addTargets(ally.getId().toString()).build());
        selections.add(Types.TargetSelection.newBuilder().setId(groups.get(1).getId()).addTargets(enemy.getId().toString()).build());
        // Client selection order must not override the card definition.
        java.util.Collections.reverse(selections);
        f.game.playCard(f.player.getId(),spell.getId(),selections); resolve();
        assertEquals(0,ally.getShields()); assertEquals(6,enemy.getHealth());
    }

    private Card asset(UUID owner,boolean queued,String... labels) {
        Card asset=new TraceAsset(f.game,owner,trace,queued,labels);
        f.play(asset,owner.equals(f.player.getId()) ? f.player : f.opponent);
        asset.enterPlay();
        return asset;
    }
    private static class TraceAsset extends Asset {
        TraceAsset(Game game,UUID owner,List<String> trace,boolean queued,String... labels) {
            super(game,"Trigger",0,null,"",owner);
            for (String label : labels) {
                EventChecker checker=EventChecker.playChecker(game,this,c -> true,c -> trace.add(label));
                if (queued) checker.createAbility(label);
                triggering.addEventChecker(checker);
            }
        }
    }
    private static class TraceSpell extends Cantrip {
        TraceSpell(Game game,UUID owner,List<String> trace) {
            super(game,"Trace spell",0,null,"",owner);
            playable.addResolveEffect(() -> trace.add("spell"));
        }
    }

    @Test public void immediateTriggersUseTurnPlayerThenCardAndCheckerRegistrationOrder() {
        asset(f.opponent.getId(),false,"opponent");
        asset(f.player.getId(),false,"first-a","first-b");
        asset(f.player.getId(),false,"second");
        Card spell=f.hand(new TraceSpell(f.game,f.player.getId(),trace),f.player);
        f.game.playCard(f.player.getId(),spell.getId(),new Arraylist<>());
        assertEquals(Arrays.asList("first-a","first-b","second","opponent"),trace);
        assertEquals(1,f.game.stack.size());
        resolve(); assertEquals("spell",trace.get(4));
    }

    @Test public void explicitlyStackedTriggersAreCreatedInOrderAndResolveLastInFirstOut() {
        asset(f.player.getId(),true,"first"); asset(f.player.getId(),true,"second");
        Card spell=f.hand(new TraceSpell(f.game,f.player.getId(),trace),f.player);
        f.game.playCard(f.player.getId(),spell.getId(),new Arraylist<>());
        assertTrue(trace.isEmpty()); assertEquals(3,f.game.stack.size());
        resolve(); assertEquals(Arrays.asList("second","first","spell"),trace);
    }
}

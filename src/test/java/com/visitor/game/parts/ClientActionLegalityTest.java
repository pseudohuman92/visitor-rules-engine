package com.visitor.game.parts;

import com.visitor.card.Card;
import com.visitor.card.containers.ActivatedAbility;
import com.visitor.card.types.Cantrip;
import com.visitor.card.types.Unit;
import com.visitor.game.IllegalGameActionException;
import com.visitor.game.Player;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.Predicates;
import com.visitor.protocol.Types;
import com.visitor.sets.base2.Barracks;
import com.visitor.sets.base2.FlameStrike;
import com.visitor.sets.base2.Stormtrooper;
import com.visitor.sets.base2.TacticalBriefing;
import com.visitor.sets.test.MultiActivatedAbilities;
import com.visitor.sets.test.MultiActivatedAbilitiesWithMultiTargeting;
import com.visitor.sets.test.MultiTargetCantrip;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static com.visitor.card.properties.Damagable.CombatAbility.*;
import static com.visitor.game.parts.Base.Zone.*;
import static com.visitor.protocol.Types.Knowledge.*;
import static com.visitor.protocol.Types.Phase.*;
import static org.junit.Assert.*;

public class ClientActionLegalityTest {
    private GameTestFixture f;
    private Game g;
    private Player a,b;
    @Before public void setUp() { f=new GameTestFixture(); g=f.game; a=f.player; b=f.opponent; }
    @After public void tearDown() throws Exception { f.close(); }
    private Arraylist<Types.TargetSelection> none() { return new Arraylist<>(); }
    private void fund(Player player,int energy) {
        player.addEnergy(energy,false);
        player.addKnowledge(new CounterMap<>(RED,3));
    }
    private void reject(Runnable action) {
        String before=f.snapshot();
        assertThrows(IllegalGameActionException.class,action::run);
        assertEquals("Rejected command must not change match state",before,f.snapshot());
    }
    private Types.TargetSelection selection(Types.Targeting group, UUID... ids) {
        Types.TargetSelection.Builder builder=Types.TargetSelection.newBuilder().setId(group.getId());
        for (UUID id : ids) builder.addTargets(id.toString());
        return builder.build();
    }
    private Types.Targeting target(Card card) {
        return card.toCardMessage().getPlayTargetsList().stream().filter(t -> t.getMaxTargets()>0).findFirst().get();
    }
    private Types.AttackerAssignment attack(Card card,UUID target) {
        return Types.AttackerAssignment.newBuilder().setAttackerId(card.getId().toString()).setAttacksTo(target.toString()).build();
    }
    private Types.BlockerAssignment block(Card card,Card attacker) {
        return Types.BlockerAssignment.newBuilder().setBlockerId(card.getId().toString()).setBlockedBy(attacker.getId().toString()).build();
    }

    @Test public void zeroEnergyCannotPlayEvenThoughCardIdIsValid() {
        a.addKnowledge(new CounterMap<>(RED,1));
        Card card=a.hand.get(0);
        assertFalse(card.canPlay(true));
        reject(() -> g.playCard(a.getId(),card.getId(),none()));
        assertEquals(0,a.getEnergy()); assertTrue(a.hand.contains(card)); assertTrue(g.stack.isEmpty());
    }
    @Test public void missingKnowledgeCannotPlay() {
        a.addEnergy(5,false);
        reject(() -> g.playCard(a.getId(),a.hand.get(0).getId(),none()));
    }
    @Test public void wrongActorUnknownCardAndWrongZoneAreRejected() {
        fund(a,5); fund(b,5);
        reject(() -> g.playCard(b.getId(),b.hand.get(0).getId(),none()));
        reject(() -> g.playCard(a.getId(),b.hand.get(0).getId(),none()));
        reject(() -> g.playCard(a.getId(),UUID.randomUUID(),none()));
        reject(() -> g.playCard(UUID.randomUUID(),a.hand.get(0).getId(),none()));
        reject(() -> g.playCard(a.getId(),a.deck.get(0).getId(),none()));
    }
    @Test public void slowPlayChecksPhaseAndEmptyStack() {
        fund(a,5);
        Card card=a.hand.get(0);
        f.setPhase(ATTACK_PLAY,a.getId(),a.getId());
        reject(() -> g.playCard(a.getId(),card.getId(),none()));
        f.setPhase(MAIN_BEFORE,a.getId(),a.getId());
        Card stacked=new Stormtrooper(g,b.getId()); stacked.zone=Stack; g.stack.add(stacked);
        reject(() -> g.playCard(a.getId(),card.getId(),none()));
    }
    @Test public void legalPlayPaysCostOnceAndHandsOverInitiative() {
        fund(a,1); Card card=a.hand.get(0);
        assertTrue(card.canPlay(true));
        g.playCard(a.getId(),card.getId(),none());
        assertEquals(0,a.getEnergy()); assertFalse(a.hand.contains(card));
        assertEquals(Stack,card.zone); assertEquals(Arrays.asList(card),g.stack);
        assertEquals(b.getId(),g.activePlayer);
        reject(() -> g.playCard(a.getId(),card.getId(),none()));
    }
    @Test public void trustedFreePlayStillWorksWithoutNormalTimingOrResources() {
        Card card=a.hand.get(0);
        f.setPhase(REDRAW,b.getId(),b.getId());
        g.playCardWithoutCost(a.getId(),card.getId(),none());
        assertEquals(0,a.getEnergy()); assertEquals(Arrays.asList(card),g.stack);
    }
    @Test public void missingUnknownAndMalformedTargetGroupsAreRejected() {
        fund(a,3); Card spell=f.hand(new FlameStrike(g,a.getId()),a);
        reject(() -> g.playCard(a.getId(),spell.getId(),none()));
        reject(() -> g.playCard(a.getId(),spell.getId(),null));
        Types.TargetSelection unknown=Types.TargetSelection.newBuilder().setId(UUID.randomUUID().toString()).addTargets(b.getId().toString()).build();
        reject(() -> g.playCard(a.getId(),spell.getId(),new Arraylist<>(unknown)));
        Types.TargetSelection malformed=Types.TargetSelection.newBuilder().setId("broken").build();
        reject(() -> g.playCard(a.getId(),spell.getId(),new Arraylist<>(malformed)));
    }
    @Test public void illegalTargetsAndRepeatedGroupsAreRejectedBeforePaying() {
        fund(a,3); Card spell=f.hand(new FlameStrike(g,a.getId()),a); Types.Targeting group=target(spell);
        reject(() -> g.playCard(a.getId(),spell.getId(),new Arraylist<>(selection(group,a.getId()))));
        reject(() -> g.playCard(a.getId(),spell.getId(),new Arraylist<>(selection(group,UUID.randomUUID()))));
        reject(() -> g.playCard(a.getId(),spell.getId(),new Arraylist<>(selection(group,b.hand.get(0).getId()))));
        Types.TargetSelection valid=selection(group,b.getId());
        reject(() -> g.playCard(a.getId(),spell.getId(),new Arraylist<>(new Types.TargetSelection[]{valid,valid})));
        Types.TargetSelection malformed=Types.TargetSelection.newBuilder().setId(group.getId()).addTargets("broken").build();
        reject(() -> g.playCard(a.getId(),spell.getId(),new Arraylist<>(malformed)));
    }
    @Test public void optionalTargetCountsAndUniquenessAreEnforced() {
        Card spell=f.hand(new MultiTargetCantrip(g,a.getId()),a); Types.Targeting group=target(spell);
        UUID x=a.hand.get(0).getId(), y=a.hand.get(1).getId(), z=a.hand.get(2).getId();
        reject(() -> g.playCard(a.getId(),spell.getId(),new Arraylist<>(selection(group,x,x))));
        reject(() -> g.playCard(a.getId(),spell.getId(),new Arraylist<>(selection(group,x,y,z))));
        reject(() -> g.playCard(a.getId(),spell.getId(),new Arraylist<>(selection(group,spell.getId()))));
        g.playCard(a.getId(),spell.getId(),none());
        assertTrue(g.stack.contains(spell)); assertTrue(spell.getAllTargets().isEmpty());
    }
    @Test public void legalTargetedPlayResolvesThroughNormalPasses() {
        fund(a,2); Card spell=f.hand(new FlameStrike(g,a.getId()),a);
        g.playCard(a.getId(),spell.getId(),new Arraylist<>(selection(target(spell),b.getId())));
        g.pass(b.getId()); g.pass(a.getId());
        assertEquals(27,b.damagable.getHealth());
        assertEquals(Discard_Pile,spell.zone); assertTrue(g.stack.isEmpty());
    }
    @Test public void fastSpellCanRespondDuringAnOpponentsTurn() {
        fund(a,1);
        Card spell=f.hand(new com.visitor.sets.base2.ExplosiveShot(g,a.getId()),a);
        f.setPhase(ATTACK_PLAY,b.getId(),a.getId());
        assertTrue(spell.canPlay(true));
        g.playCard(a.getId(),spell.getId(),new Arraylist<>(selection(target(spell),b.getId())));
        assertEquals(0,a.getEnergy()); assertTrue(g.stack.contains(spell));
        assertEquals(ATTACK_PLAY,g.phase);
    }
    @Test public void targetAvailabilityDoesNotDoubleCountPlayers() {
        // Test subclass exposes the same construction API used by real cards.
        Card needsTwo=f.hand(new TwoEnemySpell(g,a.getId()),a);
        assertFalse(needsTwo.canPlay(true));
        reject(() -> g.playCard(a.getId(),needsTwo.getId(),none()));
    }
    private static class TwoEnemySpell extends Cantrip {
        TwoEnemySpell(Game game,UUID owner) {
            super(game,"Two enemies",0,null,"",owner);
            playable.addTargetMultipleCardsOrPlayers(Players,Predicates.isEnemy(owner),2,2,"",id -> {},true,false);
        }
    }
    @Test public void costOnlyTargetsAreAdvertisedAndRequired() {
        Card unit=f.play(new Stormtrooper(g,a.getId()),a);
        Card spell=f.hand(new CostOnlySpell(g,a.getId()),a);
        assertEquals(1,spell.toCardMessage().getPlayTargetsCount());
        reject(() -> g.playCard(a.getId(),spell.getId(),none()));
        g.playCard(a.getId(),spell.getId(),new Arraylist<>(selection(target(spell),unit.getId())));
        assertEquals(Discard_Pile,unit.zone); assertEquals(Stack,spell.zone);
    }
    private static class CostOnlySpell extends Cantrip {
        CostOnlySpell(Game game,UUID owner) {
            super(game,"Sacrifice cost",0,null,"",owner);
            playable.addTargetSingleUnit(Play,Predicates.isAllyUnit(owner),game::destroy,"",true);
        }
    }
    @Test public void studyChecksTimingOwnershipAndAllowance() {
        Card card=a.hand.get(0);
        reject(() -> g.studyCard(b.getId(),card.getId(),true,new CounterMap<>(RED,1)));
        f.setPhase(ATTACK_PLAY,a.getId(),a.getId());
        reject(() -> g.studyCard(a.getId(),card.getId(),true,new CounterMap<>(RED,1)));
        f.setPhase(MAIN_BEFORE,a.getId(),a.getId()); a.removeStudy(1);
        assertFalse(card.canStudy());
        reject(() -> g.studyCard(a.getId(),card.getId(),true,new CounterMap<>(RED,1)));
    }
    @Test public void studyCannotForgeKnowledgeOrAmounts() {
        Card card=a.hand.get(0);
        reject(() -> g.studyCard(a.getId(),card.getId(),true,new CounterMap<>(BLUE,1)));
        reject(() -> g.studyCard(a.getId(),card.getId(),true,new CounterMap<>(RED,2)));
        reject(() -> g.studyCard(a.getId(),card.getId(),true,new CounterMap<>()));
        reject(() -> g.studyCard(a.getId(),card.getId(),true,new CounterMap<>(RED,1).add(BLUE,1)));
        reject(() -> g.studyCard(a.getId(),card.getId(),true,null));
    }
    @Test public void legalStudyKeepsCardInHandAndBlocksPlayUntilTurnEnd() {
        Card card=a.hand.get(0); int deckSize=a.deck.size(), handSize=a.hand.size();
        g.studyCard(a.getId(),card.getId(),true,new CounterMap<>(RED,1));
        assertEquals(deckSize,a.deck.size()); assertEquals(handSize,a.hand.size());
        assertTrue(a.hand.contains(card)); assertEquals(Hand,card.zone);
        assertEquals(1,a.getEnergy()); assertEquals(1,a.getMaxEnergy());
        assertEquals(1,a.getKnowledgeCount(RED)); assertFalse(a.hasStudy(1));
        assertFalse(card.canPlay(true)); assertFalse(card.canPlay(false));
        assertFalse(card.toCardMessage().getCanPlay());
        reject(() -> g.playCard(a.getId(),card.getId(),none()));
        a.addStudyCount(1);
        assertFalse(card.canStudy());
        reject(() -> g.studyCard(a.getId(),card.getId(),true,new CounterMap<>(RED,1)));
        f.setPhase(MAIN_AFTER,a.getId(),a.getId());
        g.changePhase();
        f.setPhase(MAIN_BEFORE,a.getId(),a.getId());
        assertTrue(card.canPlay(true)); assertTrue(card.canStudy());
    }
    @Test public void clearResetsStudiedFlagOnCardsWithoutActivation() {
        Card card=f.hand(new Cantrip(g,"Study test",0,new CounterMap<>(RED,1),"",a.getId()) {},a);
        a.addKnowledge(new CounterMap<>(RED,1));
        assertTrue(card.canPlay(true));
        g.studyCard(a.getId(),card.getId(),true,new CounterMap<>(RED,1));
        assertFalse(card.canPlay(true));
        a.addStudyCount(1);
        card.clear();
        assertTrue(card.canPlay(true)); assertTrue(card.canStudy());
    }
    @Test public void endTurnClearsStudiedFlagAfterCardLeavesHand() {
        Card card=a.hand.get(0);
        g.studyCard(a.getId(),card.getId(),true,new CounterMap<>(RED,1));
        a.discard(card.getId());
        a.endTurn();
        card.returnToHand();
        assertTrue(card.canPlay(true));
    }
    @Test public void tomeRejectsForgedChoiceButKeepsItsBuiltInKnowledgeEffect() {
        Card tome=f.hand(new TacticalBriefing(g,a.getId()),a);
        tome.clear();
        assertTrue(tome.canStudy());
        reject(() -> g.studyCard(a.getId(),tome.getId(),true,new CounterMap<>(RED,1)));
        int deckSize=a.deck.size();
        g.studyCard(a.getId(),tome.getId(),true,new CounterMap<>());
        assertEquals(deckSize,a.deck.size()); assertEquals(2,a.getKnowledgeCount(RED));
        assertFalse(a.hand.contains(tome));
    }
    @Test public void trustedNonRegularStudyDoesNotConsumeAllowance() {
        Card card=a.hand.get(0); a.removeStudy(1); f.setPhase(ATTACK,b.getId(),b.getId());
        g.studyCard(a.getId(),card.getId(),false,new CounterMap<>(RED,1));
        assertEquals(1,a.getMaxEnergy()); assertFalse(a.hasStudy(1));
        assertTrue(a.hand.contains(card)); assertFalse(card.canPlay(false));
    }
    @Test public void activationChecksTheSelectedAbilityNotJustAnyAvailableAbility() {
        Card unit=f.play(new MultiActivatedAbilities(g,a.getId()),a);
        a.addEnergy(1,false);
        UUID expensive=UUID.fromString(unit.toCardMessage().getActivateTargetsList().stream().filter(t -> t.getText().startsWith("{1}")).findFirst().get().getId());
        a.removeEnergy(1);
        assertTrue(unit.canActivate()); assertFalse(unit.canActivate(expensive));
        reject(() -> g.activateCard(a.getId(),unit.getId(),expensive,none()));
    }
    @Test public void activationRejectsWrongActorZoneAndAbilityId() {
        Card unit=f.play(new Barracks(g,a.getId()),a);
        UUID ability=UUID.fromString(unit.toCardMessage().getActivateTargets(0).getId());
        reject(() -> g.activateCard(b.getId(),unit.getId(),ability,none()));
        reject(() -> g.activateCard(a.getId(),unit.getId(),UUID.randomUUID(),none()));
        reject(() -> g.activateCard(a.getId(),a.hand.get(0).getId(),ability,none()));
        unit.deplete();
        assertFalse(unit.canActivate(ability));
        reject(() -> g.activateCard(a.getId(),unit.getId(),ability,none()));
    }
    @Test public void legalActivationDepletesAndUsesStack() {
        Card unit=f.play(new Barracks(g,a.getId()),a);
        UUID ability=UUID.fromString(unit.toCardMessage().getActivateTargets(0).getId());
        g.activateCard(a.getId(),unit.getId(),ability,none());
        assertTrue(unit.isDepleted()); assertEquals(1,g.stack.size()); assertEquals(b.getId(),g.activePlayer);
    }
    @Test public void allActivationTargetGroupsValidateBeforeAnyTargetsAreStored() {
        Card unit=f.play(new MultiActivatedAbilitiesWithMultiTargeting(g,a.getId()),a);
        Types.TargetingAbility ability=unit.toCardMessage().getActivateTargetsList().stream().filter(t -> t.getTargetsCount()==2).findFirst().get();
        UUID id=UUID.fromString(ability.getId());
        Types.TargetSelection first=selection(ability.getTargets(0),b.getId());
        Types.TargetSelection invalid=selection(ability.getTargets(1),UUID.randomUUID());
        reject(() -> g.activateCard(a.getId(),unit.getId(),id,new Arraylist<>(new Types.TargetSelection[]{first,invalid})));
        Types.TargetSelection second=selection(ability.getTargets(1),b.getId());
        g.activateCard(a.getId(),unit.getId(),id,new Arraylist<>(new Types.TargetSelection[]{first,second}));
        g.pass(b.getId()); g.pass(a.getId());
        assertEquals(28,b.damagable.getHealth());
    }
    @Test public void passingAndOpeningHandCommandsRespectActorAndPhase() {
        reject(() -> g.pass(b.getId()));
        reject(() -> g.keep(a.getId())); reject(() -> g.redraw(a.getId()));
        f.setPhase(REDRAW,a.getId(),a.getId());
        reject(() -> g.pass(a.getId())); reject(() -> g.keep(b.getId())); reject(() -> g.redraw(b.getId()));
        g.keep(a.getId()); reject(() -> g.keep(a.getId()));
        g.keep(b.getId()); assertEquals(MAIN_BEFORE,g.phase);
    }
    @Test public void legalRedrawAndPassesStillWork() {
        f.setPhase(REDRAW,a.getId(),a.getId()); int size=a.hand.size();
        g.redraw(a.getId()); assertEquals(size-1,a.hand.size()); assertEquals(a.getId(),g.activePlayer);
        f.setPhase(MAIN_BEFORE,a.getId(),a.getId());
        g.pass(a.getId()); assertEquals(b.getId(),g.activePlayer);
        g.pass(b.getId()); assertEquals(ATTACK,g.phase);
    }
    @Test public void attackDeclarationsValidateEntireBatchBeforeDepletingAnything() {
        Card first=f.play(new Stormtrooper(g,a.getId()),a);
        Card enemy=f.play(new Stormtrooper(g,b.getId()),b);
        f.setPhase(ATTACK,a.getId(),a.getId());
        reject(() -> g.selectAttackers(b.getId(),new Types.AttackerAssignment[0]));
        reject(() -> g.selectAttackers(a.getId(),new Types.AttackerAssignment[]{attack(first,b.getId()),attack(enemy,a.getId())}));
        reject(() -> g.selectAttackers(a.getId(),new Types.AttackerAssignment[]{attack(first,b.getId()),attack(first,b.getId())}));
        reject(() -> g.selectAttackers(a.getId(),new Types.AttackerAssignment[]{attack(first,a.getId())}));
        reject(() -> g.selectAttackers(a.getId(),new Types.AttackerAssignment[]{attack(first,b.hand.get(0).getId())}));
        assertFalse(first.isDepleted()); assertFalse(first.isAttacking());
    }
    @Test public void attackFlagRejectsDepletedDeployingAndDefenderUnits() {
        Card unit=f.play(new Stormtrooper(g,a.getId()),a);
        f.setPhase(ATTACK,a.getId(),a.getId()); unit.deplete();
        reject(() -> g.selectAttackers(a.getId(),new Types.AttackerAssignment[]{attack(unit,b.getId())}));
        unit.newTurn(); unit.addCombatAbility(Defender,false);
        reject(() -> g.selectAttackers(a.getId(),new Types.AttackerAssignment[]{attack(unit,b.getId())}));
        Card deploying=f.play(new Unit(g,"Deploying",0,null,"",2,2,a.getId()) {},a);
        deploying.damagable.setDeploying(); assertFalse(deploying.canAttack());
        reject(() -> g.selectAttackers(a.getId(),new Types.AttackerAssignment[]{attack(deploying,b.getId())}));
    }
    @Test public void legalAttackAndBlockUseCorrectActorsAndPhases() {
        Card attacker=f.play(new Stormtrooper(g,a.getId()),a), blocker=f.play(new Stormtrooper(g,b.getId()),b);
        f.setPhase(ATTACK,a.getId(),a.getId());
        g.selectAttackers(a.getId(),new Types.AttackerAssignment[]{attack(attacker,b.getId())});
        assertEquals(ATTACK_PLAY,g.phase); assertTrue(attacker.isDepleted()); assertTrue(attacker.isAttacking());
        g.pass(a.getId()); g.pass(b.getId()); assertEquals(BLOCK,g.phase);
        reject(() -> g.selectBlockers(a.getId(),new Types.BlockerAssignment[]{block(blocker,attacker)}));
        g.selectBlockers(b.getId(),new Types.BlockerAssignment[]{block(blocker,attacker)});
        assertEquals(BLOCK_PLAY,g.phase); assertEquals(Arrays.asList(blocker.getId()),g.blockers);
        assertTrue(blocker.isDepleted()); assertTrue(blocker.toCardMessage().getDepleted());
    }
    @Test public void blockingChecksKeywordsAndCannotOverwriteAnExistingAssignment() {
        Card attacker=f.play(new Stormtrooper(g,a.getId()),a), first=f.play(new Stormtrooper(g,b.getId()),b), second=f.play(new Stormtrooper(g,b.getId()),b);
        f.setPhase(ATTACK,a.getId(),a.getId());
        g.selectAttackers(a.getId(),new Types.AttackerAssignment[]{attack(attacker,b.getId())});
        f.setPhase(BLOCK,a.getId(),b.getId());
        attacker.addCombatAbility(Evasive,false);
        reject(() -> g.selectBlockers(b.getId(),new Types.BlockerAssignment[]{block(first,attacker)}));
        first.addCombatAbility(Reach,false); second.addCombatAbility(Reach,false);
        reject(() -> g.selectBlockers(b.getId(),new Types.BlockerAssignment[]{block(first,attacker),block(second,attacker)}));
        first.deplete();
        reject(() -> g.selectBlockers(b.getId(),new Types.BlockerAssignment[]{block(first,attacker)}));
        first.newTurn(); attacker.addCombatAbility(Unblockable,false);
        reject(() -> g.selectBlockers(b.getId(),new Types.BlockerAssignment[]{block(first,attacker)}));
    }
    @Test public void combatRejectsWrongPhaseAndUnknownOrUndeclaredCards() {
        Card unit=f.play(new Stormtrooper(g,a.getId()),a), blocker=f.play(new Stormtrooper(g,b.getId()),b);
        reject(() -> g.selectAttackers(a.getId(),new Types.AttackerAssignment[]{attack(unit,b.getId())}));
        f.setPhase(ATTACK,a.getId(),a.getId());
        Types.AttackerAssignment bad=Types.AttackerAssignment.newBuilder().setAttackerId("broken").setAttacksTo(b.getId().toString()).build();
        reject(() -> g.selectAttackers(a.getId(),new Types.AttackerAssignment[]{bad}));
        reject(() -> g.selectAttackers(a.getId(),null));
        f.setPhase(BLOCK,a.getId(),b.getId());
        reject(() -> g.selectBlockers(b.getId(),new Types.BlockerAssignment[]{block(blocker,unit)}));
    }
    @Test public void emptyCombatDeclarationsRemainLegal() {
        f.setPhase(ATTACK,a.getId(),a.getId());
        g.selectAttackers(a.getId(),new Types.AttackerAssignment[0]);
        assertEquals(MAIN_AFTER,g.phase);
        f.setPhase(BLOCK,a.getId(),b.getId());
        g.selectBlockers(b.getId(),new Types.BlockerAssignment[0]);
        assertEquals(BLOCK_PLAY,g.phase);
    }
}

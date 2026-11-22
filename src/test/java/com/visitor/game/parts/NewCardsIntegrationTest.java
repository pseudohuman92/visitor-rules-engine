package com.visitor.game.parts;

import com.visitor.card.Card;
import com.visitor.card.properties.Damagable.CombatAbility;
import com.visitor.game.Event;
import com.visitor.game.IllegalGameActionException;
import com.visitor.game.Player;
import com.visitor.helpers.Arraylist;
import com.visitor.helpers.CounterMap;
import com.visitor.helpers.HelperFunctions;
import com.visitor.protocol.ClientGameMessages;
import com.visitor.protocol.Types;
import com.visitor.sets.base2.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static com.visitor.game.parts.Base.Zone.*;
import static com.visitor.protocol.Types.Knowledge.*;
import static org.junit.Assert.*;

public class NewCardsIntegrationTest {
    private GameTestFixture f;

    @Before public void setUp() { f = new GameTestFixture(); }
    @After public void tearDown() throws Exception { f.close(); }

    private void fund(CounterMap<Types.Knowledge> knowledge) {
        f.player.addKnowledge(knowledge);
        f.player.addEnergy(20, false);
        f.player.addMaxEnergy(20);
    }

    private <T extends Card> T board(T card, Player owner) {
        f.play(card, owner);
        card.enterPlay();
        return card;
    }

    private void resolve() {
        f.game.pass(f.opponent.getId());
        f.game.pass(f.player.getId());
    }

    private Types.TargetSelection select(Types.Targeting group, UUID... targets) {
        Types.TargetSelection.Builder selection = Types.TargetSelection.newBuilder().setId(group.getId());
        for (UUID target : targets) selection.addTargets(target.toString());
        return selection.build();
    }

    private Arraylist<Types.TargetSelection> selections(Types.TargetSelection... targets) {
        return new Arraylist<>(targets);
    }

    @Test public void allTwentyFiveCardsAreLoadableWithPrintedCostsAndTypes() {
        Object[][] cards = {
                {"Bloodrunner", 1, 2, 1, Card.CardType.Unit, 1, 1, 0},
                {"GravefrontRecruiter", 2, 2, 2, Card.CardType.Unit, 1, 2, 0},
                {"BloodfireDemolitionist", 3, 3, 2, Card.CardType.Unit, 2, 1, 0},
                {"AshenReclaimer", 4, 3, 4, Card.CardType.Unit, 2, 1, 0},
                {"CrimsonWarmonger", 6, 5, 5, Card.CardType.Unit, 3, 2, 0},
                {"BloodAmmunition", 1, 0, 0, Card.CardType.Spell, 1, 1, 0},
                {"ViolentResurrection", 2, 0, 0, Card.CardType.Spell, 1, 2, 0},
                {"LastStand", 3, 0, 0, Card.CardType.Spell, 2, 1, 0},
                {"MassExecution", 4, 0, 0, Card.CardType.Spell, 2, 2, 0},
                {"TheBloodworks", 3, 0, 0, Card.CardType.Asset, 1, 2, 0},
                {"JuniorResearcher", 1, 1, 2, Card.CardType.Unit, 0, 0, 1},
                {"KnowledgeHarvester", 2, 1, 3, Card.CardType.Unit, 0, 0, 2},
                {"ExperimentalDrone", 2, 2, 2, Card.CardType.Unit, 0, 0, 1},
                {"NeuralAnalyst", 3, 2, 3, Card.CardType.Unit, 0, 0, 2},
                {"QuantumTechnician", 3, 2, 4, Card.CardType.Unit, 0, 0, 3},
                {"RealityArchitect", 4, 3, 4, Card.CardType.Unit, 0, 0, 2},
                {"CognitiveOverseer", 5, 3, 5, Card.CardType.Unit, 0, 0, 4},
                {"ParallaxEntity", 6, 6, 6, Card.CardType.Unit, 0, 0, 4},
                {"AcceleratedLearning", 1, 0, 0, Card.CardType.Spell, 0, 0, 1},
                {"EnergyConversion", 2, 0, 0, Card.CardType.Spell, 0, 0, 2},
                {"ExperimentalBreakthrough", 3, 0, 0, Card.CardType.Spell, 0, 0, 2},
                {"KnowledgeOverload", 4, 0, 0, Card.CardType.Spell, 0, 0, 3},
                {"AbsoluteUnderstanding", 6, 0, 0, Card.CardType.Spell, 0, 0, 4},
                {"ResearchLaboratory", 2, 0, 0, Card.CardType.Asset, 0, 0, 2},
                {"QuantumReactor", 4, 0, 0, Card.CardType.Asset, 0, 0, 4}
        };
        int units = 0, spells = 0, assets = 0;
        for (Object[] spec : cards) {
            Card card = HelperFunctions.createCard(f.game, f.player.getId(), "base2." + spec[0]);
            assertNotNull((String) spec[0], card);
            assertEquals(spec[0], card.getClass().getSimpleName());
            assertEquals(spec[1], card.getCost());
            assertTrue(card.hasType((Card.CardType) spec[4]));
            assertEquals(spec[0] + " Red", spec[5], card.knowledge.getOrDefault(RED, 0));
            assertEquals(spec[0] + " Purple", spec[6], card.knowledge.getOrDefault(PURPLE, 0));
            assertEquals(spec[0] + " Blue", spec[7], card.knowledge.getOrDefault(BLUE, 0));
            if (card.hasType(Card.CardType.Unit)) {
                units++;
                assertEquals(spec[2], card.getAttack());
                assertEquals(spec[3], card.getHealth());
            } else if (card.hasType(Card.CardType.Spell)) spells++;
            else assets++;
        }
        assertEquals(13, units);
        assertEquals(9, spells);
        assertEquals(3, assets);
        assertTrue(new Bloodrunner(f.game, f.player.getId()).hasCombatAbility(CombatAbility.Blitz));
        assertTrue(new ParallaxEntity(f.game, f.player.getId()).hasCombatAbility(CombatAbility.Evasive));
    }

    @Test public void researchConsumesStudyWithoutResourcesAndPersistsAcrossTurns() {
        fund(new CounterMap<>(BLUE, 1));
        ExperimentalDrone drone = f.hand(new ExperimentalDrone(f.game, f.player.getId()), f.player);
        assertFalse(drone.canPlay(true));
        assertEquals(1, drone.toCardMessage().getResearchRequirement());
        f.game.studyCard(f.player.getId(), drone.getId(), true, new CounterMap<>());
        assertEquals(1, drone.getResearchProgress());
        assertEquals(1, drone.getActualResearchStudies());
        assertEquals(20, f.player.getEnergy());
        assertEquals(20, f.player.getMaxEnergy());
        assertEquals(1, f.player.getKnowledgeCount(BLUE));
        assertFalse(drone.canPlay(true)); // Studied cards cannot be played until the next turn.
        f.player.endTurn();
        f.player.newTurn();
        assertTrue(drone.canPlay(true));
        assertEquals(1, drone.toCardMessage().getResearchProgress());
        int deckBefore = f.player.deck.size();
        f.game.playCard(f.player.getId(), drone.getId(), new Arraylist<>());
        resolve();
        assertEquals(Play, drone.zone);
        assertEquals(deckBefore - 1, f.player.deck.size());
    }

    @Test public void laboratoryCountersDoNotCountAsStudiesForRealityArchitect() {
        fund(new CounterMap<>(BLUE, 2));
        ResearchLaboratory laboratory = board(new ResearchLaboratory(f.game, f.player.getId()), f.player);
        RealityArchitect architect = f.hand(new RealityArchitect(f.game, f.player.getId()), f.player);
        Types.TargetingAbility ability = laboratory.toCardMessage().getActivateTargets(0);
        f.game.activateCard(f.player.getId(), laboratory.getId(), UUID.fromString(ability.getId()),
                new Arraylist<>(select(ability.getTargets(0), architect.getId())));
        resolve();
        assertEquals(1, architect.getResearchProgress());
        assertEquals(0, architect.getActualResearchStudies());
        assertEquals(20, f.player.getEnergy());
        f.game.studyCard(f.player.getId(), architect.getId(), true, new CounterMap<>());
        assertEquals(2, architect.getResearchProgress());
        f.player.endTurn();
        f.player.newTurn();
        f.game.playCard(f.player.getId(), architect.getId(), new Arraylist<>());
        resolve();
        assertEquals(17, f.player.getEnergy()); // Four paid, one temporary energy gained.
        assertEquals(1, architect.getActualResearchStudies());
    }

    @Test public void freePlayIsExplicitAndStillChecksResearchAndKnowledge() {
        fund(new CounterMap<>(BLUE, 4));
        AbsoluteUnderstanding understanding = f.hand(new AbsoluteUnderstanding(f.game, f.player.getId()), f.player);
        understanding.addResearchProgress(3, false);
        f.player.removeEnergy(14); // Six energy remains for Absolute Understanding.
        f.game.playCard(f.player.getId(), understanding.getId(), new Arraylist<>());
        resolve();
        assertEquals(3, f.player.getEnergy());
        assertEquals(1, f.player.toPlayerMessage(true).getFreePlayCredits());
        CrimsonWarmonger wrongKnowledge = f.hand(new CrimsonWarmonger(f.game, f.player.getId()), f.player);
        try {
            f.game.playCard(f.player.getId(), wrongKnowledge.getId(), true, new Arraylist<>());
            fail("Free play must not waive knowledge");
        } catch (IllegalGameActionException expected) { /* expected */ }
        ParallaxEntity entity = f.hand(new ParallaxEntity(f.game, f.player.getId()), f.player);
        try {
            f.game.playCard(f.player.getId(), entity.getId(), true, new Arraylist<>());
            fail("Research must still be completed");
        } catch (IllegalGameActionException expected) { /* expected */ }
        entity.addResearchProgress(3, false);
        try {
            f.game.playCard(f.player.getId(), entity.getId(), new Arraylist<>());
            fail("Normal play must still pay energy");
        } catch (IllegalGameActionException expected) { /* expected */ }
        int deckBefore = f.player.deck.size();
        f.game.playCard(f.player.getId(), entity.getId(), true, new Arraylist<>());
        assertEquals(0, f.player.toPlayerMessage(true).getFreePlayCredits());
        assertEquals(3, f.player.getEnergy());
        resolve();
        assertEquals(deckBefore - 2, f.player.deck.size());
        assertTrue(entity.hasCombatAbility(CombatAbility.Evasive));
        assertEquals(Play, entity.zone);
        assertTrue(ClientGameMessages.PlayCard.newBuilder().setUseFreePlay(true).build().getUseFreePlay());
    }

    @Test public void bloodworksCountsCostSacrificeAndLimitsReplacementToOncePerTurn() {
        TheBloodworks bloodworks = board(new TheBloodworks(f.game, f.player.getId()), f.player);
        board(new Necropainter(f.game, f.player.getId()), f.player);
        Bloodrunner runner = board(new Bloodrunner(f.game, f.player.getId()), f.player);
        int opposingHealth = f.opponent.damagable.getHealth();
        int ownHealth = f.player.damagable.getHealth();
        f.game.payAdditionalCosts(() -> f.game.destroy(runner.getId()));
        f.game.processEvents();
        assertEquals(opposingHealth - 3, f.opponent.damagable.getHealth());
        assertEquals(ownHealth + 1, f.player.damagable.getHealth());
        Bloodrunner replacement = null;
        for (Card card : f.player.playArea) if (card instanceof Bloodrunner) replacement = (Bloodrunner) card;
        assertNotNull(replacement);
        f.game.destroy(replacement.getId());
        f.game.processEvents();
        assertEquals(opposingHealth - 5, f.opponent.damagable.getHealth());
        assertEquals(0, f.player.playArea.stream().filter(card -> card instanceof Bloodrunner).count());
        f.game.addEvent(Event.turnStart(f.player.getId()), true);
        Bloodrunner next = board(new Bloodrunner(f.game, f.player.getId()), f.player);
        f.game.destroy(next.getId());
        f.game.processEvents();
        assertEquals(1, f.player.playArea.stream().filter(card -> card instanceof Bloodrunner).count());
        assertSame(bloodworks, f.player.playArea.get(0));
    }

    @Test public void temporaryBlitzIsRememberedWhenRecruiterSeesDeath() {
        board(new GravefrontRecruiter(f.game, f.player.getId()), f.player);
        Stormtrooper troop = board(new Stormtrooper(f.game, f.player.getId()), f.player);
        troop.addCombatAbility(CombatAbility.Blitz, true);
        f.game.destroy(troop.getId());
        f.game.processEvents();
        assertEquals(1, f.player.playArea.stream().filter(card -> "Bat".equals(card.name)).count());
    }

    @Test public void massExecutionCountsOnlySelectedUnitsAndWarmongerLeavesThreeTroopers() {
        fund(new CounterMap<>(RED, 3).add(PURPLE, 2));
        CrimsonWarmonger warmonger = board(new CrimsonWarmonger(f.game, f.player.getId()), f.player);
        Stormtrooper troop = board(new Stormtrooper(f.game, f.player.getId()), f.player);
        MassExecution execution = f.hand(new MassExecution(f.game, f.player.getId()), f.player);
        Types.Targeting group = execution.toCardMessage().getPlayTargets(1);
        int health = f.opponent.damagable.getHealth();
        f.game.playCard(f.player.getId(), execution.getId(),
                new Arraylist<>(select(group, warmonger.getId(), troop.getId())));
        resolve();
        assertEquals(health - 6, f.opponent.damagable.getHealth()); // Spell, Warmonger, Stormtrooper.
        assertEquals(3, f.player.playArea.stream().filter(card -> card instanceof Stormtrooper).count());
        assertEquals(Discard_Pile, warmonger.zone);
        assertEquals(Discard_Pile, troop.zone);
        MassExecution empty = f.hand(new MassExecution(f.game, f.player.getId()), f.player);
        f.game.playCard(f.player.getId(), empty.getId(), new Arraylist<>());
        resolve();
        assertEquals(health - 6, f.opponent.damagable.getHealth());
    }

    @Test public void resurrectionCreatesFreshCopyWithBlitzDecayAndPrintedDeathEffect() {
        fund(new CounterMap<>(RED, 1).add(PURPLE, 2));
        DeploymentTruck truck = board(new DeploymentTruck(f.game, f.player.getId()), f.player);
        f.game.destroy(truck.getId());
        f.game.processEvents();
        int troopsBefore = f.player.playArea.size();
        ViolentResurrection resurrection = f.hand(new ViolentResurrection(f.game, f.player.getId()), f.player);
        f.game.playCard(f.player.getId(), resurrection.getId(),
                new Arraylist<>(select(resurrection.toCardMessage().getPlayTargets(0), truck.getId())));
        resolve();
        assertFalse(f.player.discardPile.contains(truck));
        DeploymentTruck copy = null;
        for (Card card : f.player.playArea) if (card instanceof DeploymentTruck) copy = (DeploymentTruck) card;
        assertNotNull(copy);
        assertNotEquals(truck.getId(), copy.getId());
        assertTrue(copy.hasCombatAbility(CombatAbility.Blitz));
        assertTrue(copy.hasCombatAbility(CombatAbility.Decay));
        assertEquals(troopsBefore + 1, f.player.playArea.size());
        f.game.destroy(copy.getId());
        f.game.processEvents();
        assertEquals(troopsBefore + 3, f.player.playArea.size());
    }

    @Test public void lastStandDestroysUnitsThatAttackedLaterThatTurn() {
        fund(new CounterMap<>(RED, 2).add(PURPLE, 1));
        Stormtrooper attacker = board(new Stormtrooper(f.game, f.player.getId()), f.player);
        Stormtrooper bystander = board(new Stormtrooper(f.game, f.player.getId()), f.player);
        LastStand stand = f.hand(new LastStand(f.game, f.player.getId()), f.player);
        f.game.playCard(f.player.getId(), stand.getId(), new Arraylist<>());
        resolve();
        assertEquals(3, attacker.getAttack());
        assertTrue(attacker.hasCombatAbility(CombatAbility.Blitz));
        f.setPhase(Types.Phase.ATTACK, f.player.getId(), f.player.getId());
        Types.AttackerAssignment assignment = Types.AttackerAssignment.newBuilder()
                .setAttackerId(attacker.getId().toString()).setAttacksTo(f.opponent.getId().toString()).build();
        f.game.selectAttackers(f.player.getId(), new Types.AttackerAssignment[]{assignment});
        f.setPhase(Types.Phase.MAIN_AFTER, f.player.getId(), f.player.getId());
        f.game.changePhase();
        assertEquals(Discard_Pile, attacker.zone);
        assertEquals(Play, bystander.zone);
        assertEquals(1, bystander.getAttack());
    }

    @Test public void studyTriggersUseOneKnowledgeEventPerTomeAndResetEachTurn() {
        board(new KnowledgeHarvester(f.game, f.player.getId()), f.player);
        NeuralAnalyst analyst = board(new NeuralAnalyst(f.game, f.player.getId()), f.player);
        board(new CognitiveOverseer(f.game, f.player.getId()), f.player);
        board(new ResearchLaboratory(f.game, f.player.getId()), f.player);
        Xenoengineering tome = f.hand(new Xenoengineering(f.game, f.player.getId()), f.player);
        int deckBefore = f.player.deck.size();
        f.game.studyCard(f.player.getId(), tome.getId(), true, new CounterMap<>());
        assertEquals(2, f.player.getKnowledgeCount(BLUE));
        assertEquals(3, analyst.getAttack());
        assertEquals(4, analyst.getHealth());
        assertEquals(3, f.player.getEnergy()); // Study, Harvester, Laboratory.
        assertTrue(f.player.hasStudy(1)); // Overseer grants one after the first study.
        assertEquals(deckBefore - 1, f.player.deck.size());
        Xenoengineering second = f.hand(new Xenoengineering(f.game, f.player.getId()), f.player);
        f.game.studyCard(f.player.getId(), second.getId(), true, new CounterMap<>());
        assertFalse(f.player.hasStudy(1));
        assertEquals(5, f.player.getEnergy()); // Second study and Harvester only.
        f.player.endTurn();
        f.player.newTurn();
        assertEquals(0, f.player.getStudiesThisTurn());
        Xenoengineering third = f.hand(new Xenoengineering(f.game, f.player.getId()), f.player);
        f.game.studyCard(f.player.getId(), third.getId(), true, new CounterMap<>());
        assertTrue(f.player.hasStudy(1));
    }

    @Test public void knowledgeOverloadTargetingAndBlueEnergyCapsFollowKnowledge() {
        fund(new CounterMap<>(BLUE, 3));
        Stormtrooper enemy = board(new Stormtrooper(f.game, f.opponent.getId()), f.opponent);
        KnowledgeOverload overload = f.hand(new KnowledgeOverload(f.game, f.player.getId()), f.player);
        assertTrue(overload.toCardMessage().getPlayTargets(0).getPossibleTargetsList().contains(enemy.getId().toString()));
        assertFalse(overload.toCardMessage().getPlayTargets(0).getPossibleTargetsList().contains(f.opponent.getId().toString()));
        try {
            f.game.playCard(f.player.getId(), overload.getId(),
                    new Arraylist<>(select(overload.toCardMessage().getPlayTargets(0), f.opponent.getId())));
            fail("A player needs four Blue knowledge to be a legal target");
        } catch (IllegalGameActionException expected) { /* expected */ }
        f.player.addKnowledge(new CounterMap<>(BLUE, 1));
        assertTrue(overload.toCardMessage().getPlayTargets(0).getPossibleTargetsList().contains(f.opponent.getId().toString()));
        int health = f.opponent.damagable.getHealth();
        f.game.playCard(f.player.getId(), overload.getId(),
                new Arraylist<>(select(overload.toCardMessage().getPlayTargets(0), f.opponent.getId())));
        resolve();
        assertEquals(health - 4, f.opponent.damagable.getHealth());
        EnergyConversion conversion = f.hand(new EnergyConversion(f.game, f.player.getId()), f.player);
        int energy = f.player.getEnergy();
        f.game.playCard(f.player.getId(), conversion.getId(), new Arraylist<>());
        resolve();
        assertEquals(energy + 2, f.player.getEnergy());
        QuantumReactor reactor = board(new QuantumReactor(f.game, f.player.getId()), f.player);
        f.player.addKnowledge(new CounterMap<>(BLUE, 2));
        int beforeTurn = f.player.getEnergy();
        f.game.addEvent(Event.turnStart(f.player.getId()), true);
        assertEquals(beforeTurn + 3, f.player.getEnergy());
        ExperimentalDrone drone = f.hand(new ExperimentalDrone(f.game, f.player.getId()), f.player);
        drone.addResearchProgress(1, false);
        int deckBefore = f.player.deck.size();
        f.game.playCard(f.player.getId(), drone.getId(), new Arraylist<>());
        assertEquals(deckBefore - 1, f.player.deck.size()); // Reactor draws on play.
        resolve();
        assertEquals(deckBefore - 2, f.player.deck.size()); // Drone draws on entry.
        assertSame(reactor, f.player.playArea.stream().filter(card -> card == reactor).findFirst().get());
    }

    @Test public void bloodAmmunitionSnapshotsAttackOnlyAfterLegalTargetsAndSacrificesAsCost() {
        fund(new CounterMap<>(RED, 1).add(PURPLE, 1));
        Card ally = board(com.visitor.sets.token.UnitToken.Warrior_1_1(f.game, f.player.getId()), f.player);
        ally.addAttackAndHealth(2, 0, true);
        BloodAmmunition spell = f.hand(new BloodAmmunition(f.game, f.player.getId()), f.player);
        Types.Targeting cost = spell.toCardMessage().getPlayTargets(0);
        Types.Targeting effect = spell.toCardMessage().getPlayTargets(1);
        try {
            f.game.playCard(f.player.getId(), spell.getId(), selections(
                    select(cost, ally.getId()), select(effect, f.player.getId())));
            fail("Friendly player cannot be targeted");
        } catch (IllegalGameActionException expected) { /* expected */ }
        assertEquals(Play, ally.zone);
        int health = f.opponent.damagable.getHealth();
        f.game.playCard(f.player.getId(), spell.getId(), selections(
                select(cost, ally.getId()), select(effect, f.opponent.getId())));
        assertEquals(Discard_Pile, ally.zone);
        assertEquals(health, f.opponent.damagable.getHealth());
        resolve();
        assertEquals(health - 3, f.opponent.damagable.getHealth());
    }

    @Test public void demolitionistPaysSacrificeBeforeDamageAndReclaimerGainsTemporaryStats() {
        fund(new CounterMap<>(RED, 2).add(PURPLE, 1));
        BloodfireDemolitionist demolitionist = board(new BloodfireDemolitionist(f.game, f.player.getId()), f.player);
        AshenReclaimer reclaimer = board(new AshenReclaimer(f.game, f.player.getId()), f.player);
        board(new TheBloodworks(f.game, f.player.getId()), f.player);
        Card ally = board(com.visitor.sets.token.UnitToken.Warrior_1_1(f.game, f.player.getId()), f.player);
        Types.TargetingAbility ability = demolitionist.toCardMessage().getActivateTargets(0);
        int health = f.opponent.damagable.getHealth();
        f.game.activateCard(f.player.getId(), demolitionist.getId(), UUID.fromString(ability.getId()),
                selections(select(ability.getTargets(0), ally.getId()),
                        select(ability.getTargets(1), f.opponent.getId())));
        assertEquals(Discard_Pile, ally.zone);
        assertEquals(health - 1, f.opponent.damagable.getHealth()); // Bloodworks sacrifice trigger.
        resolve();
        assertEquals(health - 3, f.opponent.damagable.getHealth());
        assertEquals(4, reclaimer.getAttack());
        f.setPhase(Types.Phase.ATTACK, f.player.getId(), f.player.getId());
        Types.AttackerAssignment attack = Types.AttackerAssignment.newBuilder()
                .setAttackerId(reclaimer.getId().toString()).setAttacksTo(f.opponent.getId().toString()).build();
        f.game.selectAttackers(f.player.getId(), new Types.AttackerAssignment[]{attack});
        assertTrue(reclaimer.hasCombatAbility(CombatAbility.Trample));
        f.player.endTurn();
        assertEquals(3, reclaimer.getAttack());
        assertFalse(reclaimer.hasCombatAbility(CombatAbility.Trample));
    }

    @Test public void blueStudyAndEnergyCardsResolveWithTheirPrintedAmounts() {
        fund(new CounterMap<>(BLUE, 3));
        JuniorResearcher junior = f.hand(new JuniorResearcher(f.game, f.player.getId()), f.player);
        f.game.playCard(f.player.getId(), junior.getId(), new Arraylist<>());
        resolve();
        assertTrue(f.player.hasStudy(2));
        int deckBefore = f.player.deck.size();
        AcceleratedLearning learning = f.hand(new AcceleratedLearning(f.game, f.player.getId()), f.player);
        f.game.playCard(f.player.getId(), learning.getId(), new Arraylist<>());
        resolve();
        assertTrue(f.player.hasStudy(3));
        assertEquals(deckBefore - 1, f.player.deck.size());
        QuantumTechnician technician = board(new QuantumTechnician(f.game, f.player.getId()), f.player);
        Types.TargetingAbility ability = technician.toCardMessage().getActivateTargets(0);
        int energy = f.player.getEnergy();
        f.game.activateCard(f.player.getId(), technician.getId(), UUID.fromString(ability.getId()), new Arraylist<>());
        resolve();
        assertEquals(energy + 2, f.player.getEnergy());
        assertTrue(technician.isDepleted());
        ExperimentalBreakthrough breakthrough = f.hand(new ExperimentalBreakthrough(f.game, f.player.getId()), f.player);
        breakthrough.addResearchProgress(2, false);
        energy = f.player.getEnergy();
        deckBefore = f.player.deck.size();
        f.game.playCard(f.player.getId(), breakthrough.getId(), new Arraylist<>());
        resolve();
        assertEquals(energy - 1, f.player.getEnergy()); // Three paid, two temporary gained.
        assertEquals(deckBefore - 3, f.player.deck.size());
    }
}

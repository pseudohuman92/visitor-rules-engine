package com.visitor.game.parts;

import com.visitor.card.Card;
import com.visitor.card.containers.Damage;
import com.visitor.game.Player;
import com.visitor.helpers.Arraylist;
import com.visitor.sets.base2.Stormtrooper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class ZoneAndDeckRoutingTest {
    private GameTestFixture f;
    @Before public void setUp() { f=new GameTestFixture(); }
    @After public void tearDown() throws Exception { f.close(); }

    @Test public void eachCombinedZoneIncludesOnlyThatZoneExactlyOnce() {
        for (Player p : Arrays.asList(f.player,f.opponent)) {
            f.play(new Stormtrooper(f.game,p.getId()),p);
            Card discarded=new Stormtrooper(f.game,p.getId());
            discarded.zone=Base.Zone.Discard_Pile;
            p.discardPile.add(discarded);
        }
        Card stack=new Stormtrooper(f.game,f.player.getId());
        stack.zone=Base.Zone.Stack; f.game.stack.add(stack);
        for (Base.Zone zone : Arrays.asList(Base.Zone.Deck,Base.Zone.Hand,Base.Zone.Play,Base.Zone.Discard_Pile)) {
            Arraylist<Card> actual=f.game.getBothZones(zone);
            assertEquals(f.game.getZone(f.player.getId(),zone).size()+f.game.getZone(f.opponent.getId(),zone).size(),actual.size());
            assertEquals(actual.size(),new HashSet<>(actual).size());
            for (Card card : actual) assertEquals(zone,card.zone);
        }
        assertEquals(f.game.getBothZones(Base.Zone.Play),f.game.getBothZones(Base.Zone.Both_Play));
        assertEquals(Arrays.asList(stack),f.game.getBothZones(Base.Zone.Stack));
    }

    @Test public void globalDamageHitsBattlefieldOnceAndLeavesOtherZonesAlone() {
        Card source=f.play(new Stormtrooper(f.game,f.player.getId()),f.player);
        Card other=f.play(new Stormtrooper(f.game,f.opponent.getId()),f.opponent);
        source.setHealth(10); other.setHealth(10);
        Card hand=f.player.hand.get(0);
        Card discard=new Stormtrooper(f.game,f.player.getId());
        discard.zone=Base.Zone.Discard_Pile; f.player.discardPile.add(discard);
        Card stacked=new Stormtrooper(f.game,f.player.getId());
        stacked.zone=Base.Zone.Stack; f.game.stack.add(stacked);
        f.game.dealDamageToAll(f.player.getId(),source.getId(),new Damage(2));
        assertEquals(8,source.getHealth()); assertEquals(8,other.getHealth());
        assertEquals(1,hand.getHealth()); assertEquals(1,discard.getHealth()); assertEquals(1,stacked.getHealth());
        assertEquals(28,f.player.damagable.getHealth()); assertEquals(28,f.opponent.damagable.getHealth());
    }

    @Test public void topAndBottomRoutesExtractFromHandAndUseCorrectEnd() {
        Card top=f.player.hand.get(0), bottom=f.player.hand.get(1);
        Card previousTop=f.player.deck.get(0), previousBottom=f.player.deck.get(f.player.deck.size()-1);
        f.game.putToTopOfDeck(top.getId());
        f.game.putToBottomOfDeck(bottom.getId());
        assertSame(top,f.player.deck.get(0));
        assertSame(previousTop,f.player.deck.get(1));
        assertSame(bottom,f.player.deck.get(f.player.deck.size()-1));
        assertSame(previousBottom,f.player.deck.get(f.player.deck.size()-2));
        assertFalse(f.player.hand.contains(top)); assertFalse(f.player.hand.contains(bottom));
        assertEquals(Base.Zone.Deck,top.zone); assertEquals(Base.Zone.Deck,bottom.zone);
    }

    @Test public void gameDeckRoutesAlsoWorkWhenDestinationIsEmpty() {
        f.player.deck.clear();
        Card a=f.player.hand.get(0), b=f.player.hand.get(1), c=f.player.hand.get(2);
        f.game.putToTopOfDeck(a.getId());
        f.game.putToBottomOfDeck(b.getId());
        f.game.putToBottomOfDeck(f.player.getId(),new Arraylist<>(f.game.extractCard(c.getId())));
        assertEquals(Arrays.asList(a,b,c),f.player.deck);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visitor.game;

import com.visitor.set1.*;
import java.util.Random;

/**
 *
 * @author pseudo
 */
public class TestDecks {
    
    static final int numOfDecks = 4;
    
    public static Deck randomDeck(String username){
        Random r = new Random();
        switch (r.nextInt(numOfDecks - 1)){
            case 0:
                return blackDeck(username);
            case 1:
                return blueDeck(username);
            case 2:
                return redDeck(username);
            case 3:
                return yellowDeck(username);
                
            default:
                return null;
        }
    }
    
    public static Deck blackDeck(String username){
        Deck d = new Deck(username);
        for (int i = 0; i < 3; i++){
            
            d.add(new WeakWill(username));
            d.add(new Pilfer(username));
            d.add(new CrossroadTrade(username));
            d.add(new WeakLink(username));
            d.add(new IrRunic(username));
            
            d.add(new Thief(username));
            d.add(new HandofIllWill(username));
            d.add(new EntropySurge(username));
            d.add(new RuneofUnnaturalLife(username));
            d.add(new IdleHand(username));
            d.add(new MindSlavesThrash(username));
            d.add(new Parasytid(username));
            d.add(new EyeofIntent(username));
        }
        return d;
    }
    
    public static Deck blueDeck(String username){
        Deck d = new Deck(username);
        for (int i = 0; i < 3; i++){
            
            d.add(new RegressiveHierarchy(username));
            d.add(new Recycle(username));
            d.add(new UA03(username));
            d.add(new NaniteSabotage(username));
            d.add(new RecombinantReplication(username));
            
            d.add(new SelfdestructNode(username));
            d.add(new NaniteDeconstructor(username));
            d.add(new DataRecompiler(username));
            d.add(new SalvageForge(username));
            d.add(new QuantumStateExtractor(username));
            d.add(new SynapticCapacitor(username));
            d.add(new UncertaintyGun(username));
            d.add(new UI08(username));
        }
        return d;
    }
    
    public static Deck yellowDeck(String username){
        Deck d = new Deck(username);
        for (int i = 0; i < 3; i++){
            
            d.add(new TemporalReflection(username));
            d.add(new Nullify(username));
            d.add(new Neutralize(username));
            d.add(new Rewind(username));
            d.add(new ChronologicalReversal(username));
            d.add(new FastForward(username));
            
            d.add(new MetaphasicShieldMK5(username));
            d.add(new MetaphasicShieldMK7(username));
            d.add(new MetaphasicShieldCoil(username));
            d.add(new NSink(username));
            d.add(new KineticReflectors(username));
            d.add(new YI06(username));
        }
        return d;
    }
    
    public static Deck redDeck(String username){
        Deck d = new Deck(username);
        for (int i = 0; i < 3; i++){
            
            d.add(new ForcedRetreat(username));
            d.add(new PriorityOps(username));
            d.add(new WalkingFire(username));
            d.add(new Reinforcements(username));
            d.add(new Extraction(username));
            d.add(new Withdrawal(username));
            
            d.add(new FragGrenade(username));
            d.add(new RI02(username));
            d.add(new KillTeam(username));
            d.add(new MortarTeam(username));
            d.add(new RI05(username));
            d.add(new RI06(username));
            d.add(new ShockTroop(username));
        }
        return d;
    }
}

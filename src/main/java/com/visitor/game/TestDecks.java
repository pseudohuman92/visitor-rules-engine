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
    
    public static Deck randomDeck(String userId){
        Random r = new Random();
        switch (r.nextInt(numOfDecks - 1)){
            case 0:
                return blackDeck(userId);
            case 1:
                return blueDeck(userId);
            case 2:
                return redDeck(userId);
            case 3:
                return yellowDeck(userId);
                
            default:
                return null;
        }
    }
    
    public static Deck blackDeck(String userId){
        Deck d = new Deck(userId);
        for (int i = 0; i < 3; i++){
            
            d.add(new WeakWill(userId));
            d.add(new Pilfer(userId));
            d.add(new CrossroadTrade(userId));
            d.add(new WeakLink(userId));
            d.add(new IrRunic(userId));
            
            d.add(new Thief(userId));
            d.add(new HandofIllWill(userId));
            d.add(new EntropySurge(userId));
            d.add(new RuneofUnnaturalLife(userId));
            d.add(new IdleHand(userId));
            d.add(new MindSlavesThrash(userId));
            d.add(new Parasytid(userId));
            d.add(new EyeofIntent(userId));
        }
        return d;
    }
    
    public static Deck blueDeck(String userId){
        Deck d = new Deck(userId);
        for (int i = 0; i < 3; i++){
            
            d.add(new RegressiveHierarchy(userId));
            d.add(new Recycle(userId));
            d.add(new UA03(userId));
            d.add(new NaniteSabotage(userId));
            d.add(new RecombinantReplication(userId));
            
            d.add(new SelfdestructNode(userId));
            d.add(new NaniteDeconstructor(userId));
            d.add(new DataRecompiler(userId));
            d.add(new SalvageForge(userId));
            d.add(new QuantumStateExtractor(userId));
            d.add(new SynapticCapacitor(userId));
            d.add(new UncertaintyGun(userId));
            d.add(new UI08(userId));
        }
        return d;
    }
    
    public static Deck yellowDeck(String userId){
        Deck d = new Deck(userId);
        for (int i = 0; i < 3; i++){
            
            d.add(new TemporalReflection(userId));
            d.add(new Nullify(userId));
            d.add(new Neutralize(userId));
            d.add(new Rewind(userId));
            d.add(new ChronologicalReversal(userId));
            d.add(new FastForward(userId));
            
            d.add(new MetaphasicShieldMK5(userId));
            d.add(new MetaphasicShieldMK7(userId));
            d.add(new MetaphasicShieldCoil(userId));
            d.add(new NSink(userId));
            d.add(new KineticReflectors(userId));
            d.add(new YI06(userId));
        }
        return d;
    }
    
    public static Deck redDeck(String userId){
        Deck d = new Deck(userId);
        for (int i = 0; i < 3; i++){
            
            d.add(new ForcedRetreat(userId));
            d.add(new PriorityOps(userId));
            d.add(new WalkingFire(userId));
            d.add(new Reinforcements(userId));
            d.add(new Extraction(userId));
            d.add(new Withdrawal(userId));
            
            d.add(new FragGrenade(userId));
            d.add(new RI02(userId));
            d.add(new KillTeam(userId));
            d.add(new MortarTeam(userId));
            d.add(new RI05(userId));
            d.add(new RI06(userId));
            d.add(new ShockTroop(userId));
        }
        return d;
    }
}

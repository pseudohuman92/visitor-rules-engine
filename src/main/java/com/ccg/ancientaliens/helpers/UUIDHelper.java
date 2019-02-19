/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.helpers;

import com.ccg.ancientaliens.card.types.Card;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public abstract class UUIDHelper {
    /**
     * Extracts the uuids of the given list of cards while preserving their order.
     * @param cards
     * @return
     */
    public static ArrayList<UUID> toUUIDList(ArrayList<Card> cards) {
        ArrayList<UUID> uuids = new ArrayList<>();
        while (!cards.isEmpty()){
            Card c = cards.remove(0);
            uuids.add(c.id);
        }
        return uuids;
    }
    
    public static ArrayList<UUID> toUUIDList(String[] cards) {
        ArrayList<UUID> uuids = new ArrayList<>();
        for (int i =0; i < cards.length; i++){
            uuids.add(UUID.fromString(cards[i]));
        }
        return uuids;
    }
    
    /**
     * Sorts given list of cards in the order of the provided uuids.
     * If a card appears in the card list but not in id list, card is ignored.
 If a id appears in id list but there is no card corresponding to it in the card list, id is ignored.
     * @param cards
     * @param uuids
     * @return
     */
    public static ArrayList<Card> sortByID(ArrayList<Card> cards, ArrayList<UUID> uuids) {
        ArrayList<Card> sorted = new ArrayList<>();
        while (!uuids.isEmpty()){
            UUID u = uuids.remove(0);
            for (Card c : cards){
                if (c.id.equals(u)){
                    sorted.add(c);
                    break;
                }
            }
        }
        return sorted;
    }

    
    public static ArrayList<Card> getInList(ArrayList<Card> cards, ArrayList<UUID> uuids) {
        ArrayList<Card> selected = new ArrayList<>();
        for (int i =0; i < cards.size(); i++){
            if(uuids.contains(cards.get(i).id))
                selected.add(cards.get(i));
        }
        return selected;
    }

    public static ArrayList<Card> getNotInList(ArrayList<Card> cards, ArrayList<UUID> uuids) {
        ArrayList<Card> selected = new ArrayList<>();
        for (int i =0; i < cards.size(); i++){
            if(!uuids.contains(cards.get(i).id))
                selected.add(cards.get(i));
        }
        return selected;
    }
}

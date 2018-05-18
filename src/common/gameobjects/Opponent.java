/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.gameobjects;

import java.util.ArrayList;

/**
 *
 * @author pseudo
 */
public class Opponent {
    private String name;
    private int life;
    private int deckSize;
    private int handSize;
    private ArrayList<Card> discardPile;
	
    private ArrayList<Card> ownedItems;
    private ArrayList<Card> controlledItems;
    
    public Opponent (){}
    
    public Opponent (String name, 
                    int life,
                    int deckSize,
                    int handSize,
                    ArrayList<Card> discardPile,	
                    ArrayList<Card> ownedItems,
                    ArrayList<Card> controlledItems) {
        this.name = name;
        this.life = life;
        this.deckSize = deckSize;
        this.handSize = handSize;
        this.discardPile = discardPile;
        this.ownedItems = ownedItems;
        this.controlledItems = controlledItems;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the life
     */
    public int getLife() {
        return life;
    }

    /**
     * @param life the life to set
     */
    public void setLife(int life) {
        this.life = life;
    }

    /**
     * @return the deckSize
     */
    public int getDeckSize() {
        return deckSize;
    }

    /**
     * @param deckSize the deckSize to set
     */
    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }

    /**
     * @return the handSize
     */
    public int getHandSize() {
        return handSize;
    }

    /**
     * @param handSize the handSize to set
     */
    public void setHandSize(int handSize) {
        this.handSize = handSize;
    }

    /**
     * @return the discardPile
     */
    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }

    /**
     * @param discardPile the discardPile to set
     */
    public void setDiscardPile(ArrayList<Card> discardPile) {
        this.discardPile = discardPile;
    }

    /**
     * @return the ownedItems
     */
    public ArrayList<Card> getOwnedItems() {
        return ownedItems;
    }

    /**
     * @param ownedItems the ownedItems to set
     */
    public void setOwnedItems(ArrayList<Card> ownedItems) {
        this.ownedItems = ownedItems;
    }

    /**
     * @return the controlledItems
     */
    public ArrayList<Card> getControlledItems() {
        return controlledItems;
    }

    /**
     * @param controlledItems the controlledItems to set
     */
    public void setControlledItems(ArrayList<Card> controlledItems) {
        this.controlledItems = controlledItems;
    }
}

package common.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    String name;
    int life;
    Deck deck;
    ArrayList<Card> hand;
    ArrayList<Card> discardPile;

    ArrayList<Card> ownedItems;
    ArrayList<Card> controlledItems;


    public Opponent toOpponent ()
    {
        return new Opponent(name, life, deck.size(), hand.size(), discardPile, ownedItems, controlledItems);
    }
}

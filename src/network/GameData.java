package network;
import card.Card;
import enums.Knowledge;
import game.ClientGame;
import game.Deck;
import game.Table;
import helpers.Hashmap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import com.google.gson.*;

import enums.Knowledge;
import helpers.Hashmap;

public class GameData {
	private UUID gameID;
	private String username;
	private UUID cardID;
	private  Hashmap<Knowledge, Integer> knowledge;
	private Deck deck;
	
	
	public GameData(UUID gameID, String username, UUID cardID, Hashmap<Knowledge, Integer> knowledge, Deck deck)
	{
		this.gameID = gameID;
		this.username = username;
		this.cardID = cardID;
		this.knowledge = knowledge;
		this.deck = deck;
		
	}
	
	
	

}

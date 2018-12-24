package network;




import cards.Card;
import game.ClientGame;
import enums.Knowledge;
import game.Table;
import game.Deck;
import helpers.Hashmap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pseudo
 */



public class Message implements Serializable {

   

    public MessageLabel label;
    public Serializable object;
    
    private Message(MessageLabel label, Serializable object) {
        this.label = label;
        this.object = object;
    }
    
    @Override
    public String toString() {
        return label + ": { " + object.toString() + " }";
    }
    
    public static Message register(String username){
    return new Message(MessageLabel.REGISTER, username);
    }

    public static Message login(String username){
        return new Message(MessageLabel.LOGIN, username);
    }
    
    public static Message logout(String username){
        return new Message(MessageLabel.LOGOUT, username);
    }

    public static Message success(){
        return new Message(MessageLabel.SUCCESS, "");
    }

    public static Message fail(String error){
        return new Message(MessageLabel.FAIL, error);
    }
    
    public static Message updateChatLog(ArrayList<String> chatLog){
        return new Message(MessageLabel.UPDATE_CHAT_LOG, chatLog);
    }
    
    public static Message updateTables(Hashmap<UUID, Table> tables){
        return new Message(MessageLabel.UPDATE_TABLES, tables);
    }
    
    public static Message updatePlayers(ArrayList<String> players){
        return new Message(MessageLabel.UPDATE_PLAYERS, players);
    }
    
    public static Message chatMessage(String message){
        return new Message(MessageLabel.CHAT_MESSAGE, message);
    }
    
    public static Message updateLobby(){
        return new Message(MessageLabel.UPDATE_LOBBY, "");
    }
    
    public static Message createTable(String username, Deck deck){
        Serializable[] data = new Serializable[2];
        data[0] = username;
        data[1] = deck;
        return new Message(MessageLabel.CREATE_TABLE, data);
    }
    
    public static Message joinTable(String username, Deck deck, UUID uuid){
        Serializable[] data = new Serializable[3];
        data[0] = username;
        data[1] = deck;
        data[2] = uuid;
        return new Message(MessageLabel.JOIN_TABLE, data);
    }
    
    public static Message newGame(ClientGame game){
        return new Message(MessageLabel.NEW_GAME, game);
    }
    
    public static Message play(UUID gameID, String username, UUID cardID, ArrayList<Serializable> targets){
        Serializable[] data = new Serializable[4];
        data[0] = gameID;
        data[1] = username;
        data[2] = cardID;
        data[3] = targets;
        return new Message(MessageLabel.PLAY, data);
    }
    
    public static Message activate(UUID gameID, String username, UUID cardID, ArrayList<Serializable> targets){
        Serializable[] data = new Serializable[4];
        data[0] = gameID;
        data[1] = username;
        data[2] = cardID;
        data[3] = targets;
        return new Message(MessageLabel.ACTIVATE, data);
    }
    
    public static Message registerGameConnection(UUID gameID, String username){
        Serializable[] data = new Serializable[2];
        data[0] = gameID;
        data[1] = username;
        return new Message(MessageLabel.REGISTER_GAME_CONNECTION, data);
    }
    
    public static Message registerInteractionConnection(UUID gameID, String username){
        Serializable[] data = new Serializable[2];
        data[0] = gameID;
        data[1] = username;
        return new Message(MessageLabel.REGISTER_INTERACTION_CONNECTION, data);
    }
    
    public static Message mulligan(UUID gameID, String username){
        Serializable[] data = new Serializable[2];
        data[0] = gameID;
        data[1] = username;
        return new Message(MessageLabel.MULLIGAN, data);
    }
    
    public static Message keep(UUID gameID, String username){
        Serializable[] data = new Serializable[2];
        data[0] = gameID;
        data[1] = username;
        return new Message(MessageLabel.KEEP, data);
    }
    
    public static Message updateGame(ClientGame game){
        return new Message(MessageLabel.UPDATE_GAME, game);
    }
    
    public static Message study(UUID gameID, String username, UUID cardID, Hashmap<Knowledge, Integer> knowledge){
        Serializable[] data = new Serializable[4];
        data[0] = gameID;
        data[1] = username;
        data[2] = cardID;
        data[3] = knowledge;
        return new Message(MessageLabel.STUDY, data);
    }

    
    public static Message pass(UUID gameID){
        return new Message(MessageLabel.PASS, gameID);
    }
    
    public static Message discard(ClientGame game, int count){
        Serializable[] data = new Serializable[2];
        data[0] = game;
        data[1] = count;
        return new Message(MessageLabel.DISCARD, data);
    }
    
    public static Message discardReturn(ArrayList<Serializable> cards){
        return new Message(MessageLabel.DISCARD_RETURN, cards);
    }
    
    public static Message concede(UUID gameID, String username){
        Serializable[] data = new Serializable[2];
        data[0] = gameID;
        data[1] = username;
        return new Message(MessageLabel.CONCEDE, data);
    }
    
    public static Message lose(){
        return new Message(MessageLabel.LOSE, "");
    }
    
    public static Message win(){
        return new Message(MessageLabel.WIN, "");
    }
    
    public static Message order(ArrayList<Card> cards) {
        return new Message(MessageLabel.ORDER, cards);
    }
     
    public static Message orderReturn(ArrayList<UUID> cards){
        return new Message(MessageLabel.ORDER_RETURN, cards);
    } 
}

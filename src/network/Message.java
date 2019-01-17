package network;




import card.Card;
import com.google.gson.Gson;
import enums.Knowledge;
import game.ClientGame;
import game.Deck;
import game.Table;
import helpers.Hashmap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import static network.MessageLabel.*;

/**
 *
 * @author pseudo
 */



public class Message implements Serializable {

    
    /**
     *
     * @param username
     * @return
     */
    public static Message register(String username){
        return new Message(REGISTER, username);
    }

    /**
     *
     * @param username
     * @return
     */
    public static Message login(String username){
        return new Message(LOGIN, username);
    }
    
    /**
     *
     * @param username
     * @return
     */
    public static Message logout(String username){
        return new Message(LOGOUT, username);
    }

    /**
     *
     * @return
     */
    public static Message success(){
        return new Message(SUCCESS, "");
    }

    /**
     *
     * @param error
     * @return
     */
    public static Message fail(String error){
        return new Message(FAIL, error);
    }
    
    /**
     *
     * @param chatLog
     * @return
     */
    public static Message updateChatLog(ArrayList<String> chatLog){
        return new Message(UPDATE_CHAT_LOG, chatLog);
    }
    
    /**
     *
     * @param tables
     * @return
     */
    public static Message updateTables(Hashmap<UUID, Table> tables){
        return new Message(UPDATE_TABLES, tables);
    }
    
    /**
     *
     * @param players
     * @return
     */
    public static Message updatePlayers(ArrayList<String> players){
        return new Message(UPDATE_PLAYERS, players);
    }
    
    /**
     *
     * @param message
     * @return
     */
    public static Message chatMessage(String message){
        return new Message(CHAT_MESSAGE, message);
    }
    
    /**
     *
     * @return
     */
    public static Message updateLobby(){
        return new Message(UPDATE_LOBBY, "");
    }
    
    /**
     *
     * @param username
     * @param deck
     * @return
     */
    public static Message createTable(String username, Deck deck){
        Serializable[] data = new Serializable[2];
        data[0] = username;
        data[1] = deck;
        return new Message(CREATE_TABLE, data);
    }
    
    /**
     *
     * @param username
     * @param deck
     * @param uuid
     * @return
     */
    public static Message joinTable(String username, Deck deck, UUID uuid){
        Serializable[] data = new Serializable[3];
        data[0] = username;
        data[1] = deck;
        data[2] = uuid;
        return new Message(JOIN_TABLE, data);
    }
    
    /**
     *
     * @param game
     * @return
     */
    public static Message newGame(ClientGame game){
        return new Message(NEW_GAME, game);
    }
    
    /**
     *
     * @param gameID
     * @param username
     * @param cardID
     * @param targets
     * @return
     */
    public static Message play(UUID gameID, String username, UUID cardID, ArrayList<Serializable> targets){
        Serializable[] data = new Serializable[4];
        data[0] = gameID;
        data[1] = username;
        data[2] = cardID;
        data[3] = targets;
        return new Message(PLAY, data);
    }
    
    /**
     *
     * @param gameID
     * @param username
     * @param cardID
     * @param targets
     * @return
     */
    public static Message activate(UUID gameID, String username, UUID cardID, ArrayList<Serializable> targets){
        Serializable[] data = new Serializable[4];
        data[0] = gameID;
        data[1] = username;
        data[2] = cardID;
        data[3] = targets;
        return new Message(ACTIVATE, data);
    }
    
    /**
     *
     * @param gameID
     * @param username
     * @return
     */
    public static Message registerGameConnection(UUID gameID, String username){
        Serializable[] data = new Serializable[2];
        data[0] = gameID;
        data[1] = username;
        return new Message(REGISTER_GAME_CONNECTION, data);
    }
    
    /**
     *
     * @param gameID
     * @param username
     * @return
     */
    public static Message registerInteractionConnection(UUID gameID, String username){
        Serializable[] data = new Serializable[2];
        data[0] = gameID;
        data[1] = username;
        return new Message(REGISTER_INTERACTION_CONNECTION, data);
    }
    
    /**
     *
     * @param gameID
     * @param username
     * @return
     */
    public static Message mulligan(UUID gameID, String username){
        Serializable[] data = new Serializable[2];
        data[0] = gameID;
        data[1] = username;
        return new Message(MULLIGAN, data);
    }
    
    /**
     *
     * @param gameID
     * @param username
     * @return
     */
    public static Message keep(UUID gameID, String username){
        Serializable[] data = new Serializable[2];
        data[0] = gameID;
        data[1] = username;
        return new Message(KEEP, data);
    }
    
    /**
     *
     * @param game
     * @return
     */
    public static Message updateGame(ClientGame game){
        return new Message(UPDATE_GAME, game);
    }
    
    /**
     *
     * @param gameID
     * @param username
     * @param cardID
     * @param knowledge
     * @return
     */
    public static Message study(UUID gameID, String username, UUID cardID, Hashmap<Knowledge, Integer> knowledge){
        Serializable[] data = new Serializable[4];
        data[0] = gameID;
        data[1] = username;
        data[2] = cardID;
        data[3] = knowledge;
        return new Message(STUDY, data);
    }

    /**
     *
     * @param gameID
     * @return
     */
    public static Message pass(UUID gameID){
        return new Message(PASS, gameID);
    }
    
    /**
     *
     * @param game
     * @param count
     * @return
     */
    public static Message selectFromHand(ClientGame game, int count){
        Serializable[] data = new Serializable[2];
        data[0] = game;
        data[1] = count;
        return new Message(HAND_SELECTION, data);
    }
    
    /**
     *
     * @param cards
     * @return
     */
    public static Message selectFromHandReturn(ArrayList<Serializable> cards){
        return new Message(HAND_SELECTION_RETURN, cards);
    }
    
    /**
     *
     * @param gameID
     * @param username
     * @return
     */
    public static Message concede(UUID gameID, String username){
        Serializable[] data = new Serializable[2];
        data[0] = gameID;
        data[1] = username;
        return new Message(CONCEDE, data);
    }
    
    /**
     *
     * @return
     */
    public static Message lose(){
        return new Message(LOSE, "");
    }
    
    /**
     *
     * @return
     */
    public static Message win(){
        return new Message(WIN, "");
    }
    
    /**
     *
     * @param cards
     * @return
     */
    public static Message order(ArrayList<Card> cards) {
        return new Message(ORDER, cards);
    }
     
    /**
     *
     * @param cards
     * @return
     */
    public static Message orderReturn(ArrayList<UUID> cards){
        return new Message(ORDER_RETURN, cards);
    } 

    public MessageLabel label;
    public Object object;
    
    private Message(MessageLabel label, Serializable object) {
        this.label = label;
        this.object = object;
    }
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return label + ": { " + object.toString() + " }";
    }
    
    public static String convertToJson(Message data)
    {
    	Gson gson = new Gson();
    	return gson.toJson(data);
    }
    
    public static Message convertToObject(String json)
    {
    	Gson gson = new Gson();
    	return gson.fromJson(json, Message.class);
    }
}

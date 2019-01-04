package card;

import client.Client;
import client.gui.components.CardPane;
import enums.Counter;
import enums.Knowledge;
import static enums.Knowledge.YELLOW;
import enums.Type;
import game.ClientGame;
import game.Game;
import game.Player;
import helpers.Hashmap;
import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.black;
import static java.awt.Color.green;
import static java.awt.Color.red;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import static javax.swing.BorderFactory.createCompoundBorder;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import network.Message;

/**
 *
 * @author pseudo
 */
public abstract class Card implements Serializable {

    /**
     * Ration of the displayed cards in the height/width format.
     */
    public static final double RATIO = 3.5 / 2.5;
    /**
     * Extracts the uuids of the given list of cards while preserving their order.
     * @param cards
     * @return
     */
    public static ArrayList<UUID> toUUIDList(ArrayList<Card> cards) {
        ArrayList<UUID> uuids = new ArrayList<>();
        while (!cards.isEmpty()){
            Card c = cards.remove(0);
            uuids.add(c.uuid);
        }
        return uuids;
    }
    /**
     * Sorts given list of cards in the order of the provided uuids.
     * If a card appears in the card list but not in uuid list, card is ignored.
     * If a uuid appears in uuid list but there is no card corresponding to it in the card list, uuid is ignored.
     * @param cards
     * @param uuids
     * @return
     */
    public static ArrayList<Card> sortByUUID(ArrayList<Card> cards, ArrayList<UUID> uuids) {
        ArrayList<Card> sorted = new ArrayList<>();
        while (!uuids.isEmpty()){
            UUID u = uuids.remove(0);
            for (Card c : cards){
                if (c.uuid.equals(u)){
                    sorted.add(c);
                    break;
                }
            }
        }
        return sorted;
    }

    // intrinsic variables

    /**
     * Unique identifier for the card.
     */
    public UUID uuid;

    /**
     * GUI element for displaying the card.
     */
    transient public CardPane panel;

    /**
     * Name of the card.
     */
    public String name;

    /**
     * Energy cost of the card.
     */
    public int cost;

    /**
     * Knowledge requirement of the card.
     */
    public Hashmap<Knowledge, Integer> knowledge;

    /**
     * Text of the card
     */
    public String text;

    /**
     * Illustration of the card.
     */
    public String image;

    /**
     * Type of the card.
     */
    public Type type;

    /**
     * Owner of the card. This is the player who started the game with the card in his deck.
     */
    public String owner;

    /**
     * Controller of the card. This is the player who currently have the card in play, hand, deck, void or scrapyard.
     */
    public String controller;

    /**
     * General purpose Data field for holding information like targets etc.
     */
    public ArrayList<Serializable> supplementaryData;

    /**
     * Flag to indicate if the card is depleted.
     */
    public boolean depleted;

    /**
     * Flag to indicate if the card is marked for any reason.
     */
    public boolean marked;

    /**
     * Collection of counters on the card.
     */
    public Hashmap<Counter, Integer> counters;

    /**
     * This is the default constructor for creating a card.
     * @param name
     * @param cost
     * @param knowledge
     * @param text
     * @param image
     * @param type
     * @param owner
     */
    public Card(String name, int cost, Hashmap<Knowledge, Integer> knowledge,
            String text, String image, Type type, String owner) {
        uuid = randomUUID();
        counters = new Hashmap<>();
        supplementaryData = new ArrayList<>();
        this.name = name;
        this.cost = cost;
        this.knowledge = knowledge;
        this.text = text;
        this.image = image;
        this.type = type;
        this.owner = owner;
        controller = owner;
        
        depleted = false;
        marked = false;
    }
    
    /**
     * Called by client to check if you can play this card in current game state.
     * @param game
     * @return
     */
    public abstract boolean canPlay(ClientGame game);
    
    /**
     * Called by client to check if you can study this card in current game state.
     * @param game
     * @return
     */
    public boolean canStudy (ClientGame game){
        return game.canPlaySource();
    }

    /**
     * Called by the client when you choose to play this card. 
     * For most cards, this just sends the play message to server.
     * For some cards, this may trigger actions like target selection before sending the message.
     * @param client
     */
    public void play(Client client) {
        client.gameConnection.send(Message.play(client.game.uuid, client.username, uuid, new ArrayList<>()));
    }
    
    /**
     * Called by the client when you choose to study this card.
     * It also handles knowledge selection if card is multifaction.
     * @param client
     */
    public void study(Client client) {
        Hashmap<Knowledge, Integer> knl = new Hashmap<>();
        if (knowledge.isEmpty()) {
            client.study(this, knl);
        } else if (knowledge.size() == 1) {
            knl.put(knowledge.keySet().iterator().next(), 1);
            client.study(this, knl);
        } else {
            client.gameArea.displayKnowledgeMenu(this);
        }
    }
    
    /**
     * Called by server when this card is played.
     * Default behavior is that it deducts the energy cost of the card, 
     * removes it from player's hand and then resolves the card.
     * @param game
     */
    public void play(Game game) {
        Player player = game.players.get(owner);
        player.hand.remove(this);
        player.energy -= cost;
        resolve(game);
    }

    /**
     * Called by the server when you choose to study this card.
     * It increases player's maximum energy and adds selected knowledge.
     * @param game
     * @param knowledge
     */
    public void study(Game game, Hashmap<Knowledge, Integer> knowledge) {
        Player player = game.players.get(owner);
        player.hand.remove(this);
        player.sources.add(this);
        player.energy++;
        player.addKnowledge(knowledge);
        player.playableSource--;
    }

    /**
     * This is the function that describes what is the effect of the card when it is played.
     * This function contains the business logic of the card effect.
     * @param game
     */
    public abstract void resolve(Game game);
    
    /**
     * Function that adds counters to the card.
     * @param name
     * @param count
     */
    public void addCounters(Counter name, int count) {
        counters.merge(name, count, (a, b) -> a + b);
    }

    /**
     * Function that clears status flags and supplementary data of the card.
     * 
     */
    public void clear() {
        depleted = false;
        marked = false;
        supplementaryData = new ArrayList<>();
    }
    
    
    /**
     *
     * @return
     */
    @Override
    public String toString(){
        return name;
    }
    
    protected void drawBorders() {
        getPanel().setBorder(createCompoundBorder(new LineBorder(marked ? green : (depleted ? red : black), 2),
                marked ? new LineBorder(depleted ? red : black, 2) : null));
    }

    protected void drawCounters() {
        counters.forEach((name, count) -> getPanel().add(new JLabel(name + ": " + count)));
    }

    protected String getKnowledgeString() {
        ArrayList<Character> knowledgeChars = new ArrayList<>();
        knowledge.forEach((knowl, count) -> {
            switch (knowl) {
                case BLACK:
                    for (int i = 0; i < count; i++) {
                        knowledgeChars.add('B');
                    }
                    break;
                case GREEN:
                    for (int i = 0; i < count; i++) {
                        knowledgeChars.add('G');
                    }
                    break;
                case RED:
                    for (int i = 0; i < count; i++) {
                        knowledgeChars.add('R');
                    }
                    break;
                case BLUE:
                    for (int i = 0; i < count; i++) {
                        knowledgeChars.add('U');
                    }
                    break;
                case WHITE:
                    for (int i = 0; i < count; i++) {
                        knowledgeChars.add('W');
                    }
                    break;
            }
        });
        StringBuilder knowledgeString = new StringBuilder();

        knowledgeChars.forEach((c) -> {
            knowledgeString.append(c.toString());
        });
        return knowledgeString.toString();
    }

    protected Color getColor() {
        if (knowledge.isEmpty()) {
            return LIGHT_GRAY;
        } else if (knowledge.size() > 1) {
            return new Color(YELLOW.getValue());
        } else {
            for (Knowledge knowl : knowledge.keySet()) {
                return new Color(knowl.getValue());
            }
        }
        return BLACK;
    }
    
    protected void setToolTip() {
        String tooltip = "<html>" + cost + " " + getKnowledgeString()+"<br>";
        tooltip += name + "<br><br>";
        tooltip += type.toString() + "<br><br>";
        tooltip += text + "<br><br>";
        if (!counters.isEmpty()){
            tooltip += "<br><br>--- COUNTERS ---<br>";
            tooltip += counters.toString();
        }
        tooltip += "</html>";
        getPanel().setToolTipText(tooltip);
    }
    
    /**
     *
     */
    abstract public void updatePanel();

    /**
     * Get panel to display the card with dimensions 250 x 350. 
     * Always use one of the getPanel() functions to ensure a non-null panel is returned.
     * @return Swing panel that displays the card.
     */
    public CardPane getPanel() {
        if (panel == null) {
            panel = new CardPane();
            updatePanel();
        }
        panel.setBounds(0, 0, 250, 350);
        return panel;
    }
    
    /**
     * Get panel to display the card with dimensions width x (RATIO * width). 
     * Always use one of the getPanel() functions to ensure a non-null panel is returned.
     * @param width
     * @return Swing panel that displays the card.
     */
    public CardPane getPanel(int width) {
        if (panel == null) {
            panel = new CardPane();
            updatePanel();
        }
        panel.setBounds(0, 0, width, (int)(RATIO * width));
        return panel;
    }

    /**
     *
     * @param panel
     */
    public void setPanel(CardPane panel) {
        this.panel = panel;
    }
}

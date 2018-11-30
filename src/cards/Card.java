package cards;

import client.Client;
import client.gui.MemoryPanel;
import enums.Counter;
import enums.Type;
import enums.Knowledge;
import game.ClientGame;
import game.Game;
import game.Player;
import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import network.Message;

public abstract class Card implements Serializable {

    static final double RATIO = 3.5 / 2.5;

    

    // intrinsic variables
    public UUID uuid;
    transient public MemoryPanel panel;
    public String name;
    public int cost;
    public HashMap<Knowledge, Integer> knowledge;
    public String text;
    public String image;
    public Type type;
    public String owner;
    public int[] values;

    public ArrayList<Serializable> supplimentaryData;

    public boolean activation;
    public Card creator;

    public boolean depleted;
    public boolean marked;
    public HashMap<Counter, Integer> counters;

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public Card(String name, int cost, HashMap<Knowledge, Integer> knowledge,
            String text, String image, Type type, String owner) {
        uuid = UUID.randomUUID();
        panel = new MemoryPanel();
        counters = new HashMap<>();
        supplimentaryData = new ArrayList<>();
        this.name = name;
        this.cost = cost;
        this.knowledge = knowledge;
        this.text = text;
        this.image = image;
        this.type = type;
        this.owner = owner;

        activation = false;
        depleted = false;
        marked = false;
        values = new int[0];
    }

    public Card(Card c) {
        uuid = UUID.randomUUID();
        panel = new MemoryPanel();
        name = c.name;
        cost = c.cost;
        knowledge = c.knowledge;
        text = c.text;
        image = c.image;
        type = c.type;
        owner = c.owner;
        creator = c.creator;

        values = new int[c.values.length];
        System.arraycopy(c.values, 0, values, 0, values.length);

        counters = new HashMap<>();
        if (c.counters != null) {
            c.counters.forEach((name, value) -> counters.put(name, value));
        }

        supplimentaryData = new ArrayList<>();
        if (c.supplimentaryData != null) {
            supplimentaryData.addAll(c.supplimentaryData);
        }

        activation = c.activation;
        depleted = c.depleted;
        marked = c.marked;
    }

    public Card(Card c, String text) {
        uuid = UUID.randomUUID();
        panel = new MemoryPanel();
        counters = new HashMap<>();
        supplimentaryData = new ArrayList<>();
        knowledge = new HashMap<>();
        
        values = new int[c.values.length];
        if (values.length >= 0) System.arraycopy(c.values, 0, values, 0, values.length);
        
        if (c.supplimentaryData != null) {
            supplimentaryData.addAll(c.supplimentaryData);
        }
        this.name = c.name;
        this.cost = 0;
        this.text = text;
        this.image = c.image;
        this.type = c.type;
        this.owner = c.owner;
        this.values = c.values;

        activation = true;
        this.depleted = false;
        this.marked = false;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Client Functions">
    // Called to check if you can play this card
    public abstract boolean canPlay(ClientGame game);

    // Called by the client when you choose to play this card
    public void play(Client client) {
        client.gameConnection.send(Message.play(client.game.uuid, client.username, uuid, new ArrayList<>()));
    }
    
    public void play(Client client, ArrayList<Serializable> targets) {
        client.gameConnection.send(Message.play(client.game.uuid, client.username, uuid, targets));
    }
    
    public void playAsSource(Client client) {
        HashMap<Knowledge, Integer> knl = new HashMap<>();
        if (knowledge.isEmpty()) {
            client.playSource(this, knl);
        } else if (knowledge.size() == 1) {
            knl.put(knowledge.keySet().iterator().next(), 1);
            client.playSource(this, knl);
        } else {
            client.gameArea.displayKnowledgeMenu(this);
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Server Functions">
    // Called by the server when you play this card
    public void play(Game game) {
        Player player = game.players.get(owner);
        player.hand.remove(this);
        player.energy -= cost;
        game.addToStack(this);
        game.activePlayer = game.getOpponentName(game.activePlayer);
    }

    // Called by the server when you choose to play this card as a source
    public void playAsSource(Game game, HashMap<Knowledge, Integer> knowledge) {
        Player player = game.players.get(owner);
        player.hand.remove(this);
        player.sources.add(this);
        player.energy++;
        player.addKnowledge(knowledge);
        player.playableSource--;
    }

    // Called by server when it is resolved from the stack
    public abstract void resolve(Game game);
    //</editor-fold>
    

    public void addCounters(Counter name, int count) {
        counters.merge(name, count, (a, b) -> a + b);
    }

    public void clear() {
        depleted = false;
        marked = false;
        supplimentaryData = new ArrayList<>();
    }
    
    public static ArrayList<UUID> toUUIDList(ArrayList<Card> cards) {
        ArrayList<UUID> uuids = new ArrayList<>();
        while (!cards.isEmpty()){
            Card c = cards.remove(0);
            uuids.add(c.uuid);
        }
        return uuids;
    }
    
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

// <editor-fold defaultstate="collapsed" desc="Displayers">        
    void drawBorders() {
        getPanel().setBorder(BorderFactory.createCompoundBorder(new LineBorder(marked ? Color.green : (depleted ? Color.red : Color.yellow), 5),
                marked ? new LineBorder(depleted ? Color.red : Color.yellow, 5) : null));
    }

    void drawCounters() {
        counters.forEach((name, count) -> getPanel().add(new JLabel(name + ": " + count)));
    }

    String getKnowledgeString() {
        ArrayList<Character> knowledgeChars = new ArrayList<>();
        knowledge.forEach((knowl, count) -> {
            switch (knowl) {
                case BLACK:
                    for (int i = 0; i < count; i++) {
                        knowledgeChars.add('B');
                    }
                    break;
                case BLUE:
                    for (int i = 0; i < count; i++) {
                        knowledgeChars.add('U');
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
                case WHITE:
                    for (int i = 0; i < count; i++) {
                        knowledgeChars.add('W');
                    }
                    break;
            }
        });
        StringBuilder knowledgeString = new StringBuilder();

        for (Character c : knowledgeChars) {
            knowledgeString.append(c.toString());
        }
        return knowledgeString.toString();
    }

    Color getColor() {
        if (knowledge.isEmpty()) {
            return Color.LIGHT_GRAY;
        } else if (knowledge.size() > 1) {
            return new Color(Knowledge.YELLOW.getValue());
        } else {
            for (Knowledge knowl : knowledge.keySet()) {
                return new Color(knowl.getValue());
            }
        }
        return Color.BLACK;
    }
    
    void setToolTipHelper(JComponent c, String text) {
        c.setToolTipText(text);
        for (Component cc : c.getComponents())
            if (cc instanceof JComponent)
                setToolTipHelper((JComponent) cc, text);
    }
    
    void setToolTip() {
        String tooltip = "<html>" + cost + " " + getKnowledgeString()+"<br>";
        tooltip += name + (activation ? "'s Activation" : "")+"<br><br>";
        tooltip += type.toString() + "<br><br>";
        tooltip += String.format(text, (Object[]) Arrays.stream(values).
                        mapToObj(String::valueOf).toArray(String[]::new));
        if (!counters.isEmpty()){
            tooltip += "<br><br>--- COUNTERS ---<br>";
            tooltip += counters.toString();
        }
        tooltip += "</html>";
        setToolTipHelper(getPanel(), tooltip);
    }
    
    abstract public void updatePanel();
    //</editor-fold>

    public MemoryPanel getPanel() {
        if (panel == null) {
            panel = new MemoryPanel();
            updatePanel();
        }
        return panel;
    }

    public void setPanel(MemoryPanel panel) {
        this.panel = panel;
    }
}

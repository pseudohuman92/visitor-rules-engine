/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import helpers.Debug;
import cards.Card;
import java.awt.Component;
import java.util.ArrayList;
import client.Client;
import enums.Knowledge;
import cards.Item;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author pseudo
 */
public class GameArea extends JPanel {

    Client client;
    ArrayList<Serializable> selected;
    boolean skipPlayerBegin;
    boolean skipOpponentBegin;
    boolean skipPlayerMain;
    boolean skipOpponentMain;
    boolean skipPlayerEnd;
    boolean skipOpponentEnd;

    public GameArea(Client client) {
        this.client = client;
        selected = new ArrayList<>();
        initComponents();

        xValueSelector.setVisible(false);
        skipPlayerBegin = true;
        skipOpponentBegin = true;
        skipPlayerMain = false;
        skipOpponentMain = true;
        skipPlayerEnd = true;
        skipOpponentEnd = false;
        update();
    }

    // <editor-fold defaultstate="collapsed" desc="Displayers">
    public final void update() {
        Debug.println("Updating Display");
        setPlayerStats();
        setOpponentStats();
        setPhase();
        setTurn();
        switch (client.game.phase) {
            case BEGIN:
            case MAIN:
            case END:
                addPlayListeners();
                break;
        }
        displayHand();
        displayStack();
        displayGameText();
        switch (client.game.phase) {
            case BEGIN:
            case MAIN:
            case END:
                addActivateListeners();
                break;
        }
        displayPlayerItems();
        displayOpponentItems();
        validate();
        checkAutopass();
    }

    void setPlayerStats() {
        playerNameLabel.setText(client.game.player.name);
        playerLifeLabel.setText("Life: " + client.game.player.life);
        playerEnergyLabel.setText("Energy: " + client.game.player.energy + " / " + client.game.player.sources.size());
        playerDeckLabel.setText("Deck: " + client.game.player.deck.size());
        playerHandLabel.setText("Hand: " + client.game.player.hand.size());
        playerDiscardLabel.setText("Discard: " + client.game.player.discardPile.size());
        playerKnowledgeLabel.setText("");
        client.game.player.knowledge.forEach((knowledge, count) -> playerKnowledgeLabel.setText(playerKnowledgeLabel.getText() + knowledge.toShortString() + ": " + count + " "));
    }

    void setOpponentStats() {
        opponentNameLabel.setText(client.game.opponent.name);
        opponentLifeLabel.setText("Life: " + client.game.opponent.life);
        opponentEnergyLabel.setText("Energy: " + client.game.opponent.energy + " / " + client.game.opponent.sources.size());
        opponentDeckLabel.setText("Deck: " + client.game.opponent.deckSize);
        opponentHandLabel.setText("Hand: " + client.game.opponent.handSize);
        opponentDiscardLabel.setText("Discard: " + client.game.opponent.discardPile.size());
        opponentKnowledgeLabel.setText("");
        client.game.opponent.knowledge.forEach((knowledge, count) -> opponentKnowledgeLabel.setText(opponentKnowledgeLabel.getText() + knowledge.toShortString() + ": " + count + " "));
    }

    void setPhase() {
        switch (client.game.phase) {
            case BEGIN:
            case BEGIN_RESOLVING:
                beginPhaseLabel.setForeground(Color.RED);
                mainPhaseLabel.setForeground(Color.BLACK);
                endPhaseLabel.setForeground(Color.BLACK);
                break;
            case MAIN:
            case MAIN_RESOLVING:
                beginPhaseLabel.setForeground(Color.BLACK);
                mainPhaseLabel.setForeground(Color.RED);
                endPhaseLabel.setForeground(Color.BLACK);
                break;
            case END:
            case END_RESOLVING:
                beginPhaseLabel.setForeground(Color.BLACK);
                mainPhaseLabel.setForeground(Color.BLACK);
                endPhaseLabel.setForeground(Color.RED);
                break;
        }
    }

    void setTurn() {
        currentTurnLabel.setText(client.game.turnPlayer + "'s Turn");
    }

    void updateHand() {
        client.game.player.hand.forEach(Card::updatePanel);
        handPane.revalidate();
        handPane.repaint();
    }

    void updatePlayerItems() {
        client.game.player.items.forEach(Card::updatePanel);
        playerItemPanel.validate();
        playerItemPanel.repaint();
    }

    void updateOpponentItems() {
        client.game.opponent.items.forEach(Card::updatePanel);
        opponentItemPanel.validate();
        opponentItemPanel.repaint();
    }

    void updateAllItems() {
        updatePlayerItems();
        updateOpponentItems();
    }

    void displayPlayerItems() {
        Debug.println("Displaying Player Items: " + client.game.player.items);
        playerItemPanel.removeAll();
        client.game.player.items.forEach((card) -> {
            card.updatePanel();
            playerItemPanel.add(card.getPanel());
        });
        playerItemPanel.validate();
        playerItemPanel.repaint();
    }

    void displayOpponentItems() {
        Debug.println("Displaying Opponent Items: " + client.game.opponent.items);
        opponentItemPanel.removeAll();
        client.game.opponent.items.forEach((card) -> {
            card.updatePanel();
            opponentItemPanel.add(card.getPanel());
        });
        opponentItemPanel.validate();
        opponentItemPanel.repaint();
    }

    void displayHand() {
        Debug.println("Displaying Hand");
        ArrayList<MemoryPanel> cards = new ArrayList<>();
        client.game.player.hand.forEach((card) -> {
            card.updatePanel();
            cards.add(card.getPanel());
        });
        handPane.layAll(cards);
    }

    void displayStack() {
        ArrayList<MemoryPanel> cards = new ArrayList<>();
        for (Card card : client.game.stack) {
            card.updatePanel();
            cards.add(card.getPanel());
        }
        stackPane.layAll(cards);
    }

    void displayGameText() {
        switch (client.game.phase) {
            case MULLIGAN:
                displayMulliganText();
                break;
            case BEGIN:
            case MAIN:
            case END:
                displayPlayText();
                break;
            case BEGIN_RESOLVING:
            case MAIN_RESOLVING:
            case END_RESOLVING:
                displayResolvingText();
                break;
        }

    }

    void displayMulliganText() {
        if (client.game.hasInitiative()) {
            gameTextLabel.setText("Do you want to mulligan?");

            gameTextButtonLeft.setText("Mulligan");
            removeActionListeners(gameTextButtonLeft);
            gameTextButtonLeft.addActionListener((action) -> {
                gameTextButtonLeft.setEnabled(false);
                gameTextButtonRight.setEnabled(false);
                client.mulligan();
            });
            gameTextButtonLeft.setEnabled(true);
            gameTextButtonLeft.setVisible(true);

            gameTextButtonRight.setText("Keep");
            removeActionListeners(gameTextButtonRight);
            gameTextButtonRight.addActionListener((action) -> {
                gameTextButtonLeft.setEnabled(false);
                gameTextButtonRight.setEnabled(false);
                client.keep();
            });
            gameTextButtonRight.setEnabled(true);
            gameTextButtonRight.setVisible(true);
        } else {
            gameTextLabel.setText("Waiting for opponents mulligan.");
            gameTextButtonLeft.setVisible(false);
            gameTextButtonRight.setVisible(false);
        }
    }

    void displayPlayText() {
        if (client.game.hasInitiative()) {
            gameTextLabel.setText("Pass Initiative?");
            gameTextButtonRight.setVisible(false);

            gameTextButtonLeft.setText("Pass");
            removeActionListeners(gameTextButtonLeft);
            gameTextButtonLeft.addActionListener((action) -> {
                gameTextButtonLeft.setEnabled(false);
                client.skipInitiative();
            });
            gameTextButtonLeft.setEnabled(true);
            gameTextButtonLeft.setVisible(true);
        } else {
            gameTextLabel.setText("Waiting for opponent.");
            gameTextButtonLeft.setVisible(false);
            gameTextButtonRight.setVisible(false);
        }
    }

    void displayResolvingText() {
        gameTextLabel.setText("Stack is Resolving.");
        gameTextButtonLeft.setVisible(false);
        gameTextButtonRight.setVisible(false);
    }

    void displayTargetingText(BiConsumer<Client, ArrayList<Serializable>> func, int count) {
        gameTextLabel.setText("Choose up to " + count + " targets.");

        gameTextButtonLeft.setText("Cancel");
        removeActionListeners(gameTextButtonLeft);
        gameTextButtonLeft.addActionListener((action) -> {
            gameTextButtonLeft.setEnabled(false);
            gameTextButtonRight.setEnabled(false);
            removeAllItemListeners();
            clearItemMarks();
            selected = new ArrayList<>();
            update();
        });
        gameTextButtonLeft.setEnabled(true);
        gameTextButtonLeft.setVisible(true);

        gameTextButtonRight.setText("Done");
        removeActionListeners(gameTextButtonRight);
        gameTextButtonRight.addActionListener((action) -> {
            gameTextButtonLeft.setEnabled(false);
            gameTextButtonRight.setEnabled(false);
            removeAllItemListeners();
            clearItemMarks();
            func.accept(client, selected);
            selected = new ArrayList<>();
        });
        gameTextButtonRight.setEnabled(true);
        gameTextButtonRight.setVisible(true);
    }
    // </editor-fold>

    void checkAutopass() {
        if (client.username.equals(client.game.activePlayer) && client.game.stack.isEmpty()) {
            switch (client.game.phase) {
                case BEGIN:
                    if ((skipPlayerBegin && client.username.equals(client.game.turnPlayer))
                            || (skipOpponentBegin && !client.username.equals(client.game.turnPlayer))) {
                        client.skipInitiative();
                    }
                    break;
                case MAIN:
                    if ((skipPlayerMain && client.username.equals(client.game.turnPlayer))
                            || (skipOpponentMain && !client.username.equals(client.game.turnPlayer))) {
                        client.skipInitiative();
                    }
                    break;
                case END:
                    if ((skipPlayerEnd && client.username.equals(client.game.turnPlayer))
                            || (skipOpponentEnd && !client.username.equals(client.game.turnPlayer))) {
                        client.skipInitiative();
                    }
                    break;
            }
        }
    }

    public void discardCards(int count) {
        Debug.println("Discarding " + count);
        gameTextLabel.setText("Discard " + count + " cards.");
        gameTextButtonLeft.setVisible(false);
        gameTextButtonRight.setVisible(false);
        removeHandListeners();
        addDiscardListeners(count);
        displayHand();
    }

    public void displayKnowledgeMenu(Card card) {
        removeHandListeners();
        removeAllItemListeners();
        gameTextButtonLeft.setVisible(false);
        gameTextButtonRight.setVisible(false);
        gameTextLabel.setText("Choose knowledge to get");
        knowledgeMenu(card);
    }

    void clearItemMarks() {
        client.game.player.items.forEach((card) -> card.marked = false);
        client.game.opponent.items.forEach((card) -> card.marked = false);
    }
    
    public void getXValue(BiConsumer<Client, Integer> continuation, BiFunction <Client, Integer, Boolean> condition){
        xValueSelector.setVisible(true);
        xValueSelector.setValue(0);
        
        gameTextLabel.setText("Choose value for X");
        gameTextButtonRight.setVisible(false);

        gameTextButtonLeft.setText("Done");
        removeActionListeners(gameTextButtonLeft);
        gameTextButtonLeft.addActionListener((action) -> {
            if (condition.apply(client, (Integer) xValueSelector.getValue())){
                gameTextButtonLeft.setEnabled(false);
                gameTextButtonRight.setEnabled(false);
                xValueSelector.setVisible(false);
                continuation.accept(client, (Integer) xValueSelector.getValue());
            }
        });
        gameTextButtonLeft.setEnabled(true);
        gameTextButtonLeft.setVisible(true);
    }

    // <editor-fold defaultstate="collapsed" desc="Listeners">
    // <editor-fold defaultstate="collapsed" desc="Adders">
    void addActivateListeners() {
        removePlayerItemListeners();
        Debug.println("Adding activate listeners");
        client.game.player.items.forEach((card) -> card.getPanel().addMouseListener(activateAdapter(card)));
    }

    void addPlayListeners() {
        removeHandListeners();
        Debug.println("Adding play listeners");
        client.game.player.hand.forEach((card) -> card.getPanel().addMouseListener(playAdapter(card)));
    }

    void addDiscardListeners(int count) {
        Debug.println("Adding discard listeners");
        client.game.player.hand.forEach((card) -> card.getPanel().addMouseListener(discardAdapter(card, count)));
    }
    
    public void addFilteredPlayerTargetListeners(BiConsumer<Client, ArrayList<Serializable>> continuation, Function<Item, Boolean> filter, int count) {
        removePlayerItemListeners();
        Debug.println("Adding player target listeners");
        client.game.player.items.forEach((card) -> {
            if (filter.apply(card))
                card.getPanel().addMouseListener(targetAdapter(continuation, card, count));
        });
        displayTargetingText(continuation, count);
    }

    public void addPlayerTargetListeners(BiConsumer<Client, ArrayList<Serializable>> continuation, int count) {
        addFilteredPlayerTargetListeners(continuation, c-> true, count);
    }
    
    public void addFilteredOpponentTargetListeners(BiConsumer<Client, ArrayList<Serializable>> continuation, Function<Item, Boolean> filter, int count) {
        removeOpponentItemListeners();
        client.game.opponent.items.forEach((card) -> {
            if (filter.apply(card))
                card.getPanel().addMouseListener(targetAdapter(continuation, card, count));
        });
        displayTargetingText(continuation, count);
    }

    public void addOpponentTargetListeners(BiConsumer<Client, ArrayList<Serializable>> continuation, int count) {
        addFilteredOpponentTargetListeners(continuation, c-> true, count);
    }
    
    public void addFilteredTargetListeners(BiConsumer<Client, ArrayList<Serializable>> continuation, Function<Item, Boolean> filter, int count) {
        addFilteredPlayerTargetListeners(continuation, filter, count);
        addFilteredOpponentTargetListeners(continuation, filter, count);
    }

    public void addTargetListeners(BiConsumer<Client, ArrayList<Serializable>> continuation, int count) {
        addFilteredTargetListeners(continuation, c-> true, count);
    }

    public void addPlayerSelector(BiConsumer<Client, ArrayList<Serializable>> continuation) {
        gameTextLabel.setText("Select a player");

        gameTextButtonLeft.setText(client.game.player.name);
        removeActionListeners(gameTextButtonLeft);
        gameTextButtonLeft.addActionListener((action) -> {
            gameTextButtonLeft.setEnabled(false);
            gameTextButtonRight.setEnabled(false);
            selected.add(client.game.player.uuid);
            continuation.accept(client, selected);
            selected = new ArrayList<>();
        });
        gameTextButtonLeft.setEnabled(true);
        gameTextButtonLeft.setVisible(true);

        gameTextButtonRight.setText(client.game.opponent.name);
        removeActionListeners(gameTextButtonRight);
        gameTextButtonRight.addActionListener((action) -> {
            gameTextButtonLeft.setEnabled(false);
            gameTextButtonRight.setEnabled(false);
            selected.add(client.game.opponent.uuid);
            continuation.accept(client, selected);
            selected = new ArrayList<>();
        });
        gameTextButtonRight.setEnabled(true);
        gameTextButtonRight.setVisible(true);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Removers">
    void removeActionListeners(JButton b) {
        for (ActionListener l : b.getActionListeners()) {
            b.removeActionListener(l);
        }
    }

    void removeHandListeners() {
        Debug.println("Removing hand listeners");
        client.game.player.hand.forEach((card) -> {
            MouseListener[] listeners = card.getPanel().getMouseListeners();
            for (MouseListener l : listeners) {
                card.getPanel().removeMouseListener(l);
            }
        });
    }

    void removePlayerItemListeners() {
        Debug.println("Removing Item listeners");
        client.game.player.items.forEach((card) -> {
            MouseListener[] listeners = card.getPanel().getMouseListeners();
            for (MouseListener l : listeners) {
                card.getPanel().removeMouseListener(l);
            }
        });
    }

    void removeOpponentItemListeners() {
        Debug.println("Removing Item listeners");
        client.game.opponent.items.forEach((card) -> {
            MouseListener[] listeners = card.getPanel().getMouseListeners();
            for (MouseListener l : listeners) {
                card.getPanel().removeMouseListener(l);
            }
        });
    }

    void removeAllItemListeners() {
        removePlayerItemListeners();
        removeOpponentItemListeners();
    }
    //</editor-fold>
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Menus">
    void knowledgeMenu(Card card) {
        JPopupMenu menu = new JPopupMenu();
        for (Knowledge knowl : card.knowledge.keySet()) {
            JMenuItem menuItem = new JMenuItem(knowl.toString());
            menu.add(menuItem);
            menuItem.addActionListener((ActionEvent event) -> {
                Debug.println("Playing as a source: " + card.name);
                HashMap<Knowledge, Integer> knl = new HashMap<>();
                knl.put(knowl, 1);
                client.playSource(card, knl);
            });
        }
        menu.setVisible(true);
        menu.show((Component) card.getPanel(), card.getPanel().getX(), card.getPanel().getY());
    }

    void sourceMenu(MouseEvent evt, Card card) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Play as a source");
        menu.add(menuItem);
        menuItem.addActionListener((ActionEvent event) -> {
            Debug.println("Playing as a source: " + card.name);
            card.playAsSource(client);
        });
        menu.setVisible(true);
        menu.show((Component) evt.getSource(), evt.getX(), evt.getY());
    }

    JPopupMenu concedeMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Concede");
        menu.add(menuItem);
        menuItem.addActionListener((ActionEvent event) -> client.concede());
        menu.setVisible(true);
        return menu;
    }
//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Adapters">
    MouseAdapter playAdapter(Card card) {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (client.game.hasInitiative()) {
                    if (evt.getButton() == MouseEvent.BUTTON1) {
                        if (evt.isControlDown()) {
                            if (client.game.canPlaySource()) {
                                Debug.println("Playing as a source: " + card.name);
                                card.playAsSource(client);
                            }
                        } else if (card.canPlay(client.game)) {
                            card.play(client);
                        }
                    } else if (evt.getButton() == MouseEvent.BUTTON3 && client.game.canPlaySource()) {
                        sourceMenu(evt, card);
                    }
                }
            }
        };
    }

    MouseAdapter discardAdapter(Card card, int count) {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    if (!card.marked) {
                        Debug.println("Selected for discard: " + card.name);
                        card.marked = true;
                        selected.add(card.uuid);
                        if (selected.size() == count || selected.size() == client.game.player.hand.size()) {
                            Debug.println("All cards are selected for discard");
                            removeHandListeners();
                            client.discardReturn(selected);
                            selected = new ArrayList<>();
                        }
                        updateHand();
                    } else {
                        Debug.println("Unselected for discard: " + card.name);
                        card.marked = false;
                        selected.remove(card.uuid);
                        updateHand();
                    }
                }
            }
        };
    }

    MouseAdapter activateAdapter(Item card) {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (client.game.hasInitiative() && card.canActivate(client.game) && evt.getButton() == MouseEvent.BUTTON1) {
                    Debug.println("Activating: " + card.name);
                    card.activate(client);
                }
            }
        };
    }

    MouseAdapter targetAdapter(BiConsumer<Client, ArrayList<Serializable>> continuation, Card targetCard, int count) {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    if (!targetCard.marked) {
                        Debug.println("Selected for target: " + targetCard.name);
                        targetCard.marked = true;
                        selected.add(targetCard.uuid);
                        if (selected.size() == count) {
                            Debug.println("All cards are selected for target");
                            removeAllItemListeners();
                            clearItemMarks();
                            continuation.accept(client, selected);
                            selected = new ArrayList<>();
                        }
                        updateAllItems();
                    } else {
                        Debug.println("Unselected for target: " + targetCard.name);
                        targetCard.marked = false;
                        selected.remove(targetCard.uuid);
                        updateAllItems();
                    }
                }
            }
        };
    }
//</editor-fold>
                  
    private void initComponents() {
        
        leftColumnPanel = new JPanel();
        opponentPanel = new JPanel();
        opponentNameLabel = new JLabel();
        opponentLifeLabel = new JLabel();
        opponentDeckLabel = new JLabel();
        opponentHandLabel = new JLabel();
        opponentDiscardLabel = new JLabel();
        opponentEnergyLabel = new JLabel();
        opponentKnowledgeLabel = new JLabel();
        stackPane = new StackPane();
        gameTextPanel = new JPanel();
        gameTextLabel = new JLabel();
        gameTextButtonRight = new JButton();
        gameTextButtonLeft = new JButton();
        xValueSelector = new JSpinner();
        playerPanel = new JPanel();
        playerNameLabel = new JLabel();
        playerLifeLabel = new JLabel();
        playerDeckLabel = new JLabel();
        playerHandLabel = new JLabel();
        playerDiscardLabel = new JLabel();
        playerEnergyLabel = new JLabel();
        playerKnowledgeLabel = new JLabel();
        handPane = new HandPane();
        opponentItemPane = new JScrollPane();
        opponentItemPanel = new JPanel();
        playerItemPane = new JScrollPane();
        playerItemPanel = new JPanel();
        currentTurnLabel = new JLabel();
        phaseDisplayPanel = new JPanel();
        opponentBeginBox = new JCheckBox();
        opponentMainBox = new JCheckBox();
        opponentEndBox = new JCheckBox();
        beginPhaseLabel = new JLabel();
        mainPhaseLabel = new JLabel();
        endPhaseLabel = new JLabel();
        playerBeginBox = new JCheckBox();
        playerMainBox = new JCheckBox();
        playerEndBox = new JCheckBox();

        

        opponentPanel.setBackground(new java.awt.Color(255, 176, 180));
        opponentNameLabel.setText("opponentName");
        opponentLifeLabel.setText("opponentLife");
        opponentDeckLabel.setText("opponentDeck");
        opponentHandLabel.setText("opponentHand");
        opponentDiscardLabel.setText("opponentDiscard");
        opponentEnergyLabel.setText("opponentEnergy");
        opponentKnowledgeLabel.setText("opponentKnowledge");
        
        opponentPanel.setLayout(new MigLayout("wrap 1"));
        opponentPanel.add(opponentNameLabel);
        opponentPanel.add(opponentLifeLabel);
        opponentPanel.add(opponentEnergyLabel);
        opponentPanel.add(opponentHandLabel);
        opponentPanel.add(opponentDiscardLabel);
        opponentPanel.add(opponentDeckLabel);
        opponentPanel.add(opponentKnowledgeLabel);

        stackPane.setLayout(new MigLayout("wrap 1"));       

        gameTextPanel.setBackground(new java.awt.Color(230, 228, 140));
        gameTextPanel.setBorder(BorderFactory.createCompoundBorder());

        gameTextLabel.setText("gameTextArea");
        gameTextButtonRight.setText("rightButton");
        gameTextButtonLeft.setText("leftButton");

        xValueSelector.setModel(new SpinnerNumberModel(0, 0, null, 1));

        gameTextPanel.setLayout(new MigLayout("wrap 4", "[grow][button][button][button]"));
        gameTextPanel.add(gameTextLabel, "span 4, grow");
        gameTextPanel.add(xValueSelector, "skip 1");
        gameTextPanel.add(gameTextButtonLeft);
        gameTextPanel.add(gameTextButtonRight);
        
        playerPanel.setBackground(new java.awt.Color(155, 219, 239));
        playerNameLabel.setText("playerName");
        playerLifeLabel.setText("playerLife");
        playerDeckLabel.setText("playerDeck");
        playerHandLabel.setText("playerHand");
        playerDiscardLabel.setText("playerDiscard");
        playerEnergyLabel.setText("playerEnergy");        
        playerKnowledgeLabel.setText("playerKnowledge");
        
        playerPanel.setLayout(new MigLayout ("wrap 1"));
        playerPanel.add(playerNameLabel);
        playerPanel.add(playerLifeLabel);
        playerPanel.add(playerDeckLabel);
        playerPanel.add(playerHandLabel);
        playerPanel.add(playerDiscardLabel);
        playerPanel.add(playerEnergyLabel);
        playerPanel.add(playerKnowledgeLabel);

        leftColumnPanel.setLayout(new MigLayout("wrap 1", "[grow]", "[][grow][][]"));
        leftColumnPanel.add(opponentPanel, "growx");
        leftColumnPanel.add(stackPane, "grow");
        leftColumnPanel.add(gameTextPanel, "growx");
        leftColumnPanel.add(playerPanel, "growx");

        opponentItemPane.setViewportView(opponentItemPanel);
        playerItemPane.setViewportView(playerItemPanel);

        opponentItemPanel.setLayout(new MigLayout());
        playerItemPanel.setLayout(new MigLayout());
        handPane.setLayout(new MigLayout());
        
        phaseDisplayPanel.setLayout(new MigLayout("wrap 4", "[grow 7][grow 1][grow 1][grow 1]", "[][][]"));

        currentTurnLabel.setText("currentTurn");
        beginPhaseLabel.setText("BEGIN");
        mainPhaseLabel.setText("MAIN");
        endPhaseLabel.setText("END");
        
        opponentEndBox.setSelected(true);
        playerMainBox.setSelected(true);
        
        phaseDisplayPanel.add(opponentBeginBox, "skip 1");
        phaseDisplayPanel.add(opponentMainBox);
        phaseDisplayPanel.add(opponentEndBox);

        phaseDisplayPanel.add(currentTurnLabel, "grow");
        phaseDisplayPanel.add(beginPhaseLabel);
        phaseDisplayPanel.add(mainPhaseLabel);
        phaseDisplayPanel.add(endPhaseLabel);
        
        phaseDisplayPanel.add(playerBeginBox, "skip 1");
        phaseDisplayPanel.add(playerMainBox);
        phaseDisplayPanel.add(playerEndBox);

        
        leftColumnPanel.setBackground(new java.awt.Color(150, 100, 100));
        opponentItemPane.setBackground(new java.awt.Color(100, 150, 100));
        playerItemPane.setBackground(new java.awt.Color(100, 100, 150));
        phaseDisplayPanel.setBackground(new java.awt.Color(150, 150, 100));
        handPane.setBackground(new java.awt.Color(150, 100, 150));
        handPane.setLayout(new MigLayout("wrap 8"));
        playerItemPanel.setLayout(new MigLayout("wrap 8"));
        opponentItemPanel.setLayout(new MigLayout("wrap 8"));
        setLayout(new MigLayout("wrap 2", "[10%!][90%!]", "[grow, 35%!][grow, 35%!][grow, 5%!][grow, 25%!]"));
        add(leftColumnPanel, "span 1 4, grow");
        add(opponentItemPane, "grow");
        add(playerItemPane, "grow");
        add(phaseDisplayPanel, "grow");
        add(handPane, "grow");


        opponentDiscardLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                showOpponentDiscardPile(evt);
            }
        });
   
        opponentEnergyLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                showOpponentSources(evt);
            }
        });
        
        playerDiscardLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                showPlayerDiscardPile(evt);
            }
        });
        playerEnergyLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                showPlayerSources(evt);
            }
        });
        
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                showConcedeMenu(evt);
            }
        });
        
        handPane.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                showConcedeMenu(evt);
            }
        });

        opponentItemPane.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                showConcedeMenu(evt);
            }
        });

        playerItemPane.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                showConcedeMenu(evt);
            }
        });
        
        opponentBeginBox.addActionListener(this::toggleOpponentBeginStop);
        opponentMainBox.addActionListener(this::toggleOpponentMainStop);
        opponentEndBox.addActionListener(this::toggleOpponentEndStop);
        playerBeginBox.addActionListener(this::togglePlayerBeginStop);
        playerMainBox.addActionListener(this::togglePlayerMainStop);
        playerEndBox.addActionListener(this::togglePlayerEndStop);
    }// </editor-fold>                        

    // <editor-fold defaultstate="collapsed" desc="Actions">
    private void showPlayerSources(MouseEvent evt) {                                      
        if (evt.getButton() == MouseEvent.BUTTON1) {
            new CardDisplayPopup(client.game.player.sources);
        }
    }                                     

    private void showPlayerDiscardPile(MouseEvent evt) {                                     
        if (evt.getButton() == MouseEvent.BUTTON1) {
            new CardDisplayPopup(client.game.player.discardPile);
        }
    }                                    

    private void showOpponentSources(MouseEvent evt) {                                      
        if (evt.getButton() == MouseEvent.BUTTON1) {
            new CardDisplayPopup(client.game.opponent.sources);
        }
    }                                     

    private void showOpponentDiscardPile(MouseEvent evt) {                                      
        if (evt.getButton() == MouseEvent.BUTTON1) {
            new CardDisplayPopup(client.game.opponent.discardPile);
        }
    }                                     

    private void togglePlayerBeginStop(ActionEvent evt) {                                           
        skipPlayerBegin = !skipPlayerBegin;
        Debug.println("Checkbox");
    }                                          

    private void toggleOpponentBeginStop(ActionEvent evt) {                                           
        skipOpponentBegin = !skipOpponentBegin;
    }                                          

    private void togglePlayerMainStop(ActionEvent evt) {                                           
        skipPlayerMain = !skipPlayerMain;
    }                                          

    private void togglePlayerEndStop(ActionEvent evt) {                                           
        skipPlayerEnd = !skipPlayerEnd;
    }                                          

    private void toggleOpponentMainStop(ActionEvent evt) {                                           
        skipOpponentMain = !skipOpponentMain;
    }                                          

    private void toggleOpponentEndStop(ActionEvent evt) {                                           
        skipOpponentEnd = !skipOpponentEnd;
    }                                          

    private void showConcedeMenu(MouseEvent evt) {                                  
        if (evt.getButton() == MouseEvent.BUTTON3) {
            concedeMenu().show((Component) evt.getSource(), evt.getX(), evt.getY());
        }
    }                                                                   
//</editor-fold>
    
    private JPanel gameTextPanel;
    private JButton gameTextButtonLeft;
    private JButton gameTextButtonRight;
    private JCheckBox opponentBeginBox;
    private JCheckBox opponentMainBox;
    private JCheckBox opponentEndBox;
    private JCheckBox playerBeginBox;
    private JCheckBox playerMainBox;
    private JCheckBox playerEndBox;
    private JLabel playerNameLabel;
    private JLabel opponentDiscardLabel;
    private JLabel gameTextLabel;
    private JLabel opponentKnowledgeLabel;
    private JLabel currentTurnLabel;
    private JLabel opponentEnergyLabel;
    private JLabel playerEnergyLabel;
    private JLabel beginPhaseLabel;
    private JLabel mainPhaseLabel;
    private JLabel endPhaseLabel;
    private JLabel playerKnowledgeLabel;
    private JLabel playerLifeLabel;
    private JLabel playerDeckLabel;
    private JLabel playerHandLabel;
    private JLabel playerDiscardLabel;
    private JLabel opponentNameLabel;
    private JLabel opponentLifeLabel;
    private JLabel opponentDeckLabel;
    private JLabel opponentHandLabel;
    private JPanel opponentPanel;
    private JPanel playerPanel;
    private JPanel leftColumnPanel;
    private HandPane handPane;
    private JPanel opponentItemPanel;
    private JPanel playerItemPanel;
    private StackPane stackPane;
    private JPanel phaseDisplayPanel;
    private JScrollPane playerItemPane;
    private JScrollPane opponentItemPane;
    private JSpinner xValueSelector;       

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import helpers.Debug;
import cards.Card;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import client.Client;
import enums.Knowledge;
import cards.Item;
import java.awt.Color;
import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 * @author pseudo
 */
public class GameArea extends javax.swing.JPanel {

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

        jSpinner1.setVisible(false);
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
        jLabel1.setText(client.game.player.name);
        jLabel2.setText("Life: " + client.game.player.life);
        jLabel15.setText("Energy: " + client.game.player.energy + " / " + client.game.player.sources.size());
        jLabel3.setText("Deck: " + client.game.player.deck.size());
        jLabel4.setText("Hand: " + client.game.player.hand.size());
        jLabel5.setText("Discard: " + client.game.player.discardPile.size());
        jLabel19.setText("");
        client.game.player.knowledge.forEach((knowledge, count) -> {
            jLabel19.setText(jLabel19.getText() + knowledge.toShortString() + ": " + count + " ");
        });
    }

    void setOpponentStats() {
        jLabel6.setText(client.game.opponent.name);
        jLabel7.setText("Life: " + client.game.opponent.life);
        jLabel14.setText("Energy: " + client.game.opponent.energy + " / " + client.game.opponent.sources.size());
        jLabel8.setText("Deck: " + client.game.opponent.deckSize);
        jLabel9.setText("Hand: " + client.game.opponent.handSize);
        jLabel10.setText("Discard: " + client.game.opponent.discardPile.size());
        jLabel12.setText("");
        client.game.opponent.knowledge.forEach((knowledge, count) -> {
            jLabel12.setText(jLabel12.getText() + knowledge.toShortString() + ": " + count + " ");
        });
    }

    void setPhase() {
        switch (client.game.phase) {
            case BEGIN:
            case BEGIN_RESOLVING:
                jLabel16.setForeground(Color.RED);
                jLabel17.setForeground(Color.BLACK);
                jLabel18.setForeground(Color.BLACK);
                break;
            case MAIN:
            case MAIN_RESOLVING:
                jLabel16.setForeground(Color.BLACK);
                jLabel17.setForeground(Color.RED);
                jLabel18.setForeground(Color.BLACK);
                break;
            case END:
            case END_RESOLVING:
                jLabel16.setForeground(Color.BLACK);
                jLabel17.setForeground(Color.BLACK);
                jLabel18.setForeground(Color.RED);
                break;
        }
    }

    void setTurn() {
        jLabel13.setText(client.game.turnPlayer + "'s Turn");
    }

    void updateHand() {
        client.game.player.hand.forEach((card) -> {
            card.updatePanel();
        });
        jPanel4.revalidate();
        jPanel4.repaint();
    }

    void updatePlayerItems() {
        client.game.player.items.forEach((card) -> {
            card.updatePanel();
        });
        jPanel6.validate();
        jPanel6.repaint();
    }

    void updateOpponentItems() {
        client.game.opponent.items.forEach((card) -> {
            card.updatePanel();
        });
        jPanel5.validate();
        jPanel5.repaint();
    }

    void updateAllItems() {
        updatePlayerItems();
        updateOpponentItems();
    }

    void displayPlayerItems() {
        Debug.println("Displaying Player Items: " + client.game.player.items);
        jPanel6.removeAll();
        client.game.player.items.forEach((card) -> {
            card.updatePanel();
            jPanel6.add(card.panel);
        });
        jPanel6.validate();
        jPanel6.repaint();
    }

    void displayOpponentItems() {
        Debug.println("Displaying Opponent Items: " + client.game.opponent.items);
        jPanel5.removeAll();
        client.game.opponent.items.forEach((card) -> {
            card.updatePanel();
            jPanel5.add(card.panel);
        });
        jPanel5.validate();
        jPanel5.repaint();
    }

    void displayHand() {
        Debug.println("Displaying Hand");
        jPanel4.removeAll();
        client.game.player.hand.forEach((card) -> {
            card.updatePanel();
            jPanel4.add(card.panel);
        });
        jPanel4.validate();
        jPanel4.repaint();
    }

    void displayStack() {
        jPanel7.removeAll();
        for (Card card : client.game.stack) {
            card.updatePanel();
            jPanel7.add(card.panel);
        }
        jPanel7.validate();
        jPanel7.repaint();
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
            jLabel11.setText("Do you want to mulligan?");

            jButton1.setText("Mulligan");
            removeActionListeners(jButton1);
            jButton1.addActionListener((action) -> {
                jButton1.setEnabled(false);
                jButton2.setEnabled(false);
                client.mulligan();
            });
            jButton1.setEnabled(true);
            jButton1.setVisible(true);

            jButton2.setText("Keep");
            removeActionListeners(jButton2);
            jButton2.addActionListener((action) -> {
                jButton1.setEnabled(false);
                jButton2.setEnabled(false);
                client.keep();
            });
            jButton2.setEnabled(true);
            jButton2.setVisible(true);
        } else {
            jLabel11.setText("Waiting for opponents mulligan.");
            jButton1.setVisible(false);
            jButton2.setVisible(false);
        }
    }

    void displayPlayText() {
        if (client.game.hasInitiative()) {
            jLabel11.setText("Pass Initiative?");
            jButton2.setVisible(false);

            jButton1.setText("Pass");
            removeActionListeners(jButton1);
            jButton1.addActionListener((action) -> {
                jButton1.setEnabled(false);
                client.skipInitiative();
            });
            jButton1.setEnabled(true);
            jButton1.setVisible(true);
        } else {
            jLabel11.setText("Waiting for opponent.");
            jButton1.setVisible(false);
            jButton2.setVisible(false);
        }
    }

    void displayResolvingText() {
        jLabel11.setText("Stack is Resolving.");
        jButton1.setVisible(false);
        jButton2.setVisible(false);
    }

    void displayTargetingText(BiConsumer<Client, ArrayList<Serializable>> func, int count) {
        jLabel11.setText("Choose up to " + count + " targets.");

        jButton1.setText("Cancel");
        removeActionListeners(jButton1);
        jButton1.addActionListener((action) -> {
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            removeAllItemListeners();
            clearItemMarks();
            selected = new ArrayList<>();
            update();
        });
        jButton1.setEnabled(true);
        jButton1.setVisible(true);

        jButton2.setText("Done");
        removeActionListeners(jButton2);
        jButton2.addActionListener((action) -> {
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            removeAllItemListeners();
            clearItemMarks();
            func.accept(client, selected);
            selected = new ArrayList<>();
        });
        jButton2.setEnabled(true);
        jButton2.setVisible(true);
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

    public void discard(int count) {
        Debug.println("Discarding " + count);
        jLabel11.setText("Discard " + count + " cards.");
        jButton1.setVisible(false);
        jButton2.setVisible(false);
        removeHandListeners();
        addDiscardListeners(count);
        displayHand();
    }

    void knowledgeSelection(MouseEvent evt, Card card) {
        if (card.knowledge.isEmpty()) {
            client.playSource(card, Knowledge.NONE);
        } else if (card.knowledge.size() == 1) {
            client.playSource(card, card.knowledge.keySet().iterator().next());
        } else {
            removeHandListeners();
            removeAllItemListeners();
            jButton1.setVisible(false);
            jButton2.setVisible(false);
            jLabel11.setText("Choose knowledge to get");
            knowledgeMenu(evt, card);
        }
    }

    void clearItemMarks() {
        client.game.player.items.forEach((card) -> {
            card.marked = false;
        });
        client.game.opponent.items.forEach((card) -> {
            card.marked = false;
        });
    }
    
    public void getXValue(BiConsumer<Client, Integer> func, BiFunction <Client, Integer, Boolean> cond){
        jSpinner1.setVisible(true);
        jSpinner1.setValue(0);
        
        jLabel11.setText("Choose value for X");
        jButton2.setVisible(false);

        jButton1.setText("Done");
        removeActionListeners(jButton1);
        jButton1.addActionListener((action) -> {
            if (cond.apply(client, (Integer) jSpinner1.getValue())){
                jButton1.setEnabled(false);
                jButton2.setEnabled(false);
                jSpinner1.setVisible(false);
                func.accept(client, (Integer) jSpinner1.getValue());
            }
        });
        jButton1.setEnabled(true);
        jButton1.setVisible(true);
    }

    // <editor-fold defaultstate="collapsed" desc="Listeners">
    // <editor-fold defaultstate="collapsed" desc="Adders">
    void addActivateListeners() {
        removePlayerItemListeners();
        Debug.println("Adding activate listeners");
        client.game.player.items.forEach((card) -> {
            card.panel.addMouseListener(activateAdapter(card));
        });
    }

    void addPlayListeners() {
        removeHandListeners();
        Debug.println("Adding play listeners");
        client.game.player.hand.forEach((card) -> {
            card.panel.addMouseListener(playAdapter(card));
        });
    }

    void addDiscardListeners(int count) {
        Debug.println("Adding discard listeners");
        client.game.player.hand.forEach((card) -> {
            card.panel.addMouseListener(discardAdapter(card, count));
        });
    }
    
    public void addFilteredPlayerTargetListeners(BiConsumer<Client, ArrayList<Serializable>> func, Function<Item, Boolean> filter, int count) {
        removePlayerItemListeners();
        Debug.println("Adding player target listeners");
        client.game.player.items.forEach((card) -> {
            if (filter.apply(card))
                card.panel.addMouseListener(targetAdapter(func, card, count));
        });
        displayTargetingText(func, count);
    }

    public void addPlayerTargetListeners(BiConsumer<Client, ArrayList<Serializable>> func, int count) {
        addFilteredPlayerTargetListeners(func, c->{return true;}, count);
    }
    
    public void addFilteredOpponentTargetListeners(BiConsumer<Client, ArrayList<Serializable>> func, Function<Item, Boolean> filter, int count) {
        removeOpponentItemListeners();
        client.game.opponent.items.forEach((card) -> {
            if (filter.apply(card))
                card.panel.addMouseListener(targetAdapter(func, card, count));
        });
        displayTargetingText(func, count);
    }

    public void addOpponentTargetListeners(BiConsumer<Client, ArrayList<Serializable>> func, int count) {
        addFilteredOpponentTargetListeners(func, c->{return true;}, count);
    }
    
    public void addFilteredTargetListeners(BiConsumer<Client, ArrayList<Serializable>> func, Function<Item, Boolean> filter, int count) {
        addFilteredPlayerTargetListeners(func, filter, count);
        addFilteredOpponentTargetListeners(func, filter, count);
    }

    public void addTargetListeners(BiConsumer<Client, ArrayList<Serializable>> func, int count) {
        addFilteredTargetListeners(func, c->{return true;}, count);
    }

    public void addPlayerSelector(BiConsumer<Client, ArrayList<Serializable>> func) {
        jLabel11.setText("Select a player");

        jButton1.setText(client.game.player.name);
        removeActionListeners(jButton1);
        jButton1.addActionListener((action) -> {
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            selected.add(client.game.player.uuid);
            func.accept(client, selected);
            selected = new ArrayList<>();
        });
        jButton1.setEnabled(true);
        jButton1.setVisible(true);

        jButton2.setText(client.game.opponent.name);
        removeActionListeners(jButton2);
        jButton2.addActionListener((action) -> {
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            selected.add(client.game.opponent.uuid);
            func.accept(client, selected);
            selected = new ArrayList<>();
        });
        jButton2.setEnabled(true);
        jButton2.setVisible(true);
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
            MouseListener[] listeners = card.panel.getMouseListeners();
            for (MouseListener l : listeners) {
                card.panel.removeMouseListener(l);
            }
        });
    }

    void removePlayerItemListeners() {
        Debug.println("Removing Item listeners");
        client.game.player.items.forEach((card) -> {
            MouseListener[] listeners = card.panel.getMouseListeners();
            for (MouseListener l : listeners) {
                card.panel.removeMouseListener(l);
            }
        });
    }

    void removeOpponentItemListeners() {
        Debug.println("Removing Item listeners");
        client.game.opponent.items.forEach((card) -> {
            MouseListener[] listeners = card.panel.getMouseListeners();
            for (MouseListener l : listeners) {
                card.panel.removeMouseListener(l);
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
    void knowledgeMenu(MouseEvent evt, Card card) {
        JPopupMenu menu = new JPopupMenu();
        for (Knowledge knowl : card.knowledge.keySet()) {
            JMenuItem menuItem = new JMenuItem(knowl.toString());
            menu.add(menuItem);
            menuItem.addActionListener((ActionEvent event) -> {
                Debug.println("Playing as a source: " + card.name);
                client.playSource(card, knowl);
            });
        }
        menu.setVisible(true);
        menu.show((Component) evt.getSource(), evt.getX(), evt.getY());
    }

    void sourceMenu(MouseEvent evt, Card card) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Play as a source");
        menu.add(menuItem);
        menuItem.addActionListener((ActionEvent event) -> {
            Debug.println("Playing as a source: " + card.name);
            knowledgeSelection(evt, card);
        });
        menu.setVisible(true);
        menu.show((Component) evt.getSource(), evt.getX(), evt.getY());
    }

    JPopupMenu concedeMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Concede");
        menu.add(menuItem);
        menuItem.addActionListener((ActionEvent event) -> {
            client.concede();
        });
        menu.setVisible(true);
        return menu;
    }
//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Adapters">
    java.awt.event.MouseAdapter playAdapter(Card card) {
        return new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (client.game.hasInitiative()) {
                    if (evt.getButton() == MouseEvent.BUTTON1) {
                        if (evt.isControlDown()) {
                            if (client.game.canPlaySource()) {
                                Debug.println("Playing as a source: " + card.name);
                                knowledgeSelection(evt, card);
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

    java.awt.event.MouseAdapter discardAdapter(Card card, int count) {
        return new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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

    java.awt.event.MouseAdapter activateAdapter(Item card) {
        return new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (client.game.hasInitiative() && card.canActivate(client.game) && evt.getButton() == MouseEvent.BUTTON1) {
                    Debug.println("Activating: " + card.name);
                    card.activate(client);
                }
            }
        };
    }

    java.awt.event.MouseAdapter targetAdapter(BiConsumer<Client, ArrayList<Serializable>> func, Card targetCard, int count) {
        return new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    if (!targetCard.marked) {
                        Debug.println("Selected for target: " + targetCard.name);
                        targetCard.marked = true;
                        selected.add(targetCard.uuid);
                        if (selected.size() == count) {
                            Debug.println("All cards are selected for target");
                            removeAllItemListeners();
                            clearItemMarks();
                            func.accept(client, selected);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        gameTextPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jSpinner1 = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel1.setBackground(new java.awt.Color(255, 176, 180));

        jLabel6.setText("opponentName");

        jLabel7.setText("opponentLife");

        jLabel8.setText("opponentDeck");

        jLabel9.setText("opponentHand");

        jLabel10.setText("opponentDiscard");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        jLabel14.setText("jLabel14");
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });

        jLabel12.setText("jLabel12");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel14)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8)
                    .addComponent(jLabel12))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12))
        );

        jPanel3.add(jPanel1);

        jScrollPane5.setMaximumSize(new java.awt.Dimension(1000, 1000));
        jScrollPane5.setMinimumSize(new java.awt.Dimension(100, 200));
        jScrollPane5.setPreferredSize(new java.awt.Dimension(100, 200));

        jPanel7.setMaximumSize(new java.awt.Dimension(32000, 32000));
        jPanel7.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane5.setViewportView(jPanel7);

        jPanel3.add(jScrollPane5);

        gameTextPanel.setBackground(new java.awt.Color(230, 228, 140));
        gameTextPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        jLabel11.setText("gameTextArea");

        jButton2.setText("jButton2");
        jButton2.setMaximumSize(new java.awt.Dimension(200, 30));

        jButton1.setText("jButton1");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        javax.swing.GroupLayout gameTextPanelLayout = new javax.swing.GroupLayout(gameTextPanel);
        gameTextPanel.setLayout(gameTextPanelLayout);
        gameTextPanelLayout.setHorizontalGroup(
            gameTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameTextPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gameTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gameTextPanelLayout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addContainerGap())
                    .addGroup(gameTextPanelLayout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))))
        );
        gameTextPanelLayout.setVerticalGroup(
            gameTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameTextPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gameTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addGroup(gameTextPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gameTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)))
        );

        jPanel3.add(gameTextPanel);

        jPanel2.setBackground(new java.awt.Color(155, 219, 239));

        jLabel1.setText("Player Name");

        jLabel2.setText("Player Life");

        jLabel3.setText("playerDeck");

        jLabel4.setText("playerHand");

        jLabel5.setText("playerDiscard");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        jLabel15.setText("jLabel15");
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });

        jLabel19.setText("jLabel19");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel15)
                    .addComponent(jLabel19))
                .addContainerGap(95, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel19))
        );

        jLabel1.getAccessibleContext().setAccessibleName("playerName");
        jLabel2.getAccessibleContext().setAccessibleName("playerLife");

        jPanel3.add(jPanel2);

        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jPanel4);

        jScrollPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jPanel5);

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jPanel6);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("jLabel13");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setLayout(new java.awt.GridLayout(3, 4, 50, 0));

        jCheckBox1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel9.add(jCheckBox1);

        jCheckBox2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jPanel9.add(jCheckBox2);

        jCheckBox3.setSelected(true);
        jCheckBox3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        jPanel9.add(jCheckBox3);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("BEGIN");
        jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel9.add(jLabel16);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("MAIN");
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel9.add(jLabel17);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("END");
        jPanel9.add(jLabel18);

        jCheckBox4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });
        jPanel9.add(jCheckBox4);

        jCheckBox5.setSelected(true);
        jCheckBox5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });
        jPanel9.add(jCheckBox5);

        jCheckBox6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });
        jPanel9.add(jCheckBox6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(165, 165, 165)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Actions">
    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            new CardDisplayPopup(client.game.player.sources);
        }
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            new CardDisplayPopup(client.game.player.discardPile);
        }
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            new CardDisplayPopup(client.game.opponent.sources);
        }
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
            new CardDisplayPopup(client.game.opponent.discardPile);
        }
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        skipPlayerBegin = !skipPlayerBegin;
        Debug.println("Checkbox");
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        skipOpponentBegin = !skipOpponentBegin;
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        skipPlayerMain = !skipPlayerMain;
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        skipPlayerEnd = !skipPlayerEnd;
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        skipOpponentMain = !skipOpponentMain;
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        skipOpponentEnd = !skipOpponentEnd;
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            concedeMenu().show((Component) evt.getSource(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_formMouseClicked

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            concedeMenu().show((Component) evt.getSource(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void jScrollPane3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            concedeMenu().show((Component) evt.getSource(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jScrollPane3MouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            concedeMenu().show((Component) evt.getSource(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jScrollPane1MouseClicked
//</editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel gameTextPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSpinner jSpinner1;
    // End of variables declaration//GEN-END:variables
}

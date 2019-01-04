/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import card.Card;
import static helpers.Debug.println;
import java.awt.Dimension;
import static java.awt.EventQueue.invokeLater;
import static java.awt.event.MouseEvent.BUTTON1;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author pseudo
 */
public class CardOrderPopup extends javax.swing.JDialog {
    ArrayList<Card> unordered, ordered;
    
    /**
     * Creates new form CardDisplay
     * @param cards
     * @param continuation
     */
    public CardOrderPopup(ArrayList<Card> cards, Consumer<ArrayList<Card>> continuation) {
        super(new javax.swing.JFrame(), true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new MigLayout("wrap 4"));
        setMinimumSize(new Dimension(350, 350));
        unordered = cards;
        ordered = new ArrayList<>();
        unordered.forEach((card) -> {
            card.updatePanel();
            add(card.getPanel());
            card.getPanel().addMouseListener(orderAdapter(card, continuation));
        });
        invokeLater(() -> {
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    ordered.addAll(unordered);
                    continuation.accept(ordered);
                    dispose();
                }
            });
            pack();
            setVisible(true);
        });
    }

    java.awt.event.MouseAdapter orderAdapter(Card card, Consumer<ArrayList<Card>> func) {
        return new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getButton() == BUTTON1) {
                    println("Selected an ordering card");
                    unordered.remove(card);
                    card.getPanel().removeAll();
                    ordered.add(card);
                    if (unordered.isEmpty()){
                        func.accept(ordered);
                        dispose();
                    } else {
                        remove(card.getPanel());
                        pack();
                    }
                }
            }
        };
    }
}

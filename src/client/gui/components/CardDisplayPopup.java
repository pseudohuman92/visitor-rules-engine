/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import card.Card;
import java.awt.Dimension;
import static java.awt.EventQueue.invokeLater;
import java.util.ArrayList;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author pseudo
 */
public class CardDisplayPopup extends javax.swing.JDialog {

    /**
     * Creates new CardDisplay
     * @param cards
     */
    public CardDisplayPopup(ArrayList<Card> cards) {
        super(new javax.swing.JFrame(), true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new MigLayout("wrap 4"));
        setMinimumSize(new Dimension(350, 350));
        cards.forEach((card) -> {
            card.updatePanel();
            add(card.getPanel());});
        invokeLater(() -> {
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    dispose();
                }
            });
            pack();
            setVisible(true);
        });
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import cards.Card;
import java.util.ArrayList;
import javax.swing.WindowConstants;
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
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLayout(new MigLayout("wrap 4"));
        cards.forEach((card) -> {
            card.updatePanel();
            add(card.getPanel());});
        java.awt.EventQueue.invokeLater(() -> {
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.gui.components;

import static java.awt.EventQueue.invokeLater;
import java.awt.FlowLayout;
import java.awt.Font;
import static java.awt.Font.BOLD;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author pseudo
 */
public class TextPopup extends JDialog {
    private JLabel text;

    /**
     *
     * @param message
     */
    public TextPopup(String message) {
        super(new JFrame(), true);
        initComponents();
        text.setFont(new Font("Ubuntu", BOLD, 18));
        text.setText(message);
        invokeLater(() -> {
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dispose();
                }
            });
            setBounds(400, 300, 0, 0);
            pack();
            setVisible(true);
        });
        
    }
                      
    private void initComponents() {
        text = new JLabel();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new FlowLayout());

        text.setText("Default popup text");
        getContentPane().add(text);
        pack();
    }                     
                    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.gui.components.TextPopup;
import client.Client;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;


/**
 *
 * @author pseudo
 */
public class Login extends JPanel {

    Client client;
    
    public Login(Client client) {
        this.client = client;
        initComponents();
        //Comment out to activate registration
        registerButton.setVisible(false);
    }
                   
    private void initComponents() {

        usernameField = new JTextField();
        loginButton = new JButton();
        registerButton = new JButton();
        usernameLabel = new JLabel();
        
        setLayout(new MigLayout("wrap 5",
                                "[left, 20% | center, 20% | center, fill, 20% | center, 20% | right, 20%]", 
                                "[20%] [30%] [30%] [20%]"));
        add(usernameLabel,"newline, skip 1, right, bottom");
        add(usernameField, "growx, span 2, bottom, wrap");
        add(registerButton, "skip 1");
        add(loginButton, "skip 1, right, top");

        usernameField.setToolTipText("Username");

        loginButton.setText("Login");
        loginButton.addActionListener(this::login);

        registerButton.setText("Register");
        registerButton.addActionListener(this::register);

        usernameLabel.setText("Username");
    }// </editor-fold>                        

    private void login(java.awt.event.ActionEvent evt) {                                      
        if (!usernameField.getText().equals("")) {
            String username = usernameField.getText();
            client.login(username);
        }
    }                                     

    private void register(java.awt.event.ActionEvent evt) {                                            
        if (!usernameField.getText().equals("")) {
            String username = usernameField.getText();
            new TextPopup(client.register(username));
        }
    }                                           


    // Variables declaration - do not modify                     
    private JButton loginButton;
    private JButton registerButton;
    private JLabel usernameLabel;
    private JTextField usernameField;
    // End of variables declaration                   
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import static java.awt.EventQueue.invokeLater;

/**
 *
 * @author pseudo
 */
public class Main {
    public static void main(String args[]) {
        
        Client client = new Client();
        invokeLater(() -> client.main.setVisible(true));
    }
}

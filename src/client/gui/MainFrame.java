/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.gui;


import java.awt.Component;
import javax.swing.JPanel;
import client.Client;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;


/**
 *
 * @author pseudo
 */
public class MainFrame extends JFrame {
    
    Client client;
    
    public MainFrame(Client client) {
        this.client = client;
        initComponents();
    }

    public void add(String tabName, JPanel panel){
        tabbedPane.addTab(tabName, panel);
    }
    
    public void setSelectedComponent(Component c) {
        tabbedPane.setSelectedComponent(c);
    }
            
    
    public void remove(JPanel panel){
        tabbedPane.remove(panel);
    }
                        
    private void initComponents() {

        tabbedPane = new JTabbedPane();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                logout(evt);
            }
        });
        
        setLayout(new MigLayout("","[grow]","[grow]"));
        add(tabbedPane, "grow");
    }// </editor-fold>                        

    private void logout(WindowEvent evt) {                                   
        client.logout();
    }                                  
                  
    private JTabbedPane tabbedPane;                
}

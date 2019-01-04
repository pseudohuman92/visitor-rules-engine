/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client.gui;


import client.Client;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.miginfocom.swing.MigLayout;


/**
 *
 * @author pseudo
 */
public class MainFrame extends JFrame {
    
    Client client;
    private JTabbedPane tabbedPane;
    
    /**
     *
     * @param client
     */
    public MainFrame(Client client) {
        this.client = client;
        initComponents();
    }

    /**
     *
     * @param tabName
     * @param panel
     */
    public void add(String tabName, JPanel panel){
        tabbedPane.addTab(tabName, panel);
    }
    
    /**
     *
     * @param c
     */
    public void setSelectedComponent(Component c) {
        tabbedPane.setSelectedComponent(c);
    }
            
    /**
     *
     * @param panel
     */
    public void remove(JPanel panel){
        tabbedPane.remove(panel);
    }
                        
    private void initComponents() {

        tabbedPane = new JTabbedPane();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                if (client.connection != null)
                    logout(evt);
            }
        });
        
        setLayout(new MigLayout("","[grow]","[grow]"));
        add(tabbedPane, "grow");
    }// </editor-fold>                        

    private void logout(WindowEvent evt) {                                   
        client.logout();
    }                                  
                  
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import javax.swing.JPanel;

/**
 *
 * @author pseudo
 */
public class MemoryPanel extends JPanel {
    
    public int layer;
    
    public MemoryPanel(){
        layer = 0;
    }
    
    public MemoryPanel(int l){
        layer = l;
    }
}

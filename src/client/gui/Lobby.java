/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.Client;
import game.Table;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_ENTER;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Array;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author pseudo
 */
public class Lobby extends JPanel {

    Client client;
    private JButton sendButton;
    private JButton createButton;
    private JButton joinButton;
    private JList<String> playerList;
    private JList<String> chatHistory;
    private JScrollPane tableScrollPane;
    private JScrollPane chatScrollPane;
    private JScrollPane playerScrollPane;
    private JTable tableList;
    private JTextField newMessageBox;
    
    /**
     *
     * @param client
     */
    public Lobby(Client client) {
        this.client = client;
        initComponents();
    }
    
    /**
     *
     */
    public void updateChat(){
        chatHistory.setListData(client.chatLog.toArray(new String[client.chatLog.size()]));
        chatHistory.ensureIndexIsVisible(client.chatLog.size()-1);
    }
    
    /**
     *
     */
    public void updatePlayers(){
        playerList.setListData(client.players.toArray(new String[client.players.size()]));
    }
    
    /**
     *
     */
    public void updateTables(){
        DefaultTableModel model = (DefaultTableModel)tableList.getModel();
        model.setRowCount(0);
        client.tables.values().stream().forEachOrdered((Table ta) -> {
            Table[] tarr = {ta};
            model.addRow(tarr);
        });
    }
    
    void joinTable(){
        ListSelectionModel selectionModel = tableList.getSelectionModel();
        if (!selectionModel.isSelectionEmpty()) {
            JFileChooser chooser = new JFileChooser("./assets");
            int returnVal = chooser.showOpenDialog(this);

            if (returnVal == APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                Table table = (Table)tableList.getValueAt(selectionModel.getMaxSelectionIndex(), 0);
                client.joinTable(file, table.uuid);
            }
        }
    }
    
    void sendMessage(){
        if (!newMessageBox.getText().isEmpty()){
            client.sendMessage(newMessageBox.getText());
            newMessageBox.setText("");
        }
    }
    
    void createTable(){
        JFileChooser chooser = new JFileChooser("./assets");
        int returnVal = chooser.showOpenDialog(this);

        if (returnVal == APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            client.createTable(file);
        }
    }

                    
    private void initComponents() {

        tableScrollPane = new JScrollPane();
        tableList = new JTable();
        sendButton = new JButton();
        newMessageBox = new JTextField();
        createButton = new JButton();
        joinButton = new JButton();
        chatScrollPane = new JScrollPane();
        chatHistory = new JList<>();
        playerScrollPane = new JScrollPane();
        playerList = new JList<>();
        
        MigLayout layout = new MigLayout("wrap 5", "[button][grow 9][button][button][grow 1]",
                                        "[grow 5][][grow5][]");
        setLayout(layout);
        add(tableScrollPane, "grow, span 5");
        add(joinButton);
        add(createButton, "skip 3, right");
        add(chatScrollPane, "grow, span 4 2");
        add(playerScrollPane, "grow, span 1 3, wrap");
        add(newMessageBox, "growx, span 3");
        add(sendButton, "right");
        
        
        tableList.setModel(new DefaultTableModel(
            new Object [][] {}, new String [] { "Tables" }
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        tableList.setSelectionMode(SINGLE_SELECTION);
        tableList.setShowVerticalLines(false);
        tableList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                join(evt);
            }
        });
        tableScrollPane.setViewportView(tableList);

        sendButton.setText("Send");
        sendButton.addActionListener(this::send);

        newMessageBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                send(evt);
            }
        });

        createButton.setText("Create");
        createButton.addActionListener(this::create);

        joinButton.setText("Join");
        joinButton.addActionListener(this::join);

        
        chatHistory.setVerifyInputWhenFocusTarget(false);
        chatHistory.setVisibleRowCount(15);
        chatScrollPane.setAutoscrolls(true);
        chatScrollPane.setViewportView(chatHistory);
        
        playerScrollPane.setViewportView(playerList);
        
        setVisible(true);
    }                  

    private void send(ActionEvent evt) {                                         
        sendMessage();
    }                                        

    private void send(KeyEvent evt) {                                       
        if (evt.getKeyCode() == VK_ENTER){
            sendMessage();
        }
    }                                      

    private void create(ActionEvent evt) {                                         
        createTable();
    }                                        

    private void join(ActionEvent evt) {                                         
        joinTable();
    }                                        

    private void join(MouseEvent evt) {                                     
        if (evt.getClickCount() == 2){
            joinTable();
        }
    }                                    
                  
}

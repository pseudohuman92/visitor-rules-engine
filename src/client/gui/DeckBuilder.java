/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import cards.Card;
import cards.CardGenerator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import client.Client;
import helpers.Debug;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author pseudo
 */
public class DeckBuilder extends JPanel {
    
    Client client;
    CardGenerator cardGenerator;

    public DeckBuilder(Client client) {
        initComponents();
        this.client = client;
        cardGenerator = new CardGenerator("");
        loadCardDatabase(collectionTable);
    }
    
    void displayCard(){
        String cardName = (String) collectionTable.getValueAt(collectionTable.getSelectedRow(), 0);
        displayedCard.removeAll();
        Card c = cardGenerator.createCard(cardName);
        if (c != null) {
            c.updatePanel();
            displayedCard.add(c.getPanel(), "grow");
            displayedCard.revalidate();
            displayedCard.repaint();
            Debug.println(cardName + " selected from collection");
        }
    }
    
    void displayCardDeck(){
        String cardName = (String) deckList.getValueAt(deckList.getSelectedRow(), 1);
        displayedCard.removeAll();
        Card c = cardGenerator.createCard(cardName);
        if (c != null) {
            c.updatePanel();
            displayedCard.add(c.getPanel(), "grow");
            displayedCard.revalidate();
            displayedCard.repaint();
            Debug.println(cardName + " selected from deck");
        }
    }
    
    void loadCardDatabase(JTable cardDatabase) {
        Scanner dbFile = null;
        try {
            File db = new File("./assets/db.txt");
            if (!db.exists()) { db.createNewFile(); }
            dbFile = new Scanner(db);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(DeckBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (dbFile.hasNextLine()) {
            DefaultTableModel model = (DefaultTableModel) cardDatabase.getModel();
            model.addRow(new String[]{dbFile.nextLine()});
        }
        dbFile.close();
    }
    
    void removeFromDeck(){
        if (Integer.parseInt((String) deckList.getValueAt(deckList.getSelectedRow(), 0)) == 1) {
            ((DefaultTableModel) deckList.getModel()).removeRow(deckList.getSelectedRow());
        } else {
            deckList.setValueAt(String.valueOf(Integer.parseInt((String) 
                    deckList.getValueAt(deckList.getSelectedRow(), 0)) - 1), deckList.getSelectedRow(), 0);
        }
    }
    
    void addToDeck() {
        String cardName = (String) collectionTable.getValueAt(collectionTable.getSelectedRow(), 0);
        boolean exists = false;
        for (int i = 0; i < deckList.getModel().getRowCount(); i++) {
            if (cardName.equals(deckList.getModel().getValueAt(i, 1))) {
                exists = true;
                deckList.setValueAt(String.valueOf(Integer.parseInt((String) deckList.getValueAt(i, 0)) + 1), i, 0);
                break;
            }
        }
        if (!exists) {
            ((DefaultTableModel) deckList.getModel()).addRow(new String[]{"1", cardName});
        }
    }

    void newDeck(){
        DefaultTableModel dtf = (DefaultTableModel) deckList.getModel();
        dtf.setRowCount(0);
        deckName.setText("New Deck");
    }
    
    void loadDeck(){
        JFileChooser fc = new JFileChooser("./assets");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            DefaultTableModel dtf = (DefaultTableModel) deckList.getModel();
            dtf.setRowCount(0);

            Scanner deckFile = null;
            try {
                deckFile = new Scanner(fc.getSelectedFile());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            deckName.setText(deckFile.nextLine());

            while (deckFile.hasNextLine()) {
                String card = deckFile.nextLine();
                dtf.addRow(new String[]{card.substring(0, 1), card.substring(2)});
            }
            deckFile.close();
        }
    }
    
    void saveDeck(){
        JFileChooser fc = new JFileChooser(".");
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Writer writer;
            try {
                writer = new OutputStreamWriter(new FileOutputStream(fc.getSelectedFile()));
                writer.write(deckName.getText() + "\r\n");
                for (int i = 0; i < deckList.getRowCount(); i++) {
                    writer.write(deckList.getValueAt(i, 0) + " " + deckList.getValueAt(i, 1) + "\r\n");
                }
                writer.flush();
                writer.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
                     
    private void initComponents() {

        collectionPane = new JScrollPane();
        collectionTable = new JTable();
        decklistPane = new JScrollPane();
        deckList = new JTable();
        displayedCard = new JPanel();
        loadButton = new JButton();
        saveButton = new JButton();
        newButton = new JButton();
        deckName = new JTextField();

        displayedCard.setLayout(new MigLayout("wrap 1", "[grow]","[grow]"));
        displayedCard.setBackground(new java.awt.Color(255, 176, 180));
        collectionTable.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] { "Cards" }
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        collectionTable.setColumnSelectionAllowed(true);
        collectionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addToDeck(evt);
            }
        });

        collectionPane.setViewportView(collectionTable);
        collectionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                displayCard();
            }
        });
        
        collectionTable.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        

        deckList.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] { "Count", "Card" }
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        
        deckList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeFromDeck(evt);
            }
        });
        deckList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                displayCardDeck();
            }
        });
        deckList.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        decklistPane.setViewportView(deckList);


        loadButton.setText("Load");
        loadButton.addActionListener(this::loadDeck);

        saveButton.setText("Save");
        saveButton.addActionListener(this::saveDeck);

        newButton.setText("New");
        newButton.addActionListener(this::newDeck);

        deckName.setText("New Deck");

        setLayout(new MigLayout("wrap 5", 
                                "[button][grow 8][grow 2][button][button]", 
                                "[grow 6][grow 4][]"));
        add(collectionPane, "grow, span 2");
        add(displayedCard, "grow, span 3");
        add(decklistPane, "grow, span 5");
        add(newButton);
        add(deckName, "growx, span 2");
        add(saveButton);
        add(loadButton);
    }                  

    private void newDeck(java.awt.event.ActionEvent evt) {                                         
        newDeck();
    }                                        

    private void addToDeck(java.awt.event.MouseEvent evt) {                                     
        if (evt.getClickCount() == 2) {
            addToDeck();
        }
    }                                                       

    private void removeFromDeck(java.awt.event.MouseEvent evt) {                                     
        if (evt.getClickCount() == 2) {
            removeFromDeck();
        }
    }                                    

    private void saveDeck(java.awt.event.ActionEvent evt) {                                         
        saveDeck();
    }                                        

    private void loadDeck(java.awt.event.ActionEvent evt) {                                         
        loadDeck();
    }                                        

               
    private JButton loadButton;
    private JButton saveButton;
    private JButton newButton;
    private JScrollPane collectionPane;
    private JScrollPane decklistPane;
    private JTable collectionTable;
    private JPanel displayedCard;
    private JTable deckList;
    private JTextField deckName;               
}

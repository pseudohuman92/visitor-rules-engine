/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import game.ClientGame;
import java.util.UUID;
import network.Connection;
import network.Message;
import game.Table;
import cards.Card;
import client.gui.CardOrderPopup;
import game.Deck;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import client.gui.DeckBuilder;
import client.gui.GameArea;
import client.gui.Lobby;
import client.gui.Login;
import client.gui.Main;
import client.gui.TextPopup;
import enums.Knowledge;
import java.io.Serializable;

/**
 *
 * @author pseudo
 */
public class Client {
    String hostname;
    int hostport;
    int hostGamePort;
    Connection connection;
    public Connection gameConnection;
    public Connection interactionConnection;

    public String username;
    public ArrayList<String> players;
    public ArrayList<String> chatLog;
    public HashMap<UUID, Table> tables;
    public ClientGame game;
    
    //UI
    Main main;
    Login login;
    Lobby lobby;
    DeckBuilder deckBuilder;
    public GameArea gameArea;
    
    
    public Client(){
        try {
            //Initialize UI
            main = new Main(this);
            login = new Login(this);
            lobby = new Lobby(this);
            deckBuilder = new DeckBuilder(this);
            
            main.add("Login", login);
            login.setVisible(true);
            
            hostname = InetAddress.getLocalHost().getHostAddress();
            hostport = 8080;
            hostGamePort = 8081;
            System.out.println("Hostname: " + hostname);
            connection = new Connection();
            connection.openConnection(hostname, hostport);
            System.out.println("Client Created");
        } catch (UnknownHostException ex) {
            System.out.println("Unknown Host");
        }
    }
    
    //Game Related Functions
    public void newGame(ClientGame game){
        this.game = game;
        gameConnection = new Connection();
        gameConnection.openConnection(hostname, hostGamePort);
        new Thread(new ClientGameReceiver(gameConnection, this)).start();
        gameConnection.send(Message.registerGameConnection(game.uuid, username));
        
        interactionConnection = new Connection();
        interactionConnection.openConnection(hostname, hostGamePort);
        new Thread(new ClientGameReceiver(interactionConnection, this)).start();
        interactionConnection.send(Message.registerInteractionConnection(game.uuid, username));
        
        gameArea = new GameArea(this);
        main.add("Game", gameArea);
        gameArea.setVisible(true);
        gameArea.revalidate();
        gameArea.repaint();
        main.setSelectedComponent(gameArea);
        main.setSelectedComponent(deckBuilder);
        main.setSelectedComponent(gameArea);
    }
    
    public void updateGame(ClientGame game){
        this.game = game;
        gameArea.update();
    }
    
    public void mulligan(){
        gameConnection.send(Message.mulligan(game.uuid, username));
    }
    
    public void keep(){
        gameConnection.send(Message.keep(game.uuid, username));
    }
    
    public void playSource(Card card, Knowledge knowledge){
        gameConnection.send(Message.playSource(game.uuid, username, card.uuid, knowledge));
    }
    
    public void skipInitiative(){
        gameConnection.send(Message.pass(game.uuid));
    }
    
    public void discard(int count){
        if (!game.player.hand.isEmpty()) {
            gameArea.discard(count);
        } else {
            interactionConnection.send(Message.discardReturn(new ArrayList<>()));
        }
    }
    
    public void discardReturn(ArrayList<Serializable> cards){
        interactionConnection.send(Message.discardReturn(cards));
    }
    
    public void concede(){
        gameConnection.send(Message.concede(game.uuid, username));
    }
    
    public void lose(){
        new TextPopup("You lost");
        finishGame();
    }
    
    public void win(){
        new TextPopup("You win");
        finishGame();
    }
    
    public void finishGame(){
        gameConnection.closeConnection();
        interactionConnection.closeConnection();
        main.setSelectedComponent(lobby);
        main.remove(gameArea);
        game = null;
        gameArea = null;
    }
    
    //Chat Related Functions
    public void sendMessage(String message){
        connection.send(Message.chatMessage(username+": "+message));
    }
    
    public void updateChat(){
        lobby.updateChat();
    }
    
    public void updatePlayers(){
        lobby.updatePlayers();
    }
    
    //Table Related Functions
    public void createTable(File deckFile){
        Deck deck = new Deck(deckFile, username);
        if(deck.valid()){
            connection.send(Message.createTable(username, deck));
        } else {
            new TextPopup("Invalid deck.");
        }
    }
    
    public void joinTable(File deckFile, UUID uuid){
        Deck deck = new Deck(deckFile, username);
        if(deck.valid()){
            connection.send(Message.joinTable(username, deck, uuid));
        } else {
            new TextPopup("Invalid deck.");
        }
    }
    
    public void updateTables(){
        lobby.updateTables();
    }
    
    
    // Login related functions
    public String register(String username){
        connection.send(Message.register(username));
        Message message = connection.receive();
        switch (message.label) {
            case SUCCESS:
                return username + " successfuly registered.";
            case FAIL:
                return "Registration failed. Error: " + message.object;
            default:
                return "Unexpected server response.";
        }
    }
    
    public void signalLogin(){
        main.remove(login);
        login.setVisible(false);
        main.add("Lobby", lobby);
        main.add("Deck Editor", deckBuilder);
        lobby.setVisible(true);
        deckBuilder.setVisible(true);
        main.setSelectedComponent(lobby);
        connection.send(Message.updateLobby());
    }
    
    public void login(String username){
        connection.send(Message.login(username));
        Message message = connection.receive();
        switch (message.label) {
            case SUCCESS:
                this.username = username;
                new Thread(new ClientReceiver(connection, this)).start();
                signalLogin();
                break;
            default:
                new TextPopup((String)message.object);
        }
        
    }

    public void logout(){
        connection.send(Message.logout(username));      
    }

    public void order(ArrayList<Card> cards) {
        new CardOrderPopup(cards, this::orderReturn);
    }
    
    public void orderReturn(ArrayList<Card> cards){
        interactionConnection.send(Message.orderReturn(Card.toUUIDList(cards)));
    }
    
}



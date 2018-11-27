/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import network.Connection;
import network.Message;
import network.Receiver;
import game.ClientGame;
import game.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class ClientReceiver extends Receiver {
    Client client;
        
    public ClientReceiver(Connection connection, Client client)
    {
        super(connection);
        this.client = client;
    }
        
    @Override
    public void handleRequest(Message message){
        System.out.println(message);
        switch(message.getLabel()){
            case UPDATE_CHAT_LOG:
                client.chatLog = (ArrayList<String>)(message.getObject());
                client.updateChat();
                break;
            case UPDATE_TABLES:
                client.tables = (HashMap<UUID, Table>)message.getObject();
                client.updateTables();
                break;
            case UPDATE_PLAYERS:
                client.players = (ArrayList<String>)message.getObject();
                client.updatePlayers();
                break;
            case NEW_GAME:
                ClientGame game = (ClientGame)message.getObject();
                client.newGame(game);
                break;
            default:
                System.out.println("Unhandled label: "+ message.toString());
        }
    }
}

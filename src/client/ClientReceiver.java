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
import helpers.Hashmap;
import java.util.ArrayList;
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
        switch(message.label){
            case UPDATE_CHAT_LOG:
                client.chatLog = (ArrayList<String>)(message.object);
                client.updateChat();
                break;
            case UPDATE_TABLES:
                client.tables = (Hashmap<UUID, Table>)message.object;
                client.updateTables();
                break;
            case UPDATE_PLAYERS:
                client.players = (ArrayList<String>)message.object;
                client.updatePlayers();
                break;
            case NEW_GAME:
                ClientGame game = (ClientGame)message.object;
                client.newGame(game);
                break;
            default:
                System.out.println("Unhandled label: "+ message.toString());
        }
    }
}

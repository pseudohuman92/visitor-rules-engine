package network;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import game.Table;
import game.Deck;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class Connection {
    protected Socket socket;
    protected ObjectInputStream reader;
    protected ObjectOutputStream writer;
    
    
    public void openConnection(String hostname, int port){
        try {
            socket = new Socket(hostname, port);
            writer = new ObjectOutputStream(socket.getOutputStream());          
            reader = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Open I/O error: " + ex.getMessage());
        }
    }
    
    public void openConnection(Socket socket){
        try {
            this.socket = socket; 
            writer = new ObjectOutputStream(this.socket.getOutputStream());
            reader = new ObjectInputStream(this.socket.getInputStream());
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Open I/O error: " + ex.getMessage());
        }
    }

    public void closeConnection(){
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Close I/O error: " + ex.getMessage());
        }
    }
    
    public void send(Message message)
    {
        try {
            writer.reset();
            writer.writeObject(message);
        } catch (IOException ex) {
            System.out.println("Message send error: " + ex.getMessage());
        }

    }
        
    public Message receive()
    {
        try {
            Message message = (Message)reader.readObject();
            return message;
        } catch (IOException ex) {
            System.out.println("Message recieve error: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Message recieve typecast error: " + ex.getMessage());
        }
        return null;
    }	


}

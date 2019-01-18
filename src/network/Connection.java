package network;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author pseudo
 */
public class Connection {

    private Socket socket;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    
    /**
     *
     * @param hostname
     * @param port
     */
    public void openConnection(String hostname, int port){
        try {
            socket = new Socket(hostname, port);
            writer = new ObjectOutputStream(socket.getOutputStream());          
            reader = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        }
    }
    
    /**
     *
     * @param socket
     */
    public void openConnection(Socket socket){
        try {
            this.socket = socket; 
            writer = new ObjectOutputStream(this.socket.getOutputStream());
            reader = new ObjectInputStream(this.socket.getInputStream());
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        }
    }

    /**
     *
     */
    public void closeConnection(){
        try {
            socket.close();
        } catch (IOException ex) {
        }
    }
    
    /**
     *
     * @param message
     */
    public void send(Message message)
    {
        System.out.println("SEND: " + message);
        try {
            writer.reset();
            writer.writeObject(Message.convertToJson(message));
        } catch (IOException ex) {
        }

    }
        
    /**
     *
     * @return
     */
    public Message receive()
    {
        try {
            Message m = (Message)reader.readObject();
            System.out.println("RECEIVE: " + m);
            return m;
        } catch (IOException | ClassNotFoundException ex) {
        }
        return null;
    }	


}

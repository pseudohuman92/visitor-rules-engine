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

    /**
     *
     */
    protected Socket socket;

    /**
     *
     */
    protected ObjectInputStream reader;

    /**
     *
     */
    protected ObjectOutputStream writer;
    
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
        System.out.println("SEND: " + Message.convertToJson(message));
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
            String m = (String)reader.readObject();
            System.out.println("RECEIVE: " + m);
            System.out.println("RECEIVE: " + Message.convertToObject(m));
            return Message.convertToObject(m);
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }
        return null;
    }	


}

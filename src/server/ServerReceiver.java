package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import network.Receiver;
import network.Message;
import game.Deck;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class ServerReceiver extends Receiver {

        Server server;
        
    /**
     *
     * @param socket
     * @param server
     */
    public ServerReceiver(Socket socket, Server server)
	{
            super(socket);
            this.server = server;
	}
        
    /**
     *
     * @param message
     */
    @Override
	public void handleRequest(Message message){
            System.out.println(message);
            switch(message.label){
                case REGISTER:
                    String username = (String)message.object;
                    if (registerUser(username)){
                        connection.send(Message.success());
                    } else {
                        connection.send(Message.fail("Username already exists."));
                    }
                    break;
                case LOGIN:
                    username = (String)message.object;
                    if (server.isLoggedIn(username)) {
                        connection.send(Message.fail(username + " is already logged in."));
                    } else if (true) { //comment in to enable registering if (userRegistered(username)){
                        server.addLogin(username, connection);
                        connection.send(Message.success());
                    } else {
                        connection.send(Message.fail(username + " is not registered."));
                    }
                    server.updatePlayers();
                    break;
                case LOGOUT:
                    username = (String)message.object;
                    server.removeLogin(username);
                    server.updatePlayers();
                    server.updateTables();
                    break;
                case CHAT_MESSAGE:
                    server.appendToChatLog((String)message.object);
                    server.updateChatLogs();
                    break;
                case CREATE_TABLE:
                    Serializable[] data = (Serializable[])message.object;
                    server.createTable((String)data[0], (Deck)data[1]);
                    server.updateTables();
                    break;
                case JOIN_TABLE:
                    data = (Serializable[])message.object;
                    server.joinTable((String)data[0], (Deck)data[1], (UUID)data[2]);
                    server.updateTables();
                    break;
                case UPDATE_LOBBY:
                    server.updateTables();
                    server.updateChatLogs();
                    server.updatePlayers();
                    break;
                default:
                    System.out.println("Unhandled label: "+ message.toString());
            }
        }
        
    /**
     *
     * @param username
     * @return
     */
    public boolean userRegistered(String username){
            try {
                File userfile = new File("./users.txt");
                if (!userfile.exists()) { userfile.createNewFile(); }
                Scanner scanner = new Scanner(userfile);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (username.equals(line)){
                        scanner.close();
                        return true;
                    }
                }
                scanner.close();
                return false;
            } catch (IOException e) {
                System.out.println("Server user file I/O error: " + e.getMessage());
            }
            return false;
        }
        
    /**
     *
     * @param username
     * @return
     */
    public boolean registerUser(String username) {
            if (!userRegistered(username)){
                try {
                    File userfile = new File("./users.txt");
                    if (!userfile.exists()) { userfile.createNewFile(); }
                    BufferedWriter out = new BufferedWriter(new FileWriter(userfile, true));
                    out.write(username);
                    out.newLine();
                    out.flush();
                    out.close();
                    return true;
                } catch (FileNotFoundException ex) {
                    System.out.println("Server user file I/O error: " + ex.getMessage());
                } catch (IOException ex) {
                    System.out.println("Server user file I/O error: " + ex.getMessage());
                }
            }
            return false;
        }
}
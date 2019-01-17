package server;

import enums.Knowledge;
import helpers.Hashmap;
import java.net.Socket;
import network.Receiver;
import network.Message;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author pseudo
 */
public class ServerGameReceiver extends Receiver {

        Server server;
        
    /**
     *
     * @param socket
     * @param server
     */
    public ServerGameReceiver(Socket socket, Server server)
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
            Serializable[] data;
            switch(message.label){
                case REGISTER_GAME_CONNECTION:
                    data = (Serializable[])message.object;
                    server.addGameConnection((UUID)data[0], (String)data[1], connection);
                    break;
                case REGISTER_INTERACTION_CONNECTION:
                    data = (Serializable[])message.object;
                    server.addInteractionConnection((UUID)data[0], (String)data[1], connection);
                    stop = true;
                    break;
                case MULLIGAN:
                    data = (Serializable[])message.object;
                    server.mulligan((UUID)data[0], (String)data[1]);
                    server.updateGame((UUID)data[0]);
                    break;
                case KEEP:
                    data = (Serializable[])message.object;
                    server.keep((UUID)data[0], (String)data[1]);
                    server.updateGame((UUID)data[0]);
                    break;
                case PLAY:
                    data = (Serializable[])message.object;
                    server.play((UUID)data[0], (String)data[1], (UUID)data[2], (ArrayList<Serializable>)data[3]);
                    server.updateGame((UUID)data[0]);
                    break;
                case STUDY:
                    data = (Serializable[])message.object;
                    server.study((UUID)data[0], (String)data[1], (UUID)data[2], (Hashmap<Knowledge, Integer>)data[3]);
                    server.updateGame((UUID)data[0]);
                    break;
                case ACTIVATE:
                    data = (Serializable[])message.object;
                    server.activate((UUID)data[0], (String)data[1], (UUID)data[2], (ArrayList<Serializable>)data[3]);
                    server.updateGame((UUID)data[0]);
                    break;
                case PASS:
                    server.skipInitiative((UUID)message.object);
                    //server.updateGame((UUID)message.object);
                    break;
                case CONCEDE:
                    data = (Serializable[])message.object;
                    server.concede((UUID)data[0], (String)data[1]);
                    server.updateTables();
                    break;
                default:
                    System.out.println("Unhandled label: "+ message.toString());
            }
        }
}
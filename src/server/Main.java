package server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import server.Receiver;
import server.Server;
import server.GameEndpoint;

/**
 *
 * @author pseudo
 */
public class Main {

    static Server server;
    /**
     *
     * @param args
     */
    public static void main(String args[]) {
            System.out.println("Server starting...");
            server = new Server();
            server.port = 8_080;
            server.gamePort = 8_081;
            try {
                    server.serverSocket = new ServerSocket(server.port);
                    server.serverGameSocket = new ServerSocket(server.gamePort);
            } catch (IOException e) {
                throw new RuntimeException("Error accepting client connection", e);
            }

            new Thread(() -> {
            while(true){
                try {
                    Socket clientSocket = server.serverSocket.accept();
                    new Thread(new Receiver(clientSocket, server)).start();
                } catch (IOException e) {
                    throw new RuntimeException("Error accepting client connection", e);
                }
            }
            }).start();

            new Thread(() -> {
            while(true){
                try {
                    Socket clientSocket = server.serverGameSocket.accept();
                    new Thread(new GameEndpoint(clientSocket, server)).start();
                } catch (IOException e) {
                    throw new RuntimeException("Error accepting client connection", e);
                }
            }
            }).start();
            System.out.println("Server started.");
    }
}

package com.ccg.ancientaliens.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import javax.websocket.DeploymentException;


/**
 *
 * @author pseudo
 */
public class Main {

    static GameServer gameServer;
    /**
     *
     * @param args
     * @throws javax.websocket.DeploymentException
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    /*
    public static void main(String[] args) throws DeploymentException, IOException, InterruptedException {
        System.out.println("Server starting...");
        gameServer = new GameServer();
        
        Server server = new Server("localhost", 8080, "", null, GeneralEndpoint.class);
        server.start();
        System.out.print("---- Server Started -----");
        new CountDownLatch(1).await();
    }
*/
}

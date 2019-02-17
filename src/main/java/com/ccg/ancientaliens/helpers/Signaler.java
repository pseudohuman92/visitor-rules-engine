/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.helpers;

import static java.lang.Thread.sleep;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author pseudo
 */
public class Signaler {
    
    private final ArrayBlockingQueue<Integer> q = new ArrayBlockingQueue<>(1);
    
    public void waitSignal() throws InterruptedException{
        q.take();
    }
    
    public void signal() {
        q.add(0);
    }
}

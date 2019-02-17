/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccg.ancientaliens.helpers;

import java.util.concurrent.Semaphore;

/**
 *
 * @author pseudo
 */
public class Signaler {
    
    public synchronized void waitSignal() throws InterruptedException{
        wait();
    }
    
    public synchronized void signal() {
        notify();
    }
    
    public synchronized void signalAll() {
        notifyAll();
    }
}

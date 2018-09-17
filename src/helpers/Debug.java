package helpers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pseudo
 */



public class Debug {
    static boolean debug = true;
    
    public static void println(String s){
        if (debug) {
            System.out.println(s);
        }
    }
}

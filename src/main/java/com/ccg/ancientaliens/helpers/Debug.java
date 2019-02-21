package com.ccg.ancientaliens.helpers;

import java.util.ArrayList;


/**
 *
 * @author pseudo
 */



public class Debug {
    static boolean debug = true;
    
    /**
     *
     * @param s
     */
    public static void println(String s){
        if (debug) {
        }
    }
    
    /**
     *
     * @param list
     * @param indentation
     * @return
     */
    public static String list(ArrayList<? extends Object> list, String indentation){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++){
            sb.append(indentation).append(list.get(i)).append("\n");
        }
        return sb.toString();
    }

    private Debug() {
    }
}

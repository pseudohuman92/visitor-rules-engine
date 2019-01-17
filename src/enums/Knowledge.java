/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enums;

/**
 *
 * @author pseudo
 */
public enum Knowledge {

    BLACK(0x23_3F63),
    BLUE(0x00_A6ED), 
    GREEN(0x7F_B800), 
    RED(0x7e_0202), 
    WHITE(0xE9_EBEF), 
    YELLOW(0x7e_7902),
    NONE(0);
    
    private final int id;
    Knowledge(int id) { this.id = id; }

    /**
     *
     * @return
     */
    public int getValue() { return id; }

    /**
     *
     * @return
     */
    public String toShortString(){
        switch(this){
            case BLACK:
                return "B";
            case BLUE:
                return "U";
            case GREEN:
                return "G";
            case RED:
                return "R";
            case WHITE:
                return "W";
            default:
                return "";
        }
    }
}

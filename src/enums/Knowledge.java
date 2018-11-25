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
    BLACK(0x233F63), 
    BLUE(0x00A6ED), 
    GREEN(0x7FB800), 
    RED(0x7e0202), 
    WHITE(0xE9EBEF), 
    YELLOW(0x7e7902),
    NONE(0);
    
    private final int id;
    Knowledge(int id) { this.id = id; }
    public int getValue() { return id; }
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

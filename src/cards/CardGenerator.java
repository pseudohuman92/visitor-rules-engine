/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;

import set1.actions.*;
import set1.items.*;
import set1.tomes.*;
import java.io.Serializable;

/**
 *
 * @author pseudo
 */
public class CardGenerator implements Serializable {
    String username;
    
    public CardGenerator (String username) {
        this.username = username;

    }
    
    public Card createCard(String name){
        switch (name) {
            case "Draw And Order":
                return new DrawAndOrder(username);
            case "Seek And Destroy": 
                return new SeekAndDestroy(username);
            case "Opponent Discard":    
                return new OpponentDiscard(username);
            case "Duplicating Cube":
                return new DuplicatingCube(username);
            case "Power Infuser":
                return new PowerInfuser(username);
            case "Nanobomb":
                return new Nanobomb(username);
            case "Emp Bomb":
                return new EmpBomb(username);
            case "Quick Charge":
                return new QuickCharge(username);
            case "Confiscate":
                return new Confiscate(username);
            case "Energy Drain":
                return new EnergyDrain(username);
            case "Spare Battery":
                return new SpareBattery(username);
            case "Mystical Teachings": 
                return new MysticalTeachings(username);
            case "Cling to Life":  
                return new ClingToLife(username);
            case "Tome BG":  
                return new TomeBG(username);
            case "Tome BR":  
                return new TomeBR(username);
            case "Tome BU":  
                return new TomeBU(username);
            case "Tome BW":  
                return new TomeBW(username);
            case "Tome GR":  
                return new TomeGR(username);
            case "Tome GU":  
                return new TomeGU(username);
            case "Tome GW":  
                return new TomeGW(username);
            case "Tome RU":  
                return new TomeRU(username);
            case "Tome RW":  
                return new TomeRW(username);
            case "Tome UW":  
                return new TomeUW(username);
            default:
                return null;
        }
    }
}

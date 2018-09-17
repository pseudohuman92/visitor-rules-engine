/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;

import set1.actions.*;
import set1.items.*;
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
            default:
                return null;
        }
    }
}

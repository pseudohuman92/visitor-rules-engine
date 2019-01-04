/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card.properties;

import client.Client;
import game.ClientGame;

/**
 *
 * @author pseudo
 */
public interface XValued {
    public abstract boolean isXValid(Client c, int x);
}

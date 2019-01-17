
package game;

import card.Card;
import enums.Phase;
import static enums.Phase.MAIN;
import java.io.Serializable;
import java.util.UUID;
import java.util.function.Function;

/**
 *
 * @author pseudo
 */
public class ClientGame implements Serializable {

    public Player player;
    public Opponent opponent;
    public String turnPlayer;
    public String activePlayer;
    public Phase phase;
    public UUID id;
    
    /**
     *
     * @param player
     * @param opponent
     * @param turnPlayer
     * @param activePlayer
     * @param phase
     * @param uuid
     */
    public ClientGame(Player player, Opponent opponent, String turnPlayer, 
            String activePlayer, Phase phase, UUID uuid){
        this.player = player;
        this.opponent = opponent;
        this.turnPlayer = turnPlayer;
        this.activePlayer = activePlayer;
        this.phase = phase;
        this.id = uuid;
    }
    
    /**
     *
     * @return
     */
    public boolean canStudy(){
        return player.name.equals(turnPlayer) 
            && phase == MAIN
            && player.numOfStudiesLeft > 0;
    }
    
    /**
     *
     * @return
     */
    public boolean hasInitiative(){
        return activePlayer.equals(player.name);
    }
    
    /* Called to check if count number of unique targets among the cards in play. */

    /**
     *
     * @param valid
     * @param count
     * @return
     */

    public boolean hasValidTargetsInPlay(Function<Card, Boolean> valid, int count){
        assert (count > 0);
        for (int i = 0; i < player.inPlayCards.size(); i++){
            if (valid.apply(player.inPlayCards.get(i)))
                count--;
            if (count == 0)
                return true;
        }
        
        for (int i = 0; i < opponent.cardsInPlay.size(); i++){
            if (valid.apply(opponent.cardsInPlay.get(i)))
                count--;
            if (count == 0)
                return true;
        }
        return false;
    }

    public boolean hasAnItem(String controller) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean hasACardInVoid(String controller) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

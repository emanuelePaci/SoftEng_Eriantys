package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.listeners.ModelListener;

/**
 * turn class
 */
public class Turn{
    private Player currentPlayer;
    private int remainingMovements;
    private boolean usedCharacter;
    private ModelListener modelListener=null;

    /**
     * Constructor
     * Sets the current player
     * @param player the player
     */
    public Turn(Player player){
        usedCharacter = false;
        currentPlayer = player;
    }

    /**
     * Gets current player
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets remaining moves
     * @return the remaining moves
     */
    public int getRemainingMovements() {
        return remainingMovements;
    }

    /**
     * Reset remaining moves
     * @param num the remaining moves
     */
    public void resetRemainingMovements(int num) {
        remainingMovements = num;
    }

    /**
     * Sets used character
     * @param usedCharacter the used character
     */
    public void setUsedCharacter(boolean usedCharacter) {
        this.usedCharacter = usedCharacter;
        if(usedCharacter)
            notifyView();
    }

    /**
     * Returns true if a character has been used
     * @return true if a character has been used
     */
    public boolean isUsedCharacter() {
        return usedCharacter;
    }

    /**
     * Sets the new current player
     * @param p the current player
     */
    public void updatePlayer(Player p){
        currentPlayer = p;
    }

    /**
     * Updates the remaining moves
     */
    public void updateRemainingMovements(){
        remainingMovements -= 1;
    }

    /** attached the modelListener to the turn
     * @param modelListener to be attached
     */
    public void attach(ModelListener modelListener){
        this.modelListener=modelListener;
        notifyView();
    }

    /**
     * notifies the view
     */
    public void notifyView() {
        if (modelListener != null)
            modelListener.update();
    }
}

package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.listeners.ModelListener;

import java.util.*;

/**
 * round class
 */
public class Round {
    private List<Player> playerSequence;
    private List<Player> playerSequenceAscend;
    private List<Player> nextSequence;
    private List<Integer> cloudChosen;
    private int numTurnDone;
    private Turn turn;
    private Boolean lastRound;
    private ModelListener modelListener = null;

    /**
     * Sets lastRound
     */
    public void setLastRound() {
        lastRound = true;
    }

    /**
     * Constructor
     * Initialize the turn, the player sequence, cloud chosen and num turns done
     * @param player the list of players
     */
    public Round(List<Player> player) {
        turn = new Turn(player.get(0));
        playerSequence = new LinkedList<>(player);
        nextSequence = new LinkedList<>(player);
        playerSequenceAscend = new LinkedList<>(player);
        cloudChosen = new ArrayList<>();
        numTurnDone = 0;
        lastRound = false;
    }

    /**
     * Gets the current turn
     * @return the current turn
     */
    public Turn getTurn(){
        return turn;
    }

    /**
     * Gets the elements inside the chosen cloud
     * @return the list of elements inside the chosen cloud
     */
    public List<Integer> getCloudChosen() {
        return cloudChosen;
    }

    /**
     * Sets the chosen cloud
     * @param cloudChosen the number of the cloud
     */
    public void setCloudChosen(int cloudChosen) {
        this.cloudChosen.add(cloudChosen);
    }

    /**
     * Gets last round
     * @return if this is the last round
     */
    public Boolean getLastRound() {
        return lastRound;
    }

    /**
     * Gets num turns done
     * @return the num turn done
     */
    public int getNumTurnDone() {
        return numTurnDone;
    }

    /**
     * next action turn
     * @return true if everything is okay
     */
    public boolean nextActionTurn(){
        int index = playerSequenceAscend.indexOf(turn.getCurrentPlayer());
        turn.getCurrentPlayer().changeState(PlayerState.WAIT);
        turn.setUsedCharacter(false);
        numTurnDone += 1;

        if (index == playerSequence.size() - 1)
            return false;
        else{
            Player p = playerSequenceAscend.get(index + 1);
            turn.updatePlayer(p);
            turn.resetRemainingMovements(playerSequenceAscend.size() + 1);
            p.changeState(PlayerState.ACTION);
            notifyView();
            return true;
        }
    }

    /**
     * ends the planning phase
     */
    public void endPlanningPhase(){
        int assistenteBasso = 11;
        playerSequenceAscend.clear();

        for (Player p : playerSequence){
            if (p.getLastUsed().getWeight() < assistenteBasso){
                nextSequence.set(0, p);
                assistenteBasso = p.getLastUsed().getWeight();
            }
        }

        for (int i = 1; i < playerSequence.size(); i++) {
            nextSequence.set(i, getNextPlayer(nextSequence.get(i - 1)));
        }

        playerSequenceAscend.add(nextSequence.get(0));
        playerSequenceAscend.add(nextSequence.get(1));

        if (playerSequence.size() == 3 && nextSequence.get(1).getLastUsed().getWeight() < nextSequence.get(2).getLastUsed().getWeight())
            playerSequenceAscend.add(nextSequence.get(2));
        else if (playerSequence.size() == 3) {
            playerSequenceAscend.add(1, nextSequence.get(2));
        }

        playerSequence.clear();
        playerSequence.addAll(nextSequence);
        playerSequence.get(0).changeState(PlayerState.ACTION);
        turn.updatePlayer(playerSequence.get(0));
        turn.resetRemainingMovements(playerSequence.size()+ 1);

        numTurnDone = 0;
        notifyView();
    }

    /**
     * next planning turn
     * @return true if everything is okay
     */
    public boolean nextPlanningTurn(){
        int index = playerSequence.indexOf(turn.getCurrentPlayer());
        turn.getCurrentPlayer().changeState(PlayerState.WAIT);

        if (index == playerSequence.size() - 1)
            return false;
        else{
            Player p = playerSequence.get(index + 1);
            turn.updatePlayer(p);
            p.changeState(PlayerState.PLANNING);
            numTurnDone += 1;
            notifyView();
            return true;
        }
    }

    /**
     * sets the parameters to end the round
     */
    public void endRound(){
        numTurnDone = 0;
        cloudChosen.clear();
        turn.updatePlayer(playerSequence.get(0));
        playerSequence.get(0).changeState(PlayerState.PLANNING);
        notifyView();
    }

    /**
     * gets the next player
     * @param p the current player
     * @return the next player
     */
    public Player getNextPlayer(Player p){
        if (playerSequence.indexOf(p) + 1 == playerSequence.size())
            return playerSequence.get(0);
        else
            return playerSequence.get(1 + playerSequence.indexOf(p));


    }

    /**
     * attach the modelListener to the round
     * @param modelListener the modelListeners
     */
    public void attach(ModelListener modelListener){this.modelListener=modelListener;}

    /**
     * notifies the view
     */
    public void notifyView() {
        if (modelListener != null)
            modelListener.update();
    }
}

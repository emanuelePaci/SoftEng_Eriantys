package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.listeners.ModelListener;

import java.util.*;

/**
 * Represents a player of a game.
 */
public class Player {
    private String nickname;
    private TowerColor towerColor;
    private Board board;
    private Deck deck;
    private PlayerState state;
    private Assistant lastUsed = null;
    private int numCoin;
    private ModelListener modelListener=null;

    /**
     * Constructor
     * IInstantiates this player initializing his chosen nickname and his chosen color of the tower, then sets its state in wait.
     * @param nickname the chosen nickname.
     * @param color the chosen color of the tower.
     */
    public Player(String nickname, TowerColor color){
        this.nickname = nickname;
        towerColor = color;
        state = PlayerState.WAIT;
    }

    /**
     * Initializes player's board, player's deck, lastUsed and, if the game is played in expert mode, the number of coins of this player.
     * @param student the list of students to fill the entrance of the player's board.
     * @param expert true only if the game is played in expert mode.
     * @param numPlayer the number of player in the game.
     */
    public void setUp(List<Student> student, boolean expert, int numPlayer)
    {
        board = new Board(student, numPlayer, towerColor);
        deck = new Deck();
        lastUsed = null;
        if(expert) numCoin=1;
    }

    /**
     * Gets the color of the tower chosen by this player.
     * @return the color of the tower chosen by this player.
     */
    public TowerColor getTowerColor() {
        return towerColor;
    }

    /**
     * Gets the current state of this player.
     * @return the current state of this player.
     */
    public PlayerState getState() {
        return state;
    }

    /**
     * Gets the board of this player.
     * @return the board of this player.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the deck of this player.
     * @return the deck of this player.
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Gets the nickname of this player.
     * @return the nickname of this player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Gets the last Assistant card used by this player.
     * @return the last Assistant card used by this player.
     */
    public Assistant getLastUsed() {
        return lastUsed;
    }

    /**
     * Gets the number of coins lefts by this player.
     * @return the number of coins lefts by this player.
     */
    public int getNumCoin() {
        return numCoin;
    }

    /**
     * Decreases the number of coins owned by this player by the specified number.
     * @param numCoin the number of coins to remove from player's coins.
     */
    public void removeCoin(int numCoin){
        this.numCoin -= numCoin;
    }

    /**
     * Adds one coin at this player.
     */
    public void addCoin() {
        numCoin += 1;
    }

    /**
     * Sets the state of this player with the specified state, then if the specified state is "ENDTURN" it notifies any change at the model's listeners.
     * @param state the state to set.
     */
    public void changeState(PlayerState state)
    {
        this.state=state;
        if(state==PlayerState.ENDTURN)
            notifyView();
    }

    /**
     * Sets lastUsed with the card from the deck at the specified position.
     * @param position the position of the card to remove from the deck.
     */
    public void addAssistant (int position)
    {
       lastUsed=deck.removeAssistant(position);
    }

    /**
     * Initializes the model's listener.
     * @param modelListener the listener that will be notified when a specified sequence occurs.
     */
    public void attach(ModelListener modelListener){this.modelListener=modelListener;}

    /**
     * Notifies view when a sequence of changes occurs.
     */
    public void notifyView() {
        if (modelListener != null)
            modelListener.update();
    }
}

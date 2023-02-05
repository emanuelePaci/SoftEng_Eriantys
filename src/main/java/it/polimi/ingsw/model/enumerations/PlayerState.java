package it.polimi.ingsw.model.enumerations;

import java.io.Serializable;

/**
 * Player state enum
 */
public enum PlayerState implements Serializable {
    /**
     * Planning
     */
    PLANNING("PLANNING"),
    /**
     * Action
     */
    ACTION("ACTION"),
    /**
     * Wait
     */
    WAIT("WAIT"),
    /**
     * End turn
     */
    ENDTURN("END TURN");

    private final String text;

    /**
     * Default constructor.
     *
     * @param text the string representation of the player state.
     */
    PlayerState(String text) {
        this.text = text;
    }

    /**
     * Returns the text of the player state.
     *
     * @return a String containing the text of the player state.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the text of the player state.
     * @return a String containing the text of the player state.
     */
    @Override
    public String toString() {
        return text;
    }
}
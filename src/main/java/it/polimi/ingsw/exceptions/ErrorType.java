package it.polimi.ingsw.exceptions;

import java.io.Serializable;

/**
 * Error type enum
 */
public enum ErrorType implements Serializable {
    /**
     * Already chosen cloud
     */
    ALREADY_CHOSEN_CLOUD("This cloud has already been chosen, please select another"),
    /**
     * Already used character
     */
    ALREADY_USED_CHARACTER("A character has already been used this turn"),
    /**
     * Max student reached
     */
    MAX_STUDENT_REACHED("Max student reached for the color: "),
    /**
     * No entry tiles set
     */
    NO_ENTRY_TILES_SET("You don't have any other No Entry Tile left or the chosen island has already one No Entry Tile on"),
    /**
     * Not enough money
     */
    NOT_ENOUGH_MONEY( "You don't have enough coins (price = "),
    /**
     * Not your turn
     */
    NOT_YOUR_TURN("Not your Turn, please wait"),
    /**
     * Same assistant
     */
    SAME_ASSISTANT("Choose a different Assistant from others player"),
    /**
     * Too much moves
     */
    TOO_MUCH_MOVES("You can't go so far with your Assistant, please choose another Island"),
    /**
     * Wrong action
     */
    WRONG_ACTION("You have already moved all the student, please move Mother Nature"),
    /**
     * Wrong action 2
     */
    WRONG_ACTION2("Before moving mother nature, please move all the students"),
    /**
     * Wrong phase
     */
    WRONG_PHASE("You must choose an Assistant"),
    /**
     * Wrong phase 2
     */
    WRONG_PHASE2("You must move students or mother nature"),
    /**
     * Wrong phase 3
     */
    WRONG_PHASE3("You have to choose a cloud"),
    /**
     * Server offline
     */
    SERVER_OFFLINE("Connection refused, server may be offline");
    private final String errorText;

    /**
     * Constructor
     * @param errorText the error text
     */
    ErrorType (String errorText){
        this.errorText = errorText;
    }

    /**
     * gets error text
     * @return the error text
     */
    public String getErrorText() {
        return errorText;
    }

    /**
     * To string method
     * @return the class to string
     */
    @Override
    public String toString() {
        return super.toString();
    }
}

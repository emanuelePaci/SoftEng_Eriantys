package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.model.enumerations.PawnColor;

import java.io.Serializable;

/**
 * Client exception class
 */
public class ClientException extends Exception implements Serializable {

    private ErrorType errorType;
    private PawnColor pawnColor = null;
    private int price = -1;

    /**
     * Constructor
     * @param errorType the error type
     */
    public ClientException(ErrorType errorType){
        this.errorType = errorType;
    }

    /**
     * Constructor
     * @param errorType the error type
     * @param pawnColor the pawn color
     */
    public ClientException(ErrorType errorType, PawnColor pawnColor){
        this.errorType = errorType;
        this.pawnColor = pawnColor;
    }

    /**
     * Constructor
     * @param errorType the error type
     * @param price the price
     */
    public ClientException(ErrorType errorType, int price){
        this.price = price;
        this.errorType = errorType;
    }

    /**
     * gets the error type
     * @return the error type
     */
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * gets the price
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * gets the pawn color
     * @return the pawn color
     */
    public PawnColor getPawnColor() {
        return pawnColor;
    }
}

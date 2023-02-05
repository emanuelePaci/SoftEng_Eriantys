package it.polimi.ingsw.model.enumerations;

import java.io.Serializable;

/**
 * Pawn color enum
 */
public enum PawnColor implements Serializable {
    /**
     * Green
     */
    GREEN(0, "GREEN"),
    /**
     * Red
     */
    RED(1, "RED"),
    /**
     * Yellow
     */
    YELLOW(2, "YELLOW"),
    /**
     * Pink
     */
    PINK(3, "PINK"),
    /**
     * Blue
     */
    BLUE(4, "BLUE");

    private final int index;
    private final String text;

    /**
     * Constructor
     * Sets the index and the text for the pawn color
     * @param index the index
     * @param text the text
     */
    PawnColor(int index, String text) {
        this.index = index;
        this.text = text;
    }

    /**
     * gets the pawn color with the given index
     * @param i the color index
     * @return the pawn color with the given index
     */
    public static PawnColor getColor(int i){
        for (PawnColor c : PawnColor.values())
            if(c.getIndex() == i)
                return c;
        return null;
    }

    /**
     * gets the pawn color with the given color
     * @param color the pawn color
     * @return the pawn color with the given index
     */
    public static PawnColor lookup(String color) {
        for (PawnColor p : PawnColor.values()) {
            if (p.toString().equalsIgnoreCase(color)) {
                return p;
            }
        }
        return null;
    }

    /**
     * gets the index
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * gets the text
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * gets the color text
     * @return the color text
     */
    @Override
    public String toString() {
        return text;
    }
}
package it.polimi.ingsw.model.pawns;

import it.polimi.ingsw.model.enumerations.PawnColor;

/**
 * student class
 */
public class Student {
    private PawnColor color;

    /**
     * Constructor
     * Initialize the student
     * @param i the student color
     */
    public Student(int i){
        color = PawnColor.getColor(i);
    }

    /**
     * gets the student color
     * @return the student color
     */
    public PawnColor getColor() {
        return color;
    }
}

package it.polimi.ingsw.model.pawns;

import it.polimi.ingsw.model.enumerations.PawnColor;

/**
 * Professor class
 */
public class Professor {
    private PawnColor color;
    private int numStudent;

    /**
     * Constructor
     * Initialize the professor
     * @param i the number of the color
     */
    public Professor(int i){
        color = PawnColor.getColor(i);
        numStudent = 0;
    }

    /**
     *  gets the professor color
     * @return the professor color
     */
    public PawnColor getColor() {
        return color;
    }

    /**
     * gets the number of students
     * @return the number of students
     */
    public int getNumStudent() {
        return numStudent;
    }

    /**
     * sets the number of students
     * @param numStudent the number of students to be set
     */
    public void setNumStudent(int numStudent) {
        this.numStudent = numStudent;
    }
}

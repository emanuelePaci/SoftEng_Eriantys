package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Student;

import java.util.List;

/**
 * Board class
 */
public class Board {
    private final Entrance entrance;
    private final DiningRoom diningRoom;
    private final ProfessorTable professorTable;
    private final TowerCourt towercourt;

    /**
     *  Constructor
     *  Initialize the entrance, the dining room and the professor table
     *  Sets the number of towers depending of the number of players
     * @param student the list of students
     * @param numPlayer the num of players
     * @param towerColor the tower color
     */
    public Board(List<Student> student, int numPlayer, TowerColor towerColor){
        entrance = new Entrance(student);
        diningRoom = new DiningRoom();
        professorTable = new ProfessorTable();
        if (numPlayer == 2)
            towercourt = new TowerCourt(8, towerColor);
        else
            towercourt = new TowerCourt(6, towerColor);
    }

    /**
     * Gets the entrance
     * @return the entrance
     */
    public Entrance getEntrance() {return entrance;}

    /**
     * Gets the dining room
     * @return the dining room
     */
    public DiningRoom getDiningRoom() {
        return diningRoom;
    }

    /**
     * Gets the professor table
     * @return the professor table
     */
    public ProfessorTable getProfessorTable() {
        return professorTable;
    }

    /**
     * Gets the tower court
     * @return the tower court
     */
    public TowerCourt getTowerCourt() {
        return towercourt;
    }
}

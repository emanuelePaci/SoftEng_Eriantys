package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Student;

import java.util.*;

/**
 *The cloud containing the students to choose at the end of player's turn.
 */
public class Cloud {
    private List<Student> cloudStudent;

    /**
     * Constructor.
     * Instantiates this cloud initializing its list of the students.
     */
    public Cloud(){
        cloudStudent = new LinkedList<>();
    }

    /**
     * Gets the list of the students present on this cloud.
     * @return the list of the students on this cloud.
     */
    public List<Student> getCloudStudent() {
        return cloudStudent;
    }

    /**
     * Appends all the students of the specified list to cloudStudent.
     * @param toAdd the list of students to add.
     */
    public void addStudent(List<Student> toAdd)
    {
            cloudStudent.addAll(toAdd);
    }

    /**
     * Counts the number of students of each color present on this cloud.
     * @return the array, ordered by PawnColor's index, where each cell contains the number of students of each color present on this cloud.
     */
    public Integer[] countAll(){
        Integer[] colorsCount = new Integer[5];

        for (PawnColor p : PawnColor.values())
            colorsCount[p.getIndex()] = countStudent(p);

        return colorsCount;
    }

    /**
     * Counts the number of students of the specified color present on this cloud.
     * @param c the color of the students to count.
     * @return the number of students of the specified color present on this cloud.
     */
    public int countStudent(PawnColor c){
        int count = 0;
        for(Student s : cloudStudent){
            if(s.getColor() == c){
                count++;
            }
        }
        return count;
    }
    /**
     * Removes and returns all the students present on this cloud.
     * @return the list containing all the students present on this cloud.
     */
    public List<Student> removeAllStudent()
    {
        List<Student> removed = new LinkedList<>(cloudStudent);
        cloudStudent.clear();
        return removed;
    }

}

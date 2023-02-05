package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Student;

import java.util.*;

/**
 * entrance class
 */
public class Entrance {
    private List<Student> student;

    /**
     * Constructor
     * Initialize the students list
     * @param stud the list of students
     */
    public Entrance(List<Student> stud) {
        this.student = stud;
    }

    /**
     * Gets the students list
     * @return the students list
     */
    public List<Student> getStudent() {
        return student;
    }

    /**
     * Removes a student of the indicated color
     * @param c the color of the student to be removed
     * @return the student of the given color
     */
    public Student removeStudent(PawnColor c){
        Student temp;
        temp = find(c);
        student.remove(temp);
        return temp;
    }

    /**
     * adds a list of students to the list student
     * @param s the list of students
     */
    public void addStudent(List<Student> s){
        student.addAll(s);
    }

    /**
     * adds a student to the list student
     * @param s the student to be added
     */
    public void addStudent(Student s){
        student.add(s);
    }

    /**
     * returns a student of the indicated color
     * @param color the student color to be found
     * @return the student of the requested color
     */
    public Student find (PawnColor color){
        for(Student s : student)
            if(s.getColor() == color) return s;
        return null;
    }

    /**
     * returns a list with the count of all the students
     * @return a list with the count of all the students
     */
    public Integer[] countAll() {
        int count;
        Integer[] colorsCount = new Integer[5];

        for (PawnColor p : PawnColor.values()) {
            count = 0;
            for (Student s : student) {
                if (s.getColor().equals(p))
                    count++;
            }

            colorsCount[p.getIndex()] = count;
        }
        return colorsCount;
    }
}

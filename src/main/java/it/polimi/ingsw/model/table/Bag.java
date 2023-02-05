package it.polimi.ingsw.model.table;

import it.polimi.ingsw.exceptions.BagIsEmptyException;
import it.polimi.ingsw.model.pawns.Student;

import java.util.*;

/**
 * The bag containing the students to extract.
 */
public class Bag {

    private List<Student> student;

    /**
     * Constructor.
     * Instantiates this bag initializing its list of the students and adding 24 students for each color, then it shuffles them.
     */
    public Bag()
    {
        student= new LinkedList<>();
        for(int i=0;i<5;i++)
            for(int j=0;j<24;j++)
                student.add(new Student(i));

        Collections.shuffle(student);
    }


    /**
     * Removes the specified quantity of students from this bag, at the beginning of the game during the setup.
     * @param quantity the number of students to remove from this bag.
     * @return the list of students removed.
     */
    public List<Student> initialSetup(int quantity){
        List<Student> draw = new LinkedList<>(student.subList(0, quantity));
        student.subList(0, quantity).clear();

        return draw;
    }

    /**
     * Adds all students at this bag, then it shuffles all the students in this bag.
     * @param student the list of students to add.
     */
    public void addStudent(List<Student> student)
    {
        this.student.addAll(student);
        Collections.shuffle(this.student);
    }

    /**
     * Removes and returns the specified number of students from this bag.
     * @param numStudents the number of students to remove.
     * @return the list of students removed from this bag.
     * @throws BagIsEmptyException if the number of students to remove is higher than the number of students available in the bag.

     */
    public List<Student> withdrawStudent(int numStudents) throws BagIsEmptyException {
        List<Student> drawOut;
        try {
            drawOut = new LinkedList<>(student.subList(0, numStudents));
        }
        catch (IndexOutOfBoundsException e){
            student.clear();
            throw new BagIsEmptyException();
        }
        student.subList(0, numStudents).clear();
        return drawOut;
    }

    /**
     * Returns true if this bag contains no elements.
     * @return true if this bag contains no elements.
     */
    public boolean isEmpty()
    {
        return student.isEmpty();
    }

    /**
     * Gets the list of the students present in this bag.
     * @return the list of the students present on this bag.
     */
    public List<Student> getStudent() {
        return student;
    }
}

package it.polimi.ingsw.model.board;

import it.polimi.ingsw.listeners.ModelListener;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Student;

import java.util.LinkedList;
import java.util.List;

/**
 * Diningroom class
 */
public class DiningRoom {
    private LinkedList<Student> student;
    private ModelListener modelListener;
    private boolean listen;

    /**
     * Constructor
     * Initialize the student list
     */
    public DiningRoom() {
        student = new LinkedList<>();
        listen = false;
    }

    /**
     * gets the students list
     * @return the students list
     */
    public LinkedList<Student> getStudent() {
        return student;
    }

    /**
     * Removes numstudent of the color indicated from the list
     * @param color the color of the student to be removed
     * @param numStudent the number of students to be removed
     * @return the removed students
     */
    public List<Student> removeStudent(PawnColor color, int numStudent){
        List<Student> list = new LinkedList<>();
        Student temp;
        for(int i = 0; i < numStudent; i++){
            temp = find(color);
            if(temp != null)
                list.add(temp);
        }
        student.removeAll(list);
        return list;
    }

    /**
     * adds a student to the list
     * @param s the new student
     */
    public void addStudent(Student s){
        student.addLast(s);
        notifyView();
    }

    /**
     * adds a list of students to the list
     * @param s the list of students
     */
    public void addStudent(List<Student> s){
        student.addAll(s);
    }

    /**
     * returns true if the student list is empty
     * @return true if the student list is empty
     */
    public boolean isEmpty(){
        return student.size() == 0;
    }

    /**
     * returns a student of the indicated color
     * @param color the color of the student to be found
     * @return the student found
     */
    public Student find(PawnColor color){
        Student temp;
        for(Student s : student)
            if(s.getColor() == color){
                temp = s;
                student.remove(s);
                return temp;
            }
        return null;
    }

    /**
     * counts the students of the indicated color
     * @param c the color
     * @return the number of student of that color
     */
    public int count(PawnColor c){
        int count = 0;
        for(Student s : student){
            if(s.getColor() == c){
                count++;
            }
        }
        return count;
    }

    /**
     * returns a list with the count of all the students
     * @return list with the count of all the students
     */
    public Integer[] countAll() {
        Integer[] colorsCount = new Integer[5];

        for (PawnColor p : PawnColor.values())
            colorsCount[p.getIndex()] = count(p);

        return colorsCount;
    }

    /**
     * attach the model listener
     * @param modelListener the model listener to be attached
     */
    public void attach(ModelListener modelListener){
        this.modelListener=modelListener;
        listen = true;
    }

    /**
     * notify thw view
     */
    public void notifyView() {
        if (listen)
            modelListener.update();
    }

    /**
     * detach sets the listen to false
     */
    public void detach(){
        listen=false;
    }

    /**
     * reattach sets the listen to true
     */
    public void reattach(){listen = true;}
}

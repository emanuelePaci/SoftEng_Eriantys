package it.polimi.ingsw.model.character;

import it.polimi.ingsw.controller.BoardHandler;
import it.polimi.ingsw.controller.Context;
import it.polimi.ingsw.exceptions.BagIsEmptyException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Bag;
import it.polimi.ingsw.model.table.Island;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;



/**
 * Character group student
 * MONK
 * SPOILED_PRINCESS
 * REPLACE
 */
public class CharacterGroupStudent extends Character{
    private Bag bag;
    private List<Student> student;

    private Method checkProfessor;

    /**
     * Constructor
     * @param type the character type
     * @param bag the bag
     * @param checkProfessor the check professor method
     */
    public CharacterGroupStudent (CharacterType type, Bag bag, Method checkProfessor) {
        super(type);
        this.bag=bag;
        student=new LinkedList<>();
        student.addAll(bag.initialSetup(type.hasStudent()));
        this.checkProfessor = checkProfessor;
    }

    /**
     * Removes and returns student from the list
     * @param color the student color
     * @return the student of the given color
     */
    public Student removeStudent(PawnColor color){
        Student s = findStudent(color);
        student.remove(s);
        return s;
    }

    /**
     * Returns student from the list
     * @param color the student color
     * @return the student of the given color
     */
    public Student findStudent(PawnColor color){
        Student stud = null;

        for (Student s : student)
            if (s.getColor().equals(color)) {
                stud = s;
                break;
            }

        return stud;
    }

    /**
     * Adds the student s to the list
     * @param s the student to be added
     */
    @Override
    public void addStudent(Student s) {
        student.add(0, s);
    }

    /**
     * Activate character
     * @param game the current game
     * @param player the current player
     * @param color the pawn color
     * @param context the context
     * @param boardHandler the board handler
     * @throws BagIsEmptyException if the are no students in the bag
     */
    @Override
    public void activateCharacter(Game game, Player player, PawnColor color, Context context, BoardHandler boardHandler) throws BagIsEmptyException {
        Object[] objects = new Object[2];
        objects[0] = player;
        objects[1] = color;
        try {
            checkProfessor.invoke(boardHandler, objects);
        } catch (InvocationTargetException | IllegalAccessException ignored) {}

        player.getBoard().getDiningRoom().detach();
        player.getBoard().getDiningRoom().addStudent(removeStudent(color)); //add_student_dining
        player.getBoard().getDiningRoom().reattach();
        student.addAll(bag.withdrawStudent(1));
    }

    /**
     * Activate character
     * @param island the current island
     * @param color the pawn color
     * @throws BagIsEmptyException if there are no students in the bag
     */
    @Override
    public void activateCharacter(Island island, PawnColor color) throws BagIsEmptyException {
        island.addStudent(removeStudent(color)); //add_student_islands

        student.addAll(bag.withdrawStudent(1));
    }

    /**
     * Activate character
     * @param player the current player
     * @param color the list of pawn color
     * @param boardHandler the board handler
     */
    @Override
    public void activateCharacter(Player player, PawnColor[] color, BoardHandler boardHandler) { // replace
        List<Student> list1 = new LinkedList<>();
        List<Student> list2 = new LinkedList<>();
        for(int i = 0; i < 3; i++){
            if(color[i] != null){
                list1.add(player.getBoard().getEntrance().removeStudent(color[i]));
            }
            if(color[i + 3] != null){
                list2.add(removeStudent(color[i+3]));
            }
        }
        player.getBoard().getEntrance().addStudent(list2);
        student.addAll(list1);

    }

    /**
     * Counts the student of the given color
     * @param pawnColor the pawn color
     * @return the number of student of the given color
     */
    @Override
    public int count(PawnColor pawnColor){
        int count = 0;
        for(Student s: student)
            if(s.getColor().equals(pawnColor))
                count++;

        return count;
    }
}

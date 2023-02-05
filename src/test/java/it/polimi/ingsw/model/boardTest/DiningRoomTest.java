package it.polimi.ingsw.model.boardTest;

import it.polimi.ingsw.model.board.DiningRoom;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DiningRoomTest{
    private DiningRoom diningRoom;
    @BeforeEach
    public void setUp(){
        diningRoom = new DiningRoom();
    }
    @AfterEach
    public void tearDown(){
        diningRoom = null;
    }

    @Test
    void removeStudent() {
        List<Student> list = new LinkedList<>();
        list.add(new Student(4));
        list.add(new Student(4));
        diningRoom.addStudent(list);
        List<Student> temp = diningRoom.removeStudent(PawnColor.BLUE, 2);
        assertEquals(temp, list);
        assertEquals(diningRoom.getStudent().size(), 0);
    }
    @Test
    void addStudent() {
        Student s = new Student(0);
        diningRoom.addStudent(s);
        assertEquals(s, diningRoom.getStudent().get(0));
        diningRoom.removeStudent(PawnColor.GREEN, 1);
        List<Student> list = new LinkedList<>();
        list.add(new Student(0));
        list.add(new Student(0));
        diningRoom.addStudent(list);
        assertEquals(list, diningRoom.getStudent());
    }

    @Test
    void isEmpty() {
        assertTrue(diningRoom.isEmpty());
        diningRoom.addStudent(new Student(0));
        assertFalse(diningRoom.isEmpty());

    }

    @Test
    void find() {
        assertNull(diningRoom.find(PawnColor.BLUE));
        Student s = new Student(4);
        diningRoom.addStudent(s);

        assertEquals(s, diningRoom.find(PawnColor.BLUE));
    }

    @Test
    void count(){
        assertEquals(0, diningRoom.count(PawnColor.BLUE));
        diningRoom.addStudent(new Student(4));
        diningRoom.addStudent(new Student(4));
        diningRoom.addStudent(new Student(0));
        diningRoom.addStudent(new Student(1));
        diningRoom.addStudent(new Student(3));
        diningRoom.addStudent(new Student(4));
        int[] counting = {1,1,0,1,3};
        for (int i = 0; i < 5; i++)
            assertEquals(counting[i], diningRoom.countAll()[i]);
    }

}

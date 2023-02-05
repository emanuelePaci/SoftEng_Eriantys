package it.polimi.ingsw.model.boardTest;

import it.polimi.ingsw.model.board.Entrance;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EntranceTest{
        LinkedList<Student> stud;
        Entrance entrance;

        @BeforeEach
        public void setUp(){
                stud = new LinkedList<>();
                entrance = new Entrance(stud);
        }
        @AfterEach
        public void tearDown(){
                stud = null;
                entrance = null;
        }

        @Test
        void removeStudent() {
                Student temp = new Student(4);
                entrance.addStudent(temp);
                assertEquals(temp, entrance.removeStudent(PawnColor.BLUE));
        }

        @Test
        void addStudent() {
                Student s = new Student(0);
                entrance.addStudent(s);
                assertEquals(s, entrance.getStudent().get(0));
                entrance.removeStudent(PawnColor.GREEN);
                List<Student> list = new LinkedList<>();
                list.add(new Student(4));
                list.add(new Student(4));
                entrance.addStudent(list);
                assertEquals(list, entrance.getStudent());
        }

        @Test
        void find() {
                assertNull(entrance.find(PawnColor.GREEN));
                Student temp = new Student(4);
                entrance.addStudent(temp);
                assertEquals(temp, entrance.find(PawnColor.BLUE));
        }

        @Test
        void countAll(){
                entrance.addStudent(new Student(4));
                entrance.addStudent(new Student(4));
                entrance.addStudent(new Student(0));
                entrance.addStudent(new Student(1));
                entrance.addStudent(new Student(3));
                entrance.addStudent(new Student(4));
                int[] counting = {1,1,0,1,3};
                for (int i = 0; i < 5; i++)
                        assertEquals(counting[i], entrance.countAll()[i]);
        }


}

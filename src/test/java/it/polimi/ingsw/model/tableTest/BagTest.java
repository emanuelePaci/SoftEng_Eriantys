package it.polimi.ingsw.model.tableTest;

import it.polimi.ingsw.exceptions.BagIsEmptyException;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.table.Bag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class BagTest {
    private Bag bag;
    @BeforeEach
    void setUp(){
        bag= new Bag();
    }
    @AfterEach
    void tearDown(){
        bag=null;
    }

    @Test
    void withdrawStudent() throws BagIsEmptyException {
        int[] color = new int[5];
        List<Student> test = new LinkedList<>(bag.withdrawStudent(120));
        assertEquals(bag.getStudent().size(), 0);
        assertTrue(bag.isEmpty());
        bag.addStudent(test.subList(0,120));
        assertEquals(bag.getStudent().size(), 120);
        for(Student s: bag.getStudent()) {
            color[s.getColor().getIndex()]+=1;
        }
        for (int i=0; i<5; i++){
            assertEquals(color[i], 24);
        }
        bag.withdrawStudent(120);
        assertThrows(BagIsEmptyException.class, ()-> bag.withdrawStudent(1));
    }
}

package it.polimi.ingsw.model.tableTest;

import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.table.Cloud;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {
    private Cloud cloud;

    @BeforeEach
    public void setUp(){
        cloud = new Cloud();

    }
    @AfterEach
    void tearDown(){
        cloud=null;
    }

    @Test
    void addStudent() {
        List<Student> test= new LinkedList<>();
        test.add(new Student(1));
        test.add(new Student(1));
        test.add(new Student(0));

        cloud.addStudent(test);
        assertEquals(cloud.getCloudStudent().get(0).getColor(), PawnColor.RED);
        assertEquals(cloud.getCloudStudent().get(1).getColor(), PawnColor.RED);
        assertEquals(cloud.getCloudStudent().get(2).getColor(), PawnColor.GREEN);
    }

    @Test
    void removeAllStudent() {
        List<Student> test= new LinkedList<>();
        test.add(new Student(1));
        test.add(new Student(1));
        test.add(new Student(0));

        cloud.addStudent(test);
        assertEquals(cloud.removeAllStudent(), test);
    }

    @Test
    void countAll(){
        List<Student> test= new LinkedList<>();
        test.add(new Student(1));
        test.add(new Student(1));
        test.add(new Student(0));
        test.add(new Student(2));
        test.add(new Student(2));
        test.add(new Student(2));
        test.add(new Student(2));
        test.add(new Student(4));
        test.add(new Student(4));
        cloud.addStudent(test);
        int[] counting = {1,2,4,0,2};
        for (int i = 0; i < 5; i++){
            assertEquals(counting[i], cloud.countAll()[i]);
        }
    }
}

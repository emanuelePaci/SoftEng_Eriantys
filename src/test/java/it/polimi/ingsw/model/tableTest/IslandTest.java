package it.polimi.ingsw.model.tableTest;

import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.pawns.Tower;
import it.polimi.ingsw.model.table.Island;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    private Island island;

    @BeforeEach
    void setUp() {
       island= new Island();
    }
    @AfterEach
    void tearDown(){
        island=null;
    }

    @Test
    void setWeight() {
        int weight = 3;
        island.setWeight(3);
        assertEquals(island.getWeight(), weight);
    }

    @Test
    void setNoEntryTiles() {
        boolean noEntryTiles=true;
        island.setNoEntryTiles(true);
        assertEquals(island.isNoEntryTiles(), noEntryTiles);
    }


    @Test
    void setMotherNature() {
        boolean motherNature=true;
        island.setMotherNature(true);
        assertEquals(island.isMotherNature(), motherNature);
    }

    @Test
    void countStudent() {
        island.addStudent(new Student(0));
        assertEquals(island.getIslandStudent().get(0).getColor().getText(),PawnColor.GREEN.toString());
        assertEquals(0, island.countStudent(PawnColor.YELLOW), "0 yellow students");
        assertNotEquals(1, island.countStudent(PawnColor.RED), "0 red students");
        island.addStudent(new Student(0));
        assertEquals(2, island.countStudent(PawnColor.GREEN), "2 green students");
        island.addStudent(new Student(0));
        island.addStudent(new Student(2));
        assertEquals(1, island.countStudent(PawnColor.YELLOW), "1 yellow students");

    }

    @Test
    void addStudent() {
        island.addStudent(new Student(1));
        assertEquals(island.getIslandStudent().get(0).getColor(), PawnColor.RED);
    }

    @Test
    void addTower() {
        List<Tower> test= new LinkedList<>();
        test.add(new Tower(TowerColor.BLACK));
        test.add(new Tower(TowerColor.BLACK));
        test.add(new Tower(TowerColor.WHITE));
        island.addTower(test);
        assertEquals(test, island.getIslandTower());
        assertEquals(island.getIslandTower().get(0).getColor(), TowerColor.BLACK);
    }

    @Test
    void removeTower() {
        List<Tower> test= new LinkedList<>();
        test.add(new Tower(TowerColor.BLACK));
        assertEquals(test.get(0).getColor().getText(), TowerColor.BLACK.toString());
        test.add(new Tower(TowerColor.BLACK));
        test.add(new Tower(TowerColor.WHITE));
        island.addTower(test);
        assertEquals(island.removeTower(), test);

    }

    @Test
    void countAll() {
        island.addStudent(new Student(0));
        island.addStudent(new Student(2));
        island.addStudent(new Student(0));
        island.addStudent(new Student(2));
        island.addStudent(new Student(0));
        island.addStudent(new Student(2));
        island.addStudent(new Student(1));
        island.addStudent(new Student(1));
        island.addStudent(new Student(4));
        island.addStudent(new Student(4));
        island.addStudent(new Student(4));
        island.addStudent(new Student(4));
        Integer[] integers = {3, 2, 3, 0, 4};
        for (int i = 0; i < 5; i++)
            assertEquals(island.countAll()[i], integers[i]);

    }
}
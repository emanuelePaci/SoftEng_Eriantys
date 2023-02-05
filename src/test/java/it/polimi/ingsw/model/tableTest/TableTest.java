package it.polimi.ingsw.model.tableTest;

import it.polimi.ingsw.controller.BoardHandler;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.TableHandler;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.controller.islandStrategy.IslandContext;
import it.polimi.ingsw.controller.islandStrategy.IslandStrategyKnight;
import it.polimi.ingsw.controller.islandStrategy.IslandStrategyMushroomHunter;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureContext;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureStrategyMagicDeliveryMan;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureStrategyStandard;
import it.polimi.ingsw.controller.professorStrategy.ProfessorContext;
import it.polimi.ingsw.controller.professorStrategy.ProfessorStrategyStandard;
import it.polimi.ingsw.exceptions.BagIsEmptyException;
import it.polimi.ingsw.exceptions.GeneralSupplyFinishedException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.pawns.Tower;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.*;
import it.polimi.ingsw.server.VirtualView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {
    private Table table;
    private TableHandler tableHandler = new TableHandler(new TurnController(), new Game(), new IslandContext(new IslandStrategyMushroomHunter()), new ProfessorContext(new ProfessorStrategyStandard()), new MotherNatureContext(new MotherNatureStrategyMagicDeliveryMan()), new ProfessorStrategyStandard(), new MotherNatureStrategyMagicDeliveryMan(), new IslandStrategyMushroomHunter(), new VirtualView(new Controller()));
    private BoardHandler boardHandler = new BoardHandler(new Game(), new TurnController(), new MotherNatureContext(new MotherNatureStrategyStandard()), new VirtualView(new Controller()));
    @BeforeEach
    void setUp() throws NoSuchMethodException {
        table= new Table(3, true, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class), new IslandStrategyKnight());
    }
    @AfterEach
    void tearDown(){
        table=null;
    }

    @Test
    void fillCloud() throws BagIsEmptyException {
        table.getCloud(0).removeAllStudent();
        table.getCloud(1).removeAllStudent();
        table.getCloud(2).removeAllStudent();
        assertEquals(table.getCloud(0).getCloudStudent().size(), 0);
        table.fillCloud(3,table.getBag().withdrawStudent(12));
        for (int i=0; i<3; i++) {
           assertEquals(table.getCloud(i).getCloudStudent().size(),4);
        /*    List<Student> test = new LinkedList<>(table.getCloud(i).getCloudStudent());
            for(Student s: test){
                System.out.println(s.getColor());
            }*/
        }

    }

    @Test
    void withdrawCoin() throws GeneralSupplyFinishedException {
        table.withdrawCoin();
        assertEquals(table.getGeneralSupply(), 16);
        table.addCoin(3);
        assertEquals(table.getGeneralSupply(), 19);
    }

    @Test
    void findProfessor() {
        assertEquals(table.findProfessor(PawnColor.GREEN).getColor(), PawnColor.GREEN);
        table.findProfessor(PawnColor.GREEN).setNumStudent(4);
        assertEquals(table.findProfessor(PawnColor.GREEN).getNumStudent(), 4);

    }

    @Test
    void moveMotherNature() {
        table.getMotherNature().setPosition(9);
        table.moveMotherNature(3);
        assertEquals(table.getMotherPosition(), 0);
        table.moveMotherNature(5);
        assertEquals(table.getMotherPosition(), 5);
    }

    @Test
    void mergeIsland() throws NoSuchMethodException {
        table.mergeIsland(0);
        assertEquals(table.getNumIsland(), 12, "no Island was merged");

        table.setCharacter(0, CharacterType.GRANDMOTHER_HERBS, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));
        assertEquals(table.getCharacter(0).getType(), CharacterType.GRANDMOTHER_HERBS);
        List<Tower> test1= new LinkedList<>();
        test1.add(new Tower(TowerColor.BLACK));
        table.getIsland(0).addTower(test1);
        table.getIsland(0).addStudent(new Student(0));
        table.mergeIsland(0);
        assertEquals(table.getNumIsland(), 12, "no Island was merged because next and previous island don't have tower");



        List<Tower> test2= new LinkedList<>();
        test2.add(new Tower(TowerColor.WHITE));
        table.getIsland(3).addTower(test2);
        table.getIsland(3).addStudent(new Student(0));
        List<Tower> test4= new LinkedList<>();
        test4.add(new Tower(TowerColor.BLACK));
        table.getIsland(4).addTower(test4);
        table.mergeIsland(3);
        assertEquals(table.getNumIsland(), 12, "no Island was merged because tower color are different");

        List<Tower> test3= new LinkedList<>();
        test3.add(new Tower(TowerColor.BLACK));
        table.getIsland(11).addTower(test3);
        table.getIsland(11).addStudent(new Student(2));
        table.getIsland(11).setNoEntryTiles(true);
        table.getIsland(10).addTower(test3);
        table.getIsland(10).addStudent(new Student(3));
        table.getIsland(10).setNoEntryTiles(true);
        table.mergeIsland(11);
        assertEquals(table.getNumIsland(), 10);
        assertEquals(table.getIsland(9).getIslandTower().size(), 3);
        assertEquals(table.getIsland(9).getIslandStudent().size(), 5);
        assertTrue(table.getIsland(9).isNoEntryTiles());

        table.getIsland(0).addTower(test3);
        table.mergeIsland(0);
        assertEquals(table.getNumIsland(), 9);
        assertEquals(table.getIsland(0).getIslandTower().size(), 4);
        assertEquals(table.getIsland(0).getIslandStudent().size(), 6);
        assertTrue(table.getIsland(0).isNoEntryTiles());


    }
    @Test
    void setCharacter() throws NoSuchMethodException {
        table.setCharacter(0, CharacterType.MAGIC_DELIVERY_MAN, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));
        assertEquals(table.getCharacter(0).getType(), CharacterType.MAGIC_DELIVERY_MAN);
        table.setCharacter(0, CharacterType.FARMER, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));
        assertEquals(table.getCharacter(0).getType(),CharacterType.FARMER);
        table.setCharacter(0, CharacterType.KNIGHT, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));
        assertEquals(table.getCharacter(0).getType(),CharacterType.KNIGHT);
        table.setCharacter(0, CharacterType.CENTAUR, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));
        assertEquals(table.getCharacter(0).getType(),CharacterType.CENTAUR);
        table.setCharacter(0, CharacterType.SPOILED_PRINCESS, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));
        assertEquals(table.getCharacter(0).getType(),CharacterType.SPOILED_PRINCESS);
        table.setCharacter(0, CharacterType.GRANDMOTHER_HERBS, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));
        assertEquals(table.getCharacter(0).getType(),CharacterType.GRANDMOTHER_HERBS);
    }

    @Test
    void generalCharacter() throws NoSuchMethodException {
        table.setCharacter(0, CharacterType.MAGIC_DELIVERY_MAN, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));
        assertEquals(-1, table.getCharacter(0).getNumNoEntryTiles());
        assertEquals(-1, table.getCharacter(0).count(PawnColor.BLUE));
        table.getCharacter(0).returnNoEntryTiles();
        assertEquals(-1, table.getCharacter(0).getNumNoEntryTiles());
        table.getCharacter(0).addStudent(new Student(0));
        assertEquals(-1, table.getCharacter(0).count(PawnColor.GREEN));
    }
}
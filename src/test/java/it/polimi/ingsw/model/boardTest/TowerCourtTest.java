package it.polimi.ingsw.model.boardTest;


import it.polimi.ingsw.model.board.DiningRoom;
import it.polimi.ingsw.model.board.TowerCourt;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.pawns.Tower;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TowerCourtTest {
    private TowerCourt towerCourt;

    @BeforeEach
    public void setUp(){
        towerCourt = new TowerCourt(6, TowerColor.BLACK);
    }

    @AfterEach
    public void tearDown(){
        towerCourt = null;
    }

    @Test
    void isEmpty(){
        assertEquals(false, towerCourt.isEmpty());
        towerCourt.removeTower(6);
        assertEquals(true, towerCourt.isEmpty());
    }

    @Test
    void removeTower(){
        towerCourt.removeTower(6);
        assertEquals(true, towerCourt.isEmpty());

    }

    @Test
    void removeTowerEx(){
        towerCourt.removeTower(9);
        assertEquals(true, towerCourt.isEmpty());
    }

    @Test
    void addTower(){
        towerCourt.removeTower(6);
        LinkedList<Tower> tower = new LinkedList<>();
        tower.add(new Tower(TowerColor.BLACK));
        tower.add(new Tower(TowerColor.BLACK));
        towerCourt.addTower(tower);
        assertEquals(tower, towerCourt.getTower());
    }

}

package it.polimi.ingsw.model.playerTest;

import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;

    @BeforeEach
        void begin(){
        String nameTest = "Test";
        player= new Player(nameTest, TowerColor.WHITE);
    }
    @AfterEach
        void tearDown(){
        player=null;
    }

    @Test
    void removeCoin() {
        LinkedList<Student> test= new LinkedList<>();
        test.add(new Student(1));
        test.add(new Student(1));
        test.add(new Student(0));
        player.setUp(test,true,3);
       player.removeCoin(1);
       assertEquals(player.getNumCoin(), 0);
       player.addCoin();
       player.addCoin();
       assertEquals(player.getNumCoin(), 2);
    }
    @Test
    void setUp(){
        LinkedList<Student> test= new LinkedList<>();
        test.add(new Student(1));
        test.add(new Student(1));
        test.add(new Student(0));
        player.setUp(test,true,3);
        assertNotNull(player.getBoard());
        assertNotNull(player.getDeck());
        assertEquals(player.getTowerColor(), TowerColor.WHITE);
        assertNull(player.getLastUsed());
        assertEquals(player.getNumCoin(), 1);

    }



    @Test
    void addAssistant() {
        LinkedList<Student> test= new LinkedList<>();
        test.add(new Student(1));
        test.add(new Student(1));
        test.add(new Student(0));
        player.setUp(test,true,3);
        Assistant assistant = player.getDeck().getAssistant(0);
        player.addAssistant(0);
        assertEquals(player.getLastUsed(), assistant);
    }

    @Test
    void changeState(){
        player.changeState(PlayerState.ACTION);
        assertEquals(player.getState().getText(), PlayerState.ACTION.toString());
    }
}
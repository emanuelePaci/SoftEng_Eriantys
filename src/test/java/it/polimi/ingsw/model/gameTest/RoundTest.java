package it.polimi.ingsw.model.gameTest;

import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    private Round round;
    private LinkedList<Player> player;

    @BeforeEach
    void setUp() {
        player = new LinkedList<>();
        player.add(new Player("test1", TowerColor.WHITE));
        player.add(new Player("test2", TowerColor.BLACK));
        player.add(new Player("test3", TowerColor.GREY));
        round=new Round(player);
    }

    @AfterEach
    void tearDown() {
        round=null;
    }

    @Test
    void getTurn(){
        assertEquals(round.getTurn().getCurrentPlayer(), player.get(0));
    }

    @Test
    void setLastRound() {
        round.setLastRound();
        assertEquals(round.getLastRound(), true);
    }

    @Test
    void setCloudChosen() {
        int cloudChosen = 1;
        round.setCloudChosen(cloudChosen);
        assertEquals(round.getCloudChosen().get(0), cloudChosen);
    }

    @Test
    void nextActionTurn() {
        assertEquals(round.nextActionTurn(), true);
        round.nextActionTurn();
        round.nextActionTurn();
        round.nextActionTurn();
        assertEquals(round.nextActionTurn(), false);

    }

    @Test
    void endPlanningPhase() {
        player.get(0).setUp(new LinkedList<>(), false, 3);
        player.get(1).setUp(new LinkedList<>(), false, 3);
        player.get(2).setUp(new LinkedList<>(), false, 3);
        player.get(0).addAssistant(4);
        player.get(1).addAssistant(2);
        player.get(2).addAssistant(5);
        round.endPlanningPhase();
        assertEquals(round.getNumTurnDone(), 0);
        player.get(0).addAssistant(5);
        player.get(1).addAssistant(1);
        player.get(2).addAssistant(3);
        round.endPlanningPhase();
        assertEquals(round.getNumTurnDone(), 0);
    }

    @Test
    void nextPlanningTurn() {
        assertEquals(round.nextPlanningTurn(), true);
        round.nextPlanningTurn();
        round.nextPlanningTurn();
        round.nextPlanningTurn();
        assertEquals(round.nextPlanningTurn(), false);
    }

    @Test
    void endRound() {
        round.endRound();
        assertEquals(round.getNumTurnDone(), 0);
    }
}
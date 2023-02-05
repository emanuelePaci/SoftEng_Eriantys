package it.polimi.ingsw.model.gameTest;

import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurnTest {
    private Turn turn;
    private Player player;
    @BeforeEach
    void setUp() {
        player= new Player("test1", TowerColor.WHITE);
        turn = new Turn(player);
    }

    @AfterEach
    void tearDown() {
        turn=null;
    }

    @Test
    void resetRemainingMovements() {
        turn.resetRemainingMovements(3);
        assertEquals(turn.getRemainingMovements(), 3);
        turn.updateRemainingMovements();
        assertEquals(turn.getRemainingMovements(), 2);
    }

    @Test
    void setUsedCharacter() {
        turn.setUsedCharacter(true);
        assertTrue(turn.isUsedCharacter());
    }

    @Test
    void updatePlayer() {
        turn.updatePlayer(player);
        assertEquals(turn.getCurrentPlayer(), player);
    }

}
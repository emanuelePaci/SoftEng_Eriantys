package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.MotherNature;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    private Controller controller;
    private LobbyHandler lobbyHandler;
    Player player1 = new Player("TEST1", TowerColor.WHITE);
    Player player2= new Player("TEST2", TowerColor.BLACK);
    Player player3= new Player("TEST3", TowerColor.GREY);
    private Game game;

    @BeforeEach
    void setUp() throws InterruptedException {
        lobbyHandler = new LobbyHandler();
        controller = new Controller();
        controller.setNumPlayer(3);
        controller.setExpertMode(true);
        controller.setVirtualView(new VirtualView(controller));

        controller.addPlayer(player1);
        controller.addPlayer(player2);
        controller.addPlayer(player3);
        controller.gameStart();
        game = controller.getGame();

        game.getTable().getMotherNature().setPosition(0);
    }

    @AfterEach
    void tearDown(){
        controller = null;
        game = null;
    }

    @Test
    void setNumPlayer(){
        controller.setNumPlayer(2);
        assertEquals(2, game.getNumPlayer());
    }

    @Test
    void setExpertMode(){
        controller.setExpertMode(false);
        assertFalse(game.isExpertMode());
    }

    @Test
    void getPlayerByNickname(){
        assertEquals(player1, controller.getPlayerByNickname("TEST1"));
        assertNull(controller.getPlayerByNickname("CIAO"));
    }

    @Test
    void addPlayer(){
        assertEquals(player1, game.getPlayers().get(0));
        assertEquals("WHITE", game.getPlayers().get(0).getTowerColor().getText());

        assertEquals(player2, game.getPlayers().get(1));

        assertEquals(player3, game.getPlayers().get(2));
        assertEquals(PlayerState.PLANNING, game.getPlayers().get(0).getState());
        assertEquals(4, player1.getDeck().getAssistant(3).getWeight());
    }

    @Test
    void getAvailableColors(){
        assertEquals(0, controller.getAvailableColor().size());
    }

    @Test
    void useAssistant(){
        controller.useAssistant(6, player1);
        assertEquals(7, player1.getLastUsed().getWeight());

        controller.useAssistant(6, player2);
        assertNull(player2.getLastUsed());

        controller.useAssistant(3, player2);
        assertEquals(4, player2.getLastUsed().getWeight());

        controller.useAssistant(1, player3);
        assertEquals(2, player3.getLastUsed().getWeight());
    }

    @Test
    void useAssistant2(){
        controller.useAssistant(3, player2);
        assertNull(player2.getLastUsed());

        controller.moveMotherNature(player1, 3);
        assertEquals(0, game.getTable().getMotherPosition());

        for (int i = 3; i < 10; i++){
            player1.addAssistant(0);
            player2.addAssistant(0);
            player3.addAssistant(0);
        }
        player1.addAssistant(2);
        player2.addAssistant(1);
        player3.addAssistant(1);

        controller.useAssistant(1, player1);
        controller.useAssistant(1, player2);
        controller.useAssistant(1, player3);
        assertEquals(9, player3.getLastUsed().getWeight());

        controller.useAssistant(0, player3);

        assertEquals(9, player1.getLastUsed().getWeight());
        assertEquals(10, player2.getLastUsed().getWeight());
        assertEquals(8, player3.getLastUsed().getWeight());
    }

    @Test
    void useAssistant3(){
        for (int i = 1; i < 10; i++){
            player1.addAssistant(0);
            player2.addAssistant(0);
            player3.addAssistant(0);
        }

        controller.useAssistant(0, player1);
        controller.useAssistant(0, player2);
        controller.useAssistant(0, player3);

        assertTrue(game.getRound().getLastRound());
    }

    @Test
    void winner(){
        player1.getBoard().getTowerCourt().removeTower(4);
        player2.getBoard().getTowerCourt().removeTower(3);
        player3.getBoard().getTowerCourt().removeTower(3);

        controller.getTableHandler().winner();
        assertTrue(game.isGameEnded());
    }

    @Test
    void winner2(){
        player1.getBoard().getTowerCourt().removeTower(4);
        player2.getBoard().getTowerCourt().removeTower(4);
        player3.getBoard().getTowerCourt().removeTower(3);

        controller.getTableHandler().winner();
        assertTrue(game.isGameEnded());
    }

}

package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.MotherNature;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardHandlerTest {
    private Controller controller;
    Player player1 = new Player("TEST1", TowerColor.WHITE);
    Player player2= new Player("TEST2", TowerColor.BLACK);
    Player player3= new Player("TEST3", TowerColor.GREY);
    private Game game;

    @BeforeEach
    void setUp() throws InterruptedException {
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

        controller.useAssistant(0,player1);
        controller.useAssistant(5, player2);
        controller.useAssistant(3, player3);

        player1.getBoard().getEntrance().addStudent(new Student(0));
        player1.getBoard().getEntrance().addStudent(new Student(4));
        player1.getBoard().getEntrance().addStudent(new Student(4));
        player1.getBoard().getEntrance().addStudent(new Student(4));

        player3.getBoard().getEntrance().addStudent(new Student(0));
        player3.getBoard().getEntrance().addStudent(new Student(0));
        player3.getBoard().getEntrance().addStudent(new Student(0));
    }

    @AfterEach
    void tearDown(){
        controller = null;
        game = null;
    }

    @Test
    void useStudentDining() {
        List<Student> students = new LinkedList<>();
        for (int i = 0; i < 10; i ++)
            students.add(new Student(2));      //The color yellow line is full, after it will throw an error
        player1.getBoard().getDiningRoom().addStudent(students);

        controller.useStudentDining(player1, PawnColor.YELLOW);
        assertEquals(10, player1.getBoard().getDiningRoom().count(PawnColor.YELLOW));
        assertEquals(4, game.getRound().getTurn().getRemainingMovements());

        controller.useAssistant(3, player1);
        assertEquals(1, player1.getLastUsed().getWeight());

        controller.useStudentDining(player1, PawnColor.GREEN);
        controller.useStudentDining(player1, PawnColor.BLUE);
        controller.useStudentDining(player1, PawnColor.BLUE);
        controller.useStudentDining(player1, PawnColor.BLUE);
        assertEquals(3, player1.getBoard().getDiningRoom().count(PawnColor.BLUE));
        assertEquals(2, player1.getNumCoin());

        controller.useStudentDining(player1, PawnColor.GREEN);
        assertEquals(1, player1.getBoard().getDiningRoom().count(PawnColor.GREEN));

        game.getRound().nextActionTurn(); //Forzo il prossimo turno
        game.getTable().addCoin(-16);  //Tolgo tutte le monete rimaste
        controller.useStudentDining(player3, PawnColor.GREEN);
        controller.useStudentDining(player3, PawnColor.GREEN);
        controller.useStudentDining(player3, PawnColor.GREEN);
        assertEquals(1, player3.getNumCoin());
        assertEquals(0, game.getTable().getMotherPosition());
    }
}

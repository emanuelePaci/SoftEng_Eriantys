package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.BoardHandler;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.TableHandler;
import it.polimi.ingsw.exceptions.BagIsEmptyException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.pawns.Tower;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterHandlerTest {
    private LobbyHandler lobbyHandler;
    private Controller controller;
    Player player1 = new Player("TEST1", TowerColor.WHITE);
    Player player2= new Player("TEST2", TowerColor.BLACK);

    TableHandler tableHandler;
    BoardHandler boardHandler;
    private Game game;

    @BeforeEach
    void setUp() throws InterruptedException {
        lobbyHandler = new LobbyHandler();
        controller = new Controller();
        controller.setNumPlayer(2);
        controller.setExpertMode(true);
        controller.setVirtualView(new VirtualView(controller));

        controller.addPlayer(player1);
        controller.addPlayer(player2);
        controller.gameStart();

        game = controller.getGame();

        tableHandler = controller.getTableHandler();
        boardHandler = controller.getBoardHandler();

        game.getTable().getMotherNature().setPosition(0);

        controller.useAssistant(0,player1);
        controller.useAssistant(5, player2);
    }

    @AfterEach
    void tearDown(){
        controller = null;
        game = null;
    }

    @Test
    void characterErrors() throws NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.CENTAUR, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));
        game.getTable().setCharacter(1, CharacterType.MAGIC_DELIVERY_MAN, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        controller.useCharacter(player1, 0);
        assertFalse(game.getRound().getTurn().isUsedCharacter());
        assertEquals(1, player1.getNumCoin());

        controller.useCharacter(player1, 0, PawnColor.BLUE);
        assertFalse(game.getRound().getTurn().isUsedCharacter());
        assertEquals(1, player1.getNumCoin());

        int[] colors = new int[1];
        colors[0] = 1;
        controller.useCharacter(player1, 0, colors);
        assertFalse(game.getRound().getTurn().isUsedCharacter());
        assertEquals(1, player1.getNumCoin());

        player1.changeState(PlayerState.PLANNING);
        controller.useCharacter(player1, 0, 6, PawnColor.BLUE);
        assertFalse(game.getRound().getTurn().isUsedCharacter());
        assertEquals(1, player1.getNumCoin());

        player1.changeState(PlayerState.ENDTURN);
        controller.useCharacter(player1, 1);
        assertTrue(game.getRound().getTurn().isUsedCharacter());
        controller.useCharacter(player1, 1);
        assertEquals(0, player1.getNumCoin());
    }

    @Test
    void useNoEntryTilesCharacter() throws NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.GRANDMOTHER_HERBS, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        for (int i = 0; i < 10; i++)
            for (Player p : game.getPlayers())
                p.addCoin();
        //Aggiungo abbastanza monete da poter usare i personaggi quante volte desidero

        assertEquals(4, game.getTable().getCharacter(0).getNumNoEntryTiles());

        controller.useCharacter(player1, 0, 0);
        assertTrue(game.getTable().getIsland(0).isNoEntryTiles());
        assertEquals(3, game.getTable().getCharacter(0).getPrice());
        assertEquals(3, game.getTable().getCharacter(0).getNumNoEntryTiles());


        game.getRound().nextActionTurn();
        controller.useCharacter(player1, 0, 4);

        //Esaurisco le carte NoEntryTiles
        controller.useCharacter(player2, 0, 3);
        game.getRound().nextActionTurn();
        game.getRound().endRound();

        controller.useAssistant(4,player1);
        controller.useAssistant(2, player2);
        controller.useCharacter(player2, 0, 1);
        game.getRound().nextActionTurn();
        controller.useCharacter(player1, 0, 2);

        game.getRound().nextActionTurn();
        game.getRound().endRound();
        controller.useAssistant(6, player2);
        controller.useAssistant(1, player1);

        controller.useCharacter(player1, 0, 5);
        assertFalse(game.getRound().getTurn().isUsedCharacter());
        assertEquals(6, player1.getNumCoin());
        controller.useCharacter(player1, 0, 0);
        assertFalse(game.getRound().getTurn().isUsedCharacter());
        assertEquals(6, player1.getNumCoin());
    }

    @Test
    void useAdd_MovesCharacter() throws NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.MAGIC_DELIVERY_MAN, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        List<Tower> towers = new LinkedList<>();
        towers.add(new Tower(TowerColor.WHITE));
        game.getRound().getTurn().resetRemainingMovements(0); //Forzo il movimento degli studenti
        controller.moveMotherNature(player1, 3);
        assertEquals(0, game.getTable().getMotherPosition()); //Non si è mossa

        controller.useCharacter(player1, 0);  //Testo che io possa muovere madre natura di 3 caselle e non solo di 1
        controller.moveMotherNature(player1, 5);  //Errore
        game.getTable().getIsland(3).addTower(towers);
        controller.moveMotherNature(player1, 3);
        assertEquals(3, game.getTable().getMotherPosition());

        controller.chooseCloud(player1, 0);
        game.getRound().getTurn().resetRemainingMovements(0); //Forzo il movimento degli studenti
        game.getTable().getMotherNature().setPosition(11);
        game.getTable().getIsland(3).setMotherNature(false);
        game.getTable().getIsland(11).setMotherNature(true);
        player2.addCoin();
        controller.useCharacter(player2, 0);
        controller.moveMotherNature(player2, 1);
        assertEquals(1, game.getTable().getMotherPosition()); //Non si è mossa
    }

    @Test
    void useNo_ColorCharacter() throws NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.MUSHROOM_HUNTER, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        game.getRound().getTurn().resetRemainingMovements(0);
        game.getTable().getIsland(1).getIslandStudent().clear();
        game.getTable().getIsland(1).addStudent(new Student(4)); //Metto studente blu sull'isola
        game.getTable().getIsland(1).addStudent(new Student(4));
        game.getTable().getIsland(1).addStudent(new Student(0));
        game.getTable().getIsland(1).addStudent(new Student(0));
        List<Tower> towers = new LinkedList<>();
        towers.add(new Tower(TowerColor.WHITE));
        game.getTable().getIsland(1).addTower(towers);
        player1.getBoard().getProfessorTable().addProfessor(game.getTable().findProfessor(PawnColor.BLUE)); //Assegno il professore blu
        player2.getBoard().getProfessorTable().addProfessor(game.getTable().findProfessor(PawnColor.GREEN));
        assertEquals(game.getTable().findProfessor(PawnColor.BLUE), player1.getBoard().getProfessorTable().find(PawnColor.BLUE));

        player1.addCoin();
        player1.addCoin();
        controller.useCharacter(player1, 0, PawnColor.BLUE);
        controller.moveMotherNature(player1, 1);
        assertEquals(1, game.getTable().getIsland(1).getIslandTower().size());
        assertEquals(TowerColor.BLACK, game.getTable().getIsland(1).getIslandTower().get(0).getColor());
    }

    @Test
    void useControl_ProfessorCharacter() throws NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.FARMER, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        player1.getBoard().getEntrance().addStudent(new Student(0));
        controller.useStudentDining(player1, PawnColor.GREEN);

        game.getRound().nextActionTurn();
        player2.addCoin();

        player2.getBoard().getEntrance().addStudent(new Student(0));
        controller.useCharacter(player2, 0);
        controller.useStudentDining(player2, PawnColor.GREEN);
        assertEquals(game.getTable().findProfessor(PawnColor.GREEN), player2.getBoard().getProfessorTable().find(PawnColor.GREEN));
        assertEquals(1, game.getTable().findProfessor(PawnColor.GREEN).getNumStudent());
    }

    @Test
    void useFake_Mother_NatureCharacter() throws NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.HERALD, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        player1.addCoin();
        player1.addCoin();
        player1.getBoard().getEntrance().addStudent(new Student(1));
        game.getTable().getIsland(10).addStudent(new Student(1));
        controller.useStudentDining(player1, PawnColor.RED);
        controller.useCharacter(player1, 0, 10);
        assertEquals(game.getTable().getIsland(10).getIslandTower().size(), 1);
    }

    @Test
    void useMore_InfluenceCharacter() throws NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.KNIGHT, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        player1.getBoard().getEntrance().addStudent(new Student(0));
        player1.getBoard().getEntrance().addStudent(new Student(0));
        player1.getBoard().getEntrance().addStudent(new Student(4));
        List<Tower> towers = new LinkedList<>();
        towers.add(new Tower(TowerColor.WHITE));
        game.getTable().getIsland(6).addTower(towers);
        controller.useStudentIsland(player1, PawnColor.GREEN, 6);
        controller.useStudentIsland(player1, PawnColor.BLUE, 6);
        controller.useStudentDining(player1, PawnColor.GREEN);
        game.getRound().nextActionTurn();

        game.getTable().getMotherNature().setPosition(4);
        game.getTable().getIsland(0).setMotherNature(false);
        game.getTable().getIsland(4).setMotherNature(true);
        player2.addCoin();

        player2.getBoard().getEntrance().addStudent(new Student(1));
        player2.getBoard().getEntrance().addStudent(new Student(1));
        controller.useStudentDining(player2, PawnColor.RED);
        controller.useStudentIsland(player2, PawnColor.RED, 6);
        game.getRound().getTurn().resetRemainingMovements(0);
        controller.useCharacter(player2, 0);
        controller.moveMotherNature(player2, 6);
        assertEquals(TowerColor.BLACK, game.getTable().getIsland(6).getIslandTower().get(0).getColor());
    }

    @Test
    void useExchangeCharacter() throws NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.MINSTREL, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        player1.getBoard().getEntrance().addStudent(new Student(0));
        player1.getBoard().getEntrance().addStudent(new Student(1));
        player1.getBoard().getEntrance().addStudent(new Student(2));
        player1.getBoard().getEntrance().addStudent(new Student(2));
        player1.getBoard().getEntrance().addStudent(new Student(3));
        controller.useStudentDining(player1, PawnColor.YELLOW);
        controller.useStudentDining(player1, PawnColor.YELLOW);
        controller.useStudentDining(player1, PawnColor.PINK);

        int[] colors = new int[4];
        colors[0] = 0;  //Entrance -> Dining
        colors[1] = 1;
        colors[2] = 2;  //Dining -> Entrance
        colors[3] = 3;
        controller.useCharacter(player1, 0, colors);
        assertEquals(1, player1.getBoard().getDiningRoom().count(PawnColor.GREEN));
        assertEquals(1, player1.getBoard().getDiningRoom().count(PawnColor.RED));
        assertEquals(1, player1.getBoard().getDiningRoom().count(PawnColor.YELLOW));
        assertEquals(0, player1.getBoard().getDiningRoom().count(PawnColor.PINK));

        assertEquals(1, player1.getBoard().getProfessorTable().find(PawnColor.YELLOW).getNumStudent());
        assertEquals(1, player1.getBoard().getProfessorTable().find(PawnColor.GREEN).getNumStudent());
    }

    @Test
    void useNo_TowerCharacter() throws NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.CENTAUR, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        List<Tower> towers = new LinkedList<>(player2.getBoard().getTowerCourt().removeTower(2));
        game.getTable().getIsland(1).addTower(towers);
        game.getTable().getIsland(1).getIslandStudent().clear();

        player1.addCoin();
        player1.addCoin();
        player1.getBoard().getEntrance().addStudent(new Student(2));
        player1.getBoard().getEntrance().addStudent(new Student(2));
        controller.useStudentIsland(player1, PawnColor.YELLOW, 1);
        controller.useStudentDining(player1, PawnColor.YELLOW);
        game.getRound().getTurn().resetRemainingMovements(0);

        controller.useCharacter(player1, 0);
        controller.moveMotherNature(player1, 1);
        assertEquals(TowerColor.WHITE, game.getTable().getIsland(1).getIslandTower().get(0).getColor());
        assertEquals(1, game.getTable().getIsland(1).getIslandTower().size());

        assertEquals(8, player2.getBoard().getTowerCourt().getTower().size());
        assertEquals(7, player1.getBoard().getTowerCourt().getTower().size());
    }

    @Test
    void useReturnCharacter() throws NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.THIEF, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        player1.getBoard().getEntrance().addStudent(new Student(4));
        for (int i = 0; i < 3; i++)
            player1.getBoard().getDiningRoom().addStudent(new Student(4));
        player2.getBoard().getDiningRoom().addStudent(new Student(4));
        player2.getBoard().getDiningRoom().addStudent(new Student(4));
        player1.addCoin();
        player1.addCoin();

        controller.useStudentDining(player1, PawnColor.BLUE);
        controller.useCharacter(player1, 0, PawnColor.BLUE);
        assertEquals(1, game.getTable().findProfessor(PawnColor.BLUE).getNumStudent());
        assertEquals(0, player2.getBoard().getDiningRoom().count(PawnColor.BLUE));
        assertEquals(1, player1.getBoard().getDiningRoom().count(PawnColor.BLUE));

        game.getRound().nextActionTurn();
        for (int i = 0; i < 3; i++)
            player2.addCoin();
        controller.useCharacter(player2, 0, PawnColor.BLUE);
        assertEquals(0, player1.getBoard().getDiningRoom().count(PawnColor.BLUE));
        assertEquals(0, player1.getBoard().getProfessorTable().find(PawnColor.BLUE).getNumStudent());
    }

    @Test
    void useAdd_Student_DinningCharacter() throws BagIsEmptyException, NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.SPOILED_PRINCESS, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        game.getTable().getCharacter(0).addStudent(new Student(0));
        assertTrue(1 <= game.getTable().getCharacter(0).count(PawnColor.GREEN));
        game.getTable().getBag().withdrawStudent(game.getTable().getBag().getStudent().size());
        player1.addCoin();

        controller.useCharacter(player1, 0, PawnColor.GREEN);
        assertEquals(1, player1.getBoard().getDiningRoom().count(PawnColor.GREEN));
        assertTrue(game.getRound().getLastRound());
    }

    @Test
    void useAdd_Student_IslandCharacter() throws BagIsEmptyException, NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.MONK, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        game.getTable().getCharacter(0).addStudent(new Student(0));
        game.getTable().getBag().withdrawStudent(game.getTable().getBag().getStudent().size());

        controller.useCharacter(player1, 0, 6, PawnColor.GREEN);
        assertEquals(1, game.getTable().getIsland(6).countStudent(PawnColor.GREEN));
        assertTrue(game.getRound().getLastRound());
    }

    @Test
    void useReplaceCharacter() throws NoSuchMethodException {
        game.getTable().setCharacter(0, CharacterType.JESTER, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));

        List<Student> students = new LinkedList<>();
        students.add(new Student(4));
        students.add(new Student(2));
        game.getTable().getCharacter(0).addStudent(students.get(1));
        game.getTable().getCharacter(0).addStudent(students.get(0));
        player1.getBoard().getEntrance().getStudent().clear();
        player1.getBoard().getEntrance().addStudent(new Student(3));
        player1.getBoard().getEntrance().addStudent(new Student(3));

        int[] colors = new int[6];
        colors[0] = 3;
        colors[1] = 3;
        colors[2] = -1;
        colors[3] = 4;
        colors[4] = 2;
        colors[5] = -1;

        controller.useCharacter(player1, 0, colors);
        assertEquals(students, player1.getBoard().getEntrance().getStudent());
    }
}

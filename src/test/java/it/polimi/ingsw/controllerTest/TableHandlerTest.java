package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.BoardHandler;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.TableHandler;
import it.polimi.ingsw.controller.TurnController;
import it.polimi.ingsw.controller.islandStrategy.IslandContext;
import it.polimi.ingsw.controller.islandStrategy.IslandStrategyMushroomHunter;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureContext;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureStrategyMagicDeliveryMan;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureStrategyStandard;
import it.polimi.ingsw.controller.professorStrategy.ProfessorContext;
import it.polimi.ingsw.controller.professorStrategy.ProfessorStrategyStandard;
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
import it.polimi.ingsw.server.VirtualView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TableHandlerTest {
    private Controller controller;
    Player player1 = new Player("TEST1", TowerColor.WHITE);
    Player player2 = new Player("TEST2", TowerColor.BLACK);
    Player player3 = new Player("TEST3", TowerColor.GREY);
    private Game game;
    private TableHandler tableHandler = new TableHandler(new TurnController(), new Game(), new IslandContext(new IslandStrategyMushroomHunter()), new ProfessorContext(new ProfessorStrategyStandard()), new MotherNatureContext(new MotherNatureStrategyMagicDeliveryMan()), new ProfessorStrategyStandard(), new MotherNatureStrategyMagicDeliveryMan(), new IslandStrategyMushroomHunter(), new VirtualView(new Controller()));
    private BoardHandler boardHandler = new BoardHandler(new Game(), new TurnController(), new MotherNatureContext(new MotherNatureStrategyStandard()), new VirtualView(new Controller()));
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

        controller.useAssistant(7, player1);
        controller.useAssistant(8, player2);
        controller.useAssistant(9, player3);

        game.getTable().getMotherNature().setPosition(0);

    }

    @AfterEach
    void tearDown() {
        controller = null;
        game = null;
    }

    @Test
    void moveMotherNature() throws NoSuchMethodException {
        int pos = game.getTable().getMotherNature().getPosition();
        game.getTable().setCharacter(0, CharacterType.GRANDMOTHER_HERBS, tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class));
        game.getRound().getTurn().resetRemainingMovements(1);
        controller.moveMotherNature(player1, 1);
        assertEquals(pos, game.getTable().getMotherNature().getPosition());

        game.getRound().getTurn().resetRemainingMovements(0);
        controller.moveMotherNature(player1, 8);
        assertEquals(pos, game.getTable().getMotherNature().getPosition());

        game.getTable().getIsland(1).setNoEntryTiles(true);
        controller.moveMotherNature(player1, 1);
        assertFalse(game.getTable().getIsland(1).isNoEntryTiles());
        assertEquals(player1.getState(), PlayerState.ENDTURN);

        game.getRound().nextActionTurn();
        game.getRound().getTurn().resetRemainingMovements(0);
        List<Tower> test = new LinkedList<>();
        test.add(new Tower(TowerColor.BLACK));

        for (int i = 0; i < 5; i++) {
            game.getTable().getIsland(i).addTower(test);
        }
        game.getTable().mergeIsland(1);
        assertEquals(game.getTable().getIsland(0).getIslandTower().size(), 3);
        game.getTable().mergeIsland(1);
        game.getTable().getIsland(game.getTable().getMotherPosition()).setMotherNature(false);
        game.getTable().getMotherNature().setPosition(7);
        game.getTable().getIsland(7).setMotherNature(true);
        assertEquals(game.getTable().getIsland(0).getIslandTower().size(), 5);
        for (int i = 0; i < 16; i++) {
            game.getTable().getIsland(1).addStudent(new Student(0));
        }
        player2.getBoard().getTowerCourt().removeTower(5);
        player2.getBoard().getProfessorTable().addProfessor(game.getTable().findProfessor(PawnColor.GREEN));
        assertEquals(player2.getBoard().getProfessorTable().find(PawnColor.GREEN), game.getTable().findProfessor(PawnColor.GREEN));
        controller.moveMotherNature(player2, 1);
        assertEquals(game.getTable().getIsland(1).getIslandStudent().size(), 0);
        assertEquals(player2.getBoard().getTowerCourt().getTower().size(), 0);
        assertTrue(game.isGameEnded());
    }

    @Test
    void updateIsland() {
        List<Tower> test = new LinkedList<>();
        test.add(new Tower(TowerColor.WHITE));
        for (int i = 0; i <5; i++) {
            game.getTable().getIsland(i).addTower(test);
        }
        List<Tower> test1 = new LinkedList<>();
        test1.add(new Tower(TowerColor.BLACK));
        for (int i = 5; i <9; i++) {
            game.getTable().getIsland(i).addTower(test1);
        }

        List<Tower> test2 = new LinkedList<>();
        test2.add(new Tower(TowerColor.GREY));
        for (int i = 9; i <11; i++) {
            game.getTable().getIsland(i).addTower(test2);
        }
        game.getTable().mergeIsland(1);
        assertEquals(game.getTable().getNumIsland(), 10);
        game.getTable().mergeIsland(1);
        assertEquals(game.getTable().getNumIsland(), 8);
        game.getTable().mergeIsland(2);
        assertEquals(game.getTable().getIsland(2).getIslandTower().get(0).getColor(),TowerColor.BLACK);
        assertEquals(game.getTable().getNumIsland(), 6);
        game.getTable().mergeIsland(2);
        game.getTable().mergeIsland(3);
        assertEquals(game.getTable().getNumIsland(), 4);
        for (int i = 0; i < 16; i++) {
            game.getTable().getIsland(3).addStudent(new Student(0));
        }
        player1.getBoard().getTowerCourt().removeTower(5);
        player2.getBoard().getTowerCourt().removeTower(4);
        player3.getBoard().getTowerCourt().removeTower(2);
        game.getRound().nextActionTurn();
        game.getRound().nextActionTurn();
        game.getRound().getTurn().resetRemainingMovements(0);
        player3.getBoard().getProfessorTable().addProfessor(game.getTable().findProfessor(PawnColor.GREEN));
        controller.moveMotherNature(player3, 3);
        assertTrue(game.isGameEnded());
    }
    @Test
    void chooseCloud() throws BagIsEmptyException {
        List<Tower> test = new LinkedList<>();
        test.add(new Tower(TowerColor.GREY));
        game.getTable().getIsland(8).addTower(test);
        game.getTable().getIsland(9).addTower(test);
        game.getTable().getIsland(10).addTower(test);
        game.getTable().getIsland(11).addTower(test);
        game.getRound().getTurn().resetRemainingMovements(0);
        controller.moveMotherNature(player1, 2);
        controller.chooseCloud(player1,0);
        assertFalse(game.getRound().getTurn().isUsedCharacter());

        game.getRound().getTurn().resetRemainingMovements(0);
        controller.moveMotherNature(player2, 3);
        controller.chooseCloud(player2,0);
        assertEquals(PlayerState.ENDTURN, player2.getState());

        controller.useAssistant(6, player2);
        assertEquals(9, player2.getLastUsed().getWeight());

        controller.chooseCloud(player2,1);
        game.getRound().getTurn().resetRemainingMovements(0);
        controller.moveMotherNature(player3, 4);
        controller.chooseCloud(player3,2);
        controller.useAssistant(4, player1);
        controller.useAssistant(5, player2);
        controller.useAssistant(6, player3);

        game.getRound().nextActionTurn();
        game.getRound().nextActionTurn();
        game.getRound().getTurn().resetRemainingMovements(0);
        controller.moveMotherNature(player3, 5);
        game.getTable().getBag().withdrawStudent(game.getTable().getBag().getStudent().size());
        controller.chooseCloud(player3,2);
        controller.useAssistant(5, player1);
        controller.useAssistant(6, player2);
        controller.useAssistant(7, player3);
        player3.getBoard().getTowerCourt().removeTower(4);
        game.getRound().nextActionTurn();
        game.getRound().nextActionTurn();
        game.getRound().getTurn().resetRemainingMovements(0);
        controller.moveMotherNature(player3, 6);
        controller.chooseCloud(player3,2);
        assertTrue(game.isGameEnded());
    }

    @Test
    void useStudentIsland(){
        PawnColor color= player1.getBoard().getEntrance().getStudent().get(0).getColor();
        Island island = game.getTable().getIsland(0);
        game.getRound().getTurn().resetRemainingMovements(1);
        controller.useStudentIsland(player1, color, 0);
        assertEquals(player1.getBoard().getEntrance().getStudent().size(), 8);
        assertEquals(game.getRound().getTurn().getRemainingMovements(),0);
        assertEquals(game.getTable().getIsland(0).getIslandStudent().size(),1);
        assertEquals(game.getTable().getIsland(0).getIslandStudent().get(0).getColor(),color);
        PawnColor color2= player1.getBoard().getEntrance().getStudent().get(3).getColor();
        int countBefore = game.getTable().getIsland(0).countStudent(color2);
        controller.useStudentIsland(player1, color2, 0);
        assertEquals(countBefore, game.getTable().getIsland(0).countStudent(color2));
        assertEquals(0, game.getRound().getTurn().getRemainingMovements());
        assertEquals(island, game.getTable().getIsland(0));
    }
}
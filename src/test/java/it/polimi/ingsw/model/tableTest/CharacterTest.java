package it.polimi.ingsw.model.tableTest;

import static org.junit.jupiter.api.Assertions.*;

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
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.character.Factory;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Bag;

import it.polimi.ingsw.model.table.Island;
import it.polimi.ingsw.server.VirtualView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CharacterTest {
    Factory factory = new Factory();
    private Character character;

    private TableHandler tableHandler = new TableHandler(new TurnController(), new Game(), new IslandContext(new IslandStrategyMushroomHunter()), new ProfessorContext(new ProfessorStrategyStandard()), new MotherNatureContext(new MotherNatureStrategyMagicDeliveryMan()), new ProfessorStrategyStandard(), new MotherNatureStrategyMagicDeliveryMan(), new IslandStrategyMushroomHunter(), new VirtualView(new Controller()));
    private BoardHandler boardHandler = new BoardHandler(new Game(), new TurnController(), new MotherNatureContext(new MotherNatureStrategyStandard()), new VirtualView(new Controller()));
    @BeforeEach
    void setUp() throws NoSuchMethodException {
        character=factory.getCharacter(CharacterType.MAGIC_DELIVERY_MAN, new Bag(), tableHandler.getClass().getMethod("updateIsland", Island.class), boardHandler.getClass().getMethod("checkProfessor", Player.class, PawnColor.class), new IslandStrategyKnight());
    }
    @AfterEach
    void tearDown(){
        character=null;
    }
    @Test
    void firstUse(){
        assertEquals(character.getType().getDescription(),"You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played.");
        assertEquals(character.getType().getText(),CharacterType.MAGIC_DELIVERY_MAN.toString());
        character.firstUse();
        assertEquals(character.getType(), CharacterType.MAGIC_DELIVERY_MAN);
        assertTrue(character.isUsed());
        assertEquals(character.getPrice(), 2);
    }
}

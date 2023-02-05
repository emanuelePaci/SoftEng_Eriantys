package it.polimi.ingsw.model.character;

import it.polimi.ingsw.controller.Context;
import it.polimi.ingsw.controller.TableHandler;
import it.polimi.ingsw.controller.professorStrategy.ProfessorStrategy;
import it.polimi.ingsw.controller.professorStrategy.ProfessorStrategyFarmer;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.table.Island;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Characte group 2 class
 * FARMER
 * HERALD
 */
public class CharacterGroup2 extends Character {

    private ProfessorStrategy professor= new ProfessorStrategyFarmer();
    private Method updateIsland;

    /**
     * Constructor
     * @param type the character type
     * @param updateIsland the update Island method
     */
    public CharacterGroup2 (CharacterType type, Method updateIsland){
        super(type);
        this.updateIsland = updateIsland;
    }

    /**
     * activate character
     * @param professorContext the professor context
     * @param motherNatureContext the mother nature context
     * @param islandContext the island context
     */
    @Override
    public void activateCharacter(Context professorContext, Context motherNatureContext, Context islandContext) {
        professorContext.changeContext(professor);
    }

    /**
     * activate character
     * @param island the current island
     * @param tableHandler the table handler
     */
    @Override
    public void activateCharacter(Island island, TableHandler tableHandler) {
        try {
            updateIsland.invoke(tableHandler, island);
        } catch (IllegalAccessException | InvocationTargetException ignored){}
    }

}

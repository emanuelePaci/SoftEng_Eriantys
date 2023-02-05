package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.islandStrategy.IslandStrategy;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureStrategy;
import it.polimi.ingsw.controller.professorStrategy.ProfessorStrategy;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;
import it.polimi.ingsw.model.table.Table;

/**
 * Context abstract class
 */
public abstract class Context {
    /**
     * Constructor
     */
    public Context(){

    }
    /**
     *  changes context
     * @param strategy ths island strategy
     */
    public void changeContext(IslandStrategy strategy){}

    /**
     * changes context
     * @param strategy the mother nature strategy
     */
    public void changeContext(MotherNatureStrategy strategy){}

    /**
     * changes context
     * @param strategy the professor strategy
     */
    public void changeContext(ProfessorStrategy strategy){}

    /**
     * conquers an island
     * @param island the current island
     * @param game the current game
     * @return false
     */
    public boolean conquerIsland(Island island, Game game){
        return false;
    }

    /**
     *  professor control
     * @param game the current game
     * @param player the current player
     * @param color the student color
     */
    public void professorControl(Game game, Player player, PawnColor color){}

    /**
     * mother nature control
     * @param table the current table
     * @param endPosition the end position of mother nature
     * @param player the current player
     * @return 0
     * @throws ClientException if there are problems
     */
    public int motherNatureControl(Table table, int endPosition, Player player) throws ClientException {
        return 0;
    }
}

package it.polimi.ingsw.controller.motherNatureStrategy;

import it.polimi.ingsw.controller.Context;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Table;

/**
 * mother nature context class
 */
public class MotherNatureContext extends Context {
    private MotherNatureStrategy strategy;

    /**
     * Constructor
     * Initialize the strategy
     * @param strategy the strategy object
     */
    public MotherNatureContext(MotherNatureStrategy strategy){
        this.strategy = strategy;
    }

    /**
     * changes context
     * @param strategy the mother nature strategy
     */
    @Override
    public void changeContext(MotherNatureStrategy strategy){
        this.strategy = strategy;
    }

    /**
     * checks mother nature
     * @param table the current table
     * @param endPosition the end position of mother nature
     * @param player the current player
     * @return status
     * @throws ClientException if there are problems
     */
    @Override
    public int motherNatureControl(Table table, int endPosition, Player player) throws ClientException {
        return strategy.checkMotherNature(table, endPosition, player);
    }
}

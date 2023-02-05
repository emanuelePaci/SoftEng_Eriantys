package it.polimi.ingsw.controller.motherNatureStrategy;

import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Table;

/**
 * Mother nature strategy interface
 */
public interface MotherNatureStrategy {
    /**
     * checks mother nature
     * @param table the current table
     * @param endPosition the end mother nature position
     * @param player the current player
     * @return status
     * @throws ClientException if there are problems
     */
    int checkMotherNature(Table table, int endPosition, Player player) throws ClientException;
}

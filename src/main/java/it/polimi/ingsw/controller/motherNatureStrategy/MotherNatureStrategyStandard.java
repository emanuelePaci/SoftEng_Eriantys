package it.polimi.ingsw.controller.motherNatureStrategy;

import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.exceptions.ErrorType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Table;

/**
 * Mother Nature Strategy Standard class
 */
public class MotherNatureStrategyStandard implements MotherNatureStrategy {
    /**
     * Constructor
     */
    public MotherNatureStrategyStandard(){

    }
    /**
     * checks mother nature movements
     * @param table the current table
     * @param endPosition the end mother nature position
     * @param player the current player
     * @return num moves
     * @throws ClientException if there are problems
     */
    public int checkMotherNature(Table table, int endPosition, Player player) throws ClientException {
        int numMoves;

        int initPosition = table.getMotherPosition();
        if (endPosition <= initPosition)
            numMoves = table.getNumIsland() + endPosition - initPosition;
        else
            numMoves = endPosition - initPosition;

        if (player.getLastUsed() == null || numMoves > player.getLastUsed().getNumMovement())
            throw new ClientException(ErrorType.TOO_MUCH_MOVES);

        return numMoves;

    }
}

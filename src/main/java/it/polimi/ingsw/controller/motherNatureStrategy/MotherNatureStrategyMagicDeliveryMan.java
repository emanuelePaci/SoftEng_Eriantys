package it.polimi.ingsw.controller.motherNatureStrategy;

import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.exceptions.ErrorType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Table;

/**
 * Mother Nature Strategy Magic Delivery Man class
 */
public class MotherNatureStrategyMagicDeliveryMan implements MotherNatureStrategy {
    /**
     * Constructor
     */
    public MotherNatureStrategyMagicDeliveryMan(){

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

        if (numMoves > 2 + player.getLastUsed().getNumMovement())
            throw new ClientException(ErrorType.TOO_MUCH_MOVES);

        return numMoves;
    }
}

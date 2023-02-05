package it.polimi.ingsw.controller.islandStrategy;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

/**
 * Island Strategy interface
 */
public interface IslandStrategy {
    /**
     *  sets no color
     * @param noColor pawn color to avoid
     */
    void setNoColor(PawnColor noColor);

    /**
     * calculates the influence
     * @param island the current island
     * @param game the current game
     * @param owner the owner array
     * @param color the tower color
     * @param playerCandidate the candidate player
     */
    void calculateInfluence(Island island, Game game, String[] owner, TowerColor color, String[] playerCandidate);

    /**
     * sets player
     * @param player the player to be set
     */
    void setPlayer(Player player);
}


package it.polimi.ingsw.controller.professorStrategy;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.player.Player;

/**
 * Professor strategy interface
 */
public interface ProfessorStrategy {
    /**
     * checks professor
     * @param game the current game
     * @param player the current player
     * @param color the pawn color
     */
    void checkProfessor(Game game, Player player, PawnColor color);
}

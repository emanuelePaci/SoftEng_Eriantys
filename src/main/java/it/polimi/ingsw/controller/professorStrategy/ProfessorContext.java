package it.polimi.ingsw.controller.professorStrategy;

import it.polimi.ingsw.controller.Context;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.player.Player;

/**
 * Professor context class
 */
public class ProfessorContext extends Context {
    private ProfessorStrategy strategy;

    /**
     * Constructor
     * Initialize the strategy
     * @param strategy the strategy to be set
     */
    public ProfessorContext(ProfessorStrategy strategy){
        this.strategy = strategy;
    }

    /**
     * changes context
     * @param strategy the professor strategy
     */
    @Override
    public void changeContext(ProfessorStrategy strategy){
        this.strategy = strategy;
    }

    /**
     * checks professor 
     * @param game the current game
     * @param player the current player
     * @param color the student color
     */
    @Override
    public void professorControl(Game game, Player player, PawnColor color){
        strategy.checkProfessor(game, player, color);
    }
}

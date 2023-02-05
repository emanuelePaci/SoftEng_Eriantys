package it.polimi.ingsw.model.character;

import it.polimi.ingsw.controller.BoardHandler;
import it.polimi.ingsw.controller.Context;
import it.polimi.ingsw.controller.islandStrategy.IslandStrategy;
import it.polimi.ingsw.controller.islandStrategy.IslandStrategyCentaur;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Professor;
import it.polimi.ingsw.model.player.Player;



/**
 * Character group 4
 * CENTAUR
 * RETURN
 */
public class CharacterGroup4 extends Character{
    private IslandStrategy island = new IslandStrategyCentaur();

    /**
     * Constructor
     * @param type the character type
     */
    public CharacterGroup4 (CharacterType type){
        super(type);
    }

    /**
     * activate character
     * @param professorContext the professor context
     * @param motherNatureContext the mother nature context
     * @param islandContext the island context
     */
    @Override
    public void activateCharacter(Context professorContext, Context motherNatureContext, Context islandContext) {
        islandContext.changeContext(island);
    }

    /**
     * activate character
     * @param game the current game
     * @param player the current player
     * @param color the pawn color
     * @param context the context
     * @param boardHandler the board handler
     */
    @Override
    public void activateCharacter(Game game, Player player, PawnColor color, Context context, BoardHandler boardHandler) {
        Professor prof = game.getTable().findProfessor(color);
        for (Player p: game.getPlayers()){
                game.getTable().getBag().addStudent(p.getBoard().getDiningRoom().removeStudent(color, 3));
        }
        if (prof.getNumStudent()<3)
            prof.setNumStudent(0);
        else
            prof.setNumStudent(prof.getNumStudent() - 3);
    }
}

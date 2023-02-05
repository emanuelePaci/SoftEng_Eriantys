package it.polimi.ingsw.model.character;

import it.polimi.ingsw.controller.BoardHandler;
import it.polimi.ingsw.controller.Context;
import it.polimi.ingsw.controller.islandStrategy.IslandStrategy;
import it.polimi.ingsw.controller.islandStrategy.IslandStrategyMushroomHunter;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureStrategy;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureStrategyMagicDeliveryMan;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.player.Player;



/**
 * Character Group 1 class
 * MAGIC_DELIVERY_MAN
 * MUSHROOM_HUNTER
 */
public class CharacterGroup1 extends Character{

    private IslandStrategy islandStrategy = new IslandStrategyMushroomHunter();
    private MotherNatureStrategy motherNature = new MotherNatureStrategyMagicDeliveryMan();

    /**
     * Constructor
     * @param type the character type
     */
    public CharacterGroup1 (CharacterType type){
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
        motherNatureContext.changeContext(motherNature);
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
        islandStrategy.setNoColor(color);
        context.changeContext(islandStrategy);
    }
}

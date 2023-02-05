package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.islandStrategy.IslandStrategy;
import it.polimi.ingsw.exceptions.BagIsEmptyException;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.exceptions.ErrorType;
import it.polimi.ingsw.exceptions.GeneralSupplyFinishedException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.VirtualView;

/**
 * Character handler
 */
public class CharacterHandler {
    private TurnController turnController;
    private Game game;
    private VirtualView virtualView;
    private Context professorContext;
    private final Context motherNatureContext;
    private Context islandContext;
    private TableHandler tableHandler;
    private BoardHandler boardHandler;
    private IslandStrategy islandStrategy;

    /**
     * Constructor
     * Initialize the components
     * @param turnController the turn controller
     * @param game the current game
     * @param professorContext the professor context
     * @param motherNatureContext the mother nature context
     * @param islandContext the island context
     * @param virtualView the virtual view
     * @param tableHandler the table handler
     * @param boardHandler the board handler
     * @param islandStrategy the island strategy
     */
    public CharacterHandler(TurnController turnController, Game game, Context professorContext, Context motherNatureContext, Context islandContext, VirtualView virtualView, TableHandler tableHandler, BoardHandler boardHandler, IslandStrategy islandStrategy){
        this.game = game;
        this.virtualView = virtualView;
        this.turnController = turnController;
        this.professorContext = professorContext;
        this.motherNatureContext = motherNatureContext;
        this.islandContext = islandContext;
        this.tableHandler = tableHandler;
        this.boardHandler = boardHandler;
        this.islandStrategy = islandStrategy;
        this.game.setIslandController(islandStrategy);
    }

    /**
     *  uses the character
     * @param player the current player
     * @param characterPosition  the character position
     */
    public void useCharacter(Player player, int characterPosition){
        Character character = game.getTable().getCharacter(characterPosition);
        if (characterControls(player, character))
            return;

        if (character.getType().equals(CharacterType.KNIGHT))
            islandStrategy.setPlayer(player);
        character.activateCharacter(professorContext, motherNatureContext, islandContext);

        game.getRound().getTurn().setUsedCharacter(true);
    }

    /**
     * uses the character
     * @param player the current player
     * @param characterPosition the character position
     * @param islandPosition the island position
     */
    public void useCharacter(Player player, int characterPosition, int islandPosition){
        Character character = game.getTable().getCharacter(characterPosition);

        try {
            turnController.checkCharacter(game, player, character.getPrice(), character);
        } catch (ClientException e) {
            virtualView.printError(e, player.getNickname());
            return;
        }

        try {
            character.activateCharacter(game.getTable().getIsland(islandPosition), tableHandler);
        } catch (ClientException e) {
            return;
        }

        turnController.updateCharacter(game, player, character.getPrice(), character);

        game.getRound().getTurn().setUsedCharacter(true);
    }

    /**
     * uses the character
     * @param player the current player
     * @param characterPosition the character position
     * @param color the pawn color
     */
    public void useCharacter(Player player, int characterPosition, PawnColor color){
        Character character = game.getTable().getCharacter(characterPosition);
        if (characterControls(player, character))
            return;

        try {
            character.activateCharacter(game, player, color, islandContext, boardHandler);
        } catch (BagIsEmptyException e) {
            game.getRound().setLastRound();
        } finally {
            game.getRound().getTurn().setUsedCharacter(true);
        }
    }

    /**
     * uses the character
     * @param player the current player
     * @param characterPosition the character position
     * @param colors the array of colors
     */
    public void useCharacter(Player player, int characterPosition, int[] colors){
        Character character = game.getTable().getCharacter(characterPosition);
        if (characterControls(player, character))
            return;

        PawnColor[] color;
        int size = colors.length;
        color = new PawnColor[size];

        for (int i = 0; i < size; i++){
            color[i] = PawnColor.getColor(colors[i]);
        }

        try {
            character.activateCharacter(player, color, boardHandler);
        } catch (Exception ignored){}

        game.getRound().getTurn().setUsedCharacter(true);
    }

    /**
     * uses the character
     * @param player the current player
     * @param characterPosition the character position
     * @param islandPosition the island position
     * @param color the pawncolor
     */
    public void useCharacter(Player player, int characterPosition, int islandPosition, PawnColor color){
        Character character = game.getTable().getCharacter(characterPosition);
        if (characterControls(player, character))
            return;

        try {
            character.activateCharacter(game.getTable().getIsland(islandPosition), color);
        } catch (BagIsEmptyException emptyException) {
            game.getRound().setLastRound();
        } finally {
            game.getRound().getTurn().setUsedCharacter(true);
        }
    }

    /**
     * checks and updates characters
     * @param player the current player
     * @param character the character you want to use
     * @return true if everything is okay
     */
    public boolean characterControls(Player player, Character character){
        try {
            turnController.checkCharacter(game, player, character.getPrice(), character);
        } catch (ClientException e) {
            virtualView.printError(e, player.getNickname());
            return true;
        }

        turnController.updateCharacter(game, player, character.getPrice(), character);
        return false;
    }
}

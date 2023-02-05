package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.board.DiningRoom;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.model.player.Player;

/**
 * Turn controller class
 */
public class TurnController {
    /**
     * Checks if the character has already been used and if the turn is correct
     * @param game the current game
     * @param player the current player
     * @param costo the current cost
     * @param character the character you want to use
     * @throws ClientException if there are problems
     */
    public void checkCharacter(Game game, Player player, int costo, Character character) throws ClientException {
        if(game.getRound().getTurn().isUsedCharacter())
            throw new ClientException(ErrorType.ALREADY_USED_CHARACTER);

        checkCorrectTurn(game.getRound().getTurn(), player);
        if (player.getState().equals(PlayerState.PLANNING))
            throw new ClientException(ErrorType.WRONG_PHASE);

        enoughMoney(player, costo);
    }

    /**
     * Updates the character cost
     * @param game the current game
     * @param player the current player
     * @param costo the cost
     * @param character the character you want to use
     */
    public void updateCharacter(Game game, Player player, int costo, Character character) {
        player.removeCoin(costo);
        game.getTable().addCoin(costo);

        if (!character.isUsed()) {
            try {
                game.getTable().withdrawCoin();
            } catch (GeneralSupplyFinishedException ignored) {
            }
            character.firstUse();
        }
    }

    /**
     * checks if the turn and the action are correct
     * @param turn the current turn
     * @param player the current player
     * @param state the player state
     * @throws ClientException if there are problems
     */
    public void checkPermission(Turn turn, Player player, PlayerState state) throws ClientException {
        checkCorrectTurn(turn, player);
        checkCorrectAction(player, state);
    }

    /**
     * checks if you have other movements available for this turn
     * @param turn the current turn
     * @throws ClientException if there are problems
     */
    public void canMove(Turn turn) throws ClientException {
        if (turn.getRemainingMovements() == 0)
            throw new ClientException(ErrorType.WRONG_ACTION);

        turn.updateRemainingMovements();
    }

    /**
     * checks if there are remaining movements
     * @param turn the current turn
     * @throws ClientException if there are problems
     */
    public void canMoveMother(Turn turn) throws ClientException {
        if (turn.getRemainingMovements() > 0)
            throw new ClientException(ErrorType.WRONG_ACTION2);
    }

    /**
     * checks if the player has enough money
     * @param player the current player
     * @param costo the cost of the character
     * @throws ClientException if there are problems
     */
    public void enoughMoney(Player player, int costo) throws ClientException {
        if (player.getNumCoin() < costo)
            throw new ClientException(ErrorType.NOT_ENOUGH_MONEY, costo);
    }

    /**
     * checks if the dining is full of a specific color
     * @param dining the current dining
     * @param color the color to be counted
     * @throws ClientException if there are problems
     */
    public void checkFullDining(DiningRoom dining, PawnColor color) throws ClientException {
        if (dining.count(color) == 10)
            throw new ClientException(ErrorType.MAX_STUDENT_REACHED, color);
    }

    /**
     * checks if the turn is correct
     * @param turn the current turn
     * @param player the current player
     * @throws ClientException if there are problems
     */
    public void checkCorrectTurn(Turn turn, Player player) throws ClientException {
        if(!turn.getCurrentPlayer().equals(player))
            throw new ClientException(ErrorType.NOT_YOUR_TURN);
    }

    /**
     * checks if the action is correct
     * @param player the current player
     * @param state the current player state
     * @throws ClientException if there are problems
     */
    public void checkCorrectAction(Player player, PlayerState state) throws ClientException {
        if (!player.getState().equals(state)) {
            switch (player.getState()){
                case PLANNING: throw new ClientException(ErrorType.WRONG_PHASE);
                case ACTION: throw new ClientException(ErrorType.WRONG_PHASE2);
                case ENDTURN: throw new ClientException(ErrorType.WRONG_PHASE3);
            }
        }
    }

    /**
     * checks if the cloud has been already chosen
     * @param round the current round
     * @param position the position
     * @throws ClientException if there are problems
     */
    public void checkCloud(Round round, int position) throws ClientException {
        if (round.getCloudChosen().contains(position))
            throw new ClientException(ErrorType.ALREADY_CHOSEN_CLOUD);
    }
}

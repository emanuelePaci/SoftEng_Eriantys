package it.polimi.ingsw.server;

import it.polimi.ingsw.client.viewUtilities.GameInfo;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.player.Player;

import java.util.*;

/**
 * This class manages the virtual view of the Server, reading input and sending specific messages to the clients
 */
public class VirtualView {
    private Controller controller;
    private List<ClientHandler> clientHandlers;

    /**
     * Constructor
     * Initialize the controller and the client handlers list
     * @param controller the controller to set
     */
    public VirtualView(Controller controller){
        this.controller=controller;
        clientHandlers= new LinkedList<>();
    }

    /**
     * Returns the controller
     * @return the controller
     */
    public Controller getController() {return controller;}

    /**
     * Adds a client handler to the list client handlers
     * @param clientHandler the client handler to add
     */
    public void addClientHandler(ClientHandler clientHandler){
        clientHandlers.add(clientHandler);
    }

    /**
     * Returns the available colors for the towers
     * @return the available colors for the towers
     */
    public List<TowerColor> getAvailableColor(){
        return controller.getAvailableColor();
    }

    /**
     * Elaborates the request, from a client, to give the cloud at the specified position at the player with the specified nickname
     * @param cloudPosition the position of the cloud
     * @param playerNickname the nickname of the player that does the request
     */
    public void cloudChosen(int cloudPosition, String playerNickname){
        controller.chooseCloud(controller.getPlayerByNickname(playerNickname),cloudPosition);
    }

    /**
     * Elaborates the request, from a client, to move Mother nature on the island at the specified position
     * @param endPosition the position of the island where the client wants to set Mother Nature
     * @param playerNickname the nickname of the player that does the request
     */
    public void moveMotherNature(int endPosition, String playerNickname){
        controller.moveMotherNature(controller.getPlayerByNickname(playerNickname),endPosition);
    }

    /**
     * Elaborates the request, from a client, to move the specified student to the specified island
     * @param islandPosition the position of the island
     * @param color the color of the student to move
     * @param playerNickname the nickname of the player that does the request
     */
    public void moveStudentToIsland(int islandPosition, PawnColor color, String playerNickname){
        controller.useStudentIsland(controller.getPlayerByNickname(playerNickname), color, islandPosition);
    }

    /**
     * Elaborates the request, from a client, to move the specified student to the dining room of the player
     * @param color the color of the student to move
     * @param playerNickname the nickname of the player that does the request
     */
    public void moveStudentToDining(PawnColor color, String playerNickname){
        controller.useStudentDining(controller.getPlayerByNickname(playerNickname), color);
    }

    /**
     * Elaborates the request, from a client, to use the Assistant card of the specified weight
     * @param weight the weight of the assistant card
     * @param playerNickname the nickname of the player that does the request
     */
    public void useAssistant(int weight, String playerNickname){
        controller.useAssistant(weight, controller.getPlayerByNickname(playerNickname));
    }

    /**
     * Elaborates the request, from a client, to use the Character card at the specified position.
     * The Character to use is one of them: JESTER, MINSTREL
     * @param colors the array of the color of the student to move.
     *               If the Character is the JESTER, the first 3 colors are the students present at the entrance, the others are the students on the Character card.
     *               If the Character is the MINSTREL, the first 2 colors are the students present at the entrance, the other 2 are students in the dining room
     * @param characterPosition the position of the Character card to play
     * @param playerNickname the nickname of the player that does the request
     */
    public void useCharacter(int[] colors, int characterPosition, String playerNickname){
        controller.useCharacter(controller.getPlayerByNickname(playerNickname), characterPosition, colors);
    }

    /**
     * Elaborates the request, from a client, to use the Character card at the specified position.
     * The Character to use is one of them: MONK
     * @param islandPosition the position of the island where the card will activate its effect
     * @param color the color of the student to move
     * @param characterPosition the position of the Character card to play
     * @param playerNickname the nickname of the player that does the request
     */
    public void useCharacter(int islandPosition, PawnColor color , int characterPosition, String playerNickname){
        controller.useCharacter(controller.getPlayerByNickname(playerNickname), characterPosition, islandPosition, color);
    }

    /**
     * Elaborates the request, from a client, to use the Character card at the specified position.
     * The Character to use is one of them: SPOILED_PRINCESS, THIEF, MUSHROOM_HUNTER
     * @param color the color of the student to move
     * @param characterPosition the position of the Character card to play
     * @param playerNickname the nickname of the player that does the request
     */
    public void useCharacter(PawnColor color, int characterPosition, String playerNickname){
        controller.useCharacter(controller.getPlayerByNickname(playerNickname), characterPosition, color);
    }

    /**
     * Elaborates the request, from a client, to use the Character card at the specified position.
     * The Character to use is one of them: HERALD, GRANDMOTHER_HERBS
     * @param islandPosition the position of the island where the card will activate its effect
     * @param characterPosition the position of the Character card to play
     * @param playerNickname the nickname of the player that does the request
     */
    public void useCharacter(int islandPosition, int characterPosition, String playerNickname){
        controller.useCharacter(controller.getPlayerByNickname(playerNickname), characterPosition, islandPosition);
    }

    /**
     * Elaborates the request, from a client, to use the Character card at the specified position.
     * The Character to use is one of them: MAGIC_DELIVERY_MAN, CENTAUR, KNIGHT, FARMER
     * @param characterPosition the position of the Character card to play
     * @param playerNickname the nickname of the player that does the request
     */
    public void useCharacter(int characterPosition, String playerNickname){
        controller.useCharacter(controller.getPlayerByNickname(playerNickname), characterPosition);
    }

    /**
     * Inform the controller about the number of player and the difficulty of the game
     * @param numPlayer the number of player of this game
     * @param expert true if the game will be played in expert mode
     * @param playerNickname the nickname of the player that provides the information
     */
    public void setUpGameInfo(int numPlayer, boolean expert, String playerNickname){
        controller.setExpertMode(expert);
        controller.setNumPlayer(numPlayer);

        colorSetUp(playerNickname);
    }

    /**
     * Starts the request of the color of the Tower available
     * @param nickname the nickname of the player that will choose the color
     */
    public void colorSetUp(String nickname){
        getClientHandlerByNickname(nickname).colorSetUp(false);
    }

    /**
     * Controls if the color chosen is available: if it is, creates a new player, otherwise asks to choose again another color
     * @param color the color of the tower chosen
     * @param playerNickname the nickname of the player that chose the color
     */
    public synchronized void setUpPlayerColor(TowerColor color, String playerNickname){
        if(getAvailableColor().contains(color))
            controller.addPlayer(new Player(playerNickname, color));
        else
            getClientHandlerByNickname(playerNickname).colorSetUp(true);
    }

    /**
     * Returns the client handler associated to the specified nickname
     * @param nickname the nickname of the player
     * @return the client handler associated to the specified nickname
     */
    public ClientHandler getClientHandlerByNickname(String nickname){
        for (ClientHandler cl: clientHandlers){
            if(nickname.equals(cl.getPlayerNickname()))
                return cl;
        }
        return null;
    }

    /**
     * Creates, for each client handler, the info of the game
     * @param game the current game
     */
    public void printGameBoard(Game game){
        for (ClientHandler c : clientHandlers)
            c.printGameBoard(new GameInfo(game, c.getPlayerNickname()));
    }

    /**
     * Informs the specific client handler if any exception occurs
     * @param exception the exception occurred
     * @param player the nickname associated with the client handler to inform
     */
    public void printError(ClientException exception, String player){
        if (getClientHandlerByNickname(player) != null)
            getClientHandlerByNickname(player).printError(exception);
    }

    /**
     * Informs all the client handler but the one associated with the specified nickname if any interruption occurred
     * @param nickname the player to not inform
     */
    public void printInterrupt(String nickname){
        for (ClientHandler c : clientHandlers)
            if (!nickname.equals(c.getPlayerNickname()) && c.connectionAlive()) {
                c.printInterrupt(nickname, false);
        }
    }

    /**
     * Informs all the client handlers about the nickname of the winner (or the nicknames of the winners, if there is a draw )
     * @param winner1 the nickname of the winner
     * @param winner2 the nickname of the second winner, it is null if there is not a draw
     */
    public void printWinner(String winner1, String winner2){
        for (ClientHandler c : clientHandlers) {
            c.printWinner(winner1, winner2, c.getPlayerNickname());
            c.setDisconnected();
        }
    }

}

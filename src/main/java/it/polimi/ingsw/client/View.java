package it.polimi.ingsw.client;

import it.polimi.ingsw.client.viewUtilities.GameInfo;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.List;

/**
 * Generic View interface
 */
public interface View {

    /**
     * Asks the ServerIp, then gives it to the ServerHandler which will establish the connection between server and Client
     */
    void start();

    /**
     * Asks the client if it wants to start a new game or prefers to enter a lobby created previously by another player
     */
    void gameSetUp();

    /**
     * Creates a new lobby if there are no other present, otherwise prints the list of available lobbies and asks which one the client wants to join
     * @param lobbies the list of the names of the lobbies present
     * @param firstLobby a flag which is true only if there are no lobbies
     */
    void refreshLobbies(List<String[]> lobbies, boolean firstLobby);

    /**
     * Informs the client that the lobby it chooses is full, then prints the list of the lobbies available
     */
    void fullLobby();

    /**
     * Asks at the client, only if it creates a new lobby, the number of player and the difficulty of the game it wants to create
     */
    void initialSetUp();

    /**
     * Asks the client its nickname
     * @param requestAgain if is it true, the client inserts a nickname which has already been chosen, so it has to choose another
     */
    void playerSetUp(boolean requestAgain);

    /**
     * Asks at the client which color wants for its towers
     * @param tower the list of the colors of the towers available
     * @param requestAgain if is it true, the client inserts a color which has already been chosen, so it has to choose another
     */
    void colorSetUp(List<TowerColor> tower, boolean requestAgain);

    /**
     * Prints at screen the actual status of the game
     * @param gameInfo contains the information of the status of the game
     */
    void printGameBoard(GameInfo gameInfo);

    /**
     * Asks at the client which action it would like to do among the actions available
     */
    void choseAction();

    /**
     * Clears the buffer of a client while it is not its turn
     */
    void bufferClearer();

    /**
     * Prints a description of some possible exception, if one of them occurs
     * @param clientException the exception occurred
     */
    void printError(ClientException clientException);

    /**
     * Prints the winner of the game, then calls {@link #newGame(String)} method
     * @param winner1 the nickname of the winner
     * @param winner2 the nickname of the eventual second winner in case of draw. It may be null if there is only one winner
     * @param nickname the nickname of the client
     */
    void printWinner(String winner1, String winner2, String nickname);

    /**
     * Informs the client, while it is choosing a lobby, if that lobby is no more available because of a disconnection of one of the player in the lobby, otherwise calls {@link #newGame(String)} method
     * @param nickname the nickname of the player disconnected
     * @param notEntered true if the client is not entered in the lobby
     */
    void printInterrupt(String nickname, boolean notEntered);

    /**
     * Asks the client if it wants to start a new game
     * @param nickname the nickname of the player disconnected, if present. It may be null
     */
    void newGame(String nickname);

    /**
     * Informs the client that the game is ended due to a problem with the server
     */
    void printServerDown();

    /**
     * Stops the cleaning of the buffer
     */
    void setClearer(boolean condition);
}


package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ACKControl;
import it.polimi.ingsw.client.viewUtilities.GameInfo;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.service.ErrorMessage;
import it.polimi.ingsw.network.messages.service.InterruptedGameMessage;
import it.polimi.ingsw.network.messages.setUp.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

/**
 * A client handler which manages the communication the client, there is one client handler for each client
 */
public class ClientHandler extends Thread implements NetworkHandler{
    private final Socket socket;
    private String playerNickname;
    private final LobbyHandler lobbyHandler;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private VirtualView virtualView;
    private boolean isConnected;

    /**
     * Constructor
     * Sets the socket, the lobby handler, the player nickname and the ack control
     * @param socket the socket
     * @param playerNickname the player nickname
     * @param lobbyHandler the lobby handler
     */
    public ClientHandler(Socket socket, String playerNickname, LobbyHandler lobbyHandler){
        this.socket=socket;
        this.lobbyHandler = lobbyHandler;
        isConnected = true;
        this.playerNickname = playerNickname;

        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            socket.setSoTimeout(25000);
        } catch (IOException e) {
        }

        ACKControl aCKcontrol= new ACKControl(this, true);
        aCKcontrol.start();
    }

    /**
     * Starts listening
     */
    public void run(){
        listen();
    }

    /**
     * While the connection is present it listens for any message that could arrive and, based on the message's type, it performs different actions
     */
    public void listen(){
        playerSetUp(false);

        while (isConnected) {
            try {
                GenericMessage message = (GenericMessage) input.readObject();
                if (message.getType() == MessageType.Lobby){
                    LobbyMessage lobbyMessage = (LobbyMessage) message;
                    lobbyMessage.action(lobbyHandler, this);
                } else {
                    message.action(virtualView, playerNickname);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(getPlayerNickname() + " has disconnected");
                if (isConnected){
                    isConnected = false;

                    lobbyHandler.terminateLobby(virtualView, this);
                    if (virtualView != null)
                        virtualView.printInterrupt(getPlayerNickname());
                }
                endConnection();
            }
        }
    }

    /**
     * Sends the given message
     * @param message the generic message
     */
    public synchronized void send(GenericMessage message) {
        if(isConnected){
            try {
                output.writeUnshared(message);
                output.flush();
                output.reset();
            }
            catch(IOException e){
                System.out.println(getPlayerNickname() + "disconnected during Message sending");
                if (isConnected){
                    isConnected = false;

                    lobbyHandler.terminateLobby(virtualView, this);
                    if (virtualView != null)
                        virtualView.printInterrupt(getPlayerNickname());
                }
                endConnection();
            }
        }
    }

    /**
     * Sends gameSetUp message
     */
    public void gameSetUp(){
        send(new GameSetUp());
    }

    /**
     * Game setup method
     * @param fullGame if the game is full
     */
    public void gameSetUp(boolean fullGame){
        send(new GameSetUp(fullGame));
    }

    /**
     * Initial setup method
     */
    public void initialSetUp(){
        send(new InitialSetUp());
    }

    /**
     * Player setup method
     * @param requestAgain true if the player setup has to be set again
     */
    public void playerSetUp(boolean requestAgain){
        send(new PlayerSetUp(requestAgain));
    }

    /**
     * sets the tower color
     * @param requestAgain true if the color setup has to be set again
     */
    public void colorSetUp(boolean requestAgain){
        List<TowerColor> towerColor;
        towerColor = virtualView.getAvailableColor();
        send(new ColorSetUp(towerColor, requestAgain));
    }

    /**
     * gets player nickname
     * @return the player nickname
     */
    public String getPlayerNickname() {
        return playerNickname;
    }

    /**
     * sets player nickname
     * @param playerNickname the player nickname
     */
    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    /**
     * prints the game board
     * @param gameInfo the game info
     */
    public void printGameBoard(GameInfo gameInfo){
        send(new GameInfoMessage(gameInfo));
    }

    /**
     * prints the error message
     * @param error the client exception error
     */
    public void printError(ClientException error){
        send (new ErrorMessage(error));
    }

    /**
     * prints interrupt
     * @param nickname the player nickname
     * @param notEntered if the player has not entered yet
     */
    public void printInterrupt(String nickname, boolean notEntered){
        if (notEntered)
            send(new InterruptedGameMessage(nickname, true));
        else
            send(new InterruptedGameMessage(nickname));
    }

    /**
     * gets true if the connection is alive
     * @return true if the connection is alive
     */
    public boolean connectionAlive() {
        return isConnected;
    }

    /**
     * ends the connection
     */
    public void setDisconnected() {
        isConnected = false;
        lobbyHandler.terminateLobby(virtualView, this);
        endConnection();
    }

    /**
     * prints the winner
     * @param winner1 the winner player
     * @param winner2 the other player
     * @param nickname the winner nickname
     */
    public void printWinner(String winner1, String winner2, String nickname){
        send(new WinnerMessage(winner1, winner2, nickname));
    }

    /**
     * closes the connection
     */
    public void endConnection() {
        try {
            socket.close();
            isConnected = false;
        } catch (IOException e) {
        }
    }

    /**
     * sets the virtual view
     * @param virtualView the virtual view
     */
    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * refreshes the lobbies
     * @param lobbies the lobby list
     * @param firstLobby the first lobby
     */
    public void refreshLobbies(List<String[]> lobbies, boolean firstLobby) {
        send(new RefreshMessage(lobbies, firstLobby));
    }
}



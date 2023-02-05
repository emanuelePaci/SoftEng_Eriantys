package it.polimi.ingsw.network.messages.setUp;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.ControllerViewMessage;
import it.polimi.ingsw.network.LobbyMessage;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;
import java.util.List;

/**
 * Game setup class
 */
public class GameSetUp implements Serializable, LobbyMessage, ControllerViewMessage {
    MessageType type;

    boolean newGame;

    boolean fullGame = false;
    int lobby;

    /**
     * Constructor
     * Initialize the message type
     */
    public GameSetUp(){
        type = MessageType.ControllerView;
    }

    /**
     * Constructor
     * Initialize the lobbies and the boolean full game
     * @param fullGame the full game boolean
     */
    public GameSetUp(boolean fullGame){
        this.fullGame = fullGame;
    }

    /**
     * Constructor
     * @param newGame the new game boolean
     * @param lobby the number of lobby
     */
    public GameSetUp(boolean newGame, int lobby){
        type = MessageType.Lobby;
        this.newGame = newGame;
        this.lobby = lobby;
    }

    /**
     * Gets the message type
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return type;
    }

    /**
     * action
     * @param view the view
     */
    @Override
    public void action(View view) {
        if (!fullGame)
            view.gameSetUp();
        else
            view.fullLobby();
    }

    /**
     * action
     * @param lobbyHandler the lobby handler
     * @param clientHandler the client handler
     */
    @Override
    public void action(LobbyHandler lobbyHandler, ClientHandler clientHandler) {
        if (newGame)
            lobbyHandler.newClient(clientHandler);
        else
            lobbyHandler.addClient(lobby, clientHandler);
    }

    /**
     * action
     * @param virtualView the virtual view
     * @param playerNickname the player nickname
     */
    @Override
    public void action(VirtualView virtualView, String playerNickname) {
    }
}

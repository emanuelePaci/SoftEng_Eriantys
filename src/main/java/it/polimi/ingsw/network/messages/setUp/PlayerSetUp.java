package it.polimi.ingsw.network.messages.setUp;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.ControllerViewMessage;
import it.polimi.ingsw.network.LobbyMessage;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;

/**
 * Player setup class
 */
public class PlayerSetUp implements Serializable, ControllerViewMessage, LobbyMessage {
    private MessageType type;
    private String nickname;
    private boolean requestAgain;

    /**
     * Constructor
     * Initialize the request again boolean and the message type
     * @param requestAgain the request again boolean
     */
    public PlayerSetUp(boolean requestAgain){
        this.requestAgain = requestAgain;
        type = MessageType.ControllerView;
    }

    /**
     * Constructor
     * Initialize the nickname and the message type
     * @param nickname the nickname
     */
    public PlayerSetUp(String nickname){
        this.nickname=nickname;
        type = MessageType.Lobby;
    }

    /**
     * View player setup
     * @param view the view
     */
    @Override
    public void action(View view) {
        view.playerSetUp(requestAgain);
    }

    /**
     * gets the message type
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return type;
    }

    /**
     * action
     * @param virtualView the virtual view
     * @param playerNickname the player nickname
     */
    @Override
    public void action(VirtualView virtualView, String playerNickname) {
    }

    /**
     * Sets the player nickname
     * @param lobbyHandler the lobby handler
     * @param clientHandler the client handler
     */
    @Override
    public void action(LobbyHandler lobbyHandler, ClientHandler clientHandler) {
        lobbyHandler.setPlayerNickname(nickname, clientHandler);
    }
}

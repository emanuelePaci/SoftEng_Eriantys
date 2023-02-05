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
 * Refresh Message class
 */
public class RefreshMessage implements Serializable, LobbyMessage, ControllerViewMessage {
    MessageType type;
    List<String[]> lobbies;

    boolean firstLobby = false;

    /**
     * Constructor
     * Initialize the message type
     */
    public RefreshMessage(){
        type = MessageType.Lobby;
    }

    /**
     * Constructor
     * Initialize the message type, the boolean first lobby and the list of lobbies
     * @param lobbies the list of lobbies
     * @param firstLobby the first lobby
     */
    public RefreshMessage(List<String[]> lobbies, boolean firstLobby){
        type = MessageType.ControllerView;
        this.firstLobby = firstLobby;
        this.lobbies = lobbies;
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
     * refresh the lobbies
     * @param view the view
     */
    @Override
    public void action(View view) {
        view.refreshLobbies(lobbies, firstLobby);
    }

    /**
     * action method
     * @param virtualView the virtual view
     * @param playerNickname the player nickname
     */
    @Override
    public void action(VirtualView virtualView, String playerNickname) {

    }

    /**
     * refresh the lobbies
     * @param lobbyHandler the lobby handler
     * @param clientHandler the client handler
     */
    @Override
    public void action(LobbyHandler lobbyHandler, ClientHandler clientHandler) {
        lobbyHandler.refreshLobbies(clientHandler);
    }
}

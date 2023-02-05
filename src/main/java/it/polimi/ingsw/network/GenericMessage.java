package it.polimi.ingsw.network;


import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

/**
 * Generic message interface
 */
public interface GenericMessage {
    /**
     * gets the message type
     * @return the message type
     */
    MessageType getType();

    /**
     * action
     * @param view the view
     */
    void action(View view);

    /**
     * action
     * @param virtualView the virtual view
     * @param playerNickname the player nickname
     */
    void action(VirtualView virtualView, String playerNickname);

    /**
     * action
     * @param lobbyHandler the lobby handler
     * @param clientHandler the client handler
     */
    void action(LobbyHandler lobbyHandler, ClientHandler clientHandler);
}

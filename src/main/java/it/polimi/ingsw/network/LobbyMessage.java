package it.polimi.ingsw.network;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;

/**
 * Lobby message interface
 */
public interface LobbyMessage extends GenericMessage{
    /**
     * action
     * @param lobbyHandler the lobby handler
     * @param clientHandler the client handler
     */
    void action(LobbyHandler lobbyHandler, ClientHandler clientHandler);
}

package it.polimi.ingsw.network.messages.service;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.network.ControllerViewMessage;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;

/**
 * Error message class
 */
public class ErrorMessage implements Serializable, ControllerViewMessage {
    private MessageType type;
    private ClientException clientException;

    /**
     * Constructor
     * Initialize the message type
     * @param exception the exception
     */
    public ErrorMessage(ClientException exception){
        type = MessageType.ControllerView;
        clientException = exception;
    }

    /**
     * View print error
     * @param view the view
     */
    @Override
    public void action(View view) {
        view.printError(clientException);
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
     * action
     * @param lobbyHandler the lobby handler
     * @param clientHandler the client handler
     */
    @Override
    public void action(LobbyHandler lobbyHandler, ClientHandler clientHandler) {

    }

    /**
     * Gets the message type
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return type;
    }
}

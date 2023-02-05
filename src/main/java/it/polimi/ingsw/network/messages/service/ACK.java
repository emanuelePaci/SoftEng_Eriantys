package it.polimi.ingsw.network.messages.service;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.ControllerViewMessage;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.ViewControllerMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;

/**
 * Ack class
 */
public class ACK implements Serializable, ViewControllerMessage, ControllerViewMessage {
    private MessageType type;

    /**
     * Constructor
     * Initialize the message type
     * @param isServer boolean
     */
    public ACK(boolean isServer){
        if(isServer)
            type = MessageType.ControllerView;
        else
            type = MessageType.ViewController;
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
     * @param view the view
     */
    @Override
    public void action(View view) {
        //When using ACK this method is never called
    }

    /**
     * action
     * @param virtualView the virtual view
     * @param playerNickname the player nickname
     */
    @Override
    public void action(VirtualView virtualView, String playerNickname) {
        //When using ACK this method is never called
    }

    /**
     * action
     * @param lobbyHandler the lobby handler
     * @param clientHandler the client handler
     */
    @Override
    public void action(LobbyHandler lobbyHandler, ClientHandler clientHandler) {
        //When using ACK this method is never called
    }
}

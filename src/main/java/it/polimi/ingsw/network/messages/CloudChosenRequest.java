package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.ViewControllerMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;

/**
 * Cloud chosen request
 */
public class CloudChosenRequest implements Serializable, ViewControllerMessage {
    private MessageType type;
    private int cloudPosition;

    /**
     * Constructor
     * @param cloudPosition thd cloud position
     */
    public CloudChosenRequest(int cloudPosition){
        type = MessageType.ViewController;
        this.cloudPosition=cloudPosition;
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

    }

    /**
     * action
     * @param virtualView the virtual view
     * @param playerNickname the player nickname
     */
    @Override
    public void action(VirtualView virtualView, String playerNickname) {
        virtualView.cloudChosen(cloudPosition, playerNickname);
    }

    /**
     * action
     * @param lobbyHandler the lobby handler
     * @param clientHandler the client handler
     */
    @Override
    public void action(LobbyHandler lobbyHandler, ClientHandler clientHandler) {

    }
}

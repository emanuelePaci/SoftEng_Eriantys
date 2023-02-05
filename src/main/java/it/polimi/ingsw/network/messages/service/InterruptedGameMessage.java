package it.polimi.ingsw.network.messages.service;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.ControllerViewMessage;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.ModelViewMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;

/**
 * Interrupted game message class
 */
public class InterruptedGameMessage  implements Serializable, ControllerViewMessage, ModelViewMessage {
    private String nickname;
    private MessageType type;
    private boolean notEntered;

    /**
     * Constructor
     * Initialize the nickname, the message type and not entered boolean
     * @param nickname the player nickname
     * @param notEntered the not entered boolean
     */
    public InterruptedGameMessage(String nickname, boolean notEntered) {
        this.nickname = nickname;
        type = MessageType.ControllerView;
        this.notEntered = notEntered;
    }

    /**
     * Constructor
     * @param nickname the player nickname
     */
    public InterruptedGameMessage(String nickname){
        type = MessageType.ModelView;
        this.nickname = nickname;
        this.notEntered = false;
    }

    /**
     * View print interrupt
     * @param view the view
     */
    @Override
    public void action(View view) {
        view.printInterrupt(nickname, notEntered );
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
     * gets the message type
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return type;
    }
}

package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.ControllerViewMessage;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;

/**
 * Winner message class
 */
public class WinnerMessage implements Serializable, ControllerViewMessage {
    private String winner1;
    private String winner2;
    private String nickname;
    private MessageType type;

    /**
     * Constructor
     * Initialize the winner1, the winner2 and the nickname
     * @param winner1 the first player
     * @param winner2 the second player
     * @param nickname the nickname
     */
    public WinnerMessage(String winner1, String winner2, String nickname){
        this.winner1=winner1;
        this.winner2=winner2;
        this.nickname=nickname;
        type = MessageType.ControllerView;
    }

    /**
     * action
     * @param view the view
     */
    @Override
    public void action(View view) {
        view.printWinner(winner1, winner2, nickname);
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

package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.ViewControllerMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;

/**
 * Move student to island request
 */
public class MoveStudentToIslandRequest implements Serializable, ViewControllerMessage {
    private MessageType type;
    private int islandPosition;
    private PawnColor color;

    /**
     * Constructor
     * Initialize the island position and the pawn color
     * @param islandPosition the island position
     * @param color the pawn color
     */
    public MoveStudentToIslandRequest (int islandPosition, PawnColor color){
        this.islandPosition = islandPosition;
        this.color=color;
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

    }

    /**
     * action
     * @param virtualView the virtual view
     * @param playerNickname the player nickname
     */
    @Override
    public void action(VirtualView virtualView, String playerNickname) {
        virtualView.moveStudentToIsland(islandPosition, color, playerNickname);
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

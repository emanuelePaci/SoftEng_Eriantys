package it.polimi.ingsw.network.messages.setUp;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.ControllerViewMessage;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.ViewControllerMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;
import java.util.List;

/**
 * Color setup class
 */
public class ColorSetUp implements Serializable, ControllerViewMessage, ViewControllerMessage {
    private MessageType type;
    List<TowerColor> towerColor;
    TowerColor chosenColor;
    private boolean requestAgain;

    /**
     * Constructor
     * Initialize the tower color list and the request again boolean
     * @param towerColor the tower color list
     * @param requestAgain the request again boolean
     */
    public ColorSetUp(List<TowerColor> towerColor, boolean requestAgain){
        this.requestAgain=requestAgain;
        type = MessageType.ControllerView;
        this.towerColor = towerColor;
    }

    /**
     * Constructor
     * Initialize the chosen tower color
     * @param color the chosen tower color
     */
    public ColorSetUp(TowerColor color){
        this.chosenColor=color;
        type = MessageType.ViewController;

    }

    /**
     * Gets the message type
     * @return the message type
     */
    @Override
    public MessageType getType() {
        return type;
    }

    /**
     * View color setup
     * @param view the view
     */
    @Override
    public void action(View view) {
        view.colorSetUp(towerColor, requestAgain);
    }

    /**
     * Virtual view set up player color
     * @param virtualView the virtual view
     * @param playerNickname the player nickname
     */
    @Override
    public void action(VirtualView virtualView, String playerNickname) {
        virtualView.setUpPlayerColor(chosenColor, playerNickname);
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
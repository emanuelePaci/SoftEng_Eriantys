package it.polimi.ingsw.network.messages.setUp;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.network.ControllerViewMessage;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.ViewControllerMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;

/**
 * Initial setup class
 */
public class InitialSetUp implements Serializable, ControllerViewMessage, ViewControllerMessage {
    private MessageType type;
    private int numPlayer;
    private boolean expert;

    /**
     * Constructor
     * Initialize the message type
     */
    public InitialSetUp(){
        type = MessageType.ControllerView;
    }

    /**
     * Constructor
     * Initialize the number of players, the expert mode and the message type
     * @param numPlayer the number of players
     * @param expert true if expert mode
     */
    public InitialSetUp(int numPlayer, boolean expert){
        this.numPlayer=numPlayer;
        this.expert=expert;
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
     * View initial setup
     * @param view the view
     */
    @Override
    public void action(View view) {
        view.initialSetUp();
    }

    /**
     * Setup game info
     * @param virtualView the virtual view
     * @param playerNickname the player nickname
     */
    @Override
    public void action(VirtualView virtualView, String playerNickname) {
        virtualView.setUpGameInfo(numPlayer, expert, playerNickname);
    }

    /**
     * Action method
     * @param lobbyHandler the lobby handler
     * @param clientHandler the client handler
     */
    @Override
    public void action(LobbyHandler lobbyHandler, ClientHandler clientHandler) {

    }
}

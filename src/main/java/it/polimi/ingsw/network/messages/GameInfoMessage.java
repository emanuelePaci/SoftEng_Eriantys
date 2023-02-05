package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.viewUtilities.GameInfo;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.ModelViewMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;

/**
 * Game info message
 */
public class GameInfoMessage implements Serializable, ModelViewMessage, Runnable {
    private MessageType type;
    private GameInfo gameInfo;
    private View view;
    /**
     * public thread
     */
    public Thread thread = null;

    /**
     * Constructor
     * @param gameInfo the game info
     */
    public GameInfoMessage(GameInfo gameInfo){
        this.gameInfo = gameInfo;
        type = MessageType.ModelView;
    }

    /**
     * action
     * @param view the view
     */
    @Override
    public void action(View view) {
        this.view = view;
        if (!gameInfo.getCurrentPlayer().equals(gameInfo.getFrontPlayer())) {
            thread = new Thread(this);
            thread.start();
        } else {
            view.printGameBoard(gameInfo);
            view.choseAction();
        }
    }

    /**
     * prints the game board
     */
    public void run(){
        view.printGameBoard(gameInfo);
        view.setClearer(true);
        view.bufferClearer();
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
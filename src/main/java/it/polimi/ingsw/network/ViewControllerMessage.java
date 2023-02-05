package it.polimi.ingsw.network;

import it.polimi.ingsw.server.VirtualView;

/**
 * View controller message interface
 */
public interface ViewControllerMessage extends GenericMessage {
    /**
     * action
     * @param virtualView the virtual view
     * @param playerNickname the player nickname
     */
    void action(VirtualView virtualView, String playerNickname);
}

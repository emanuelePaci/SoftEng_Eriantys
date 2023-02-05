package it.polimi.ingsw.network;

import it.polimi.ingsw.client.View;

/**
 * Controller view message interface
 */
public interface ControllerViewMessage extends GenericMessage {
    /**
     * action
     * @param view the view
     */
    void action(View view);
}

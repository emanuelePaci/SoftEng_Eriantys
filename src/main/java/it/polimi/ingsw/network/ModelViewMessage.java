package it.polimi.ingsw.network;

import it.polimi.ingsw.client.View;

/**
 * Model view message interface
 */
public interface ModelViewMessage extends GenericMessage{
    /**
     * action
     * @param view the view
     */
    void action (View view);
}

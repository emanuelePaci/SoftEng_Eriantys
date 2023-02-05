package it.polimi.ingsw.listeners;

import it.polimi.ingsw.server.VirtualView;

/**
 * Model listener class
 */
public class ModelListener implements Listener {

    private VirtualView virtualView;

    /**
     * Constructor
     * @param virtualView the virtualView to be set
     */
    public ModelListener(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * prints the game board
     */
    @Override
    public void update() {
        virtualView.printGameBoard(virtualView.getController().getGame());
    }
}

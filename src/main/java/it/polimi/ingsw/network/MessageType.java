package it.polimi.ingsw.network;

import java.io.Serializable;

/**
 * Message type enum
 */
public enum MessageType implements Serializable {
    /**
     * Lobby
     */
    Lobby,
    /**
     * Controller view
     */
    ControllerView,
    /**
     * Model view
     */
    ModelView,
    /**
     * View controller
     */
    ViewController

}

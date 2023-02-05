package it.polimi.ingsw.network;

/**
 * Network handler interface
 */
public interface NetworkHandler {
    /**
     * sends the given message
     * @param message the generic message
     */
    void send(GenericMessage message);

    /**
     * checks if the connection is alive
     * @return true if the connection is alive
     */
    boolean connectionAlive();
}

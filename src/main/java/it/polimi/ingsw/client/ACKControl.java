package it.polimi.ingsw.client;

import it.polimi.ingsw.network.NetworkHandler;
import it.polimi.ingsw.network.messages.service.ACK;

/**
 *This class represents an ACK generator
 */
public class ACKControl extends Thread {
    private NetworkHandler networkHandler;
    private boolean isServer;

    /**
     * Constructor
     * Initializes this ACK control
     * @param networkHandler the generic network handler that creates this ACK control
     * @param isServer true if the network handler is the server
     */
    public ACKControl(NetworkHandler networkHandler, boolean isServer){
        this.networkHandler=networkHandler;
        this.isServer=isServer;
    }

    /**
     * If the network handler is still connected, send an ACK messages to another network handler (of the other type) at regular intervals
     */
    @Override
    public void run (){
        while (networkHandler.connectionAlive()){
            networkHandler.send(new ACK(isServer));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


        }
    }
}

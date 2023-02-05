package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.exceptions.ErrorType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.service.ACK;
import it.polimi.ingsw.network.messages.service.InterruptedGameMessage;
import it.polimi.ingsw.network.messages.setUp.*;

import java.io.*;
import java.net.Socket;

/**
 * A server handler which manages the communication the server
 */
public class ServerHandler implements NetworkHandler {
    private Socket socket;
    private GameInfoMessage currentMessage;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private View view;
    private boolean isConnected = false;

    /**
     * Establishes the socket between this server handler and the server, at the specified serverIP on the specified Port, then instantiates a new ACKControl and begin to listen for any message that could arrive
     * @param serverIp the ip address of the server
     * @param serverPort the port's number of the server
     */
    public synchronized void initConnection(String serverIp, String serverPort){
        try{
            socket = new Socket(serverIp, Integer.parseInt(serverPort));
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            isConnected = true;
            socket.setSoTimeout(25000);
            ACKControl aCKcontrol= new ACKControl(this, false);
            aCKcontrol.start();
            listen();
        }
        catch(IOException e){
            ClientException exception = new ClientException(ErrorType.SERVER_OFFLINE);
            view.printError(exception);
            try {
                this.wait(2000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            view.start();
        }
    }

    /**
     * While the connection is present it listens for any message that could arrive and, based on the message's type, it performs different actions
     */
    public void listen(){
        while (isConnected) {
            try {
                GenericMessage message = (GenericMessage) input.readObject();
                if(message instanceof InterruptedGameMessage && message.getType().equals(MessageType.ModelView)) {
                    isConnected = false;
                    endConnection();
                }

                if (!(message instanceof ACK))
                    stopBufferClearer();

                if (message instanceof GameInfoMessage)
                    currentMessage = (GameInfoMessage) message;


                message.action(view);
            } catch (IOException | ClassNotFoundException e) {
                if (isConnected) {
                    view.printServerDown();
                    isConnected = false;
                }

                endConnection();
            }
        }
    }

    /**
     * Stops the bufferClearer (Cli version of the game)
     */
    private void stopBufferClearer() {
        if (currentMessage != null && currentMessage.thread != null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            view.setClearer(false);
            currentMessage.thread.interrupt();
        }
    }

    /**
     * Returns true if the connection is still alive
     * @return true if the connection is still alive
     */
    public boolean connectionAlive() {
        return isConnected;
    }

    /**
     * Sets the specified view
     * @param view the view to set
     */
    public void setView(View view){
        this.view = view;

    }

    /**
     * Ends the connection with the server
     */
    public void endConnection() {
        try {
            isConnected = false;
            stopBufferClearer();
            socket.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Sends the given message
     * @param message the generic message
     */
    public void send(GenericMessage message) {
        if(isConnected) {
            try {
                output.writeUnshared(message);
                output.flush();
                output.reset();
            } catch (IOException e) {
                if (isConnected) {
                    isConnected = false;
                    view.printServerDown();
                }

                endConnection();
            }
        }
    }

    /**
     * Sends to the server the request to claim a specif cloud
     * @param cloudPosition the number of the cloud to claim
     */
    public void cloudChosenRequest(int cloudPosition){
        send(new CloudChosenRequest(cloudPosition));
    }

    /**
     * Sends to the server the request to move Mother Nature to the specified position
     * @param endPosition the position of the island where Mother Nature will be at the end of this action, if valid
     */
    public void moveMotherNatureRequest (int endPosition){
        send(new MoveMotherNatureRequest(endPosition));
    }

    /**
     * Sends to the server the request to move the specified student to the specified island
     * @param islandPosition the position of the island
     * @param color the color of the student to move
     */
    public void moveStudentToIslandRequest(int islandPosition, PawnColor color){
        send(new MoveStudentToIslandRequest(islandPosition, color));
    }

    /**
     * Sends to the server the request to move the specified student to the dining room of the player
     * @param color the color of the student to move
     */
    public void moveToDiningRoomRequest(PawnColor color){
        send(new MoveToDiningRoomRequest(color));
    }

    /**
     * Sends to the server the request to use the Assistant card of the specified weight
     * @param weight the weight of the assistant card
     */
    public void useAssistantRequest(int weight){
        send(new UseAssistantRequest(weight));
    }

    /**
     * Sends to the server the request to use the Character card at the specified position.
     * It's used to play this of this Character: MAGIC_DELIVERY_MAN, CENTAUR, KNIGHT, FARMER
     * @param characterPosition the position of the Character card to play
     */
    public void useCharacterRequest(int characterPosition){send(new UseCharacterRequest(characterPosition));}

    /**
     * Sends to the server the request to use the Character card at the specified position.
     * It's used to play this of this Character: HERALD, GRANDMOTHER_HERBS
     * @param islandPosition the position of the island where the card will activate its effect
     * @param characterPosition the position of the Character card to play
     */
    public void useCharacterRequest(int islandPosition, int characterPosition){send(new UseCharacterRequest(islandPosition, characterPosition));}

    /**
     * Sends to the server the request to use the Character card at the specified position.
     * It's used to play this of this Character: MONK
     * @param islandPosition the position of the island where the card will activate its effect
     * @param color the color of the student to move
     * @param characterPosition the position of the Character card to play
     */
    public void useCharacterRequest(int islandPosition, PawnColor color, int characterPosition){send(new UseCharacterRequest(islandPosition, color,characterPosition));}

    /**
     * Sends to the server the request to use the Character card at the specified position.
     * It's used to play this of this Character: SPOILED_PRINCESS, THIEF, MUSHROOM_HUNTER
     * @param color the color of the student to move
     * @param characterPosition the position of the Character card to play
     */
    public void useCharacterRequest(PawnColor color, int characterPosition){send(new UseCharacterRequest(color, characterPosition));}

    /**
     * Sends to the server the request to use the Character card at the specified position.
     * It's used to play this of this Character: JESTER, MINSTREL
     * @param color the array of the color of the student to move.
     *              If the Character is the JESTER, the first 3 colors are the students present at the entrance, the others are the students on the Character card.
     *              If the Character is the MINSTREL, the first 2 colors are the students present at the entrance, the other 2 are students in the dining room
     * @param characterPosition the position of the Character card to play
     */
    public void useCharacterRequest(int[] color, int characterPosition){send(new UseCharacterRequest(color, characterPosition));}

    /**
     * Sends to the server the request to start a new game or to add a new client to the lobby
     * @param newGame true if there is the request to start a new game
     * @param lobby the number of the lobby
     */
    public void setGame(boolean newGame, int lobby){
        send(new GameSetUp(newGame, lobby));
    }

    /**
     * Sends to the server the information about the nickname chosen by one player
     * @param nickname the nickname chosen by the player
     */
    public void setPlayerInfo(String nickname){
        send(new PlayerSetUp(nickname));
    }

    /**
     * Sends to the server the information about the number of player and the mode of this game and
     * @param numPlayer the number of player of this game
     * @param expert true if the game will be played in expert mode
     */
    public void initialSetUp(int numPlayer, boolean expert){
        send(new InitialSetUp(numPlayer, expert));
    }

    /**
     * Sends to the server the information about the color of the tower chosen by a client
     * @param color the color chosen by the player
     */
    public void setPlayerColor(TowerColor color){
        send(new ColorSetUp(color));
    }

    /**
     * Sends to the server the request to print the list of the available lobbies present
     */
    public void refreshLobbies(){
        send(new RefreshMessage());
    }

}

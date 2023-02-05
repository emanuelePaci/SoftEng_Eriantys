package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.network.MessageType;
import it.polimi.ingsw.network.ViewControllerMessage;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.LobbyHandler;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;

/**
 * Use character request class
 */
public class UseCharacterRequest implements Serializable, ViewControllerMessage {
    private MessageType type;
    int islandPosition=0;
    PawnColor color=null;
    int[] colors;
    int characterPosition;

    /**
     * Constructor
     * Initialize the character position
     * @param characterPosition the character position
     */
    public UseCharacterRequest(int characterPosition) {
        type = MessageType.ViewController;
        this.characterPosition=characterPosition;
    }

    /**
     * Constructor
     * Initialize the island and character positions
     * @param IslandPosition the island position
     * @param characterPosition the character position
     */
    public UseCharacterRequest(int IslandPosition , int characterPosition) {
        type = MessageType.ViewController;
        this.islandPosition = IslandPosition;
        this.characterPosition=characterPosition;
    }

    /**
     * Constructor
     * Initialize the island and character position and the pawn color
     * @param IslandPosition the island position
     * @param color the pawn color
     * @param characterPosition the character position
     */
    public UseCharacterRequest(int IslandPosition, PawnColor color, int characterPosition) {
        type = MessageType.ViewController;
        this.islandPosition = IslandPosition;
        this.color = color;
        this.characterPosition=characterPosition;
    }

    /**
     * Constructor
     * Initialize the pawn color and the character position
     * @param color the pawn color
     * @param characterPosition the character position
     */
    public UseCharacterRequest(PawnColor color,  int characterPosition) {
        type = MessageType.ViewController;
        this.color = color;
        this.characterPosition=characterPosition;
    }

    /**
     * Constructor
     * Initialize the pawn colors and the character position
     * @param colors the array of colors
     * @param characterPosition the character position
     */
    public UseCharacterRequest(int[] colors,  int characterPosition) {
        type = MessageType.ViewController;
        this.colors = colors;
        this.characterPosition=characterPosition;
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
     * action
     * @param view the view
     */
    @Override
    public void action(View view) {

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
     * action
     * @param virtualView the virtual view
     * @param playerNickname the player nickname
     */
    @Override
    public void action(VirtualView virtualView, String playerNickname) {
        if(colors!=null)
            virtualView.useCharacter(colors, characterPosition, playerNickname);
        else if (color!=null){
            if(islandPosition!=0)
                virtualView.useCharacter(islandPosition, color, characterPosition, playerNickname);
            else
                virtualView.useCharacter(color, characterPosition, playerNickname);
        }
        else if (islandPosition!=0)
            virtualView.useCharacter(islandPosition, characterPosition, playerNickname);
        else
            virtualView.useCharacter(characterPosition, playerNickname);
    }
}
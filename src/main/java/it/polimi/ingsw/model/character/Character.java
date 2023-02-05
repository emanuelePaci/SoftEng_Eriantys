package it.polimi.ingsw.model.character;

import it.polimi.ingsw.controller.BoardHandler;
import it.polimi.ingsw.controller.Context;
import it.polimi.ingsw.controller.TableHandler;
import it.polimi.ingsw.exceptions.BagIsEmptyException;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.lang.reflect.InvocationTargetException;

/**
 * Abstract class character
 */
public abstract class Character {

    private  CharacterType type;
    private int price;
    private boolean used;

    /**
     * counts the number of pawns of the given color
     * @param pawnColor the pawn color
     * @return -1
     */
    public int count(PawnColor pawnColor){return -1;}

    /**
     * gets the num of no entry tiles
     * @return -1
     */
    public int getNumNoEntryTiles(){return -1;}

    /**
     * return no entry tiles
     */
    public void returnNoEntryTiles(){}

    /**
     * activates the character
     * @param professorContext the professor context
     * @param motherNatureContext the mother nature context
     * @param islandContext the island context
     */
    public void activateCharacter(Context professorContext, Context motherNatureContext, Context islandContext){}

    /**
     * activates the character
     * @param game the current game
     * @param player the current player
     * @param color the pawn color
     * @param context the context
     * @param boardHandler the board handler
     * @throws BagIsEmptyException if you don't have any student
     */
    public void activateCharacter(Game game, Player player, PawnColor color, Context context, BoardHandler boardHandler) throws BagIsEmptyException{}

    /**
     * activates the character
     * @param island the current island
     * @param color the pawn color
     * @throws BagIsEmptyException if you don't have any student
     */
    public void activateCharacter(Island island, PawnColor color) throws BagIsEmptyException{}

    /**
     * activates the character
     * @param island the current island
     * @param tableHandler the table handler
     * @throws ClientException if the client has problems
     */
    public void activateCharacter(Island island, TableHandler tableHandler) throws ClientException{}

    /**
     * activates the character
     * @param player the current player
     * @param color the list of pawn color
     * @param boardHandler the board handler
     * @throws InvocationTargetException if you can't invocate the character
     * @throws IllegalAccessException if you don't have access
     */
    public void activateCharacter(Player player, PawnColor[] color, BoardHandler boardHandler) throws InvocationTargetException, IllegalAccessException{}

    /**
     * Constructor
     * Initialize the character type, the price and the boolean used to false
     * @param type the character type
     */
    public Character (CharacterType type){
        this.type=type;
        price=type.getPrice();
        used=false;
    }

    /**
     * Increments the price and sets the boolean used to true
     */
    public void firstUse(){
        price += 1;
        used = true;
    }

    /**
     * gets if the character has been used
     * @return true if the character has been used
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * gets the price
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * gets the character type
     * @return the character type
     */
    public CharacterType getType() {
        return type;
    }

    /**
     * add student
     * @param s the student to be added
     */
    public void addStudent(Student s){}
}





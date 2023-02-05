package it.polimi.ingsw.model.character;

import it.polimi.ingsw.controller.TableHandler;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.exceptions.ErrorType;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.table.Island;


/**
 * Character no entry tiles class
 * GRANDMOTHER_HERBS
 */
public class CharacterNoEntryTiles extends Character{
    private int numNoEntryTiles;

    /**
     * Constructor
     * Initialize and sets the number of no entry tiles
     * @param type the character type
     */
    public CharacterNoEntryTiles (CharacterType type){
        super(type);
        numNoEntryTiles=4;
    }

    /**
     * increments the num entry tiles
     */
    @Override
    public void returnNoEntryTiles() {
        numNoEntryTiles += 1;
    }

    /**
     * gets the number of no entry tiles
     * @return the number of no entry tiles
     */
    @Override
    public int getNumNoEntryTiles() {
        return numNoEntryTiles;
    }

    /**
     * activate character
     * @param island the current island
     * @param tableHandler the table handler
     * @throws ClientException if there are no sufficient no entry tiles
     */
    @Override
    public void activateCharacter(Island island, TableHandler tableHandler)throws ClientException {
        if ((numNoEntryTiles < 1 || island.isNoEntryTiles()))
            {throw new ClientException(ErrorType.NO_ENTRY_TILES_SET);}
        else {
            island.setNoEntryTiles(true);
            numNoEntryTiles -= 1;
        }
    }
}

package it.polimi.ingsw.model.character;

import it.polimi.ingsw.controller.islandStrategy.IslandStrategy;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.table.Bag;

import java.lang.reflect.Method;

/**
 * Factory pattern Class
 */
public class Factory {
    /**
     * Constructor
     */
    public Factory(){

    }

    /**
     * Get character
     * @param type the character type
     * @param bag the current bag
     * @param updateIsland the upload island method
     * @param checkProfessor the check professor method
     * @param island the island strategy
     * @return the character
     */
    public  Character getCharacter (CharacterType type, Bag bag, Method updateIsland, Method checkProfessor, IslandStrategy island) {
        Character character;
        if(type.equals(CharacterType.MAGIC_DELIVERY_MAN) || type.equals(CharacterType.MUSHROOM_HUNTER))
            character =new CharacterGroup1(type);
        else if(type.equals(CharacterType.FARMER) || type.equals(CharacterType.HERALD))
            character =new CharacterGroup2(type, updateIsland);
        else if(type.equals(CharacterType.KNIGHT) || type.equals(CharacterType.MINSTREL))
            character =new CharacterGroup3(type, checkProfessor, island);
        else if(type.equals(CharacterType.CENTAUR) || type.equals(CharacterType.THIEF))
            character =new CharacterGroup4(type);
        else if(type.equals(CharacterType.GRANDMOTHER_HERBS))
            character =new CharacterNoEntryTiles(type);
        else
            character =new CharacterGroupStudent(type, bag, checkProfessor);
        return character;
    }
}

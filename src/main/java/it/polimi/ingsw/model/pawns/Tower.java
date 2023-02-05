package it.polimi.ingsw.model.pawns;

import it.polimi.ingsw.model.enumerations.TowerColor;

/**
 * Tower class
 */
public class Tower {
    private TowerColor color;

    /**
     * Constructor
     * Initialize the tower color
     * @param towerColor the tower color to be set
     */
    public Tower(TowerColor towerColor){
        color = towerColor;
    }

    /**
     * gets the tower color
     * @return the tower color
     */
    public TowerColor getColor() {
        return color;
    }
}

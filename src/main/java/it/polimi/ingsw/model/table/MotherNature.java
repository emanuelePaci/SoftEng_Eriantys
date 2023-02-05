package it.polimi.ingsw.model.table;

/**
 * Represents the "Mother Nature" pawn.
 */
public class MotherNature {
    private int position;

    /**
     * Constructor.
     * Instantiates Mother Nature initializing its position at 0.
     */
    public MotherNature(){
        position = 0;
    }

    /**
     * Gets the position of Mother Nature.
     * @return the position of Mother Nature.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position of Mother Nature at the specified value.
     * @param position the new position of Mother Nature.
     */
    public void setPosition(int position) {
        this.position = position;
    }
}

package it.polimi.ingsw.model.player;

/**
 * Represents an Assistant card.
 */
public class Assistant {

    /**
     * Represents the value of this card, it assumes values between 0 and 10.
     */
    private final int weight;

    /**
     * Represents the number of moves related to this card, it assumes values between 1 and 5.
     */
    private final int numMovement;

    /**
     * Constructor
     * Instantiates this Assistant card initializing the weight and the number of moves related to the number of the card.
     * @param weight the number of the card.
     */
    public Assistant(int weight)
    {
        this.weight=weight;
        numMovement=(weight/2 + weight%2);
    }

    /**
     * Gets the number of moves related to this card.
     * @return the number of moves related to this card.
     */
    public int getNumMovement() {
        return numMovement;
    }

    /**
     * Gets the value of this card.
     * @return the value of this card.
     */
    public int getWeight() {
        return weight;
    }
}

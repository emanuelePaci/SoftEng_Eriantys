package it.polimi.ingsw.model.player;

import java.util.*;

/**
 * Represents a deck of Assistant card.
 */
public class Deck {
    private List<Assistant> assistant;
    private int size;

    /**
     * Constructor.
     * Instantiates this deck initializing its size and the list containing the assistant cards, ordered by weight from 1 to 10.
     */
    public Deck () {
        assistant = new ArrayList<>(10);
        for(int i=1; i<=10; i++)
            assistant.add(new Assistant(i));

        size = 10;
    }

    /**
     * Removes and returns the Assistant card at the specified position in this deck, decreasing its size by one.
     * @param position the index of the Assistant card to return.
     * @return the Assistant card at the specified position in this deck.
     */
    public Assistant removeAssistant(int position)
    {
        size -= 1;
        return assistant.remove(position);
    }

    /**
     * Gets the size of this deck.
     * @return the size of this deck.
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the Assistant card at the specified position in this deck.
     * @param position the index of the Assistant card to return.
     * @return the Assistant card at the specified position in this deck.
     */
    public Assistant getAssistant(int position){
        return assistant.get(position);
    }

    /**
     * Gets the list of available Assistant cards.
     * @return the list of available Assistant cards.
     */
    public List<Assistant> getAssistant(){
        return assistant;
    }

    /**
     * Returns true if this deck contains no elements.
     * @return true if this deck contains no elements.
     */
    public boolean isEmpty(){
        return assistant.size() == 0;
    }

}

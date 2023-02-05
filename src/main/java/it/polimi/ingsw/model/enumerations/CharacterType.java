package it.polimi.ingsw.model.enumerations;

/**
 * Character type enum
 */
public enum CharacterType {
    /**
     * Monk
     */
    MONK(1, "MONK", "Take 1 student from this card and place it on an Island of your choice."),
    /**
     * Farmer
     */
    FARMER(2,"FARMER", "During this turn, you take control of any number of Professors even if you have the same number of Students as the player who currently controls them."),
    /**
     * Herald
     */
    HERALD(3,"HERALD", "Choose an Island and resolve the Island as if Mother Nature had ended her movement there. Mother Nature will still move and the Island where she ends her movement will also be resolved."),
    /**
     * Magic delivery man
     */
    MAGIC_DELIVERY_MAN(1, "MAGIC_DELIVERY_MAN", "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played."),
    /**
     * Grand mother herbs
     */
    GRANDMOTHER_HERBS(2,"GRANDMOTHER_HERBS", "Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her movement there, put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place any Towers."),
    /**
     * Centaur
     */
    CENTAUR(3, "CENTAUR", "When resolving a Conquering on an Island, Towers do not count towards influence."),
    /**
     * Jester
     */
    JESTER(1,"JESTER", "You may take up to 3 Students from this card and replace them with the same number of Students from your Entrance."),
    /**
     * Knight
     */
    KNIGHT(2,"KNIGHT", "During the influence calculation this turn, you count as, having 2 more influence."),
    /**
     * Mushroom hunter
     */
    MUSHROOM_HUNTER(3,"MUSHROOM_HUNTER", "Choose a color of Student: during the influence calculation this turn, that color adds no influence."),
    /**
     * Minstrel
     */
    MINSTREL(1, "MINSTREL", "You may exchange up to 2 Students between your Entrance and your Dining Room."),
    /**
     * Spoiled princess
     */
    SPOILED_PRINCESS(2,"SPOILED_PRINCESS", "Take 1 Student from this card and place it in your Dining Room."),
    /**
     * Thief
     */
    THIEF(3, "THIEF", "Choose a type of Student; every player (including yourself) must return 3 Students of that type from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as many Students as they have.");

    private final int price;
    private final String name;
    private final String description;

    /**
     * Default constructor.
     *
     * @param name the string representation of the pawn color.
     */
    CharacterType(int cost, String name, String description) {
        this.name = name;
        this.price = cost;
        this.description = description;
    }

    /**
     * Returns the text of the player state.
     *
     * @return a String containing the text of the pawn color.
     */
    public String getText() {
        return name;
    }

    /**
     * gets the character description
     * @return the character description
     */
    public String getDescription() {
        return description;
    }

    /**
     * gets the character price
     * @return the character price
     */
    public int getPrice() {
        return price;
    }

    /**
     * returns the number of students for this character
     * @return the number of students for this character
     */
    public int hasStudent(){
        int value = 0;

        if (this.equals(MONK) || this.equals(SPOILED_PRINCESS))
            value = 4;

        if (this.equals(JESTER))
            value = 6;

        return value;
    }

    /**
     * name to string
     * @return name to string
     */
    @Override
    public String toString() {
        return name;
    }
}

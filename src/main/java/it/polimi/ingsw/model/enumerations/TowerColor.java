package it.polimi.ingsw.model.enumerations;

/**
 * Tower color enum
 */
public enum TowerColor {
    /**
     * white
     */
    WHITE("WHITE", 0),
    /**
     * black
     */
    BLACK("BLACK", 1),
    /**
     * grey
     */
    GREY("GREY", 2);

    private final String text;
    private final Integer index;

    /**
     * Default constructor.
     *
     * @param text the string representation of the tower color.
     */
    TowerColor(String text, Integer index) {
        this.text = text;
        this.index = index;
    }

    /**
     * Returns the text of the player state.
     *
     * @return a String containing the text of the tower color.
     */
    public String getText() {
        return text;
    }
    /**
     * Returns the text of the player state.
     * @return a String containing the text of the tower color.
     */
    @Override
    public String toString() {
        return text;
    }

    /**
     * gets the index
     * @return the index
     */
    public Integer getIndex(){
        return index;
    }

    /**
     * gets the tower color for the given string
     * @param color the color string
     * @return the tower color for the given string
     */
    public static TowerColor getColor(String color){
        for (TowerColor t : TowerColor.values())
            if(t.toString().equals(color.toUpperCase()))
                return t;
        return null;
    }

    /**
     * gets the tower color from the given number
     * @param color the color number
     * @return the tower color from the given number
     */
    public static TowerColor getColor(int color){
        for (TowerColor t : TowerColor.values())
            if(t.index.equals(color))
                return t;
        return null;
    }
}
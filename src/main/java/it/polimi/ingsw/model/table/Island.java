package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.pawns.Tower;
import it.polimi.ingsw.listeners.ModelListener;

import java.util.*;

/**
 * An island, it could contain students of different colors, towers of the same color, Mother Nature's pawn and, if the game is played in expert mode, one "no entry" tile
 */
public class Island {
    private int weight;
    private List<Student> islandStudent;
    private boolean motherNature;
    private boolean noEntryTiles;
    private List<Tower> islandTower;
    private ModelListener modelListener=null;

    /**
     * Constructor
     * Instantiates this island.
     */
   public Island()
    {
        weight=1;
        islandStudent = new LinkedList<>();
        motherNature = false;
        noEntryTiles = false;
        islandTower = new LinkedList<>();
    }

    /**
     * Sets the new weight of this island.
     * @param weight the new weight of this island, the minimum is 1 and the maximum is 8.
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Gets the number of islands merged with this island, the minimum is 1 when no other island are merged with it and the maximum is 8.
     * @return the number of islands merged.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets not entry tiles
     * @param noEntryTiles the entry tile to be set
     */
    public void setNoEntryTiles(boolean noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }

    /**
     * Gets the list of student present on this island.
     * @return the list of student present on this island.
     */
    public List<Student> getIslandStudent() {
        return islandStudent;
    }

    /**
     * Returns true if Mother Nature is on this island.
     * @return true if Mother Nature is on this island.
     */
    public boolean isMotherNature() {
        return motherNature;
    }

    /**
     * Sets true if Mother Nature is present on this island.
     * @param motherNature the presence of Mother Nature.
     */
    public void setMotherNature(boolean motherNature) {
        this.motherNature = motherNature;
    }

    /**
     * Returns true if this island has a "no entry" tile on it.
     * @return true if this island has a "no entry" tile on it.
     */
    public boolean isNoEntryTiles() {
        return noEntryTiles;
    }

    /**
     * Gets the list of tower present on this island.
     * @return the list of tower present on this island.
     */
    public List<Tower> getIslandTower() { return islandTower; }

    /**
     * Counts the number of students of the specified color present on this island.
     * @param c the color of the students to count.
     * @return the number of students of the specified color present on this island.
     */
    public int countStudent(PawnColor c){
        int count = 0;
        for(Student s : islandStudent){
            if(s.getColor() == c){
                count++;
            }
        }
        return count;
    }

    /**
     * Counts the number of students of each color present on this island.
     * @return the array, ordered by PawnColor's index, where each cell contains the number of students of each color present on this island.
     */
    public Integer[] countAll(){
        Integer[] colorsCount = new Integer[5];

        for (PawnColor p : PawnColor.values())
            colorsCount[p.getIndex()] = countStudent(p);

        return colorsCount;
    }

    /**
     * Appends the specified student at the end of islandStudent.
     * @param s the student to add.
     */
    public void addStudent(Student s) {
        islandStudent.add(s);
        notifyView();
    }

    /**
     * Removes and returns all the towers present on this island.
     * @return the list containing all the towers present on this island.
     */
    public List<Tower> removeTower(){
       List<Tower> tower = new LinkedList<>(islandTower);
       islandTower.removeAll(tower);
       return tower;
    }

    /**
     * Appends all the tower of the specified list to islandTower.
     * @param tower the list of towers to add.
     */
    public void addTower(List<Tower> tower){
       islandTower.addAll(tower);
    }

    /**
     * Initializes the model's listener.
     * @param modelListener the listener that will be notified when a specified sequence occurs.
     */
    public void attach(ModelListener modelListener){this.modelListener=modelListener;}

    /**
     * Notifies view when a sequence of changes occurs.
     */
    public void notifyView() {
        if (modelListener != null)
            modelListener.update();
    }

    /**
     * Sets to null the reference of the model's listener.
     */
    public void detach(){
       modelListener=null;
   }
}
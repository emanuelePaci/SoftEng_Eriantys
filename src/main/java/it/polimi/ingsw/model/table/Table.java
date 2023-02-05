package it.polimi.ingsw.model.table;

import it.polimi.ingsw.controller.islandStrategy.IslandStrategy;
import it.polimi.ingsw.exceptions.GeneralSupplyFinishedException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.character.Character;
import it.polimi.ingsw.model.character.Factory;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Professor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.player.Player;

import java.lang.reflect.Method;
import java.util.*;

/**
 * The table contains all the islands, the clouds, the amount of money available, the Character card and the professors when none has gained them yet.
 */
public class Table {
    private int generalSupply;
    private MotherNature motherNature;
    private List<Cloud> cloud;
    private List<Island> island;
    private List<Character> character;
    private Bag bag;
    private List<Professor> professor;
    private IslandStrategy islandStrategy;
    private int[] lastMerged = {-1,-1,-1};

    /**
     * Instantiates this table and the elements it contains.
     * @param numPlayer the number of player that joined the game.
     * @param expert true only if the game is played in expert mode.
     * @param islandUpdate the update island method {@link it.polimi.ingsw.controller.TableHandler#updateIsland(Island)} .
     * @param checkProfessor the check professor method {@link it.polimi.ingsw.controller.professorStrategy.ProfessorStrategy#checkProfessor(Game, Player, PawnColor)}.
     * @param islandStrategy the island strategy, needed to use the strategy pattern.
     */
    public Table(int numPlayer, boolean expert, Method islandUpdate, Method checkProfessor, IslandStrategy islandStrategy) {
        motherNature = new MotherNature();
        Factory factory = new Factory();
        this.islandStrategy = islandStrategy;

        bag=new Bag();

        cloud=new LinkedList<>();
        for(int i=1;i<=numPlayer;i++)
            cloud.add(new Cloud());

        fillCloud(numPlayer, bag.initialSetup(numPlayer*(numPlayer+1)));

        island=new LinkedList<>();
        for(int i=1;i<=12;i++)
            island.add(new Island());

        island.get(0).setMotherNature(true);

        List<Student> student = new ArrayList<>();
        for(int i=0;i<5;i++)
            for (int j = 0; j < 2; j++)
                student.add(new Student(i));

        Collections.shuffle(student);
        for (int i = 1; i < 6; i++)
            island.get(i).addStudent(student.get(i - 1));
        for (int i = 7; i < 12; i++)
            island.get(i).addStudent(student.get(i - 2));

        student.clear();

        professor= new LinkedList<>();
        for(int i=0; i<5;i++)
            professor.add(new Professor(i));

        if(expert) {
            generalSupply=20-numPlayer;
            character=new LinkedList<>();

            List<CharacterType> type = Arrays.asList(CharacterType.values());
            Collections.shuffle(type);

            for (int i = 0; i < 3; i++){
                character.add(factory.getCharacter(type.get(i), bag, islandUpdate, checkProfessor, islandStrategy));
            }
        }


    }

    /**
     * Adds, according to the number of players, the specified students to each cloud.
     * The number of students present on each cloud is numPlayer+1.
     * @param numPlayer the number of player in the game.
     * @param student the list of student to add containing numPlayer*(numPlayer+1) students.
     */
    public void fillCloud(int numPlayer, List<Student> student){
        int init = 0;
        int end = numPlayer + 1;
        for (Cloud c: cloud) {
            List<Student> sublist = student.subList(init, end);
            c.addStudent(sublist);
            init = end;
            end += numPlayer + 1;
        }
        student.clear();
    }

    /**
     * Gets the reference of the bag.
     * @return the reference of the bag.
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * Returns the island at the specified position.
     * @param position the index of the cloud to return.
     * @return the island at the specified position.
     */
    public Island getIsland(int position)
    {
        return island.get(position);
    }

    /**
     * Gets the reference of Mother Nature.
     * @return the reference of the Mother Nature.
     */
    public MotherNature getMotherNature() {
        return motherNature;
    }

    /**
     * Gets the list of islands present on this table.
     * @return the list of islands present on this table.
     */
    public List<Island> getIsland() {
        return island;
    }

    /**
     * Gets the position of the island where is present Mother Nature.
     * @return the position of the island where is present Mother Nature.
     */
    public int getMotherPosition(){
        return motherNature.getPosition();
    }

    /**
     * Gets the number of islands present on this table.
     * @return the number of islands present on this table.
     */
    public int getNumIsland () {return island.size();}

    /**
     * Returns the Character card at the specified position.
     * @param position the index of the Character card to return.
     * @return the Character card at the specified position.
     */
    public Character getCharacter(int position) {
        return character.get(position);
    }

    /**
     * Gets the number of coins available on this table.
     * @return the number of coins available on this table.
     */
    public int getGeneralSupply() {
        return generalSupply;
    }

    /**
     * Returns the cloud at the specified position.
     * @param position the index of the cloud to return.
     * @return the cloud at the specified position.
     */
    public Cloud getCloud(int position){
        return cloud.get(position);
    }

    /**
     * Adds the specified number of coins at the general supply.
     * @param numCoin the number of coins to add.
     */
    public void addCoin(int numCoin) {generalSupply=generalSupply+numCoin;}

    /**
     * Decreases the amount of money available in the general supply by one unit.
     * @throws GeneralSupplyFinishedException if the number of coins to remove is zero.
     */
    public void withdrawCoin() throws GeneralSupplyFinishedException {
        if (generalSupply == 0) throw new GeneralSupplyFinishedException();
        generalSupply -= 1;
    }

    /**
     * Returns the professor of the color specified if present on this table.
     * The professor is present on this table until any player has the majority of the students of the professor's color in his dining room.
     * @param color the color of teh professor to find.
     * @return the professor of the color specified, if present on this table, otherwise it returns null.
     */
    public Professor findProfessor(PawnColor color) {
        Professor temp = null;
        for(Professor p: professor)
        {
            if(p.getColor()==color) {
                temp = p;
            }
        }

        return temp;
    }

    /**
     * Changes the position o mother nature by the specified value.
     * @param moves the number of moves.
     */
    public void moveMotherNature(int moves){
        island.get(getMotherPosition()).setMotherNature(false);
        if (moves + getMotherPosition() > getNumIsland() - 1)
            motherNature.setPosition(moves + getMotherPosition() - getNumIsland());
        else
            motherNature.setPosition(moves + getMotherPosition());

        island.get(getMotherPosition()).setMotherNature(true);
    }

    /**
     * Checks if the island at the specified position could be merged with the previous or with the next.
     * If there is the possibility to merge two adjacent islands, moves all the pawns and tower present on the island with the lower position between them
     * to the other island (if the islands to merge are the first and the last, it removes the pawns and the towers from the last island), then removes the empty island from
     * this table, updates the weight of the remaining island and eventually returns any "no entry" tile present.
     *
     * @param position the position of the island to check.
     */
    public void mergeIsland (int position) {
        int positionNext;
        int positionPrev;

        if(position==0)
        {
            positionPrev=getNumIsland()-1;
            positionNext=1;
        }
        else if(position==getNumIsland()-1)
        {
            positionPrev=position-1;
            positionNext=0;
        }
        else
        {
            positionPrev=position-1;
            positionNext=position+1;
        }

        lastMerged = new int[]{positionPrev, position, positionNext};

        Island islandPrev= island.get(positionPrev);
        Island islandCurr= island.get(position);
        Island islandNext= island.get(positionNext);

        updateIsland(islandPrev, islandCurr, 0);
        updateIsland(islandNext, islandCurr, 2);

        motherNature.setPosition(island.indexOf(islandCurr));
    }

    private void updateIsland(Island islandNotCurr, Island islandCurr, int notCurr) {
        if (islandNotCurr.getIslandTower().isEmpty()) {
            lastMerged[notCurr] = -1;
            return;
        }

        if(islandNotCurr.getIslandTower().get(0).getColor()==islandCurr.getIslandTower().get(0).getColor())
        {
            islandCurr.setWeight(islandNotCurr.getWeight()+islandCurr.getWeight());
            islandCurr.getIslandStudent().addAll(islandNotCurr.getIslandStudent());
            islandCurr.getIslandTower() .addAll(islandNotCurr.getIslandTower());
            if (islandCurr.isNoEntryTiles() && islandNotCurr.isNoEntryTiles()) {
                islandCurr.setNoEntryTiles(true);
                for (Character c : character){
                    if (c.getType().equals(CharacterType.GRANDMOTHER_HERBS)) {
                        c.returnNoEntryTiles();
                    }
                }
            }
            else if (islandCurr.isNoEntryTiles() || islandNotCurr.isNoEntryTiles())
                islandCurr.setNoEntryTiles(true);

            island.remove(islandNotCurr);
        } else {
            lastMerged[notCurr] = -1;
        }
    }

    /**
     * Sets a specific character card, it's only used in test classes.
     * @param position the position of the character in the character's list, it must be a number between 0 and 2.
     * @param type the type of the character to set at the specified position.
     * @param islandUpdate the update island method {@link it.polimi.ingsw.controller.TableHandler#updateIsland(Island)}.
     * @param checkProfessor the check professor method {@link it.polimi.ingsw.controller.professorStrategy.ProfessorStrategy#checkProfessor(Game, Player, PawnColor)}.
     */

    public void setCharacter(int position, CharacterType type, Method islandUpdate, Method checkProfessor){
        Factory factory = new Factory();
        character.set(position,factory.getCharacter(type, bag, islandUpdate, checkProfessor, islandStrategy));
    }

    /**
     * @return the indexes of the latest merged island (-1 if not merged)
     */
    public int[] getMerged() {
        return lastMerged;
    }
}



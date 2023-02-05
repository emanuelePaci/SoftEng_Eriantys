package it.polimi.ingsw.client.viewUtilities;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Professor;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.io.Serializable;
import java.util.*;

/**
 * This class contains the status of the game, it is sent as a message from the server  to all the server handler
 */
public class GameInfo implements Serializable {
    private String[][] players;
    private int[] playersInfo = new int[2]; //Indexes of the currentTurnPlayer and the frontPlayer
    private int remainingMoves;
    private int[] islandSize;
    private int motherNaturePosition;
    private int numberOfPlayer;
    private int numberOfIsland;
    private PlayerState playerState;
    private Integer[] diningStudents;
    private Integer[] entranceStudents;
    private String[] professors;
    private int[] boardTower;
    private Integer[] assistants;
    private int[] lastUsed;

    private Integer[] islandStudents;
    private int[] islandTowers;
    private boolean[] noEntryTile;

    private Integer[] cloudStudents;
    private CharacterType[] character;
    private int[] characterCost;
    private boolean expertMode;
    private int[] playersCoin;
    private int[] lastMerged = new int[3];
    private Integer[][] characterInfo; //The first 5 are the eventual student, the sixth is the number of NoEntryTiles

    /**
     * Constructor
     * Initializes the class' attributes taking information by the game itself
     * @param game the status of the game
     * @param frontPlayer the first player of this round
     */
    public GameInfo(Game game, String frontPlayer){
        int numAssistant = 0;
        for (Player p: game.getPlayers())
            if (p.getNickname().equals(frontPlayer)) {
                numAssistant = p.getDeck().getSize();
                playerState = p.getState();
            }

        init(game.getNumPlayer(), game.getTable().getNumIsland(), game.isExpertMode(), numAssistant);

        for(int i = 0; i < game.getNumPlayer(); i++){
            players[i][0] = game.getPlayers().get(i).getNickname();
            players[i][1] = String.valueOf(game.getPlayers().get(i).getTowerColor());
        }

        lastMerged = game.getTable().getMerged();

        expertMode = game.isExpertMode();

        playersInfo[0] = playerPosition(game.getRound().getTurn().getCurrentPlayer().getNickname());
        playersInfo[1] = playerPosition(frontPlayer);
        motherNaturePosition = game.getTable().getMotherPosition();

        remainingMoves = game.getRound().getTurn().getRemainingMovements();
        for (int i = 0; i < islandSize.length; i++){
            islandSize[i] = game.getTable().getIsland(i).getWeight();
        }

        List<Integer> tempListEntrance = new ArrayList<>();
        List<Integer> tempListDining = new ArrayList<>();
        List<Integer> tempAssistant = new ArrayList<>();
        int index = 0;
        for(Player p : game.getPlayers()){
            playersCoin[index] = p.getNumCoin();
            tempListEntrance.addAll(index*5, Arrays.asList(p.getBoard().getEntrance().countAll()));
            tempListDining.addAll(index*5, Arrays.asList(p.getBoard().getDiningRoom().countAll()));
            for(Professor prof : p.getBoard().getProfessorTable().getProfessors()){
                professors[prof.getColor().getIndex()] = p.getNickname();
            }
            boardTower[index] = p.getBoard().getTowerCourt().getTower().size();
            if (p.getLastUsed() != null)
                lastUsed[index] = p.getLastUsed().getWeight();
            if (p.getNickname().equals(getFrontPlayer()))
                for (Assistant a : p.getDeck().getAssistant())
                    tempAssistant.add(a.getWeight());

            index++;
        }
        entranceStudents = tempListEntrance.toArray(entranceStudents);
        diningStudents = tempListDining.toArray(diningStudents);
        assistants = tempAssistant.toArray(assistants);

        List<Integer> tempIslandList = new ArrayList<>();
        index = 0;
        for (Island i : game.getTable().getIsland()){
            tempIslandList.addAll(index*5, Arrays.asList(i.countAll()));
            noEntryTile[index] = i.isNoEntryTiles();
            if (!i.getIslandTower().isEmpty())
                islandTowers[index] = i.getIslandTower().get(0).getColor().getIndex();
            else
                islandTowers[index] = -1;

            index++;
        }
        islandStudents = tempIslandList.toArray(islandStudents);

        List<Integer> tempListCloud = new ArrayList<>();
        for (int i = 0; i < numberOfPlayer; i++){
            tempListCloud.addAll(i*5, Arrays.asList(game.getTable().getCloud(i).countAll()));
        }
        cloudStudents = tempListCloud.toArray(cloudStudents);

        if (game.isExpertMode()){
            for(int i = 0; i < 3; i++) {
                character[i] = game.getTable().getCharacter(i).getType();
                characterCost[i] = game.getTable().getCharacter(i).getPrice();
                characterInfo[i][5] = game.getTable().getCharacter(i).getNumNoEntryTiles();
                for (PawnColor pawnColor : PawnColor.values())
                    characterInfo[i][pawnColor.getIndex()] = game.getTable().getCharacter(i).count(pawnColor);
            }
        }
    }

    /**
     * Initialize all the variables to their correct dimensions and default values.
     * @param numPlayer the number of the player fo this game
     * @param numIsland the number of island remaining
     * @param expertMode if the game is in expert mode
     * @param numAssistant number of assistant remaining in the deck of a player
     */
    private void init(int numPlayer, int numIsland, boolean expertMode, int numAssistant){
        players = new String[numPlayer][2];
        numberOfPlayer=numPlayer;
        islandSize = new int[numIsland];
        numberOfIsland=numIsland;

        diningStudents = new Integer[5*numPlayer];
        entranceStudents = new Integer[5*numPlayer];
        assistants = new Integer[numAssistant];
        boardTower = new int[numPlayer];
        professors = new String[] {"","","","",""};
        lastUsed = new int[] {-1, -1, -1};
        noEntryTile = new boolean[numIsland];
        playersCoin = new int[numPlayer];

        islandStudents = new Integer[numIsland * 5];
        islandTowers = new int [numIsland];
        cloudStudents = new Integer[numPlayer*5];

        if (expertMode) {
            character = new CharacterType[3];
            characterInfo = new Integer[3][6];
            characterCost = new int[3];
        }
    }

    /**
     * Given a String find the index of the player in the playerList
     * @param nickname the nickname of the player
     * @return the index of the player in the List
     */
    private int playerPosition(String nickname){
        for (int index = 0; index < numberOfPlayer; index++ ){
            if (players[index][0].equals(nickname))
                return index;
        }

        return -1;
    }

    /**
     * Returns true only if the game is played in expert mode
     * @return true only if the game is played in expert mode
     */
    public boolean isExpertMode() {
        return expertMode;
    }

    /**
     * Returns the number of the player of this game
     * @return the number of the player of this game
     */
    public int getNumPlayer(){
        return numberOfPlayer;
    }

    /**
     * gets the player state
     * @return the player state
     */
    public PlayerState getPlayerState(){
        return playerState;
    }

    /**
     * Returns the number of coin the player with the specified nickname
     * @param nickname the nickname of the player
     * @return the number of coin the player with the specified nickname
     */
    public int getNumCoin(String nickname){
        return playersCoin[playerPosition(nickname)];
    }

    /**
     * Returns the number of the island present in the game
     * @return the number of the island present in the game
     */
    public int getNumIsland(){
        return numberOfIsland;
    }

    /**
     * Returns the character at the specified position
     * @param position the position of the character to get
     * @return the character at the specified position
     */
    public CharacterType getCharacter(int position){
        return character[position];
    }

    /**
     * Returns an array of Integer in which the first 5 are the eventual student, the sixth is the number of NoEntryTiles
     * @param position the position of the character
     * @return an array of Integer in which the first 5 are the eventual student, the sixth is the number of NoEntryTiles
     */
    public Integer [] getCharacterInfo(int position){
        return characterInfo [position];
    }

    /**
     * Returns an array on int representing the indexes of last merged islands
     * @return the array
     */
    public int[] getLastMerged(){
        return lastMerged;
    }

    /**
     * Returns the cost of the Character at the specified position
     * @param position the position of the character
     * @return the cost of the Character at the specified position
     */
    public int getCharacterCost(int position) {
        return characterCost[position];
    }

    /**
     * Returns an array containing the nickname of each player, in order
     * @return an array containing the nickname of each player, in order
     */
    public String[] getPlayersName(){
        String[] names = new String[players.length];
        for(int i = 0; i < players.length; i++){
            names[i] = players[i][0];
        }
        return names;
    }

    /**
     * Returns the tower color as index of the specified player
     * @param player the nickname of the player
     * @return the tower color as index of the specified player
     */
    public Integer getPlayersTowerColor(String player){
        String towerColor = players[playerPosition(player)][1];
        return TowerColor.getColor(towerColor).getIndex() + 5;
    }

    /**
     * Returns the current player
     * @return the current player
     */
    public String getCurrentPlayer(){
        return players[playersInfo[0]][0];
    }

    /**
     * Returns the nickname of the first player of this round
     * @return the nickname of the first player of this round
     */
    public String getFrontPlayer(){
        return players[playersInfo[1]][0];
    }

    /**
     * Returns true only if the specified island has a NoEntryTiles on
     * @param numIsland the number of the island
     * @return true only if the specified island has a NoEntryTiles on
     */
    public boolean hasNoEntryTile(int numIsland){
        return noEntryTile[numIsland];
    }

    /**
     * Returns the number of students to move in this turn
     * @return the number of students to move in this turn
     */
    public int getRemainingMoves(){
        return remainingMoves;
    }

    /**
     * Returns the weight of the specified island
     * @param numIsland the position of the island
     * @return the weight of the specified island
     */
    public int getIslandSize(int numIsland){
        return islandSize[numIsland];
    }

    /**
     * Returns an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index). Each cell contains the number of student of a color present on the cloud at the specified position
     * @param number the position of the cloud
     * @return Returns an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index)
     */
    public Integer[] getCloudStudents(int number) {
        Integer[] students = new Integer[5];
        System.arraycopy(cloudStudents, number * 5, students, 0, 5);

        return students;
    }

    /**
     * Returns an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index). Each cell contains the number of student of a color present on the entrance of the specified player
     * @param nickname the nickname of the player
     * @return an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index)
     */
    public Integer[] getEntranceStudents(String nickname){
        return getIntegers(nickname, 1);
    }

    private Integer[] getIntegers(String nickname, int from) {
        Integer[] students = new Integer[5];
        int numPlayer = 0;
        for(int i = 0; i < players.length; i++){
            if(players[i][0].equals(nickname)) numPlayer = i;
        }
        int cont = 0;
        if (from == 1)
            for(int j = numPlayer*5; j < (numPlayer+1)*5; j++){
                students[cont] = entranceStudents[j];
                cont++;
            }
        else {
            for(int j = numPlayer*5; j < (numPlayer+1)*5; j++){
                students[cont] = diningStudents[j];
                cont++;
            }
        }
        return students;
    }

    /**
     * Returns the number of tower left at the specified player
     * @param nickname the nickname of the player
     * @return the number of tower left at the specified player
     */
    public int getBoardTower(String nickname){
        int numPlayer = 0;
        for(int i = 0; i < players.length; i++){
            if(players[i][0].equals(nickname)) numPlayer = i;
        }
        return boardTower[numPlayer];
    }

    /**
     * Returns the position of Mother Nature
     * @return the position of Mother Nature
     */
    public int getMotherNaturePosition(){
        return motherNaturePosition;
    }

    /**
     * Returns an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index). Each cell contains the number of student of a color present in the dining room of the specified player
     * @param nickname the nickname of the player
     * @return an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index)
     */
    public Integer[] getDiningStudents(String nickname){
        return getIntegers(nickname, -1);
    }

    /**
     * Returns an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index). Each cell contains the number of student of a color present on the island of the specified position
     * @param numIsland the position of the island
     * @return an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index)
     */
    public Integer[] getStudentsOnIsland(int numIsland){
        Integer[] students = new Integer[5];
        int cont = 0;
        for(int j = numIsland*5; j < (numIsland+1)*5; j++){
            students[cont] = islandStudents[j];
            cont++;
        }
        return students;
    }

    /**
     * Returns the number of tower present on the specified island
     * @param numIsland the position of the island
     * @return  the number of tower present on the specified island
     */
    public int getTowersOnIsland(int numIsland){
        if(islandTowers[numIsland] == - 1) return -1;
        else return TowerColor.getColor(islandTowers[numIsland]).getIndex() + 5;
    }

    /**
     * Returns an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index). Each cell contains 1 if the professor of a color is present in the dining room of the specified player
     * @param nickName the nickname of the player
     * @return an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index). Each cell contains 1 if the professor of a color is present in the dining room of the specified player
     */
    public int[] getProfessors(String nickName) {
        int[] colors = {0,0,0,0,0};
        for (int i = 0; i < 5; i++)
            if (professors[i].equals(nickName))
                colors[i] = 1;

        return colors;
    }

    /**
     * Returns an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index). Each cell contains 1 if the professor of a color is present on the table
     * @return an array of 5 cells, each one representing one Pawncolor (ordered by Pawncolor's index). Each cell contains 1 if the professor of a color is present on the table
     */
    public int[] getProfessors(){
        int[] colors = {0,0,0,0,0};
        for (int i = 0; i < 5; i++)
            if (!professors[i].equals(""))
                colors[i] = 1;

        return colors;
    }

    /**
     * Returns the last used Assistant card of the specified player
     * @param nickName the nickname of the player
     * @return the last used Assistant card of the specified player
     */
    public int getLastUsed(String nickName){
        return lastUsed[playerPosition(nickName)];
    }

    /**
     * Returns an array with the weight of the assistant left
     * @return an array with the weight of the assistant left
     */
    public Integer[] getAssistants(){
        return assistants;
    }
}

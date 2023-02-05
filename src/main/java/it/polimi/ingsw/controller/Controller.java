package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.islandStrategy.IslandContext;
import it.polimi.ingsw.controller.islandStrategy.IslandStrategy;
import it.polimi.ingsw.controller.islandStrategy.IslandStrategyKnight;
import it.polimi.ingsw.controller.islandStrategy.IslandStrategyStandard;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureContext;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureStrategy;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureStrategyStandard;
import it.polimi.ingsw.controller.professorStrategy.ProfessorContext;
import it.polimi.ingsw.controller.professorStrategy.ProfessorStrategy;
import it.polimi.ingsw.controller.professorStrategy.ProfessorStrategyStandard;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;
import it.polimi.ingsw.listeners.ModelListener;
import it.polimi.ingsw.server.VirtualView;

import java.util.LinkedList;
import java.util.List;

/**
 * Controller class
 */
public class Controller {
    private final TurnController turnController;
    private Context professorContext;
    private final ProfessorStrategy professorStrategyStandard;
    private final Context motherNatureContext;
    private final MotherNatureStrategy motherNatureStrategy;
    private Context islandContext;
    private final IslandStrategy islandStrategy;
    private IslandStrategy islandStrategyMoreInfluence;
    private Game game;
    private BoardHandler boardHandler;
    private TableHandler tableHandler;
    private CharacterHandler characterHandler;
    private VirtualView virtualView;
    private ModelListener modelListener;
    boolean canStart;

    /**
     * Constructor
     * Initialize the controller, the turn controller, professor, mother nature and island strategy and context
     */
    public Controller(){
        canStart = false;
        game = new Game();
        game.setExpertMode(false);
        turnController = new TurnController();

        professorStrategyStandard = new ProfessorStrategyStandard();
        professorContext = new ProfessorContext(professorStrategyStandard);

        motherNatureStrategy = new MotherNatureStrategyStandard();
        motherNatureContext = new MotherNatureContext(motherNatureStrategy);

        islandStrategy = new IslandStrategyStandard();
        islandContext = new IslandContext(islandStrategy);
        islandStrategyMoreInfluence = new IslandStrategyKnight();
    }

    /**
     * sets the virtual view
     * @param virtualView the virtual view
     */
    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;

        boardHandler = new BoardHandler(game, turnController, professorContext, virtualView);
        tableHandler = new TableHandler(turnController, game, professorContext, motherNatureContext, islandContext, professorStrategyStandard, motherNatureStrategy, islandStrategy, virtualView);
        characterHandler = new CharacterHandler(turnController, game, professorContext, motherNatureContext, islandContext, virtualView, tableHandler, boardHandler, islandStrategyMoreInfluence);
        modelListener=new ModelListener(virtualView);
    }

    /**
     * sets the number of players
     * @param num the number of players
     */
    public void setNumPlayer(int num) {
        game.setNumPlayer(num);
    }

    /**
     * sets the expert mode
     * @param bool true if expert
     */
    public void setExpertMode(boolean bool){
        game.setExpertMode(bool);
    }

    /**
     * adds a player to the game
     * @param player the player to be added
     */
    public void addPlayer(Player player) {
        int remaining = game.addPlayer(player);
        if (remaining == 0) {
            canStart = true;
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    /**
     * gets the tower colors available
     * @return the tower colors available
     */
    public List<TowerColor> getAvailableColor(){
        List<TowerColor> colors = new LinkedList<>();
        colors.add(TowerColor.WHITE);
        colors.add(TowerColor.BLACK);
        if (game.getNumPlayer() == 3)
            colors.add(TowerColor.GREY);

        for (Player p : game.getPlayers())
            colors.remove(p.getTowerColor());

        return colors;
    }

    /**
     * uses the assistant
     * @param position the assistant position
     * @param player the current player
     */
    public void useAssistant(int position, Player player){
        Round round = game.getRound();
        int weight = player.getDeck().getAssistant(position).getWeight();

        try {
            turnController.checkPermission(round.getTurn(), player, PlayerState.PLANNING);
            sameAssistant(weight, player);
        } catch (ClientException e) {
            virtualView.printError(e, player.getNickname());
            return;
        }

        player.addAssistant(position);

        if (!round.nextPlanningTurn()) {
            if (player.getDeck().isEmpty())
                round.setLastRound();
            round.endPlanningPhase();
        }
    }

    /**
     * Check if the assistant is already used in that turn by other players,
     * if the assistant played was the only option available do nothing
     * if the player has other assistants to play throws an exception
     *
     * @param weight The weight of the assistant card chosen
     * @param player The player that has called the action: useAssistant
     * @throws ClientException When the player try to use the same assistant of others
     */
    private void sameAssistant(int weight, Player player) throws ClientException {
        boolean value = false;
        List<Integer> weights = new LinkedList<>();
        int size = player.getDeck().getSize();

        if (size == 1)
            return;

        for (Player p: game.getPlayers()){
            if (p.getDeck().getSize() == size - 1) {
                weights.add(p.getLastUsed().getWeight());
                if (p.getLastUsed().getWeight() == weight)
                    value = true;
            }
        }

        if (value && size == 2 && game.getRound().getNumTurnDone() == 2) {
            value = false;
            for (Assistant a : player.getDeck().getAssistant())
                if (!weights.contains(a.getWeight())) {
                    value = true;
                    break;
                }
        }

        if (value)
            throw new ClientException(ErrorType.SAME_ASSISTANT);
    }

    /**
     * uses the student on dining
     * @param player the current player
     * @param color the student color
     */
    public void useStudentDining(Player player, PawnColor color){
        boardHandler.useStudentDining(player, color);
    }

    /**
     * uses the student on island
     * @param player the current player
     * @param color the student color
     * @param position the island position
     */
    public void useStudentIsland(Player player, PawnColor color, int position){
        tableHandler.useStudentIsland(player, color, position);
    }

    /**
     * moves mother nature
     * @param player the current player
     * @param endPosition the end position of mother nature
     */
    public void moveMotherNature(Player player, int endPosition) {
        tableHandler.moveMotherNature(player, endPosition);
    }

    /**
     * choose cloud
     * @param player the current player
     * @param position the current position
     */
    public void chooseCloud(Player player, int position){
        tableHandler.chooseCloud(player, position);
    }

    /**
     * uses the character
     * @param player the current player
     * @param characterPosition the character position
     */
    public void useCharacter(Player player, int characterPosition){
        characterHandler.useCharacter(player, characterPosition);
    }

    /**
     * uses the character
     * @param player the current player
     * @param characterPosition the character position
     * @param islandPosition the island position
     */
    public void useCharacter(Player player, int characterPosition, int islandPosition){
        characterHandler.useCharacter(player, characterPosition, islandPosition);
    }

    /**
     * uses the character
     * @param player the current player
     * @param characterPosition the character position
     * @param color the student color
     */
    public void useCharacter(Player player, int characterPosition, PawnColor color){
        characterHandler.useCharacter(player, characterPosition, color);
    }

    /**
     * uses the character
     * @param player the current player
     * @param characterPosition the character position
     * @param colors the colors array
     */
    public void useCharacter(Player player, int characterPosition, int[] colors){
        player.getBoard().getDiningRoom().detach();
        characterHandler.useCharacter(player, characterPosition, colors);
        player.getBoard().getDiningRoom().reattach();
    }

    /**
     *  uses the character
     * @param player the current player
     * @param characterPosition the character position
     * @param islandPosition the island position
     * @param color the student color
     */
    public void useCharacter(Player player, int characterPosition, int islandPosition, PawnColor color){
        game.getTable().getIsland(islandPosition).detach();
        characterHandler.useCharacter(player, characterPosition, islandPosition, color);
        game.getTable().getIsland(islandPosition).attach(modelListener);

    }

    /**
     * gets the player by nickname
     * @param nickname the player nickname
     * @return the player with the given nickname
     */
    public Player getPlayerByNickname(String nickname){
        for (Player p: game.getPlayers()){
            if(p.getNickname().equals(nickname)) return p;
        }
        return null;
    }

    /**
     * gets the game
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * gets the table handler
     * @return the table handler
     */
    public TableHandler getTableHandler() {
        return tableHandler;
    }

    /**
     * starts the game
     * @throws InterruptedException the interrupted exception
     */
    public void gameStart() throws InterruptedException {
        synchronized (this) {
            while (!readyToStart())
                this.wait();

            game.startGame();
            game.getRound().attach(modelListener);
            for (Island i : game.getTable().getIsland()) {
                i.attach(modelListener);
            }
            for (Player p : game.getPlayers()) {
                p.attach(modelListener);
                p.getBoard().getDiningRoom().attach(modelListener);
            }
            game.getRound().getTurn().attach(modelListener);

        }
    }

    /**
     * gets true if the game is ready to start
     * @return true if the game is ready to start
     */
    private boolean readyToStart(){
        return canStart;
    }

    /**
     * gets the board handler
     * @return the board handler
     */
    public BoardHandler getBoardHandler() {
        return boardHandler;
    }
}

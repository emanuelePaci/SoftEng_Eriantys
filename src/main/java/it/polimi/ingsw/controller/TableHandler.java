package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.islandStrategy.IslandStrategy;
import it.polimi.ingsw.controller.motherNatureStrategy.MotherNatureStrategy;
import it.polimi.ingsw.controller.professorStrategy.ProfessorStrategy;
import it.polimi.ingsw.exceptions.BagIsEmptyException;
import it.polimi.ingsw.exceptions.ClientException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.enumerations.CharacterType;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.PlayerState;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;
import it.polimi.ingsw.model.table.Table;
import it.polimi.ingsw.server.VirtualView;

import java.util.List;

/**
 * Table handler class
 */
public class TableHandler {
    private TurnController turnController;
    private VirtualView virtualView;
    private Game game;
    private Context professorContext;
    private final ProfessorStrategy professorStrategyStandard;
    private final Context motherNatureContext;
    private final MotherNatureStrategy motherNatureStrategy;
    private Context islandContext;
    private final IslandStrategy islandStrategy;

    /**
     * Constructor
     * @param turnController the turn controller
     * @param game the current game
     * @param professorContext the professor context
     * @param motherNatureContext the mother nature context
     * @param islandContext the island context
     * @param professorStrategyStandard the professor strategy
     * @param motherNatureStrategy the mother nature strategy
     * @param islandStrategy the island strategy
     * @param virtualView the virtual view
     */
    public TableHandler(TurnController turnController, Game game, Context professorContext, Context motherNatureContext, Context islandContext, ProfessorStrategy professorStrategyStandard, MotherNatureStrategy motherNatureStrategy, IslandStrategy islandStrategy, VirtualView virtualView) {
        this.game = game;
        this.virtualView = virtualView;
        this.turnController = turnController;
        this.professorStrategyStandard = professorStrategyStandard;
        this.professorContext = professorContext;
        this.motherNatureContext = motherNatureContext;
        this.motherNatureStrategy = motherNatureStrategy;
        this.islandContext = islandContext;
        this.islandStrategy = islandStrategy;
        try {
            this.game.setMethodTable(getClass().getMethod("updateIsland", Island.class));
        } catch (NoSuchMethodException ignored){}
    }

    /**
     * removes the student from the entrance and adds it to the island
     * @param player the current player
     * @param color the student color
     * @param position the island position
     */
    public void useStudentIsland(Player player, PawnColor color, int position){
        Round round = game.getRound();

        try {
            turnController.checkPermission(round.getTurn(), player, PlayerState.ACTION);
            turnController.canMove(round.getTurn());
        } catch (ClientException e) {
            virtualView.printError(e, player.getNickname());
            return;
        }

        Student student = player.getBoard().getEntrance().removeStudent(color);
        game.getTable().getIsland(position).addStudent(student);
    }

    /**
     * moves mother nature
     * @param player the current player
     * @param endPosition the new mother nature position
     */
    public void moveMotherNature(Player player, int endPosition) {
        int numMoves;

        try {
            turnController.checkPermission(game.getRound().getTurn(), player, PlayerState.ACTION);
            turnController.canMoveMother(game.getRound().getTurn());
            numMoves = motherNatureContext.motherNatureControl(game.getTable(), endPosition, player);
        } catch (ClientException e) {
            virtualView.printError(e, player.getNickname());
            return;
        }

        game.getTable().moveMotherNature(numMoves);

        if (!updateIsland(game.getTable().getIsland(game.getTable().getMotherPosition())))
            player.changeState(PlayerState.ENDTURN);
    }

    /**
     * updated the islands, checks for merging and winner
     * @param island the current island
     * @return true if there is a winner
     */
    public boolean updateIsland(Island island){
        if (island.isNoEntryTiles()){
            for (int i = 0; i < 3; i++)
                if (game.getTable().getCharacter(i).getType().equals(CharacterType.GRANDMOTHER_HERBS)) {
                    game.getTable().getCharacter(i).returnNoEntryTiles();
                    island.setNoEntryTiles(false);
                    return false;
                }
        }
        else if(islandContext.conquerIsland(island , game)) {
            game.getTable().mergeIsland(game.getTable().getIsland().indexOf(island));

            for (Player p : game.getPlayers())
                if (p.getBoard().getTowerCourt().isEmpty()) {
                    winner();
                    return true;
                }

            if(game.getTable().getNumIsland() <= 3) {
                winner();
                return true;
            }
        }
        return false;
    }

    /**
     * takes the students from the chosen cloud and adds them to the player entrance
     * @param player the current player
     * @param position the cloud position
     */
    public void chooseCloud(Player player, int position){
        try {
            turnController.checkPermission(game.getRound().getTurn(), player, PlayerState.ENDTURN);
            turnController.checkCloud(game.getRound(), position);
        } catch (ClientException e){
            virtualView.printError(e, player.getNickname());
            return;
        }

        game.getRound().setCloudChosen(position);

        Table table = game.getTable();
        List<Student> student = table.getCloud(position).removeAllStudent();
        player.getBoard().getEntrance().addStudent(student);

        professorContext.changeContext(professorStrategyStandard);
        islandContext.changeContext(islandStrategy);
        motherNatureContext.changeContext(motherNatureStrategy);
        game.getRound().getTurn().setUsedCharacter(false);

        if (!game.getRound().nextActionTurn()){
            if (game.getRound().getLastRound().equals(true)) {
                winner();
                return;
            }

            try {
                List<Student> students = table.getBag().withdrawStudent(game.getNumPlayer() * (game.getNumPlayer() + 1));
                game.getTable().fillCloud(game.getNumPlayer(), students);
            }
            catch(BagIsEmptyException e) {
                game.getRound().setLastRound();
            }
            finally {
                game.getRound().endRound();
            }
        }
    }

    /**
     * checks if there is a winner
     */
    public void winner(){
        Player winner1 = new Player("", TowerColor.WHITE);
        Player winner2 = null;
        int numTowers = 9;
        int numProfessors = 0;

        for (Player p: game.getPlayers()){
            int nTowers = p.getBoard().getTowerCourt().getTower().size();
            int nProfessors = p.getBoard().getProfessorTable().getProfessors().size();

            if (numTowers > nTowers || (numTowers == nTowers && numProfessors < nProfessors)){
                winner1 = p;
                winner2 = null;
                numProfessors = nProfessors;
                numTowers = nTowers;
            } else if (numTowers == nTowers && numProfessors == nProfessors){
                winner2 = p;
            }
        }

        game.endGame();

        if(winner2 != null) {
            virtualView.printWinner(winner1.getNickname(), winner2.getNickname());
        }
        else
            virtualView.printWinner(winner1.getNickname(), null);
    }
}

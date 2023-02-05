package it.polimi.ingsw.controller.islandStrategy;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.PawnColor;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Professor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

/**
 * Mushroom Hunter strategy
 */
public class IslandStrategyMushroomHunter implements IslandStrategy {
    private PawnColor noColor = null;

    /**
     * Constructor
     */
    public IslandStrategyMushroomHunter(){

    }

    /**
     * calculates influence
     * @param island the current island
     * @param game the current game
     * @param owner the owner array
     * @param color the tower color
     * @param playerCandidate the player candidate
     */
    public void calculateInfluence(Island island, Game game, String[] owner, TowerColor color, String[] playerCandidate){
        int MoreInfluence = 0;

        for (Player p : game.getPlayers()){
            int TempInfluence = 0;
            for(Professor prof : p.getBoard().getProfessorTable().getProfessors()){
                if (!prof.getColor().equals(noColor))
                    TempInfluence += island.countStudent(prof.getColor());
            }
            if(!island.getIslandTower().isEmpty() && p.getTowerColor().equals(color)) {
                TempInfluence += island.getWeight();
                owner[0] = p.getNickname();
            }

            if (TempInfluence > MoreInfluence){
                MoreInfluence = TempInfluence;
                playerCandidate[0] = p.getNickname();
            } else if (TempInfluence == MoreInfluence) {
                playerCandidate[0] = "";
            }
        }
    }

    /**
     * sets the player
     * @param player the player to be set
     */
    @Override
    public void setPlayer(Player player) {

    }

    /**
     * sets color to avoid
     * @param noColor pawn color to avoid
     */
    @Override
    public void setNoColor(PawnColor noColor) {
        this.noColor = noColor;
    }
}

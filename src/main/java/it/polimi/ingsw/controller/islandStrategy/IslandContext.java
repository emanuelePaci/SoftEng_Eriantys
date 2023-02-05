package it.polimi.ingsw.controller.islandStrategy;

import it.polimi.ingsw.controller.Context;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

/**
 * Island Context class
 */
public class IslandContext extends Context {
    private IslandStrategy strategy;

    /**
     * Constructor
     * @param strategy the strategy
     */
    public IslandContext(IslandStrategy strategy){
        this.strategy = strategy;
    }

    /**
     * changes context
     * @param strategy ths island strategy
     */
    @Override
    public void changeContext(IslandStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * conquers an island
     * @param island the current island
     * @param game the current game
     * @return true if it is everything ok
     */
    @Override
    public boolean conquerIsland(Island island, Game game){
        boolean value = false;
        TowerColor color = null;
        String[] ownerNickname = {""};
        String[] playerCandidateNickname = {""};
        Player playerCandidate = null;
        Player owner = null;

        if (!island.getIslandTower().isEmpty())
            color = island.getIslandTower().get(0).getColor();

        strategy.calculateInfluence(island, game, ownerNickname, color, playerCandidateNickname);

        for (Player p: game.getPlayers()) {
            if (p.getNickname().equals(ownerNickname[0]))
                owner = p;
            else if (p.getNickname().equals(playerCandidateNickname[0]))
                playerCandidate = p;
        }

        if (playerCandidate == null)
            return false;

        if (owner == null){
            island.addTower(playerCandidate.getBoard().getTowerCourt().removeTower(1));
            return true;
        }

        if (!playerCandidate.getTowerColor().equals(color)) {
            value = true;

            owner.getBoard().getTowerCourt().addTower(island.removeTower());
            island.addTower(playerCandidate.getBoard().getTowerCourt().removeTower(island.getWeight()));
        }

        return value;
    }
}

package mc_minigames_plugin.mc_minigames_plugin.minigames;

import java.util.ArrayList;

/**
 * Program Description: Holds the basic information for all Player Area objects: Minigames, lobbies, hubs, etc.
 *
 * @author Kirt Robinson
 * @version March 5, 2023
 */
abstract public class PlayerArea {
    //FIELDS
    protected ArrayList<GamePlayer> areaPlayers; //List of current players in the specified game or lobby area


    //GENERAL MINI-GAME FEATURES

    /**
     * Returns the players in the current PlayerArea that are ready for game start
     * @param players
     * @return
     */
    public ArrayList<GamePlayer> getReadyPlayers (ArrayList<GamePlayer> players) {
        //Remove a player from the list if they are not ready
        players.removeIf(guy -> !guy.isGameReady());
        return players;
    }

}

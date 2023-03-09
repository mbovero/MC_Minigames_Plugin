package mc_minigames_plugin.mc_minigames_plugin.minigames;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Program Description: Holds the basic information for all Player Area objects: Minigames, lobbies, hubs, etc.
 *
 * @author Kirt Robinson
 * @version March 5, 2023
 */
abstract public class PlayerArea {
    // FIELDS
    protected ArrayList<GamePlayer> areaPlayers; // List of current players in the specified game or lobby area
    protected String areaName;


    // GENERAL PlayerArea FEATURES --------------------------------------------------------------------------------------

    /**
     * Returns the players in the current PlayerArea that are ready for game start
     * @param gamePlayers
     * @return
     */
    public ArrayList<GamePlayer> getReadyPlayers (ArrayList<GamePlayer> gamePlayers) {
        // Remove a player from the list if they are not ready
        gamePlayers.removeIf(gamePlayer -> !gamePlayer.isGameReady());
        return gamePlayers;
    }

    /**
     * Method adds the specified minecraft player reference to the PlayerArea object using that area's specific GamePlayer object
     * @param MCPlayer
     */
    abstract public void addPlayer (Player MCPlayer);

    /**
     * Method removes the specified minecraft player reference from the current PlayerArea object by
     * comparing individual GamePlayers to the reference
     * @param MCPlayer
     */
    public void removePlayer (Player MCPlayer) {
        for (GamePlayer gamePlayer : areaPlayers)
        {
            if (gamePlayer.isPlayer(MCPlayer)) {
                areaPlayers.remove(gamePlayer);
                break;
            }
        }
    }

    //Accessors --------------------------------------------------------------------------------------------------------

    /**
     * Accessor method that returns the name of the current PlayerArea
     * @return
     */
    public String getAreaName () {return areaName;}

    /**
     * Accessor method that returns the list of GamePlayer objects from the current PlayerArea
     * @return
     */
    public ArrayList<GamePlayer> getPlayers () {return areaPlayers;}



}

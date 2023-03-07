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
    //FIELDS
    protected ArrayList<GamePlayer> areaPlayers; //List of current players in the specified game or lobby area
    protected String areaName;


    //GENERAL PlayerArea FEATURES --------------------------------------------------------------------------------------

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

    /**
     * Method adds the specified minecraft player reference to the PlayerArea object using that area's specific player object
     * @param mcPlayer
     */
    abstract public void addPlayer (Player mcPlayer);

    /**
     * Method removes the specified minecraft player reference from the current PlayerArea object by
     * comparing individual players to the reference
     * @param player
     */
    public void removePlayer (Player player) {
        for (GamePlayer listPlayer : areaPlayers)
        {
            if (listPlayer.isPlayer(player)) {
                areaPlayers.remove(listPlayer);
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

package mc_minigames_plugin.mc_minigames_plugin.minigames;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

/**
 * Program Description: Holds the basic information for all Player Area objects: Minigames, lobbies, hubs, etc.
 *
 * @author Kirt Robinson
 * @version March 5, 2023
 */
abstract public class PlayerArea {
    // FIELDS
    protected Plugin plugin;                     // Reference to master plugin for initializing new objects
    protected HashMap<String,GamePlayer> areaPlayers; // Collection of current players in the specified game or lobby area
    protected String areaName;                   // The name of this area  -- DO NOT MAKE THIS STATIC


    // GENERAL PlayerArea FEATURES --------------------------------------------------------------------------------------

    /**
     * Adds the specified minecraft player reference to the PlayerArea object using that area's specific GamePlayer object
     * @param gamePlayer the gamePlayer to be added to this area
     */
    abstract public void addPlayer (GamePlayer gamePlayer);

    /**
         * Removes the specified MCPlayer from the current PlayerArea object by
         * comparing individual GamePlayers to the provided MCPlayer
         * @param gamePlayer the gamePlayer to be removed from this area
         */
    public void removePlayer (GamePlayer gamePlayer) {
        areaPlayers.remove(gamePlayer.MCPlayer.getName());
    }

    /**
     * Attempts to locate the specified MCPlayer's associated GamePlayer object from this PlayerArea's
     * areaPlayers collection. If found, returns the GamePlayer object. Otherwise, warn player and return null.
     *
     * @param MCPlayer the MCPlayer to find the gamePlayer of
     * @return the gamePlayer associated with the specified MCPlayer or null if not found
     */
    public GamePlayer findPlayer (Player MCPlayer) {
        // Try to retrieve gamePlayer reference from this areaPlayers
        GamePlayer result = this.areaPlayers.get(MCPlayer.getName());
        // If player not found...
        if (result == null) {
            // Warn player and return null
            MCPlayer.sendMessage("\nBAD BAD\n");
            return null;
        }
        // Otherwise, return the found gamePlayer
        return result;
    }

    //Accessors --------------------------------------------------------------------------------------------------------

    /**
     * Accessor method that returns the name of the current PlayerArea
     */
    public String getAreaName () {return areaName;}

    /**
     * Accessor method that returns the list of GamePlayer objects from the current PlayerArea
     */
    public HashMap<String,GamePlayer> getAreaPlayers () {return areaPlayers;}


}

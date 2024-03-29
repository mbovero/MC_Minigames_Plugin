package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Maps;

import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Random;

public abstract class Map {
    protected String mapName;           // The name of this map
    protected Location[] respawnLocs;   // A list of possible respawn locations for this map

    // Map bounds - used to keep players in bounds and reset map after each game
    protected int[] lowSWBound;             // The lower, South West bound of this map (x,y,z)
    protected int[] highNEBound;            // The higher, North East bound of this map (x,y,z)

    /**
     * Randomly teleports an ArrayList of gamePlayers to this map's respawn locations.
     */
    public void tpAll(HashMap<String, GamePlayer> playersToTeleport) {

        // CHANGE TO ASSIGN EACH PLAYER THEIR OWN SPAWN LOCATION

        // Setup
        Random rand = new Random();
        int respawnLocCount = this.respawnLocs.length;
        // Teleport each gamePlayer to a random respawn location
        for (GamePlayer gamePlayer : playersToTeleport.values())
            gamePlayer.getPlayer().teleport(this.respawnLocs[rand.nextInt(respawnLocCount)]);
    }

    /**
     * Teleports a single gamePlayer to one of this map's respawn locations.
     */
    public void tpPlayer(GamePlayer playerToTeleport) {
        // Setup
        Random rand = new Random();
        int respawnLocCount = this.respawnLocs.length;
        // Teleport gamePlayer to a random respawn location
        playerToTeleport.getPlayer().teleport(this.respawnLocs[rand.nextInt(respawnLocCount)]);
    }

    /**
     * Randomly returns one of this map's respawn locations.
     */
    public Location randomRespawnLoc() {
        // Setup
        Random rand = new Random();
        int respawnLocCount = this.respawnLocs.length;
        // Return a random respawn location
        return this.respawnLocs[rand.nextInt(respawnLocCount)];
    }

    /**
     * Removes all leftover items/entities on the map from the last game instance
     */
    public void cleanUp() {
        // Use bounds to reset map
    }

    public String getMapName() {
        return this.mapName;
    }
}

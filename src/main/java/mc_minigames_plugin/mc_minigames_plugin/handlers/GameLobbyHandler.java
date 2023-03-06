package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.Game;
import mc_minigames_plugin.mc_minigames_plugin.util.Locations;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.event.world.WorldInitEvent;

import java.util.ArrayList;

/**
 * Class Description: Class handles the states of game
 * lobbies and their relationships to players
 *
 * @author Kirt Robinson
 * @version March 5, 2023
 */
public class GameLobbyHandler implements Listener {

    ArrayList<Game> gameList;

    public GameLobbyHandler (MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWorldInitEvent (WorldInitEvent event) {

    }


    /*
     * Lobby/hub tags for keeping track of player location:
     *
     *  - "testing"  - does not perform usual resets to player
     *  - "mainHub"
     *  - "KOTHLobby"
     *  - "MMLobby"
     */

    /**
     * Sends the provided player to the main hub and sets their tags accordingly.
     *
     * @param player player to be sent
     */
    public static void sendMainHub(Player player) {
        player.teleport(Locations.mainHub);
        Tools.resetTags(player);
        player.addScoreboardTag("mainHub");
        player.addScoreboardTag("notInGame");
    }

    /**
     * Sends the provided player to the King of The Hill lobby and sets their tags accordingly.
     *
     * @param player player to be sent
     */
    public static void sendKOTHLobby(Player player) {
        player.teleport(Locations.KOTHLobby);
        Tools.resetTags(player);
        player.addScoreboardTag("KOTHLobby");
        player.addScoreboardTag("notInGame");
    }

    /**
     * Sends the provided player to the Murder Mystery lobby and sets their tags accordingly.
     *
     * @param player player to be sent
     */
    public static void sendMMLobby(Player player) {
        player.teleport(Locations.MMLobby);
        Tools.resetTags(player);
        player.addScoreboardTag("MMLobby");
        player.addScoreboardTag("notInGame");
    }
}

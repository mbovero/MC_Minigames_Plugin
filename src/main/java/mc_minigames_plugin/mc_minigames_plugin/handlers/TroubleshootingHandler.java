package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import static mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler.findPlayerGlobal;

/**
 * Class that allows for troubleshooting of server functions.
 *  - Prints name and location of entities that are right-clicked
 *
 * @author Miles Bovero
 * @version March 6, 2023
 */

public class TroubleshootingHandler implements Listener {

    public TroubleshootingHandler(MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        //Setup
        Player MCPlayer = event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayerGlobal(MCPlayer);
        Location entityLoc = event.getRightClicked().getLocation();

        if (gamePlayer.isTroubleshooting()) {
            // Print name/location of entities that the player right-clicks
            MCPlayer.sendMessage("You right-clicked " + event.getRightClicked().getName() + ChatColor.translateAlternateColorCodes('&',  "&f at " + entityLoc.getX() + ", " + entityLoc.getY() + ", " + entityLoc.getZ()));
            Bukkit.getLogger().info(MCPlayer.getName() + " right-clicked " + event.getRightClicked().getName() + ChatColor.translateAlternateColorCodes('&',  "&f at " + entityLoc.getX() + ", " + entityLoc.getY() + ", " + entityLoc.getZ()));
        }
    }
}

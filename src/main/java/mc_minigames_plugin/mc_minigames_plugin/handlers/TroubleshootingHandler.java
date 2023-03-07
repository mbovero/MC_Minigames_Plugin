package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Set;

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
        Player player = event.getPlayer();
        Set<String> tags = event.getPlayer().getScoreboardTags();
        Location entityLoc = event.getRightClicked().getLocation();

        if (player.getScoreboardTags().contains("troubleshooting")) {
            // Print name/location of entities that the player right-clicks
            player.sendMessage("You right-clicked " + event.getRightClicked().getName() + ChatColor.translateAlternateColorCodes('&',  "&f at " + entityLoc.getX() + ", " + entityLoc.getY() + ", " + entityLoc.getZ()));
            Bukkit.getLogger().info(player.getName() + " right-clicked " + event.getRightClicked().getName() + ChatColor.translateAlternateColorCodes('&',  "&f at " + entityLoc.getX() + ", " + entityLoc.getY() + ", " + entityLoc.getZ()));
        }
    }
}

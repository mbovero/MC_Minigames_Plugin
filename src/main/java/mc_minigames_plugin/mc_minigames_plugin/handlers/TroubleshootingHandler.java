package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Set;

public class TroubleshootingHandler implements Listener {

    public TroubleshootingHandler(MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        //Setup
        Player player = event.getPlayer();
        Set<String> tags = event.getPlayer().getScoreboardTags();
        Location entityLoc = event.getRightClicked().getLocation();

        if (player.getScoreboardTags().contains("testing")) {
            // Print name of entities that the player right-clicks
            player.sendMessage("You right-clicked " + event.getRightClicked().getName() + ChatColor.translateAlternateColorCodes('&',  "&f at " + entityLoc.getX() + ", " + entityLoc.getY() + ", " + entityLoc.getZ()));
        }
    }
}

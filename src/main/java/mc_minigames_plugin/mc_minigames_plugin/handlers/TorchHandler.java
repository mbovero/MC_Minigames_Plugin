package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class TorchHandler implements Listener {
    public TorchHandler (MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Lowest
     * Low
     * Normal
     * High
     * Highest
     * --------
     * Monitor
     */

    @EventHandler(priority = EventPriority.LOW)
    public void onTorchPlace_Low(BlockPlaceEvent event) {
        // If a torch is placed...
        if (event.getBlock().getType() == Material.TORCH) {
//            event.setCancelled(true);   // Other events will still run!!!

            // Permission node
            // MC_Minigames_Plugin.torch.diamond

            // Check if player has permission
            if (event.getPlayer().hasPermission("MC_Minigames_Plugin.torch.diamond")) {
                // Place diamond block instead of a torch
//                event.getBlock().setType(Material.DIAMOND_BLOCK);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)   // This is added to ensure this does not run if the event was previously cancelled
    public void onTorchPlace_Normal(BlockPlaceEvent event) {
        Block block = event.getBlock();

        if (block.getType() != Material.TORCH) {
            return;
        }

        Bukkit.getLogger().info("A torch was placed");
    }
}

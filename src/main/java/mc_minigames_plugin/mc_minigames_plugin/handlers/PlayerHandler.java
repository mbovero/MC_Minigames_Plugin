package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerHandler implements Listener {
    public PlayerHandler (MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // How to give items to players:
        ItemStack item = new ItemStack(Material.STICK, 10);
        Inventory inv = player.getInventory();

            // Item info
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Testing");
        item.setItemMeta(meta);

        inv.addItem(item);  // Added to next available spot
        inv.setItem(8, item);   // Added to slot 8 in inventory
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        // Ensure that a player was hurt by fall damage
        if (!(event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        // Give a player a diamond after five seconds
        Player player = (Player) event.getEntity();
        DelayedTask task = new DelayedTask(() -> {player.getInventory().addItem(new ItemStack(Material.DIAMOND));}, 20 * 5);
        // Cancel the task
        Bukkit.getScheduler().cancelTask(task.getId());
    }
}

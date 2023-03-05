package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import mc_minigames_plugin.mc_minigames_plugin.util.Locations;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public class PlayerHandler implements Listener {
    public PlayerHandler (MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Setup & retrieve data
        Player player = event.getPlayer();
        Inventory inv = player.getInventory();

        // Tp player to hub
        player.teleport(Locations.hub);
        // Clear inventory
        inv.clear();
        // Clear potion effects
        Collection<PotionEffect> effectsToClear = player.getActivePotionEffects();
        for (PotionEffect pE : effectsToClear)
            player.removePotionEffect(pE.getType());

        ItemStack item = new ItemStack(Material.COMPASS, 1);


        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aGame Selector"));
//        meta.

//        // How to give items to players:
//        ItemStack item = new ItemStack(Material.STICK, 10);
//        Inventory inv = player.getInventory();
//
//            // Item info
//        ItemMeta meta = item.getItemMeta();
//        meta.setDisplayName("Testing");
//        item.setItemMeta(meta);
//
//        inv.addItem(item);  // Added to next available spot
//        inv.setItem(8, item);   // Added to slot 8 in inventory
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

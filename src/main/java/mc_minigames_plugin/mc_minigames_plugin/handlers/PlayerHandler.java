package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import mc_minigames_plugin.mc_minigames_plugin.util.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.Set;

import static mc_minigames_plugin.mc_minigames_plugin.util.Tools.createItem;

public class PlayerHandler implements Listener {
    public PlayerHandler (MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
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
        // Clear player tags  -  change to handle actions based on current tags
        Set<String> tagsToRemove = player.getScoreboardTags();
        for (String tag : tagsToRemove)
            if (!tag.equals("GameDev"))
                player.removeScoreboardTag(tag);


        // Remove from teams

        // Reset display name



        // Main hub menu selection
        ItemStack item = createItem(new ItemStack(Material.COMPASS), "&aLobby Selector", "&fExplore our selection of games!");
        inv.setItem(4, item);


    }

    /**
     * Opens/formats the player's Game Selector menu
     */
    @EventHandler
    public void onPlayerUseMenu(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            if (player.getItemInHand().getItemMeta() != null && player.getItemInHand().getItemMeta().getDisplayName().equals("§aLobby Selector")) {

                Inventory inv = Bukkit.createInventory(player, 9 * 3, "Lobby Selector");

                inv.setItem(11, createItem(new ItemStack(Material.RED_BED), "&2Main Hub","&7Home sweet home"));
                inv.setItem(13, createItem(new ItemStack(Material.GRASS_BLOCK), "&aKOTH","&7Conquer the Hill"));
                inv.setItem(15, createItem(new ItemStack(Material.DIAMOND_SWORD), "&cMurder Mystery","&7Stab your friends! :D"));

                player.openInventory(inv);
            }
    }

    /**
     * Detects and handles player interactions within the Game Selection menu
     */
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        // Only handle inv clicks if player is in /menu inventory
        if (event.getView().getTitle().equals("Lobby Selector")) {

            // Retrieve the slot number that the player clicked on
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();

            // Make sure an item was clicked
            if (event.getCurrentItem() != null) {
                // Execute correlating operation
                if (slot == 11 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§2Main Hub"))
                    player.teleport(Locations.hub);
                else if (slot == 13 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§aKOTH"))
                    player.teleport(Locations.KOTHhub);
                else if (slot == 15 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§cMurder Mystery"))
                    player.teleport(Locations.MMhub);

                // Cannot move items around, only click on them
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
            }
        }
    }






    @EventHandler
    public void preventHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
            event.setCancelled(true);




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

package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import mc_minigames_plugin.mc_minigames_plugin.util.Locations;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.HashSet;
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

    /**
     * Reset the player and their data when they join the server
     */
    @EventHandler
    public void resetOnPlayerJoin(PlayerJoinEvent event) {
        // Setup & retrieve data
        Player player = event.getPlayer();
        Inventory inv = player.getInventory();

        // Reset player data on join -----------------------------------------------------------------------------------
        // Unless troubleshooting...
        if (!player.getScoreboardTags().contains("Testing")) {
            // Tp player to hub
            player.teleport(Locations.hub);
            // Set to adventure mode
            player.setGameMode(GameMode.ADVENTURE);
            // Prevent/reset flying
            player.setAllowFlight(false);
            player.setFlying(false);
            // Clear inventory
            inv.clear();
            // Clear potion effects
            Collection<PotionEffect> effectsToClear = player.getActivePotionEffects();
            for (PotionEffect pE : effectsToClear)
                player.removePotionEffect(pE.getType());
            // Reset health
            player.resetMaxHealth();
            player.setHealth(player.getMaxHealth());
            // Reset hunger
            player.setFoodLevel(20);
            // Reset player scores
            Bukkit.getScoreboardManager().getMainScoreboard().resetScores(player);      // Resets all objective scores for player - get individual objectives and player score object to change individual scores
            // Remove player from team
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(player);
            if (team != null)
                Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(player).removePlayer(player);
            // Clear player tags  -  change to handle actions based on current tags
            Set<String> tagsToRemove = new HashSet<>(player.getScoreboardTags());       // Get copy of player's current tags
            for (String tag : tagsToRemove)
                if (!tag.equals("GameDev")) {      // TEMP... only used for data pack capabilities
                    player.removeScoreboardTag(tag);
                    Bukkit.getLogger().info("removed " + tag);
                }
            // Give mainHub tag
            player.addScoreboardTag("mainHub");


            // Reset display name


            // Lobby selection tool ----------------------------------------------------------------------------------------
            ItemStack item = createItem(new ItemStack(Material.COMPASS), "&aLobby Selector", "&fExplore our selection of games!");
            inv.setItem(4, item);
        }
    }

    /**
     * Opens/formats the player's Lobby Selector menu when the item is right-clicked
     */
    @EventHandler
    public void onPlayerUseMenu(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        // Detect when player right clicks
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            // Detect right click with Lobby Selector compass
            if (player.getItemInHand().getItemMeta() != null && player.getItemInHand().getItemMeta().getDisplayName().equals("§aLobby Selector")) {

                // Create "UI"
                Inventory inv = Bukkit.createInventory(player, 9 * 3, "Lobby Selector");
                // UI options:
                // Main Hub
                inv.setItem(11, createItem(new ItemStack(Material.RED_BED), "&2Main Hub","&7Home sweet home"));
                // KOTH lobby
                inv.setItem(13, createItem(new ItemStack(Material.GRASS_BLOCK), "&aKOTH","&7Conquer the Hill"));
                // MM lobby
                inv.setItem(15, createItem(new ItemStack(Material.DIAMOND_SWORD), "&cMurder Mystery","&7Stab your friends! :D"));

                // Open the created "UI"
                player.openInventory(inv);
            }
    }

    /**
     * Detects and handles player interactions within the Game Selection menu
     */
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        // Only handle inv clicks if player is in Lobby Selector inventory
        if (event.getView().getTitle().equals("Lobby Selector")) {

            // Retrieve the slot number that the player clicked on
            Player player = (Player) event.getWhoClicked();
            int slot = event.getSlot();

            // Make sure an inventory item was clicked
            if (event.getCurrentItem() != null) {
                // Send player to...
                // Main Hub
                if (slot == 11 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§2Main Hub"))
                    player.teleport(Locations.hub);
                // KOTH lobby
                else if (slot == 13 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§aKOTH"))
                    player.teleport(Locations.KOTHhub);
                // MM lobby
                else if (slot == 15 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§cMurder Mystery"))
                    player.teleport(Locations.MMhub);

                // Cannot move items around, only click on them
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
            }
        }
    }

    /**
     * Prevents players from dropping items in lobbies
     */
    @EventHandler
    public void preventItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Set<String> tags = player.getScoreboardTags();
        if (tags.contains("mainHub"))
            event.setCancelled(true);
    }

    /**
     * Prevents hunger in lobbies
     */
    @EventHandler
    public void preventHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Set<String> tags = player.getScoreboardTags();
            if (tags.contains("mainHub"))
                event.setCancelled(true);
        }
    }

    /**
     * Prevents player damage in lobbies
     */
    @EventHandler
    public void preventDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Set<String> tags = player.getScoreboardTags();
            if (tags.contains("mainHub"))
                event.setCancelled(true);
        }




        // Tutorial stuff
//        // Ensure that a player was hurt by fall damage
//        if (!(event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL)) {
//            return;
//        }
//
//        // Give a player a diamond after five seconds
//        DelayedTask task = new DelayedTask(() -> {player.getInventory().addItem(new ItemStack(Material.DIAMOND));}, 20 * 5);
//        // Cancel the task
//        Bukkit.getScheduler().cancelTask(task.getId());
    }
}

package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.util.Locations;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
        // Reset player scores
        Bukkit.getScoreboardManager().getMainScoreboard().resetScores(player);      // Resets all objective scores for player - get individual objectives and player score object to change individual scores
        // Remove player from team
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(player);
        if (team != null)
            Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(player).removePlayer(player);
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
        if (!player.getScoreboardTags().contains("testing")) {
            // Send player to hub (reset inv and tp)
            GameLobbyHandler.sendMainHub(player);
            // Set to adventure mode
            player.setGameMode(GameMode.ADVENTURE);
            // Prevent/reset flying
            player.setAllowFlight(false);
            player.setFlying(false);
            // Clear potion effects
            Collection<PotionEffect> effectsToClear = player.getActivePotionEffects();
            for (PotionEffect pE : effectsToClear)
                player.removePotionEffect(pE.getType());
            // Reset health
            player.resetMaxHealth();
            player.setHealth(player.getMaxHealth());
            // Reset hunger
            player.setFoodLevel(20);

            // Currently on leave and join, probably only need one
            // Reset player scores
            Bukkit.getScoreboardManager().getMainScoreboard().resetScores(player);      // Resets all objective scores for player - get individual objectives and player score object to change individual scores
            // Remove player from team
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(player);
            if (team != null)
                Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(player).removePlayer(player);

            // Clear player tags  -  change to handle actions based on current tags
            Tools.resetTags(player);
            // Give necessary tags
            player.addScoreboardTag("mainHub");
            player.addScoreboardTag("notInGame");



        }
    }

    /**
     *  - Opens/formats the player's Lobby Selector menu when the item is right-clicked
     *  - Gives functionality of button press to KOTH lobby
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Set<String> tags = player.getScoreboardTags();
        if (tags.contains("notInGame")) {

            // Detect when player right clicks
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                // Detect right click with Lobby Selector compass
                if (player.getItemInHand().getItemMeta() != null && player.getItemInHand().getItemMeta().getDisplayName().equals("§aLobby Selector")) {

                    // Create "UI"
                    Inventory inv = Bukkit.createInventory(player, 9 * 3, "Lobby Selector");
                    // UI options:
                    // KOTH lobby
                    inv.setItem(11, createItem(new ItemStack(Material.GRASS_BLOCK), "&aKOTH", "&7Conquer the Hill"));
                    // Main Hub
                    inv.setItem(13, createItem(new ItemStack(Material.RED_BED), "&2Main Hub", "&7Home sweet home"));
                    // MM lobby
                    inv.setItem(15, createItem(new ItemStack(Material.DIAMOND_SWORD), "&cMurder Mystery", "&7Stab your friends! :D"));

                    // Open the created "UI"
                    player.openInventory(inv);
                }


            // LOBBY BUTTON CLICK DETECTIONS:

            // KOTH lobby button
            // Detect when player right-clicks on a block
            if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                // Define button location
                Location KOTHButtonLoc = new Location(Bukkit.getWorld("world"), 17, -46, -26);
                // Detect click on button
                if (event.getClickedBlock().getLocation().equals(KOTHButtonLoc)) {
                    // Transport player
                    GameLobbyHandler.sendKOTHLobby(player);
                    player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1.2f);
                }
            }

            // MM lobby button
            // Detect when player right-clicks on a block
            if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                // Define button location
                Location MMButtonLoc = new Location(Bukkit.getWorld("world"), 9, -44, 24);
                Location MMButtonLoc2 = new Location(Bukkit.getWorld("world"), 7, -44, 24);
                // Detect click on button
                if (event.getClickedBlock().getLocation().equals(MMButtonLoc) || event.getClickedBlock().getLocation().equals(MMButtonLoc2)) {
                    // Transport player
                    GameLobbyHandler.sendMMLobby(player);
                    player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1.2f);
                }
            }
        }
    }

    /**
     * Detects and handles player interactions within the Game Selection menu
     */
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Set<String> tags = player.getScoreboardTags();
        // For players not in a game...
        if (tags.contains("notInGame")) {
            // Only handle inv clicks if player is in Lobby Selector inventory
            if (event.getView().getTitle().equals("Lobby Selector")) {

                // Retrieve the slot number that the player clicked on
                int slot = event.getSlot();

                // Make sure an inventory item was clicked
                if (event.getCurrentItem() != null) {
                    // Send player to...
                    // KOTH lobby
                    if (slot == 11 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§aKOTH")) {
                        GameLobbyHandler.sendKOTHLobby(player);
                        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
                        event.getWhoClicked().closeInventory();
                    }
                    // Main Hub
                    else if (slot == 13 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§2Main Hub")) {
                        GameLobbyHandler.sendMainHub(player);
                        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
                        event.getWhoClicked().closeInventory();
                    }
                    // MM lobby
                    else if (slot == 15 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§cMurder Mystery")) {
                        GameLobbyHandler.sendMMLobby(player);
                        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
                        event.getWhoClicked().closeInventory();
                    }

                    // Cannot move items around, only click on them
                    event.setCancelled(true);
                }
            }
            // Lock inventory
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players from dropping items in lobbies
     */
    @EventHandler
    public void preventItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Set<String> tags = player.getScoreboardTags();
        if (tags.contains("notInGame"))
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
            if (tags.contains("notInGame"))
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
            if (tags.contains("notInGame"))
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

    /**
     * Gives players levitation when they fall below a certain Y-level in lobbies.
     */
    @EventHandler
    public void voidLevitation(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Set<String> tags = player.getScoreboardTags();
        // For all players not in a game...
        if (event.getTo().getY() < -66 && event.getTo().getY() > -85 && tags.contains("notInGame")) {
            // Apply main hub levitation
            if (tags.contains("mainHub"))
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 22, false));
            // Apply KOTH lobby levitation
            else if (tags.contains("KOTHLobby"))
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 10, false));
            // Apply MM lobby levitation
            else if (tags.contains("MMLobby"))
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 18, false));
        }
        // Return players to main hub when they go out of bounds
        else if (event.getTo().getY() < -90 && tags.contains("notInGame"))
            GameLobbyHandler.sendMainHub(player);
    }





    /**
     * Provides functionality for portal returning players to main hub from KOTH lobby
     *
     * MOVE TO KOTH LOBBY HANDLER
     */
    @EventHandler
    public void returnPortal(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Set<String> tags = player.getScoreboardTags();
        // Detect players in portal range and in KOTH lobby
        if (event.getTo().getY() < -56 && event.getTo().getY() > -63 &&
                event.getTo().getZ() < -594 && event.getTo().getZ() > -595 &&
                event.getTo().getX() < 11 && event.getTo().getX() > 5 &&
                tags.contains("KOTHLobby")) {
            // Play portal sound at KOTH lobby to surrounding players
            for (Player p : Bukkit.getOnlinePlayers())
                if (p.getScoreboardTags().contains("notInGame"))
                    p.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, .8f);
            // Transport player to main hub
            GameLobbyHandler.sendMainHub(player);
            // Play portal sound at main hub to surrounding players
            for (Player p : Bukkit.getOnlinePlayers())
                if (p.getScoreboardTags().contains("notInGame"))
                    p.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, .8f);
        }
    }
}

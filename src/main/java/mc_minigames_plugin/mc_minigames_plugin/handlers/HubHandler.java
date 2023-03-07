package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHPlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Set;

/**
 *  - Handles players when leaving/joining
 *  - Provides functionality for main hub buttons
 *  - Prevents players from changing the environment
 *  - Prevents players from getting hungry, taking damage, and dying
 *  - Levitates players when they fall into the void
 *
 * @author Kirt Robinson, Miles Bovero
 * @version March 6, 2023
 */
public class HubHandler extends PlayerArea implements Listener {
    public HubHandler(MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        areaName = "mainHub";
    }

    /**
     * Resets player team and scoreboard data when they leave.
     */
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
     * Resets the player and their data when they join the server
     */
    @EventHandler
    public void resetOnPlayerJoin(PlayerJoinEvent event) {
        // Setup & retrieve data
        Player player = event.getPlayer();

        // Unless troubleshooting...
        if (!player.getScoreboardTags().contains("troubleshooting")) {
            // Send player to hub (reset inv and tp)
            GeneralLobbyHandler.sendMainHub(player, this);
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
     * Gives functionality of button presses within main hub
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // Setup
        Player player = event.getPlayer();
        Set<String> tags = player.getScoreboardTags();

        // For players that are not in a game or troubleshooting...
        if (tags.contains("notInGame") || tags.contains("troubleshooting")) {

            // LOBBY BUTTON CLICK DETECTIONS:

            // KOTH lobby button
            // Detect when player right-clicks on a block
            if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                // Define button location
                Location KOTHButtonLoc = new Location(Bukkit.getWorld("world"), 17, -46, -26);
                // Detect click on button
                if (event.getClickedBlock().getLocation().equals(KOTHButtonLoc)) {
                    // Transport player
                    GeneralLobbyHandler.sendKOTHLobby(player, this);
                    // Play sound
                    player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1.2f);
                }
            }

            // MM lobby button
            // Detect when player right-clicks on a block
            if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                // Define button locations
                Location MMButtonLoc = new Location(Bukkit.getWorld("world"), 9, -44, 24);
                Location MMButtonLoc2 = new Location(Bukkit.getWorld("world"), 7, -44, 24);
                // Detect click on button
                if (event.getClickedBlock().getLocation().equals(MMButtonLoc) || event.getClickedBlock().getLocation().equals(MMButtonLoc2)) {
                    // Transport player
                    GeneralLobbyHandler.sendMMLobby(player, this);
                    // Play sound
                    player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1.2f);
                }
            }
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from harvesting blocks.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventHarvestBlock(PlayerHarvestBlockEvent event) {
        Set<String> tags = event.getPlayer().getScoreboardTags();
        if (tags.contains("notInGame") && !tags.contains("troubleshooting")) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from manipulating armor stands.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        Set<String> tags = event.getPlayer().getScoreboardTags();
        if (tags.contains("notInGame") && !tags.contains("troubleshooting")) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from consuming items.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventItemConsume(PlayerItemConsumeEvent event) {
        Set<String> tags = event.getPlayer().getScoreboardTags();
        if (tags.contains("notInGame") && !tags.contains("troubleshooting")) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from placing blacks.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventBlockPlace(BlockPlaceEvent event) {
        Set<String> tags = event.getPlayer().getScoreboardTags();
        if (tags.contains("notInGame") && !tags.contains("troubleshooting")) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from breaking blocks.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventBlockBreak(BlockBreakEvent event) {
        Set<String> tags = event.getPlayer().getScoreboardTags();
        if (tags.contains("notInGame") && !tags.contains("troubleshooting")) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from damaging entities and breaking armor stands.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventEntityDamage(EntityDamageByEntityEvent event) {
        Set<String> tags = event.getDamager().getScoreboardTags();
        if (tags.contains("notInGame") && !tags.contains("troubleshooting")) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from killing entities.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null)
            return;
        Set<String> tags = event.getEntity().getKiller().getScoreboardTags();
        if (tags.contains("notInGame") && !tags.contains("troubleshooting")) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from dropping items in lobbies
     */
    @EventHandler
    public void preventItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Set<String> tags = player.getScoreboardTags();
        if (tags.contains("notInGame") && !tags.contains("troubleshooting"))
            event.setCancelled(true);
    }

    /**
     * Prevents hunger in lobbies for players not in games and not troubleshooting
     */
    @EventHandler
    public void preventHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Set<String> tags = player.getScoreboardTags();
            if (tags.contains("notInGame") && !tags.contains("troubleshooting"))
                event.setCancelled(true);
        }
    }

    /**
     * Prevents damage in lobbies for players not in games and not troubleshooting
     */
    @EventHandler
    public void preventDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Set<String> tags = player.getScoreboardTags();
            if (tags.contains("notInGame") && !tags.contains("troubleshooting"))
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
     * Gives players not in games and not troubleshooting levitation when they fall below a certain Y-level in lobbies.
     */
    @EventHandler
    public void voidLevitation(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Set<String> tags = player.getScoreboardTags();
        // For all players not in a game and not troubleshooting...
        if (event.getTo().getY() < -66 && event.getTo().getY() > -85 && tags.contains("notInGame") && !tags.contains("troubleshooting")) {
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
            GeneralLobbyHandler.sendMainHub(player, this);
    }

    @Override
    public void addPlayer(Player mcPlayer) {

    }
}

package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.HubPlayer;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
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
        areaPlayers = new ArrayList<>();
        areaName = "mainHub";
    }

    /**
     * Resets player team and scoreboard data when they leave.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player MCPlayer = event.getPlayer();
        // Reset player scores
        Bukkit.getScoreboardManager().getMainScoreboard().resetScores(MCPlayer);      // Resets all objective scores for player - get individual objectives and player score object to change individual scores
        // Remove player from team
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(MCPlayer);
        if (team != null)
            Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(MCPlayer).removePlayer(MCPlayer);
    }

    /**
     * Resets the player and their data when they join the server
     */
    @EventHandler
    public void resetOnPlayerJoin(PlayerJoinEvent event) {
        // Setup & retrieve data
        Player MCPlayer = event.getPlayer();

        // Unless troubleshooting...
        if (!MCPlayer.getScoreboardTags().contains("troubleshooting")) {
            // Send player to hub (reset inv and tp)
            areaPlayers.add(new HubPlayer(MCPlayer, this));
            GeneralLobbyHandler.sendMainHub(MCPlayer, this);
            // Set to adventure mode
            MCPlayer.setGameMode(GameMode.ADVENTURE);
            // Prevent/reset flying
            MCPlayer.setAllowFlight(false);
            MCPlayer.setFlying(false);
            // Clear potion effects
            Collection<PotionEffect> effectsToClear = MCPlayer.getActivePotionEffects();
            for (PotionEffect pE : effectsToClear)
                MCPlayer.removePotionEffect(pE.getType());
            // Reset health
            MCPlayer.resetMaxHealth();
            MCPlayer.setHealth(MCPlayer.getMaxHealth());
            // Reset hunger
            MCPlayer.setFoodLevel(20);

            // Currently on leave and join, probably only need one
            // Reset player scores
            Bukkit.getScoreboardManager().getMainScoreboard().resetScores(MCPlayer);      // Resets all objective scores for player - get individual objectives and player score object to change individual scores
            // Remove player from team
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(MCPlayer);
            if (team != null)
                Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(MCPlayer).removePlayer(MCPlayer);

            // Clear player tags  -  change to handle actions based on current tags
            Tools.resetTags(MCPlayer);
            // Give necessary tags
            MCPlayer.addScoreboardTag("mainHub");
            MCPlayer.addScoreboardTag("notInGame");
        }
    }

    /**
     * Gives functionality of button presses within main hub
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Setup
        Player MCPlayer = event.getPlayer();
        Set<String> tags = MCPlayer.getScoreboardTags();

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
                    GeneralLobbyHandler.sendKOTHLobby(MCPlayer, this);
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
                    GeneralLobbyHandler.sendMMLobby(MCPlayer, this);
                }
            }
        }
    }

    @Override
    public void addPlayer(Player MCPlayer) {
        areaPlayers.add(new HubPlayer(MCPlayer, this));
    }
}

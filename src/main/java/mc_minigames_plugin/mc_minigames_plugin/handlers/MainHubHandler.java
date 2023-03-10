package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.HubPlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collection;

import static mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler.*;

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
public class MainHubHandler extends PlayerArea implements Listener {

    public MainHubHandler(MC_Minigames_Plugin plugin) {
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

        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // Get the player's current area
        PlayerArea playerArea = gamePlayer.getCurrentArea();
        // Remove the player from that area
        playerArea.removePlayer(gamePlayer);

    }

    /**
     * Resets the player and their data when they join the server
     */
    @EventHandler
    public void resetOnPlayerJoin(PlayerJoinEvent event) {
        // Setup & retrieve data
        Player MCPlayer = event.getPlayer();

        // Send player to hub (reset inv and tp)
        areaPlayers.add(new HubPlayer(MCPlayer, this));

        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);

        // Send to mainHub
        GeneralLobbyHandler.sendMainHub(gamePlayer);


        // Unless troubleshooting...
        if (!gamePlayer.isTroubleshooting()) {
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
            MCPlayer.setSaturation(5);

            // Currently on leave and join, probably only need one
            // Reset player scores
            Bukkit.getScoreboardManager().getMainScoreboard().resetScores(MCPlayer);      // Resets all objective scores for player - get individual objectives and player score object to change individual scores
            // Remove player from team
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(MCPlayer);
            if (team != null)
                Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(MCPlayer).removePlayer(MCPlayer);

            // Clear player tags  -  change to handle actions based on current tags
            Tools.resetTags(MCPlayer);
        }
    }

    /**
     * Gives functionality of button presses within main hub
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Setup
        Player MCPlayer = event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);

        // For players that are not in a game or are troubleshooting and are in mainHub...
        if (!gamePlayer.isInGame() || gamePlayer.isTroubleshooting() && gamePlayer.getCurrentArea().getAreaName().equals("mainHub")) {

            // LOBBY BUTTON CLICK DETECTIONS:

            // KOTH lobby button
            // Detect when player right-clicks on a block
            if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                // Define button location
                Location KOTHButtonLoc = new Location(Bukkit.getWorld("world"), 17, -46, -26);
                // Detect click on button
                if (event.getClickedBlock().getLocation().equals(KOTHButtonLoc))
                    // Transport player
                    GeneralLobbyHandler.sendKOTHLobby(gamePlayer);
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
                    GeneralLobbyHandler.sendMMLobby(gamePlayer);
                }
            }
        }
    }

    /**
     * Adds the specified gamePlayer to mainHub's list of players. Also resets
     * the gamePlayer's current area, isInGame, and isGameReady
     *
     * @param gamePlayer the gamePlayer to be added to this area
     */
    @Override
    public void addPlayer(GamePlayer gamePlayer) {
        gamePlayer.setCurrentArea(this);
        gamePlayer.setIsInGame(false);
        gamePlayer.setIsGameReady(false);
        areaPlayers.add(new HubPlayer(gamePlayer));
    }

    /**
     * If the given gamePlayer is not already in the mainHub, they are
     * removed from their previous area and placed in the list of mainHub players.
     * @param gamePlayer object to be set to mainHub
     */
    public static void sendPlayer(GamePlayer gamePlayer) {
        // If the player's current area is not mainHub...
        if (!(gamePlayer.getCurrentArea().getAreaName().equals("mainHub"))) {
            // Remove the player from their previous area
            gamePlayer.getCurrentArea().removePlayer(gamePlayer);
            // Add the player to the main hub
            mainHub.addPlayer(gamePlayer);
        }
    }
}

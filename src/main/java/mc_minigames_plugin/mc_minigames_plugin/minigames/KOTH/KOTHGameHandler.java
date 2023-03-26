package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Maps.Map;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.List;

import static mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler.findPlayer;

public class KOTHGameHandler extends PlayerArea implements Listener {

    // KOTH game settings
    String gameMode;          // The currently selected KOTH gamemode
    Map map;             // The currently selected KOTH map

    public KOTHGameHandler (MC_Minigames_Plugin plugin, ArrayList<GamePlayer> gamePlayers, String gameMode, Map map) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.areaPlayers = gamePlayers;
        this.gameMode = gameMode;
        this.map = map;
        areaName = this.map.getMapName();
        gameStart();
    }

    /**
     * Begins this KOTHGame, updates the gamePlayers currentAreas and isInGame values,
     * and begins other initial game functions
     */
    public void gameStart() {
        // Update/reset all players'
        for (GamePlayer gamePlayer : areaPlayers)
        {
            // Change gamePlayer's current area to this game
            gamePlayer.setCurrentArea(this);
            // Set gamePlayer to be inGame
            gamePlayer.setIsInGame(true);

            // Reset MCPlayer's tags, scores, flight, potion effects, health, hunger, and inventory
            Tools.resetAllKOTH(gamePlayer.getPlayer());
            // Give player kit gear
            ((KOTHPlayer)gamePlayer).getKit().giveBasicGear();
            // Alert players of game start
            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BIT, 5, 2);
            gamePlayer.getPlayer().sendTitle(ChatColor.GOLD + "KOTH", "");
            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer(), Sound.ENTITY_ENDER_DRAGON_GROWL, 5, .1F);
        }

        // Tp all MCPlayers to this game's map
        this.map.tpAll(areaPlayers);

        // Initialize game timers
        // Initialize game specific blocks and objects
        // Scoreboard displays

        // Stop game in 60 seconds
        new DelayedTask(this::gameStop, 20*60);
    }

    // Running Game
    /**
     * Updates KOTHPlayer's kills/kill rewards when they kill another player
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Store player that died
        Player MCPlayer = event.getPlayer();
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // Check that player died in this game
        if (!gamePlayer.getCurrentArea().getAreaName().equals(this.areaName))
            return;
        // Store MC killer
        Player MCKiller = event.getPlayer().getKiller();
        // Check that killer is not null
        if(MCKiller == null)
            return;
        // Locate killer's gamePlayer
        GamePlayer gameKiller = findPlayer(MCKiller);
        // Check that the killer exists in this KOTHGame
        if(gameKiller.getCurrentArea().getAreaName().equals(this.areaName)) {
            // Update KOTHPlayer kills
            ((KOTHPlayer)gameKiller).updateKills();
        }
    }

    /**
     * Randomly respawns game players at the map's possible respawn locations with
     * their kit's gear
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // Store player that is respawning
        Player MCPlayer = event.getPlayer();
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // Check that player is respawning in this game
        if (gamePlayer.getCurrentArea().getAreaName().equals(this.areaName)) {
            // Set respawn location
            event.setRespawnLocation(this.map.randomRespawnLoc());
            // Reset MCPlayer's tags, scores, flight, potion effects, health, hunger, and inventory
            Tools.resetAllKOTH(gamePlayer.getPlayer());
            // Give player kit gear
            ((KOTHPlayer)gamePlayer).getKit().giveBasicGear();
        }
    }

    /**
     * Stops this KOTHGame's functions and returns all gamePlayers to the KOTHLobby
     */
    public void gameStop() {
        // Return all gamePlayer objects to lobby
        List<GamePlayer> playersToReturn = new ArrayList<>(areaPlayers);
        for (GamePlayer gamePlayer : playersToReturn) {
            // Send/reset all game players to KOTH Lobby
            GeneralLobbyHandler.sendKOTHLobby(gamePlayer);

            // Tell players that game ended
            gamePlayer.getPlayer().sendMessage("Stopped KOTH game");
        }

        // Remove this game from the KOTHLobby game list after some time
        new DelayedTask(this::gameKill, 20);    // Allows gamePlayers to be moved before deleting this game area
    }

    /**
     * Removes this game from KOTHLobbyHandler's list of active games and
     * unregisters this game's Event Handlers.
     */
    public void gameKill() {
        KOTHGameHandler[] activeGames = KOTHLobbyHandler.getActiveGames();
        for (int i=0; i<activeGames.length; i++)
            if (activeGames[i] == this) {
                activeGames[i] = null;
                HandlerList.unregisterAll(this);
                return;
            }
    }

    // Never used?
    @Override
    public void addPlayer(GamePlayer gamePlayer) {
    }
}

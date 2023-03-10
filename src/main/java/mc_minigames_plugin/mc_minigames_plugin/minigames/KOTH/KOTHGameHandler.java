package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Maps.Map;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Maps.MapCastleOfDreams;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

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
     * and begins other initial game functions.
     */
    public void gameStart() {
        // Update all game players' data / MCPlayers
        for (GamePlayer gamePlayer : areaPlayers)
        {
            gamePlayer.setCurrentArea(this);
            gamePlayer.setIsInGame(true);
        }
        // Give everyone their kit abilities and items

        // Initialize game timers
        // Initialize game specific blocks and objects
        // Scoreboard displays

        // Stop game in 5 seconds
        new DelayedTask(this::gameStop, 100);
    }

    // Running Game
    /**
     * Updates KOTHPlayer's kills/kill rewards when they kill another player.
     */
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        // Store player that died
        Player MCPlayer = event.getPlayer();
        GamePlayer gamePlayer = findPlayer(MCPlayer);

        // Check that player died in this game
        if (!gamePlayer.getCurrentArea().getAreaName().equals(this.areaName))
            return;

        // Store killer
        Player MCKiller = event.getPlayer().getKiller();
        GamePlayer gameKiller = findPlayer(MCKiller);

        // Check that the killer exists in this KOTHGame
        if(MCKiller != null && gameKiller.getCurrentArea().getAreaName().equals(this.areaName)) {
            // Update KOTHPlayer kills
            ((KOTHPlayer)gameKiller).updateKills();
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

        // Remove this game from the KOTHLobby game list
    }

    // Never used?
    @Override
    public void addPlayer(GamePlayer gamePlayer) {
    }
}

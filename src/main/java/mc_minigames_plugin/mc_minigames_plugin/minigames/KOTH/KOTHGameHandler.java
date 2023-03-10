package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class KOTHGameHandler extends PlayerArea implements Listener {

    // KOTH game settings
    String gameMode;          // The currently selected KOTH gamemode
    Location map;             // The currently selected KOTH map

    public KOTHGameHandler (MC_Minigames_Plugin plugin, ArrayList<GamePlayer> gamePlayers, String gameMode, Location map) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.areaPlayers = gamePlayers;
        this.gameMode = gameMode;
        this.map = map;
        areaName = "KOTHGame";     // Eventually use map objects and change this to map name
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
     * Stops this KOTHGame's functions and returns all gamePlayers to the KOTHLobby
     */
    public void gameStop() {
        // Return all gamePlayer objects to lobby
        List<GamePlayer> playersToReturn = new ArrayList<>(areaPlayers);
        for (GamePlayer gamePlayer : playersToReturn) {
            KOTHLobbyHandler.sendPlayer(gamePlayer);
            gamePlayer.getPlayer().sendMessage("Stopped KOTH game");
        }
    }

    // Never used?
    @Override
    public void addPlayer(GamePlayer gamePlayer) {
    }
}

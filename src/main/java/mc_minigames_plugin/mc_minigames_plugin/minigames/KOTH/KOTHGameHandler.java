package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class KOTHGameHandler extends PlayerArea implements Listener {

    // KOTH game settings
    String gameMode;        // The currently selected KOTH gamemode
    String map;             // The currently selected KOTH map

    public KOTHGameHandler (MC_Minigames_Plugin plugin, ArrayList<GamePlayer> gamePlayers, String gameMode, String map) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.areaPlayers = gamePlayers;
        gameStart();
    }

    //KOTH GAME FEATURES------------------------------------------------------------------------------------------------
    //Game start
    public void gameStart () {
        //Move everyone to the KOTH map
        for (GamePlayer gamePlayer : areaPlayers)
        {
            teleportToMap(gamePlayer, map);
            gamePlayer.setIsInGame(true);

            giveKit(gamePlayer);
        }
        //Give everyone their kit abilities and items

        //Initialize game timers
        //Initialize game specific blocks and objects
        //Scoreboard displays
        //
    }

    //Running Game

    //Game reset

    //KIT METHODS
    public void giveKit (GamePlayer gamePlayer)
    {

    }

    public void teleportToMap (GamePlayer gamePlayer, String map) {

        if (map.equals("CastleOfDreams")) {

        }
    }

    @Override
    public void addPlayer(GamePlayer gamePlayer) {
        areaPlayers.add(new KOTHPlayer(gamePlayer));
    }
}

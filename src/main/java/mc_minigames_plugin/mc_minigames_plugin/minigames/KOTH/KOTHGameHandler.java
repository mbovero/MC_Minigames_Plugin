package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class KOTHGameHandler extends PlayerArea implements Listener {

    String gameMode;
    String map;


    public KOTHGameHandler (MC_Minigames_Plugin plugin, ArrayList<GamePlayer> players, String gameMode, String map) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.areaPlayers = players;
        gameStart();
    }

    //KOTH GAME FEATURES------------------------------------------------------------------------------------------------
    //Game start
    public void gameStart () {
        //Move everyone to the KOTH map
        for (GamePlayer player : areaPlayers)
        {
            teleportToMap(player, map);
            player.removeTag("notInGame");

            giveKit(player);
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
    public void giveKit (GamePlayer player)
    {

    }

    public void teleportToMap (GamePlayer player, String map) {

        if (map.equals("CastleOfDreams")) {

        }
    }


    @Override
    public void addPlayer(Player mcPlayer) {areaPlayers.add(new KOTHPlayer(mcPlayer));}
}

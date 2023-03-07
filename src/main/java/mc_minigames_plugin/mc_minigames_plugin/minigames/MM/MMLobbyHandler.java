package mc_minigames_plugin.mc_minigames_plugin.minigames.MM;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import org.bukkit.entity.Player;

public class MMLobbyHandler extends PlayerArea {

    public MMLobbyHandler(MC_Minigames_Plugin plugin, Player player) {
        areaName = "MMLobby";
    }

    @Override
    public void addPlayer(Player mcPlayer) {

    }
}

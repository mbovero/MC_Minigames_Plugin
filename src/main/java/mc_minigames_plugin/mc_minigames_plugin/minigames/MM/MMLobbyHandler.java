package mc_minigames_plugin.mc_minigames_plugin.minigames.MM;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.HubPlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;

public class MMLobbyHandler extends PlayerArea {

    public MMLobbyHandler(MC_Minigames_Plugin plugin, Player MCPlayer) {
        areaName = "MMLobby";
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMRed", " ⧫ ", "Red", null, ChatColor.RED, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMBlue", " ⧫ ", "Blue", null, ChatColor.BLUE, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMGreen", " ⧫ ", "Green", null, ChatColor.GREEN, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMYellow", " ⧫ ", "Yellow", null, ChatColor.YELLOW, false, true, NameTagVisibility.ALWAYS);
    }

    @Override
    public void addPlayer(Player MCPlayer) {
        areaPlayers.add(new MMPlayer(MCPlayer, this));
    }
}

package mc_minigames_plugin.mc_minigames_plugin.minigames.MM;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.NameTagVisibility;

import java.util.ArrayList;

import static mc_minigames_plugin.mc_minigames_plugin.util.Tools.createItem;

public class MMLobbyHandler extends PlayerArea implements Listener {

// ITEMS ---------------------------------------------------------------------------------------------------------------
    // MM lobby hot bar menu items
    static ItemStack MMQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the MM queue!");
    static ItemStack MMDequeue = createItem(new ItemStack(Material.LIME_DYE), "&aReady", "&fClick with this item to leave the MM queue");

    static ItemStack MMTeamNone = createItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "&7No Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamRed = createItem(new ItemStack(Material.RED_WOOL), "&4Red Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamBlue = createItem(new ItemStack(Material.BLUE_WOOL), "&1Blue Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamGreen = createItem(new ItemStack(Material.LIME_WOOL), "&2Green Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamYellow = createItem(new ItemStack(Material.YELLOW_WOOL), "&eYellow Team", "&fClick with this item to change MM teams!");
// ---------------------------------------------------------------------------------------------------------------------

    public MMLobbyHandler(MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        // Create new list of players for this area
        areaPlayers = new ArrayList<>();
        areaName = "MMLobby";
        // Create MM teams
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMRed", " ⧫ ", "Red", null, ChatColor.RED, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMBlue", " ⧫ ", "Blue", null, ChatColor.BLUE, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMGreen", " ⧫ ", "Green", null, ChatColor.GREEN, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMYellow", " ⧫ ", "Yellow", null, ChatColor.YELLOW, false, true, NameTagVisibility.ALWAYS);
    }

    @Override
    public void addPlayer(GamePlayer gamePlayer) {
        gamePlayer.setCurrentArea(this);
        gamePlayer.setIsInGame(false);
        gamePlayer.setIsGameReady(false);
        areaPlayers.add(new MMPlayer(gamePlayer));
    }
}

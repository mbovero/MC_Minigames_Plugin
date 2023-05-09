package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHGameHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.TroubleshootUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler.allPlayerAreas;
import static mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler.findPlayerGlobal;

/**
 * Command that give the player troubleshooting abilities:
 *  - Removes any existing restrictions on the player's actions
 *  - Right-click entities to get their name and location (printed to player and console)
 *
 * @author Miles Bovero
 * @version March 6, 2023
 */
public class ListPlayerAreas implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Search allPlayerAreas (lobbies)
        for (PlayerArea area : allPlayerAreas) {
            // List collection data
            Bukkit.getLogger().info(area.getAreaName() + area.getAreaPlayers().toString());
        }

        // Search KOTH games
        if (GeneralLobbyHandler.getKOTHLobby() != null)
            for (KOTHGameHandler KOTHGame : KOTHLobbyHandler.getActiveGames()) {
                // List collection data
                if (KOTHGame != null)
                    Bukkit.getLogger().info(KOTHGame.getAreaName() + KOTHGame.getAreaPlayers().toString());
            }




        return true;
    }
}

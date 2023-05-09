package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHGameHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler.allPlayerAreas;

/**
 * Provides functionality for the /listPlayerAreas command. This command iterates through every existing
 * PlayerArea and lists the players within that area
 *
 * @author Miles Bovero
 * @version May 9, 2023
 */
public class ListPlayerAreas implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Search allPlayerAreas (lobbies)
        for (PlayerArea area : allPlayerAreas) {
            // List collection data
            Bukkit.getLogger().info(area.getAreaName() + area.getAreaPlayers().keySet());
        }

        // Search KOTH games
        if (GeneralLobbyHandler.getKOTHLobby() != null)
            for (KOTHGameHandler KOTHGame : KOTHLobbyHandler.getActiveGames()) {
                // List collection data
                if (KOTHGame != null)
                    Bukkit.getLogger().info(KOTHGame.getAreaName() + KOTHGame.getAreaPlayers().keySet());
            }




        return true;
    }
}

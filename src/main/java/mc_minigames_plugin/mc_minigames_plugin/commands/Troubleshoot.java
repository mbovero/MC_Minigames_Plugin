package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

import static mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler.findPlayer;

/**
 * Command that give the player troubleshooting abilities:
 *  - Removes any existing restrictions on the player's actions
 *  - Right-click entities to get their name and location (printed to player and console)
 *
 * @author Miles Bovero
 * @version March 6, 2023
 */
public class Troubleshoot implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only executable by players
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        // Setup
        Player MCPlayer = (Player)sender;
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);

        // If not troubleshooting...
        if (!gamePlayer.isTroubleShooting()) {
            // Enter troubleshooting
            gamePlayer.setTroubleShooting(true);
            MCPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[Entered troubleshooting mode]"));
        }
        // Otherwise,
        else {
            // Stop troubleshooting
            gamePlayer.setTroubleShooting(false);
            MCPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[Exited troubleshooting mode]"));
        }

        return true;
    }
}

package mc_minigames_plugin.mc_minigames_plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

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
        Set<String> tags = MCPlayer.getScoreboardTags();

        // If not troubleshooting...
        if (!tags.contains("troubleshooting")) {
            // Enter troubleshooting
            MCPlayer.addScoreboardTag("troubleshooting");
            MCPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[Entered troubleshooting mode]"));
        }
        // Otherwise,
        else {
            // Stop troubleshooting
            MCPlayer.removeScoreboardTag("troubleshooting");
            MCPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[Exited troubleshooting mode]"));
        }

        return true;
    }
}

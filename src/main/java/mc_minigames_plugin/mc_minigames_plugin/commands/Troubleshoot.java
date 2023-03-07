package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.util.Locations;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Troubleshoot implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only executable by players
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        // Setup
        Player player = (Player)sender;
        Set<String> tags = player.getScoreboardTags();

        // If not troubleshooting...
        if (!tags.contains("troubleshooting")) {
            // Enter troubleshooting
            player.addScoreboardTag("troubleshooting");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aEntered troubleshooting mode"));
        }
        // Otherwise,
        else {
            // Stop troubleshooting
            player.removeScoreboardTag("troubleshooting");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cExited troubleshooting mode"));
        }


        return true;
    }
}

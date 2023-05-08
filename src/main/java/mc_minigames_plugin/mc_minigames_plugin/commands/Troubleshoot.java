package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.util.TroubleshootUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    public TroubleshootUtil troubleshootUtil;

    public Troubleshoot (TroubleshootUtil troubleshootUtil) {
        this.troubleshootUtil = troubleshootUtil;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only executable by players
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        // Setup
        Player MCPlayer = null;
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = null;

        if (args.length == 0) {
            // MCPlayer is the sender
            MCPlayer = (Player) sender;
            // Find the gamePlayer matching with the sender's MCPlayer
            gamePlayer = findPlayer(MCPlayer);
        }
        else if (args.length == 1) {
            // MCPlayer is specified player
            MCPlayer = Bukkit.getPlayerExact(args[0]);
            // If player exists...
            if (MCPlayer != null)
            // Find the gamePlayer matching with the event's MCPlayer
                gamePlayer = findPlayer(MCPlayer);
            // Otherwise, send warning
            else {
                sender.sendMessage(ChatColor.RED + "Player " + args[0] + " could not be found");
                return true;
            }
        }
        // Otherwise, invalid input
        else {
            sender.sendMessage(ChatColor.RED + "Provide a valid input:\n/troubleshoot [username]");
            return true;
        }


        // If not troubleshooting...
        if (!gamePlayer.isTroubleshooting()) {
            // Add player to list of troubleshooters
            troubleshootUtil.add(MCPlayer);
            // Enter troubleshooting
            gamePlayer.setTroubleshooting(true);
            MCPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[Entered troubleshooting mode]"));
        }
        // Otherwise,
        else {
            // Add player to list of troubleshooters
            troubleshootUtil.remove(MCPlayer);
            // Stop troubleshooting
            gamePlayer.setTroubleshooting(false);
            MCPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[Exited troubleshooting mode]"));
        }

        return true;
    }
}

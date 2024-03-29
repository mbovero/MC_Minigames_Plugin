package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.handlers.MainHubHandler;
import mc_minigames_plugin.mc_minigames_plugin.util.Locations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command that teleports the player to the main hub
 *
 * @author Miles Bovero
 * @version March 6, 2023
 */
public class Hub implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only executable by players
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        Player MCPlayer = (Player)sender;

        // Teleport player to hub
        MCPlayer.teleport(MainHubHandler.getLocation());

        return true;
    }
}

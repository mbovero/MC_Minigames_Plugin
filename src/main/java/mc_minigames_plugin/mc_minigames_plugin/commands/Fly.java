package mc_minigames_plugin.mc_minigames_plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command that enables/disables the player's ability to fly
 *
 * @author Miles Bovero
 * @version March 6, 2023
 */
public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure that the console cannot run the command (only players)
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command!");
            return true;
        }

        Player MCPlayer = (Player) sender;

        // If the player can already fly, disable it
        if (MCPlayer.getAllowFlight()) {
            MCPlayer.setAllowFlight(false);
            MCPlayer.sendMessage("Flying disabled");
        }
        // Otherwise, allow flight
        else {
            MCPlayer.setAllowFlight(true);
            MCPlayer.sendMessage("Flying enabled");
        }

        // Command was run successfully (false returns error message)
        return true;
    }
}

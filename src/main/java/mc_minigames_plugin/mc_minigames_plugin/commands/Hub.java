package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.util.Locations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Hub implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only executable by players
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        Player player = (Player)sender;

        // Teleport player to hub
        player.teleport(Locations.mainHub);

        return true;
    }
}

package mc_minigames_plugin.mc_minigames_plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        }
        Player player = (Player)sender;

        // Make hub location        world name, x, y, z, y, p
        Location hub = new Location(Bukkit.getWorld("world"), -15.5, -43, -17.5, -45, 15);
        // Teleport player to hub
        player.teleport(hub);

        return true;
    }
}

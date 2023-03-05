package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.util.SpawnUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {
    public SpawnUtil spawnUtil;

    public Spawn(SpawnUtil spawnUtil) {
        this.spawnUtil = spawnUtil;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command.");
            return true;
        }

        Player player = (Player) sender;
        spawnUtil.teleport(player);

        return true;
    }
}

package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reset implements CommandExecutor {

    // The list of possible data resets
    String list = "all, team, tags, scores, flight, potionEffects, health, hunger, inventory";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        // If only "/reset" is typed...
        if (args.length == 0) {
            // Warn non-player executors
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("Please specify a type and player to reset:\n/reset <type> <username>" +
                        "\nType '/reset list' to see a list of types");
                return true;
            }
            // Reset all player data
            Player MCPlayer = (Player)commandSender;
            Tools.resetAll(MCPlayer);
            commandSender.sendMessage("Reset all of " + MCPlayer.getName() + "'s data");
            return true;
        }

        // Initiate target
        Player MCPlayer = null;

        // If only a type is provided
        if (args.length == 1) {
            // Warn non-player executors
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("Specify which player to reset:\n/reset <type> <username>");
                return true;
            }
            // Set target as the command sender
            MCPlayer = (Player) commandSender;
        }

        // If there are more than 1 args...  (previous statements did not run)
        if (MCPlayer == null)
            // Set target to specified player
            MCPlayer = Bukkit.getPlayerExact(args[1]);

        // Send warning if specified player was not found
        if (MCPlayer == null) {
            commandSender.sendMessage("Player " + args[1] + " could not be found");
            return true;
        }

        // Store specified type to reset
        String type = args[0];
        // Reset
        switch (type) {
            case "list":
                commandSender.sendMessage(list);
                break;
            case "all":
                Tools.resetAll(MCPlayer);
                commandSender.sendMessage("Reset all of " + MCPlayer.getName() + "'s data");
                break;
            case "team":
                Tools.resetTeam(MCPlayer);
                commandSender.sendMessage("Removed " + MCPlayer.getName() + " from their team");
                break;
            case "tags":
                Tools.resetTags(MCPlayer);
                commandSender.sendMessage("Removed all of " + MCPlayer.getName() + "'s tags");
                break;
            case "scores":
                Tools.resetScores(MCPlayer);
                commandSender.sendMessage("Reset all of " + MCPlayer.getName() + "'s scoreboard data");
                break;
            case "flight":
                Tools.resetFlight(MCPlayer);
                commandSender.sendMessage("Reset " + MCPlayer.getName() + "'s flight capability");
                break;
            case "potionEffects":
                Tools.resetPotionEffects(MCPlayer);
                commandSender.sendMessage("Removed all of " + MCPlayer.getName() + "'s potion effects");
                break;
            case "health":
                Tools.resetHealth(MCPlayer);
                commandSender.sendMessage("Reset " + MCPlayer.getName() + "'s health data");
                break;
            case "hunger":
                Tools.resetHunger(MCPlayer);
                commandSender.sendMessage("Reset " + MCPlayer.getName() + "'s hunger data");
                break;
            case "inventory":
                Tools.resetInventory(MCPlayer);
                commandSender.sendMessage("Cleared " + MCPlayer.getName() + "'s inventory");
                break;
            default:
                commandSender.sendMessage(ChatColor.RED + "Please enter a valid type.\nType '/reset list' to see a list of types");
        }

        return true;
    }
}

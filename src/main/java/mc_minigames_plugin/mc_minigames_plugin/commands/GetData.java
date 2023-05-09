package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc_minigames_plugin.mc_minigames_plugin.util.Tools.createItem;

public class GetData implements CommandExecutor {

    // List of data types
    String list = ChatColor.AQUA + "isGameReady, isTroubleshooting, isInGame, MCPlayer, currentArea, KOTHKit";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        // If no type or player is specified
        if (args.length == 0) {
            // Send warning
            commandSender.sendMessage(ChatColor.RED + "Specify a data type to receive. Type '/getdata list' to see a list of data types");
            return true;
        }

        // Initiate target
        Player MCPlayer = null;

        // If only a type is specified...
        if (args.length == 1) {
            // Send warning if the sender is not a player
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "Specify which player to get data from:\n/getdata <type> <username>");
                return true;
            }
            // Otherwise, set target as the command sender
            MCPlayer = (Player)commandSender;
        }

        // If there are more than 1 args...  (previous statements did not run)
        if (MCPlayer == null)
            // Set target to specified player
            MCPlayer = Bukkit.getPlayerExact(args[1]);

        // Send warning if specified player was not found
        if (MCPlayer == null) {
            commandSender.sendMessage(ChatColor.RED + "Player " + args[1] + " could not be found");
            return true;
        }

        // Store specified data type to retrieve
        String data = args[0];
        // Locate gamePlayer
        GamePlayer gamePlayer = GeneralLobbyHandler.findPlayerGlobal(MCPlayer);
        // Message sender an error if gamePlayer could not be found
        if (gamePlayer == null) {
            commandSender.sendMessage(ChatColor.RED + "Could not find a gamePlayer with the same name as " + MCPlayer.getName());
            return true;
        }
        switch (data) {
            case "list":
                commandSender.sendMessage(list);
                break;
            case "isGameReady":
                commandSender.sendMessage(ChatColor.GREEN + MCPlayer.getName() + "'s isGameReady value is " + ChatColor.GRAY + (gamePlayer.isGameReady()));
                break;
            case "isTroubleshooting":
                commandSender.sendMessage(ChatColor.GREEN + MCPlayer.getName() + "'s isTroubleshooting value is " + ChatColor.GRAY + (gamePlayer.isTroubleshooting()));
                break;
            case "isInGame":
                commandSender.sendMessage(ChatColor.GREEN + MCPlayer.getName() + "'s isInGame value is " + ChatColor.GRAY + (gamePlayer.isInGame()));
                break;
            case "MCPlayer":
                commandSender.sendMessage(ChatColor.GREEN + MCPlayer.getName() + "'s MCPlayer's name is " + ChatColor.GRAY + (gamePlayer.getPlayer().getName()));
                break;
            case "currentArea":
                commandSender.sendMessage(ChatColor.GREEN + MCPlayer.getName() + "'s currentArea name is " + ChatColor.GRAY + (gamePlayer.getCurrentArea().getAreaName()));
                break;
            case "KOTHKit":
                if(gamePlayer.getCurrentArea().getAreaName().equals("KOTHLobby") || gamePlayer.getCurrentArea().getAreaName().equals("KOTHGame"))
                    commandSender.sendMessage(ChatColor.GREEN + MCPlayer.getName() + "'s KOTHKit is " + ChatColor.GRAY + (((KOTHPlayer)gamePlayer).getKit().getKitName()));
                else
                    commandSender.sendMessage(ChatColor.RED + MCPlayer.getName() + " is not currently a KOTHPlayer");
                break;
            default:
                commandSender.sendMessage(ChatColor.RED + "Specify a valid data type to receive. Type '/getdata list' to see a list of data types");
        }

        return true;
    }
}

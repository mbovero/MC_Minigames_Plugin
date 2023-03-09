package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static mc_minigames_plugin.mc_minigames_plugin.util.Tools.createItem;

public class GetItem implements CommandExecutor {

// ITEMS ---------------------------------------------------------------------------------------------------------------
    // Lobby selector tool
    static ItemStack lobbySelector = createItem(new ItemStack(Material.COMPASS), "&aLobby Selector", "&fExplore our selection of games!");

    // KOTH lobby hot bar menu items
    static ItemStack KOTHQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the KOTH queue!");
    static ItemStack KOTHTeamNone = createItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "&7No Team", "&fClick with this item to change KOTH teams!");

    // MM lobby hot bar menu items
    static ItemStack MMQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the MM queue!");
    static ItemStack MMTeamNone = createItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "&7No Team", "&fClick with this item to change MM teams!");

// ---------------------------------------------------------------------------------------------------------------------


    // List of items
    String list = "lobbySelector, KOTHQueue, KOTHTeamNone, MMQueue, MMTeamNone";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        // If no item or player is specified
        if (args.length == 0) {
            // Send warning
            commandSender.sendMessage("Specify an item to receive. Type '/getitem list' to see a list of items");
            return true;
        }

        // Initiate target
        Player MCPlayer = null;

        // If only an item is specified...
        if (args.length == 1) {
            // Send warning if the sender is not a player
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage("Specify which player to give the item to:\n/getitem <item> <username>");
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
            commandSender.sendMessage("Player " + args[1] + " could not be found");
            return true;
        }

        // Store specified type to reset
        String item = args[0];
        // Get target inventory
        Inventory inv = MCPlayer.getInventory();
        // Give item
        switch (item) {
            case "list":
                commandSender.sendMessage(list);
                break;
            case "lobbySelector":
                inv.addItem(lobbySelector);
                break;
            case "KOTHQueue":
                inv.addItem(KOTHQueue);
                break;
            case "KOTHTeamNone":
                inv.addItem(KOTHTeamNone);
                break;
            case "MMQueue":
                inv.addItem(MMQueue);
                break;
            case "MMTeamNone":
                inv.addItem(MMTeamNone);
                break;
            default:
                commandSender.sendMessage(ChatColor.RED + "Specify a valid item to receive. Type '/getitem list' to see a list of items");
        }

        return true;
    }
}

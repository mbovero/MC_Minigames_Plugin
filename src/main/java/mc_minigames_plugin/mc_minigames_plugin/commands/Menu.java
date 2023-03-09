package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Menu implements Listener, CommandExecutor {

    private String invName = "Server Selector";

    public Menu(MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Only handle inv clicks if player is in /menu inventory
        if (!event.getView().getTitle().equals(invName)) {
            return;
        }

        // Retrieve the slot number that the player clicked on
        Player MCPlayer = (Player)event.getWhoClicked();
        int slot = event.getSlot();

        // Execute correlating operation
        if (slot == 11)
            MCPlayer.sendMessage("You clicked option 1");
        else if (slot == 13)
            MCPlayer.sendMessage("You clicked option 2");
        else if (slot == 15)
            MCPlayer.sendMessage("You clicked option 3");

        // Cannot move items around, only click on them
        event.setCancelled(true);
    }

    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command.");
            return true;
        }

        Player MCPlayer = (Player)sender;

        Inventory inv = Bukkit.createInventory(MCPlayer, 9 *3, invName);

        inv.setItem(11, createItem(new ItemStack(Material.DIAMOND_SWORD), "&9PVP", "&aClick to Join", "&aBattle it out in our PVP arena!"));
        inv.setItem(13, createItem(new ItemStack(Material.BEACON), "&9Creative Plots", "&aClick to Join", "&aWeekly build competitions!"));
        inv.setItem(15, createItem(new ItemStack(Material.GRASS_BLOCK), "&9Sky Block", "&aClick to Join", "&aHow long can you survive?"));

        MCPlayer.openInventory(inv);

        return true;
    }

    private ItemStack createItem(ItemStack item, String itemName, String ... lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));

        List<String> lores = new ArrayList<>();
        for (String s : lore) {
            lores.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
    }

}

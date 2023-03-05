package mc_minigames_plugin.mc_minigames_plugin.util;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * A class full of useful tools/methods to simplify workflow.
 *
 * Useful stuff:
 * - §
 */
public class Tools {

    /**
     * Method that creates an item with a specified name and lore.
     *
     * @param item the item to create
     * @param itemName the item's display name
     * @param lore the item's description
     * @return your custom made item
     */
    public static ItemStack createItem(ItemStack item, String itemName, String ... lore) {
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

package mc_minigames_plugin.mc_minigames_plugin.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static void resetTags(Player player) {
        // Get copy of player's current tags
        Set<String> tagsToRemove = new HashSet<>(player.getScoreboardTags());
        // Remove all tags
        for (String tag : tagsToRemove)
            if (!tag.equals("GameDev") && !tag.equals("testing"))      // TEMP... only used for data pack capabilities
                player.removeScoreboardTag(tag);
    }

}

package mc_minigames_plugin.mc_minigames_plugin.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

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

        if (lore != null) {
            List<String> lores = new ArrayList<>();
            for (String s : lore)
                lores.add(ChatColor.translateAlternateColorCodes('&', s));
            // Check for empty lore
            if (!lores.contains(""))
                meta.setLore(lores);
        }
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Method that creates an item with a specified name and lore.
     *
     * @param item the item to create
     * @param itemName the item's display name
     * @param lore the item's description
     * @return your custom made item
     */
    public static ItemStack createItem(ItemStack item, String itemName, boolean isUnbreakable, String ... lore) {
        // Create basic item using existing method
        ItemStack specialItem = createItem(item, itemName, lore);
        // Retrieve new item meta
        ItemMeta meta = specialItem.getItemMeta();
        // Set unbreakability
        meta.setUnbreakable(isUnbreakable);
        // Update item meta
        specialItem.setItemMeta(meta);

        return specialItem;
    }

    /**
     * Method that creates an item with a specified name and lore.
     *
     * @param item the item to create
     * @param itemName the item's display name
     * @param lore the item's description
     * @return your custom made item
     */
    public static ItemStack createItem(ItemStack item, String itemName, short durability, String ... lore) {
        // Create basic item using existing method
        ItemStack specialItem = createItem(item, itemName, lore);
        // Set durability
        specialItem.setDurability(durability);

        return specialItem;
    }

    public static void addPotionItemEffect(ItemStack potionItem, PotionEffectType potionEffectType, int duration, int amplifier, boolean overwrite) {
        PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier), overwrite);
        potionItem.setItemMeta(potionMeta);
    }

    /**
     * Method that assists in setting the potion color of a potion item.
     * Tip: use Color.fromRGB(R, G, B) for custom colors
     *
     * @param potionItem the potion item to be changed
     * @param color the color to set the potion item to
     */
    public static void setPotionItemColor(ItemStack potionItem, Color color) {
        PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();
        potionMeta.setColor(color);
        potionItem.setItemMeta(potionMeta);
    }

    /**
     * Method to facilitate the creation of new teams.
     * Inputting "null" for certain parameters will result in default settings.
     *
     * @param scoreboard the scoreboard to add the new team to
     * @param teamName the team name used by java (don't use color codes)
     * @param prefix the text to be displayed before team members' names (color code capability)
     * @param displayName the name of the team to be displayed in game (color code capability)
     * @param suffix the text ot be displayed after team members' names (color code capability)
     * @param teamColor the color of the team
     * @param friendlyFire whether players on this team can attack each other or not
     * @param seeInvisibleFriends whether players on this team can see invisible teammates or not
     * @param nameTagVisibility the visibility of players' name tags (ALWAYS, HIDE FOR OTHER TEAMS, HIDE FOR OWN TEAM, NEVER)
     */
    public static void newTeam(Scoreboard scoreboard, String teamName, String prefix, String displayName, String suffix, ChatColor teamColor, boolean friendlyFire, boolean seeInvisibleFriends, NameTagVisibility nameTagVisibility) {
        if (scoreboard.getTeam(teamName) != null) {
            Bukkit.getLogger().info(teamName + " was not created because it already exists.");
            return;
        }
        scoreboard.registerNewTeam(teamName);
        Team team = scoreboard.getTeam(teamName);
        team.setAllowFriendlyFire(friendlyFire);
        team.setCanSeeFriendlyInvisibles(seeInvisibleFriends);
        if (teamColor != null)
            team.setColor(teamColor);
        if (displayName != null)
            team.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        if (nameTagVisibility != null)
            team.setNameTagVisibility(nameTagVisibility);
        if (prefix != null)
            team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));
        if (suffix != null)
            team.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));
    }



// Player reset methods ------------------------------------------------------------------------------------------------

    /**
     * Resets all the player's data using every possible Tools.reset() method
     */
    public static void resetAll(Player MCPlayer) {
        resetTeam(MCPlayer);
        resetTags(MCPlayer);
        resetScores(MCPlayer);
        resetFlight(MCPlayer);
        resetPotionEffects(MCPlayer);
        resetHealth(MCPlayer);
        resetHunger(MCPlayer);
        resetInventory(MCPlayer);
        resetDisplayName(MCPlayer);
    }

    /**
     * Resets all the player's data according to KOTH game needs
     */
    public static void resetAllKOTH(Player MCPlayer) {
        resetTags(MCPlayer);
        resetScores(MCPlayer);
        resetFlight(MCPlayer);
        resetPotionEffects(MCPlayer);
        resetHealth(MCPlayer);
        resetHunger(MCPlayer);
        resetInventory(MCPlayer);
    }

    /**
     * Removes the player from whatever team they were on
     */
    public static void resetTeam(Player MCPlayer) {
        // Get team the player is on
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(MCPlayer);
        // If the player was on a team
        if (team != null)
            // Remove the player from that team
            Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(MCPlayer).removePlayer(MCPlayer);
    }

    /**
     * Removes all player tags
     */
    public static void resetTags(Player MCPlayer) {
        // Get copy of player's current tags
        Set<String> tagsToRemove = new HashSet<>(MCPlayer.getScoreboardTags());
        // Remove all tags
        for (String tag : tagsToRemove)
            if (!tag.equals("GameDev"))      // game dev is TEMP... only used for data pack capabilities
                MCPlayer.removeScoreboardTag(tag);
    }

    /**
     * Resets all the player's objective scores
     */
    public static void resetScores(Player MCPlayer) {
        // Resets all objective scores for player - get individual objectives and player score object to change individual scores
        Bukkit.getScoreboardManager().getMainScoreboard().resetScores(MCPlayer);
    }

    /**
     * Disables the player's flight and their ability to fly
     */
    public static void resetFlight(Player MCPlayer) {
        // Disable ability to fly
        MCPlayer.setAllowFlight(false);
        // Disable flight
        MCPlayer.setFlying(false);
    }

    /**
     * Clears the player's current potion effects
     */
    public static void resetPotionEffects(Player MCPlayer) {
        // Retrieve player's current effects
        Collection<PotionEffect> effectsToClear = MCPlayer.getActivePotionEffects();
        // Remove each effect
        for (PotionEffect pE : effectsToClear)
            MCPlayer.removePotionEffect(pE.getType());
    }

    /**
     * Resets the player's max health and sets their health to be full
     */
    public static void resetHealth(Player MCPlayer) {
        // Reset maximum health to default
        MCPlayer.resetMaxHealth();
        // Set health to max health
        MCPlayer.setHealth(MCPlayer.getMaxHealth());
    }

    /**
     * Resets the player's hunger bar and saturation
     */
    public static void resetHunger(Player MCPlayer) {
        // Fill up hunger bar
        MCPlayer.setFoodLevel(20);
        // Set saturation to default
        MCPlayer.setSaturation(5);
    }

    /**
     * Clears the player's inventory
     */
    public static void resetInventory(Player MCPlayer) {
        Inventory inv = MCPlayer.getInventory();
        inv.clear();
    }

    /**
     * Resets the player's display name to their actual username
     */
    public static void resetDisplayName(Player MCPlayer) {
        MCPlayer.setDisplayName(MCPlayer.getName());
    }

    /**
     * Resets the player's player list footer and header
     */
    public static void resetPlayerList(Player MCPlayer) {
        MCPlayer.setPlayerListHeader(null);
        MCPlayer.setPlayerListFooter(null);
    }
}

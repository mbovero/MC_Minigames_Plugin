package mc_minigames_plugin.mc_minigames_plugin.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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

    /**
     * Removes the player from whatever team they were on
     */
    public static void resetTeam(Player player) {
        // Get team the player is on
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(player);
        // If the player was on a team
        if (team != null)
            // Remove the player from that team
            Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(player).removePlayer(player);
    }

    /**
     * Removes all player tags (except tags for troubleshooting)
     */
    public static void resetTags(Player player) {
        // Get copy of player's current tags
        Set<String> tagsToRemove = new HashSet<>(player.getScoreboardTags());
        // Remove all tags
        for (String tag : tagsToRemove)
            if (!tag.equals("GameDev") && !tag.equals("testing"))      // game dev is TEMP... only used for data pack capabilities
                player.removeScoreboardTag(tag);
    }

}

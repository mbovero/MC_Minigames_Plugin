package mc_minigames_plugin.mc_minigames_plugin.handlers;

import com.sun.tools.javac.jvm.Items;
import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHPlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import mc_minigames_plugin.mc_minigames_plugin.util.Locations;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.NameTagVisibility;
import org.checkerframework.checker.units.qual.K;

import java.util.ArrayList;
import java.util.Set;

import static mc_minigames_plugin.mc_minigames_plugin.util.Tools.createItem;

/**
 * Class Description: Class handles the states of game
 * lobbies and their relationships to players
 *
 * @author Kirt Robinson
 * @version March 5, 2023
 */
public class GameLobbyHandler implements Listener {

    static ItemStack lobbySelector = createItem(new ItemStack(Material.COMPASS), "&aLobby Selector", "&fExplore our selection of games!");

    protected static ArrayList<PlayerArea> playerAreas;
    protected static MC_Minigames_Plugin plugin;

    // KOTH lobby hot bar menu items
    static ItemStack KOTHQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the KOTH queue!");
    static ItemStack KOTHDequeue = createItem(new ItemStack(Material.LIME_DYE), "&aReady", "&fClick with this item to leave the KOTH queue");

    static ItemStack KOTHTeamNone = createItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "&7No Team", "&fClick with this item to change KOTH teams!");
    static ItemStack KOTHTeamRed = createItem(new ItemStack(Material.RED_WOOL), "&4Red Team", "&fClick with this item to change KOTH teams!");
    static ItemStack KOTHTeamBlue = createItem(new ItemStack(Material.BLUE_WOOL), "&1Blue Team", "&fClick with this item to change KOTH teams!");
    static ItemStack KOTHTeamGreen = createItem(new ItemStack(Material.LIME_WOOL), "&2Green Team", "&fClick with this item to change KOTH teams!");
    static ItemStack KOTHTeamYellow = createItem(new ItemStack(Material.YELLOW_WOOL), "&eYellow Team", "&fClick with this item to change KOTH teams!");



    // MM lobby hot bar menu items
    static ItemStack MMQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the MM queue!");
    static ItemStack MMDequeue = createItem(new ItemStack(Material.LIME_DYE), "&aReady", "&fClick with this item to leave the MM queue");

    static ItemStack MMTeamNone = createItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "&7No Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamRed = createItem(new ItemStack(Material.RED_WOOL), "&4Red Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamBlue = createItem(new ItemStack(Material.BLUE_WOOL), "&1Blue Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamGreen = createItem(new ItemStack(Material.LIME_WOOL), "&2Green Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamYellow = createItem(new ItemStack(Material.YELLOW_WOOL), "&eYellow Team", "&fClick with this item to change MM teams!");

    public GameLobbyHandler(MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        // Create MM teams
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMRed", " ⧫ ", "Red", null, ChatColor.RED,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMBlue", " ⧫ ", "Blue", null, ChatColor.BLUE,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMGreen", " ⧫ ", "Green", null, ChatColor.GREEN,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMYellow", " ⧫ ", "Yellow", null, ChatColor.YELLOW,false, true, NameTagVisibility.ALWAYS);

    }

    @EventHandler
    public void onWorldInitEvent(WorldInitEvent event) {

    }


    /*
     * Lobby/hub tags for keeping track of player location/status:
     *
     *  - "testing"  - does not perform usual resets to player
     *  - "mainHub"
     *  - "KOTHLobby"
     *  - "KOTHQueued"
     *  - "MMLobby"
     *  - "MMQueued"
     */

    /**
     * Sends the provided player to the main hub and sets their tags accordingly.
     *
     * @param player player to be sent
     */
    public static void sendMainHub(Player player) {
        // Tp player
        player.teleport(Locations.mainHub);
        // Reset tags
        Set<String> tags = player.getScoreboardTags();
        Tools.resetTags(player);
        // Set tags
        player.addScoreboardTag("mainHub");         // Player is now in main hub
        player.addScoreboardTag("notInGame");
        // Reset team
        Tools.resetTeam(player);
        // Reset/set inventory
        Inventory inv = player.getInventory();
        if (!player.getScoreboardTags().contains("testing"))    // Only clear inventory if not troubleshooting
            inv.clear();
        new DelayedTask(() -> {inv.setItem(4, lobbySelector);}, 10);
    }

    /**
     * Sends the provided player to the King of The Hill lobby and sets their tags accordingly.
     *
     * @param player player to be sent
     */
    public static void sendKOTHLobby(Player player) {
        // Tp player
        player.teleport(Locations.KOTHLobby);
        //Check if the playerAreas array is null
        if (!(playerAreas == null)) {
            //counter to check if a KOTHLobbyHandler object exists
            boolean kothExist = false;
            //add to counter and add the player when coming to an instance of the KOTHLobbyHandler
            for (PlayerArea area : playerAreas) {
                if ((area instanceof KOTHLobbyHandler)) {
                    area.addPlayer(player);;
                    kothExist = true;
                }
            //Create new KOTHLobbyHandler if none already exist
                if (!kothExist) {playerAreas.add(new KOTHLobbyHandler(plugin, player));}
            }
        }
        // Reset tags
        Set<String> tags = player.getScoreboardTags();
        Tools.resetTags(player);
        // Set tags
        player.addScoreboardTag("KOTHLobby");         // Player is now in KOTH lobby
        player.addScoreboardTag("notInGame");
        // Reset team
        Tools.resetTeam(player);
        // Reset/set inventory
        Inventory inv = player.getInventory();
        if (!player.getScoreboardTags().contains("testing"))    // Only clear inventory if not troubleshooting
            inv.clear();
        new DelayedTask(() -> {inv.setItem(0, KOTHQueue);}, 10);
        new DelayedTask(() -> {inv.setItem(2, KOTHTeamNone);}, 10);
        new DelayedTask(() -> {inv.setItem(4, lobbySelector);}, 10);
    }

    /**
     * Sends the provided player to the Murder Mystery lobby and sets their tags accordingly.
     *
     * @param player player to be sent
     */
    public static void sendMMLobby(Player player) {
        // Tp player
        player.teleport(Locations.MMLobby);
        // Reset tags
        Set<String> tags = player.getScoreboardTags();
        Tools.resetTags(player);
        // Set tags
        player.addScoreboardTag("MMLobby");         // Player is now in MM lobby
        player.addScoreboardTag("notInGame");
        // Reset team
        Tools.resetTeam(player);
        // Reset/set inventory
        Inventory inv = player.getInventory();
        if (!player.getScoreboardTags().contains("testing"))    // Only clear inventory if not troubleshooting
            inv.clear();
        new DelayedTask(() -> {inv.setItem(0, MMQueue);}, 10);
        new DelayedTask(() -> {inv.setItem(2, MMTeamNone);}, 10);
        new DelayedTask(() -> {inv.setItem(4, lobbySelector);}, 10);
    }

    /**
     * Handles lobby hot bar menu items
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // Setup
        Player player = event.getPlayer();
        Inventory inv = player.getInventory();
        Set<String> tags = player.getScoreboardTags();

        // For all players in the KOTH Lobby...
        if (tags.contains("KOTHLobby")) {
            // Detect when player clicks
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
                // Detect click with an item
                if (player.getItemInHand().getItemMeta() != null) {
                // QUEUE ITEM
                    // Detect click with KOTH queue item
                    if (player.getItemInHand().getItemMeta().getDisplayName().equals(KOTHQueue.getItemMeta().getDisplayName())) {
                        // Queue player
                        player.addScoreboardTag("KOTHQueued");
                        // Switch to dequeue item
                        inv.setItem(0, KOTHDequeue);
                        // Play sound
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 5, 1.5f);
                    }
                    // Detect click with KOTH dequeue item
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(KOTHDequeue.getItemMeta().getDisplayName())) {
                        // Dequeue player
                        player.removeScoreboardTag("KOTHQueued");
                        // Switch to queue item
                        inv.setItem(0, KOTHQueue);
                        // Play sound
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_COW_BELL, 5, .8f);
                    }

                // TEAM SELECTOR
                    // Detect click with KOTH team selector (none)
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(KOTHTeamNone.getItemMeta().getDisplayName())) {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("KOTHRed").addPlayer(player);
                        // Switch to next item
                        inv.setItem(2, KOTHTeamRed);
                        // Play sound
                        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }
                    // Detect click with KOTH team selector (red)
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(KOTHTeamRed.getItemMeta().getDisplayName())) {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("KOTHBlue").addPlayer(player);
                        // Switch to next item
                        inv.setItem(2, KOTHTeamBlue);
                        // Play sound
                        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }
                    // Detect click with KOTH team selector (blue)
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(KOTHTeamBlue.getItemMeta().getDisplayName())) {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("KOTHGreen").addPlayer(player);
                        // Switch to next item
                        inv.setItem(2, KOTHTeamGreen);
                        // Play sound
                        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }
                    // Detect click with KOTH team selector (green)
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(KOTHTeamGreen.getItemMeta().getDisplayName())) {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("KOTHYellow").addPlayer(player);
                        // Switch to next item
                        inv.setItem(2, KOTHTeamYellow);
                        // Play sound
                        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }
                    // Detect click with KOTH team selector (yellow)
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(KOTHTeamYellow.getItemMeta().getDisplayName())) {
                        // Put player on red team
                        Tools.resetTeam(player);
                        // Switch to next item
                        inv.setItem(2, KOTHTeamNone);
                        // Play sound
                        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }


                }
        }

        // For all players in the MM Lobby...
        if (tags.contains("MMLobby")) {
            // Detect when player clicks
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
                // Detect click with an item
                if (player.getItemInHand().getItemMeta() != null) {
                // QUEUE ITEM
                    // Detect click with KOTH queue item
                    if (player.getItemInHand().getItemMeta().getDisplayName().equals(MMQueue.getItemMeta().getDisplayName())) {
                        // Queue player
                        player.addScoreboardTag("MMQueued");
                        // Switch to dequeue item
                        inv.setItem(0, MMDequeue);
                        // Play sound
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 5, 1.5f);
                    }
                    // Detect click with KOTH dequeue item
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(MMDequeue.getItemMeta().getDisplayName())) {
                        // Dequeue player
                        player.removeScoreboardTag("MMQueued");
                        // Switch to queue item
                        inv.setItem(0, MMQueue);
                        // Play sound
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_COW_BELL, 5, .8f);
                    }

                    // TEAM SELECTOR
                    // Detect click with MM team selector (none)
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(MMTeamNone.getItemMeta().getDisplayName())) {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("MMRed").addPlayer(player);
                        // Switch to next item
                        inv.setItem(2, MMTeamRed);
                        // Play sound
                        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }
                    // Detect click with MM team selector (red)
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(MMTeamRed.getItemMeta().getDisplayName())) {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("MMBlue").addPlayer(player);
                        // Switch to next item
                        inv.setItem(2, MMTeamBlue);
                        // Play sound
                        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }
                    // Detect click with MM team selector (blue)
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(MMTeamBlue.getItemMeta().getDisplayName())) {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("MMGreen").addPlayer(player);
                        // Switch to next item
                        inv.setItem(2, MMTeamGreen);
                        // Play sound
                        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }
                    // Detect click with MM team selector (green)
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(MMTeamGreen.getItemMeta().getDisplayName())) {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("MMYellow").addPlayer(player);
                        // Switch to next item
                        inv.setItem(2, MMTeamYellow);
                        // Play sound
                        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }
                    // Detect click with MM team selector (yellow)
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(MMTeamYellow.getItemMeta().getDisplayName())) {
                        // Put player on red team
                        Tools.resetTeam(player);
                        // Switch to next item
                        inv.setItem(2, MMTeamNone);
                        // Play sound
                        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }

                }
        }

    }




}

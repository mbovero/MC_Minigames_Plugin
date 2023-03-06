package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.Game;
import mc_minigames_plugin.mc_minigames_plugin.util.Locations;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

    ArrayList<Game> gameList;
    static ItemStack KOTHQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the KOTH queue!");
    static ItemStack KOTHDequeue = createItem(new ItemStack(Material.LIME_DYE), "&aReady", "&fClick with this item to leave the KOTH queue");

    public GameLobbyHandler(MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWorldInitEvent(WorldInitEvent event) {

    }


    /*
     * Lobby/hub tags for keeping track of player location:
     *
     *  - "testing"  - does not perform usual resets to player
     *  - "mainHub"
     *  - "KOTHLobby"
     *  - "MMLobby"
     */

    /**
     * Sends the provided player to the main hub and sets their tags accordingly.
     *
     * @param player player to be sent
     */
    public static void sendMainHub(Player player) {
        // Tp player
        player.teleport(Locations.mainHub);
        // Reset/set tags
        Tools.resetTags(player);
        player.addScoreboardTag("mainHub");
        player.addScoreboardTag("notInGame");
        // Reset/set inventory
        Inventory inv = player.getInventory();
        if (!player.getScoreboardTags().contains("testing"))    // Only clear inventory if not troubleshooting
            inv.clear();
        inv.setItem(4, createItem(new ItemStack(Material.COMPASS), "&aLobby Selector", "&fExplore our selection of games!"));
    }

    /**
     * Sends the provided player to the King of The Hill lobby and sets their tags accordingly.
     *
     * @param player player to be sent
     */
    public static void sendKOTHLobby(Player player) {
        // Tp player
        player.teleport(Locations.KOTHLobby);
        // Reset/set tags
        Tools.resetTags(player);
        player.addScoreboardTag("KOTHLobby");
        player.addScoreboardTag("notInGame");
        // Reset/set inventory
        Inventory inv = player.getInventory();
        if (!player.getScoreboardTags().contains("testing"))    // Only clear inventory if not troubleshooting
            inv.clear();
        inv.setItem(0, KOTHQueue);
        inv.setItem(4, createItem(new ItemStack(Material.COMPASS), "&aLobby Selector", "&fExplore our selection of games!"));
    }

    /**
     * Sends the provided player to the Murder Mystery lobby and sets their tags accordingly.
     *
     * @param player player to be sent
     */
    public static void sendMMLobby(Player player) {
        // Tp player
        player.teleport(Locations.MMLobby);
        // Reset/set tags
        Tools.resetTags(player);
        player.addScoreboardTag("MMLobby");
        player.addScoreboardTag("notInGame");
        // Reset/set inventory
        Inventory inv = player.getInventory();
        if (!player.getScoreboardTags().contains("testing"))    // Only clear inventory if not troubleshooting
            inv.clear();
        inv.setItem(4, createItem(new ItemStack(Material.COMPASS), "&aLobby Selector", "&fExplore our selection of games!"));
    }

    /**
     * Handles lobby hot bar menu items
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Inventory inv = player.getInventory();
        Set<String> tags = player.getScoreboardTags();
        if (tags.contains("KOTHLobby")) {
            // Detect when player clicks
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
                // Detect right click with an item
                if (player.getItemInHand().getItemMeta() != null) {
                    // Detect right click with KOTH queue item
                    if (player.getItemInHand().getItemMeta().getDisplayName().equals(KOTHQueue.getItemMeta().getDisplayName())) {
                        // Queue player
                        player.addScoreboardTag("KOTHQueued");
                        // Switch to dequeue item
                        inv.setItem(0, KOTHDequeue);
                        // Play sound
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 5, 1.5f);
                    }
                    // Detect right click with KOTH dequeue item
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(KOTHDequeue.getItemMeta().getDisplayName())) {
                        // Dequeue player
                        player.removeScoreboardTag("KOTHQueued");
                        // Switch to queue item
                        inv.setItem(0, KOTHQueue);
                        // Play sound
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_COW_BELL, 5, .8f);
                    }
                }
        }
    }




}

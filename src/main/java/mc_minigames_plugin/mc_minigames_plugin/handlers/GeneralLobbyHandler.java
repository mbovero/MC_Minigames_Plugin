package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHLobbyHandler;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.NameTagVisibility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static mc_minigames_plugin.mc_minigames_plugin.util.Tools.createItem;

/**
 * Class Description: Class handles the states of game
 * lobbies and their relationships to players
 *
 * @author Kirt Robinson, Miles Bovero
 * @version March 6, 2023
 */
public class GeneralLobbyHandler implements Listener {

    // Player tracking resources
    protected static ArrayList<PlayerArea> playerAreas;     // A list of areas players are in
    protected static MC_Minigames_Plugin plugin;            // Overarching plugin

    static boolean KOTHExist;      // Boolean to check if a KOTHLobbyHandler object exists


// ITEMS ---------------------------------------------------------------------------------------------------------------
    // Lobby selector tool
    static ItemStack lobbySelector = createItem(new ItemStack(Material.COMPASS), "&aLobby Selector", "&fExplore our selection of games!");

    // Initial KOTH lobby hot bar menu items
    static ItemStack KOTHQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the KOTH queue!");
    static ItemStack KOTHTeamNone = createItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "&7No Team", "&fClick with this item to change KOTH teams!");

    // MM lobby hot bar menu items
    static ItemStack MMQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the MM queue!");
    static ItemStack MMDequeue = createItem(new ItemStack(Material.LIME_DYE), "&aReady", "&fClick with this item to leave the MM queue");

    static ItemStack MMTeamNone = createItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "&7No Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamRed = createItem(new ItemStack(Material.RED_WOOL), "&4Red Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamBlue = createItem(new ItemStack(Material.BLUE_WOOL), "&1Blue Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamGreen = createItem(new ItemStack(Material.LIME_WOOL), "&2Green Team", "&fClick with this item to change MM teams!");
    static ItemStack MMTeamYellow = createItem(new ItemStack(Material.YELLOW_WOOL), "&eYellow Team", "&fClick with this item to change MM teams!");
// ---------------------------------------------------------------------------------------------------------------------

    public GeneralLobbyHandler(MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        // Initialize fields
        GeneralLobbyHandler.plugin = plugin;
        playerAreas = new ArrayList<>();
        KOTHExist = false;  // KOTHLobbyHandler has not been created

        // Create MM teams   --- MOVE TO MM HANDLER
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMRed", " ⧫ ", "Red", null, ChatColor.RED,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMBlue", " ⧫ ", "Blue", null, ChatColor.BLUE,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMGreen", " ⧫ ", "Green", null, ChatColor.GREEN,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMYellow", " ⧫ ", "Yellow", null, ChatColor.YELLOW,false, true, NameTagVisibility.ALWAYS);
    }


    /*
     * Lobby/hub tags for keeping track of player location/status:
     *
     *  - "troubleshooting"  - does not perform usual resets to player
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
        // Clear potion effects
        Collection<PotionEffect> effectsToClear = player.getActivePotionEffects();
        for (PotionEffect pE : effectsToClear)
            player.removePotionEffect(pE.getType());
        // Reset tags
        Set<String> tags = player.getScoreboardTags();
        Tools.resetTags(player);
        // Set tags
        player.addScoreboardTag("mainHub");         // Player is now in main hub
        player.addScoreboardTag("notInGame");       // Player is still not in a game
        // Reset team
        Tools.resetTeam(player);
        // Reset inventory
        Inventory inv = player.getInventory();
        if (!player.getScoreboardTags().contains("troubleshooting"))    // Only clear inventory if not troubleshooting
            inv.clear();
        // Give lobby selector after some time
        new DelayedTask(() -> {inv.setItem(4, lobbySelector);}, 10);
    }

    /**
     * Sends the provided player to the King of The Hill lobby and sets their tags accordingly.
     * If an instance of KOTHLobbyHandler does not already exist, a new one is made.
     *
     * @param player player to be sent
     */
    public static void sendKOTHLobby(Player player) {
        // Tp player
        player.teleport(Locations.KOTHLobby);
        // Clear potion effects
        Collection<PotionEffect> effectsToClear = player.getActivePotionEffects();
        for (PotionEffect pE : effectsToClear)
            player.removePotionEffect(pE.getType());
        // Reset tags
        Set<String> tags = player.getScoreboardTags();
        Tools.resetTags(player);
        // Set tags
        player.addScoreboardTag("KOTHLobby");       // Player is now in KOTH lobby
        player.addScoreboardTag("notInGame");       // Player is still not in a game
        // Reset team
        Tools.resetTeam(player);
        // Reset inventory
        Inventory inv = player.getInventory();
        if (!tags.contains("troubleshooting"))    // Only clear inventory if not troubleshooting
            inv.clear();
        // Give items for lobby hot bar menu after some time
        new DelayedTask(() -> {inv.setItem(0, KOTHQueue);}, 10);        // Queue/Dequeue item
        new DelayedTask(() -> {inv.setItem(2, KOTHTeamNone);}, 10);     // Team selector item (no team by default)
        new DelayedTask(() -> {inv.setItem(4, lobbySelector);}, 10);    // Lobby selector item


        // Change boolean and add the player when an instance of KOTHLobbyHandler is found
        for (PlayerArea area : playerAreas)
            if (area instanceof KOTHLobbyHandler) {
                area.addPlayer(player);
                KOTHExist = true;
            }
        // Create new KOTHLobbyHandler if one doesn't already exist
        if (!KOTHExist) {
            playerAreas.add(new KOTHLobbyHandler(plugin, player));
            KOTHExist = true;
        }
    }

    /**
     * Sends the provided player to the Murder Mystery lobby and sets their tags accordingly.
     *
     * @param player player to be sent
     */
    public static void sendMMLobby(Player player) {
        // Tp player
        player.teleport(Locations.MMLobby);
        // Clear potion effects
        Collection<PotionEffect> effectsToClear = player.getActivePotionEffects();
        for (PotionEffect pE : effectsToClear)
            player.removePotionEffect(pE.getType());
        // Reset tags
        Set<String> tags = player.getScoreboardTags();
        Tools.resetTags(player);
        // Set tags
        player.addScoreboardTag("MMLobby");         // Player is now in MM lobby
        player.addScoreboardTag("notInGame");       // Player is still not in a game
        // Reset team
        Tools.resetTeam(player);
        // Reset inventory
        Inventory inv = player.getInventory();
        if (!player.getScoreboardTags().contains("troubleshooting"))    // Only clear inventory if not troubleshooting
            inv.clear();
        // Give items for lobby hot bar menu after some time
        new DelayedTask(() -> {inv.setItem(0, MMQueue);}, 10);        // Queue/Dequeue item
        new DelayedTask(() -> {inv.setItem(2, MMTeamNone);}, 10);     // Team selector item (no team by default)
        new DelayedTask(() -> {inv.setItem(4, lobbySelector);}, 10);  // Lobby selector item
    }




    /**
     * Provides functionality to the Lobby Selector item (and MM lobby hot bar menu -- MOVE TO MMHANDLER)
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Setup
        Player player = event.getPlayer();
        Inventory inv = player.getInventory();
        Set<String> tags = player.getScoreboardTags();

        // When a player interacts while not in a game or while troubleshooting...
        if (tags.contains("notInGame") || tags.contains("troubleshooting")) {
            // Detect when player right clicks
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                // Detect right click with Lobby Selector compass
                if (player.getItemInHand().getItemMeta() != null && player.getItemInHand().getItemMeta().getDisplayName().equals("§aLobby Selector")) {

                    // Create "UI"
                    Inventory menu = Bukkit.createInventory(player, 9 * 3, "Lobby Selector");
                    // UI options:
                    // KOTH lobby
                    inv.setItem(11, createItem(new ItemStack(Material.GRASS_BLOCK), "&aKOTH", "&7Conquer the Hill"));
                    // Main Hub
                    inv.setItem(13, createItem(new ItemStack(Material.RED_BED), "&2Main Hub", "&7Home sweet home"));
                    // MM lobby
                    inv.setItem(15, createItem(new ItemStack(Material.DIAMOND_SWORD), "&cMurder Mystery", "&7Stab your friends! :D"));

                    // Open the created "UI"
                    player.openInventory(menu);
                }
        }

// MOVE TO MM LOBBY HANDLER ---------------------------------------------------------------------------------------------
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
//----------------------------------------------------------------------------------------------------------------------

    }

    /**
     * Detects and handles player interactions within the Lobby Selection item menu
     */
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Set<String> tags = player.getScoreboardTags();
        // For players not in a game...
        if (tags.contains("notInGame")) {
            // Only handle inv clicks if player is in Lobby Selector inventory
            if (event.getView().getTitle().equals("Lobby Selector")) {

                // Retrieve the slot number that the player clicked on
                int slot = event.getSlot();

                // Make sure an inventory item was clicked
                if (event.getCurrentItem() != null) {
                    // Send player to...

                    // KOTH lobby
                    if (slot == 11 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§aKOTH")) {
                        // Tp player to KOTH lobby
                        GeneralLobbyHandler.sendKOTHLobby(player);
                        // Play tp sound
                        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
                        // Close player inventory
                        event.getWhoClicked().closeInventory();
                    }
                    // Main Hub
                    else if (slot == 13 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§2Main Hub")) {
                        // Tp player to main hub
                        GeneralLobbyHandler.sendMainHub(player);
                        // Play tp sound
                        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
                        // Close player inventory
                        event.getWhoClicked().closeInventory();
                    }
                    // MM lobby
                    else if (slot == 15 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§cMurder Mystery")) {
                        // Tp player to MM lobby
                        GeneralLobbyHandler.sendMMLobby(player);
                        // Play tp sound
                        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
                        // Close player inventory
                        event.getWhoClicked().closeInventory();
                    }

                    // Prevent moving menu items, only allow clicking
                    event.setCancelled(true);
                }
            }
            if (!tags.contains("troubleshooting"))  // Unless troubleshooting...
                // Lock inventory when not in a game
                event.setCancelled(true);
        }
    }

}

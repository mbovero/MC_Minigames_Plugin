package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.MM.MMLobbyHandler;
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
    static boolean MMExist;      // Boolean to check if a KOTHLobbyHandler object exists


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
        playerAreas.add(new HubHandler(plugin));
        KOTHExist = false;  // KOTHLobbyHandler has not been created

        // Create MM teams   --- MOVE TO MM HANDLER
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMRed", " ⧫ ", "Red", null, ChatColor.RED, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMBlue", " ⧫ ", "Blue", null, ChatColor.BLUE, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMGreen", " ⧫ ", "Green", null, ChatColor.GREEN, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "MMYellow", " ⧫ ", "Yellow", null, ChatColor.YELLOW, false, true, NameTagVisibility.ALWAYS);
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
    public static DelayedTask sendMainHub(Player player, PlayerArea prevArea) {
        // Delay operation by some time
        return new DelayedTask(() -> {
            if (!(prevArea.getAreaName().equals("mainHub"))) {
                prevArea.removePlayer(player);

                player.sendMessage("Removed " + player.getName() + " from " + prevArea.getAreaName());

                // Add the player when an instance of HubHandler is found
                for (PlayerArea lobby : playerAreas)
                    if (lobby instanceof HubHandler) {
                        lobby.addPlayer(player);
                        player.sendMessage("Added " + player.getName() + " to " + lobby.getAreaName());
                    }
            }

            // Tp player
            player.teleport(Locations.mainHub);
            // Play tp sound
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
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
            if (!tags.contains("troubleshooting"))    // Only clear inventory if not troubleshooting
                inv.clear();
            // Give lobby selector after some time
            inv.setItem(4, lobbySelector);
        }, 5);
    }

    /**
     * Sends the provided player to the King of The Hill lobby and sets their tags accordingly.
     * If an instance of KOTHLobbyHandler does not already exist, a new one is made.
     *
     * @param player player to be sent
     */
    public static DelayedTask sendKOTHLobby(Player player, PlayerArea prevArea) {
        // Delay operation by some time
        return new DelayedTask(() -> {
            if (!(prevArea.getAreaName().equals("KOTHLobby"))) {
                prevArea.removePlayer(player);

                // Change boolean and add the player when an instance of KOTHLobbyHandler is found
                for (PlayerArea lobby : playerAreas)
                    if (lobby instanceof KOTHLobbyHandler) {
                        lobby.addPlayer(player);
                        KOTHExist = true;
                    }
                // Create new KOTHLobbyHandler if one doesn't already exist
                if (!KOTHExist) {
                    playerAreas.add(new KOTHLobbyHandler(plugin, player));
                    KOTHExist = true;
                }
            }
            // Tp player
            player.teleport(Locations.KOTHLobby);
            // Play tp sound
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
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
            inv.setItem(0, KOTHQueue);        // Queue/Dequeue item
            inv.setItem(2, KOTHTeamNone);     // Team selector item (no team by default)
            inv.setItem(4, lobbySelector);    // Lobby selector item


        }, 5);
    }

    /**
     * Sends the provided player to the Murder Mystery lobby and sets their tags accordingly.
     *
     * @param player player to be sent
     */
    public static DelayedTask sendMMLobby(Player player, PlayerArea prevArea) {
        // Delay operation by some time
        return new DelayedTask(() -> {
            if (!(prevArea.getAreaName().equals("MMLobby"))) {
                prevArea.removePlayer(player);
                // Change boolean and add the player when an instance of MMLobbyHandler is found
                for (PlayerArea lobby : playerAreas)
                    if (lobby instanceof MMLobbyHandler) {
                        lobby.addPlayer(player);
                        MMExist = true;
                    }
                // Create new KOTHLobbyHandler if one doesn't already exist
                if (!MMExist) {
                    playerAreas.add(new MMLobbyHandler(plugin, player));
                    MMExist = true;
                }
            }

            // Tp player
            player.teleport(Locations.MMLobby);
            // Play tp sound
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
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
            // Give items for lobby hot bar menu
            inv.setItem(0, MMQueue);        // Queue/Dequeue item
            inv.setItem(2, MMTeamNone);     // Team selector item (no team by default)
            inv.setItem(4, lobbySelector);  // Lobby selector item
        }, 5);
    }

    /**
     * Provides functionality to the Lobby Selector item
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
                    menu.setItem(11, createItem(new ItemStack(Material.GRASS_BLOCK), "&aKOTH", "&7Conquer the Hill"));
                    // Main Hub
                    menu.setItem(13, createItem(new ItemStack(Material.RED_BED), "&2Main Hub", "&7Home sweet home"));
                    // MM lobby
                    menu.setItem(15, createItem(new ItemStack(Material.DIAMOND_SWORD), "&cMurder Mystery", "&7Stab your friends! :D"));

                    // Open the created "UI"
                    player.openInventory(menu);
                }
        }
    }

    /**
     * Detects and handles player interactions within the Lobby Selection item menu
     */
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Set<String> tags = player.getScoreboardTags();
        // Holds the area the minecraft player reference is in based on the GamePlayer reference
        PlayerArea playerArea = findPlayer(player);
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
                        GeneralLobbyHandler.sendKOTHLobby(player, playerArea);
                        // Close player inventory
                        event.getWhoClicked().closeInventory();
                    }
                    // Main Hub
                    else if (slot == 13 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§2Main Hub")) {
                        // Tp player to main hub
                        GeneralLobbyHandler.sendMainHub(player, playerArea);
                        // Close player inventory
                        event.getWhoClicked().closeInventory();
                    }
                    // MM lobby
                    else if (slot == 15 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§cMurder Mystery")) {
                        // Tp player to MM lobby
                        GeneralLobbyHandler.sendMMLobby(player, playerArea);
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

    /**
     * Method returns the PlayerArea object that the associated player reference is currently held inside.
     *
     * @param mcPlayer
     * @return
     */
    public PlayerArea findPlayer(Player mcPlayer) {
        for (PlayerArea area : playerAreas) {

            mcPlayer.sendMessage(area.getAreaName());

                for (GamePlayer gamePlayer : area.getPlayers()) {
                    mcPlayer.sendMessage("Players: " + gamePlayer.getPlayer().getName());
                    if (gamePlayer.isPlayer(mcPlayer)) {
                        return area;
                    }
                }
        }
        mcPlayer.sendMessage("\nBAD BAD\n");
        return null;
    }

}

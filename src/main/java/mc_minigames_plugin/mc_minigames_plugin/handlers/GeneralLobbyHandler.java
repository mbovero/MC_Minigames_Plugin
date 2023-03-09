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
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    }


// PLAYER INTERACTION --------------------------------------------------------------------------------------------------

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
     * @param MCPlayer player to be sent
     */
    public static DelayedTask sendMainHub(Player MCPlayer, PlayerArea prevArea) {
        // Delay operation by some time
        return new DelayedTask(() -> {
            // Find the gamePlayer matching with the event's MCPlayer
            GamePlayer gamePlayer = findPlayer(MCPlayer);

            //
            if (!(prevArea.getAreaName().equals("mainHub"))) {
                prevArea.removePlayer(MCPlayer);

                MCPlayer.sendMessage("Removed " + MCPlayer.getName() + " from " + prevArea.getAreaName());

                // Add the player when an instance of HubHandler is found
                for (PlayerArea lobby : playerAreas)
                    if (lobby instanceof HubHandler) {
                        lobby.addPlayer(MCPlayer);

                        MCPlayer.sendMessage("Added " + MCPlayer.getName() + " to " + lobby.getAreaName());

                    }
            }

            // Tp player
            MCPlayer.teleport(Locations.mainHub);
            // Play tp sound
            MCPlayer.playSound(MCPlayer, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
            // Clear potion effects
            Collection<PotionEffect> effectsToClear = MCPlayer.getActivePotionEffects();
            for (PotionEffect pE : effectsToClear)
                MCPlayer.removePotionEffect(pE.getType());
            // Reset tags
            Set<String> tags = MCPlayer.getScoreboardTags();
            Tools.resetTags(MCPlayer);
            // Set tags
            MCPlayer.addScoreboardTag("mainHub");         // Player is now in main hub
            MCPlayer.addScoreboardTag("notInGame");       // Player is still not in a game
            // Reset team
            Tools.resetTeam(MCPlayer);
            // Reset inventory
            Inventory inv = MCPlayer.getInventory();
            if (!gamePlayer.isTroubleShooting())    // Only clear inventory if not troubleshooting
                inv.clear();
            // Give lobby selector after some time
            inv.setItem(4, lobbySelector);
        }, 5);
    }

    /**
     * Sends the provided player to the King of The Hill lobby and sets their tags accordingly.
     * If an instance of KOTHLobbyHandler does not already exist, a new one is made.
     *
     * @param MCPlayer player to be sent
     */
    public static DelayedTask sendKOTHLobby(Player MCPlayer, PlayerArea prevArea) {
        // Delay operation by some time
        return new DelayedTask(() -> {
            // Find the gamePlayer matching with the event's MCPlayer
            GamePlayer gamePlayer = findPlayer(MCPlayer);

            // If the previous area is not KOTHLobby...
            if (!(prevArea.getAreaName().equals("KOTHLobby"))) {
                // Remove the player from their previous area
                prevArea.removePlayer(MCPlayer);

                // Search through current PlayerAreas
                for (PlayerArea lobby : playerAreas)
                    // If KOTHLobby is found...
                    if (lobby instanceof KOTHLobbyHandler) {
                        // Add the player to that KOTHLobby
                        lobby.addPlayer(MCPlayer);
                        // and update KOTHExist boolean
                        KOTHExist = true;
                    }
                // Create new KOTHLobbyHandler if one doesn't already exist, and add the player to it
                if (!KOTHExist) {
                    playerAreas.add(new KOTHLobbyHandler(plugin, MCPlayer));
                    KOTHExist = true;
                }
            }
            // Tp player
            MCPlayer.teleport(Locations.KOTHLobby);
            // Play tp sound
            MCPlayer.playSound(MCPlayer, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
            // Clear potion effects
            Collection<PotionEffect> effectsToClear = MCPlayer.getActivePotionEffects();
            for (PotionEffect pE : effectsToClear)
                MCPlayer.removePotionEffect(pE.getType());
            // Reset tags
            Set<String> tags = MCPlayer.getScoreboardTags();
            Tools.resetTags(MCPlayer);
            // Set tags
            MCPlayer.addScoreboardTag("KOTHLobby");       // Player is now in KOTH lobby
            MCPlayer.addScoreboardTag("notInGame");       // Player is still not in a game
            // Reset team
            Tools.resetTeam(MCPlayer);
            // Reset inventory
            Inventory inv = MCPlayer.getInventory();
            if (!gamePlayer.isTroubleShooting())    // Only clear inventory if not troubleshooting
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
     * @param MCPlayer player to be sent
     */
    public static DelayedTask sendMMLobby(Player MCPlayer, PlayerArea prevArea) {
        // Delay operation by some time
        return new DelayedTask(() -> {
            // Find the gamePlayer matching with the event's MCPlayer
            GamePlayer gamePlayer = findPlayer(MCPlayer);

            if (!(prevArea.getAreaName().equals("MMLobby"))) {
                prevArea.removePlayer(MCPlayer);
                // Change boolean and add the player when an instance of MMLobbyHandler is found
                for (PlayerArea lobby : playerAreas)
                    if (lobby instanceof MMLobbyHandler) {
                        lobby.addPlayer(MCPlayer);
                        MMExist = true;
                    }
                // Create new KOTHLobbyHandler if one doesn't already exist
                if (!MMExist) {
                    playerAreas.add(new MMLobbyHandler(plugin, MCPlayer));
                    MMExist = true;
                }
            }

            // Tp player
            MCPlayer.teleport(Locations.MMLobby);
            // Play tp sound
            MCPlayer.playSound(MCPlayer, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
            // Clear potion effects
            Collection<PotionEffect> effectsToClear = MCPlayer.getActivePotionEffects();
            for (PotionEffect pE : effectsToClear)
                MCPlayer.removePotionEffect(pE.getType());
            // Reset tags
            Set<String> tags = MCPlayer.getScoreboardTags();
            Tools.resetTags(MCPlayer);
            // Set tags
            MCPlayer.addScoreboardTag("MMLobby");         // Player is now in MM lobby
            MCPlayer.addScoreboardTag("notInGame");       // Player is still not in a game
            // Reset team
            Tools.resetTeam(MCPlayer);
            // Reset inventory
            Inventory inv = MCPlayer.getInventory();
            if (!gamePlayer.isTroubleShooting())    // Only clear inventory if not troubleshooting
                inv.clear();
            // Give items for lobby hot bar menu
            inv.setItem(0, MMQueue);        // Queue/Dequeue item
            inv.setItem(2, MMTeamNone);     // Team selector item (no team by default)
            inv.setItem(4, lobbySelector);  // Lobby selector item
        }, 5);
    }

    /**
     * Method returns the PlayerArea object that the associated player reference is currently held inside.
     *
     * @param MCPlayer
     * @return
     */
    public static GamePlayer findPlayer(Player MCPlayer) {
        // Iterate through every existing playerArea
        for (PlayerArea area : playerAreas) {
            // Iterate through each playerArea's list of gamePlayers
            for (GamePlayer gamePlayer : area.getPlayers()) {
                // Compare gamePlayer to MCPlayer
                if (gamePlayer.isPlayer(MCPlayer)) {
                    // If the player is found, return gamePlayer
                    return gamePlayer;
                }
            }
        }
        MCPlayer.sendMessage("\nBAD BAD\n");
        return null;
    }




// LOBBY ITEM FUNCTIONALITY --------------------------------------------------------------------------------------------

    /**
     * Provides functionality to the Lobby Selector item
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Setup
        Player MCPlayer = event.getPlayer();
        Set<String> tags = MCPlayer.getScoreboardTags();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);

        // When a player interacts while not in a game or while troubleshooting...
        if (!gamePlayer.isInGame() || !gamePlayer.isTroubleShooting()) {
            // Detect when player right clicks
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                // Detect right click with Lobby Selector compass
                if (MCPlayer.getItemInHand().getItemMeta() != null && MCPlayer.getItemInHand().getItemMeta().getDisplayName().equals("§aLobby Selector")) {

                    // Create "UI"
                    Inventory menu = Bukkit.createInventory(MCPlayer, 9 * 3, "Lobby Selector");
                    // UI options:
                    // KOTH lobby
                    menu.setItem(11, createItem(new ItemStack(Material.GRASS_BLOCK), "&aKOTH", "&7Conquer the Hill"));
                    // Main Hub
                    menu.setItem(13, createItem(new ItemStack(Material.RED_BED), "&2Main Hub", "&7Home sweet home"));
                    // MM lobby
                    menu.setItem(15, createItem(new ItemStack(Material.DIAMOND_SWORD), "&cMurder Mystery", "&7Stab your friends! :D"));

                    // Open the created "UI"
                    MCPlayer.openInventory(menu);
                }
        }
    }

    /**
     * Detects and handles player interactions within the Lobby Selection item menu
     */
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player MCPlayer = (Player) event.getWhoClicked();
        Set<String> tags = MCPlayer.getScoreboardTags();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // Store the gamePlayer's current area
        PlayerArea playerArea = gamePlayer.getCurrentArea();
        // For players not in a game...
        if (!gamePlayer.isInGame()) {
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
                        GeneralLobbyHandler.sendKOTHLobby(MCPlayer, playerArea);
                        // Close player inventory
                        event.getWhoClicked().closeInventory();
                    }
                    // Main Hub
                    else if (slot == 13 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§2Main Hub")) {
                        // Tp player to main hub
                        GeneralLobbyHandler.sendMainHub(MCPlayer, playerArea);
                        // Close player inventory
                        event.getWhoClicked().closeInventory();
                    }
                    // MM lobby
                    else if (slot == 15 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§cMurder Mystery")) {
                        // Tp player to MM lobby
                        GeneralLobbyHandler.sendMMLobby(MCPlayer, playerArea);
                        // Close player inventory
                        event.getWhoClicked().closeInventory();
                    }

                    // Prevent moving menu items, only allow clicking
                    event.setCancelled(true);
                }
            }
            if (!gamePlayer.isTroubleShooting())  // Unless troubleshooting...
                // Lock inventory when not in a game
                event.setCancelled(true);
        }
    }




// WORLD INTERACTION ---------------------------------------------------------------------------------------------------

    /**
     * Prevents players not in games and not troubleshooting from harvesting blocks.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventHarvestBlock(PlayerHarvestBlockEvent event) {
        Player MCPlayer = (Player) event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleShooting()) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from manipulating armor stands.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        Player MCPlayer = (Player) event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleShooting()) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from consuming items.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventItemConsume(PlayerItemConsumeEvent event) {
        Player MCPlayer = (Player) event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleShooting()) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from placing blacks.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventBlockPlace(BlockPlaceEvent event) {
        Player MCPlayer = (Player) event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleShooting()) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from breaking blocks.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventBlockBreak(BlockBreakEvent event) {
        Player MCPlayer = (Player) event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleShooting()) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from damaging entities and breaking armor stands.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventEntityDamage(EntityDamageByEntityEvent event) {
        Player MCPlayer = (Player) event.getDamager();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleShooting()) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from killing entities.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null)
            return;
        Player MCPlayer = (Player) event.getEntity().getKiller();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleShooting()) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from dropping items in lobbies
     */
    @EventHandler
    public void preventItemDrop(PlayerDropItemEvent event) {
        Player MCPlayer = event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleShooting())
            event.setCancelled(true);
    }

    /**
     * Prevents hunger in lobbies for players not in games and not troubleshooting
     */
    @EventHandler
    public void preventHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player MCPlayer = (Player) event.getEntity();
            // Find the gamePlayer matching with the event's MCPlayer
            GamePlayer gamePlayer = findPlayer(MCPlayer);
            if (!gamePlayer.isInGame() && !gamePlayer.isTroubleShooting())
                event.setCancelled(true);
        }
    }

    /**
     * Prevents damage in lobbies for players not in games and not troubleshooting
     */
    @EventHandler
    public void preventDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player MCPlayer = (Player) event.getEntity();
            // Find the gamePlayer matching with the event's MCPlayer
            GamePlayer gamePlayer = findPlayer(MCPlayer);
            if (!gamePlayer.isInGame() && !gamePlayer.isTroubleShooting())
                event.setCancelled(true);
        }




        // Tutorial stuff
//        // Ensure that a player was hurt by fall damage
//        if (!(event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL)) {
//            return;
//        }
//
//        // Give a player a diamond after five seconds
//        DelayedTask task = new DelayedTask(() -> {player.getInventory().addItem(new ItemStack(Material.DIAMOND));}, 20 * 5);
//        // Cancel the task
//        Bukkit.getScheduler().cancelTask(task.getId());
    }

    /**
     * Gives players not in games and not troubleshooting levitation when they fall below a certain Y-level in lobbies.
     */
    @EventHandler
    public void voidLevitation(PlayerMoveEvent event) {
        Player MCPlayer = event.getPlayer();
        Set<String> tags = MCPlayer.getScoreboardTags();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // For all players not in a game and not troubleshooting...
        if (event.getTo().getY() < -66 && event.getTo().getY() > -85 && !gamePlayer.isInGame() && !gamePlayer.isTroubleShooting()) {
            // Apply main hub levitation
            if (tags.contains("mainHub"))
                MCPlayer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 22, false));
                // Apply KOTH lobby levitation
            else if (tags.contains("KOTHLobby"))
                MCPlayer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 10, false));
                // Apply MM lobby levitation
            else if (tags.contains("MMLobby"))
                MCPlayer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 18, false));
        }
        // Return players to main hub when they go out of bounds
        else if (event.getTo().getY() < -90 && !gamePlayer.isInGame())
            GeneralLobbyHandler.sendMainHub(MCPlayer, findPlayer(MCPlayer).getCurrentArea());
    }
}

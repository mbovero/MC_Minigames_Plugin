package mc_minigames_plugin.mc_minigames_plugin.handlers;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHGameHandler;
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
    protected static ArrayList<PlayerArea> allPlayerAreas;     // A list of every area players may be in
    protected static MC_Minigames_Plugin plugin;            // Overarching plugin

    protected static PlayerArea mainHub;
    protected static PlayerArea KOTHLobby;
    protected static PlayerArea MMLobby;


    // ITEMS ---------------------------------------------------------------------------------------------------------------
    // Lobby selector tool
    static ItemStack lobbySelector = createItem(new ItemStack(Material.COMPASS), "&aLobby Selector", "&fExplore our selection of games!");

    // Initial KOTH lobby hot bar menu items
    static ItemStack KOTHQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the KOTH queue!");
    static ItemStack KOTHTeamNone = createItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "&7No Team", "&fClick with this item to change KOTH teams!");

    // Initial MM lobby hot bar menu items
    static ItemStack MMQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the MM queue!");
    static ItemStack MMTeamNone = createItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "&7No Team", "&fClick with this item to change MM teams!");
// ---------------------------------------------------------------------------------------------------------------------

    public GeneralLobbyHandler(MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        // Initialize fields
        GeneralLobbyHandler.plugin = plugin;
        allPlayerAreas = new ArrayList<>();
        mainHub = new MainHubHandler(plugin);   // Initiate new mainHub
        allPlayerAreas.add(mainHub);            // Add mainHub to list of player areas
        KOTHLobby = null;                       // No KOTHLobby exists yet
        MMLobby = null;                         // No MMLobby exists yet
    }


// PLAYER INTERACTION --------------------------------------------------------------------------------------------------

    /**
     * Sends the provided player to the main hub and sets their tags accordingly.
     *
     * @param gamePlayer gamePlayer to be sent
     */
    public static DelayedTask sendMainHub(GamePlayer gamePlayer) {
        // Delay operation by some time
        return new DelayedTask(() -> {
            // Store gamePlayer's MCPlayer reference
            Player MCPlayer = gamePlayer.getPlayer();

            // Send gamePlayer to mainHub
            MainHubHandler.sendPlayer(gamePlayer);

            // Only do if not troubleshooting
            if (!gamePlayer.isTroubleshooting()) {
                // Tp player
                MCPlayer.teleport(Locations.mainHub);
                // Play tp sound
                MCPlayer.playSound(MCPlayer, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);

                // Clear potion effects
                Tools.resetPotionEffects(MCPlayer);
                // Reset tags
                Tools.resetTags(MCPlayer);
                // Reset team
                Tools.resetTeam(MCPlayer);
                // Reset inventory
                Tools.resetInventory(MCPlayer);
            }

            // Give lobby selector
            MCPlayer.getInventory().setItem(4, lobbySelector);
        }, 5);
    }

    /**
     * Sends the provided player to the King of The Hill lobby and sets their tags accordingly.
     * If an instance of KOTHLobbyHandler does not already exist, a new one is made.
     *
     * @param gamePlayer gamePlayer to be sent
     */
    public static DelayedTask sendKOTHLobby(GamePlayer gamePlayer) {
        // Delay operation by some time
        return new DelayedTask(() -> {
            // Store gamePlayer's MCPlayer reference
            Player MCPlayer = gamePlayer.getPlayer();

            // Send gamePlayer to KOTHLobby
            KOTHLobbyHandler.sendPlayer(gamePlayer);

            // Tp player
            MCPlayer.teleport(Locations.KOTHLobby);

            // Only do if not troubleshooting
            if (!gamePlayer.isTroubleshooting()) {
                // Play tp sound
                MCPlayer.playSound(MCPlayer, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);

                // Clear potion effects
                Tools.resetPotionEffects(MCPlayer);
                // Reset tags
                Tools.resetTags(MCPlayer);
                // Reset team
                Tools.resetTeam(MCPlayer);
                // Reset inventory
                Tools.resetInventory(MCPlayer);
            }

            // Give items for lobby hot bar menu
            Inventory inv = MCPlayer.getInventory();
            inv.setItem(0, KOTHQueue);        // Queue/Dequeue item
            inv.setItem(2, KOTHTeamNone);     // Team selector item (no team by default)
            inv.setItem(4, lobbySelector);    // Lobby selector item
        }, 5);
    }

    /**
     * Sends the provided player to the Murder Mystery lobby and sets their tags accordingly.
     *
     * @param gamePlayer gamePlayer to be sent
     */
    public static DelayedTask sendMMLobby(GamePlayer gamePlayer) {
        // Delay operation by some time
        return new DelayedTask(() -> {
            // Store gamePlayer's MCPlayer reference
            Player MCPlayer = gamePlayer.getPlayer();

            // Send gamePlayer to MMLobby
            MMLobbyHandler.sendPlayer(gamePlayer);

            // Tp player
            MCPlayer.teleport(Locations.MMLobby);

            // Only do if not troubleshooting
            if (!gamePlayer.isTroubleshooting()) {
                // Play tp sound
                MCPlayer.playSound(MCPlayer, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);

                // Clear potion effects
                Tools.resetPotionEffects(MCPlayer);
                // Reset tags
                Tools.resetTags(MCPlayer);
                // Reset team
                Tools.resetTeam(MCPlayer);
                // Reset inventory
                Tools.resetInventory(MCPlayer);
            }

            // Give items for lobby hot bar menu
            Inventory inv = MCPlayer.getInventory();
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
        for (PlayerArea area : allPlayerAreas) {
            // Iterate through each playerArea's list of gamePlayers
            for (GamePlayer gamePlayer : area.getPlayers()) {
                // Compare gamePlayer to MCPlayer
                if (gamePlayer.isPlayer(MCPlayer)) {
                    // If the player is found, return gamePlayer
                    return gamePlayer;
                }
            }
            // Also search through KOTH games
            if (area instanceof KOTHLobbyHandler)
                // Retrieve and search through KOTHLobby active games
                for (KOTHGameHandler KOTHGame : ((KOTHLobbyHandler) area).getActiveGames())
                    // If the game object is not null...
                    if (KOTHGame != null)
                        // Iterate through game's players
                        for (GamePlayer gamePlayer : KOTHGame.getPlayers())
                            // Compare gamePlayer to MCPlayer
                            if (gamePlayer.isPlayer(MCPlayer))
                                // If the player is found, return gamePlayer
                                return gamePlayer;
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
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);

        // When a player interacts while not in a game or while troubleshooting...
        if (!gamePlayer.isInGame() || !gamePlayer.isTroubleshooting()) {
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
                        GeneralLobbyHandler.sendKOTHLobby(gamePlayer);
                        // Close player inventory
                        event.getWhoClicked().closeInventory();
                    }
                    // Main Hub
                    else if (slot == 13 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§2Main Hub")) {
                        // Tp player to main hub
                        GeneralLobbyHandler.sendMainHub(gamePlayer);
                        // Just tp if troubleshooting
                        if (gamePlayer.isTroubleshooting())
                            MCPlayer.teleport(Locations.mainHub);
                        // Close player inventory
                        event.getWhoClicked().closeInventory();
                    }
                    // MM lobby
                    else if (slot == 15 && event.getCurrentItem().getItemMeta().getDisplayName().equals("§cMurder Mystery")) {
                        // Tp player to MM lobby
                        GeneralLobbyHandler.sendMMLobby(gamePlayer);
                        // Close player inventory
                        event.getWhoClicked().closeInventory();
                    }

                    // Prevent moving menu items, only allow clicking
                    event.setCancelled(true);
                }
            }
            if (!gamePlayer.isTroubleshooting())  // Unless troubleshooting...
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
        Player MCPlayer = event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleshooting()) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from manipulating armor stands.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        Player MCPlayer = event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleshooting()) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from consuming items.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventItemConsume(PlayerItemConsumeEvent event) {
        Player MCPlayer = event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleshooting()) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from placing blacks.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventBlockPlace(BlockPlaceEvent event) {
        Player MCPlayer = event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleshooting()) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players not in games and not troubleshooting from breaking blocks.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventBlockBreak(BlockBreakEvent event) {
        Player MCPlayer = event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleshooting()) {
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
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleshooting()) {
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
        Player MCPlayer = event.getEntity().getKiller();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleshooting()) {
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
        if (!gamePlayer.isInGame() && !gamePlayer.isTroubleshooting())
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
            if (!gamePlayer.isInGame() && !gamePlayer.isTroubleshooting())
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
            if (!gamePlayer.isInGame() && !gamePlayer.isTroubleshooting())
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
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // Find gamePlayer's area
        String currentArea = gamePlayer.getCurrentArea().getAreaName();
        // For all players not in a game and not troubleshooting...
        if (event.getTo().getY() < -66 && event.getTo().getY() > -85 && !gamePlayer.isInGame() && !gamePlayer.isTroubleshooting()) {
            // Apply main hub levitation
            if (currentArea.equals("mainHub"))
                MCPlayer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 22, false));
                // Apply KOTH lobby levitation
            else if (currentArea.equals("KOTHLobby"))
                MCPlayer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 10, false));
                // Apply MM lobby levitation
            else if (currentArea.equals("MMLobby"))
                MCPlayer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20, 18, false));
        }
        // Return players to main hub when they go out of bounds
        else if (event.getTo().getY() < -90 && !gamePlayer.isInGame())
            GeneralLobbyHandler.sendMainHub(gamePlayer);
    }

    /**
     * Returns GeneralLobbyHandler's KOTHLobby, null if not yet initialized
     */
    public static PlayerArea getKOTHLobby() {
        return KOTHLobby;
    }

    /**
     * Creates a new KOTHLobbyHandler and adds it to the list of all player areas
     */
    public static void createKOTHLobby() {
        // Make a new KOTHLobby
        KOTHLobby = new KOTHLobbyHandler(plugin);
        // Add the new KOTHLobby to the list of player areas
        allPlayerAreas.add(KOTHLobby);
    }

    /**
     * Returns GeneralLobbyHandler's MMLobby, null if not yet initialized
     */
    public static PlayerArea getMMLobby() {
        return MMLobby;
    }

    /**
     * Creates a new MMLobbyHandler and adds it to the list of all player areas
     */
    public static void createMMLobby() {
        // Make a new MMLobby
        MMLobby = new MMLobbyHandler(plugin);
        // Add the new MMLobby to the list of player areas
        allPlayerAreas.add(MMLobby);
    }
}

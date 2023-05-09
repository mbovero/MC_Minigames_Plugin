package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.handlers.MainHubHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits.KOTHKitStriker;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Maps.Map;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Maps.MapCastleOfDreams;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.NameTagVisibility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler.*;
import static mc_minigames_plugin.mc_minigames_plugin.util.Tools.createItem;

/**
 * Class Description: Holds players in lobby and handles run time events for those players
 *
 * @author Kirt Robinson and Miles Bovero
 * @version March 5, 2023
 */
public class KOTHLobbyHandler extends PlayerArea implements Listener {

    // ITEMS ---------------------------------------------------------------------------------------------------------------
    // KOTH lobby hot bar menu items
    private static final ItemStack KOTHQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the KOTH queue!");
    private static final ItemStack KOTHDequeue = createItem(new ItemStack(Material.LIME_DYE), "&aReady", "&fClick with this item to leave the KOTH queue");

    private static final ItemStack KOTHTeamNone = createItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "&7No Team", "&fClick with this item to change KOTH teams!");
    private static final ItemStack KOTHTeamRed = createItem(new ItemStack(Material.RED_WOOL), "&4Red Team", "&fClick with this item to change KOTH teams!");
    private static final ItemStack KOTHTeamBlue = createItem(new ItemStack(Material.BLUE_WOOL), "&1Blue Team", "&fClick with this item to change KOTH teams!");
    private static final ItemStack KOTHTeamGreen = createItem(new ItemStack(Material.LIME_WOOL), "&2Green Team", "&fClick with this item to change KOTH teams!");
    private static final ItemStack KOTHTeamYellow = createItem(new ItemStack(Material.YELLOW_WOOL), "&eYellow Team", "&fClick with this item to change KOTH teams!");
// ---------------------------------------------------------------------------------------------------------------------

    private String selectedGamemode;                // The KOTH gamemode that is currently selected
    private Map selectedMap;                        // The KOTH map that is currently selected
    // The location of the KOTH lobby
    private static final Location location = new Location(Bukkit.getWorld("world"), 8, -60, -600, -180, 0);

    // A list of the currently running KOTH games, each index correlates to a map
    private static final KOTHGameHandler[] activeGames = new KOTHGameHandler[9];

    private boolean isStartingGame;                 // Whether a game is currently starting or not
    private final DelayedTask[] countdownTasks;     // Stores the game start countdown's delayed tasks to allow for start cancellation


    /**
     * Constructor that initiates this area's list of players, the area name, and the KOTH teams
     */
    public KOTHLobbyHandler(MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        areaPlayers = new HashMap<>();          // Create new collection of players for this area
        areaName = "KOTHLobby";
        selectedGamemode = "default";
        selectedMap = new MapCastleOfDreams();
        isStartingGame = false;
        countdownTasks = new DelayedTask[11];
        // Create KOTH teams                ---   needs rework to allow for multiple games
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHRed", " ⧫ ", "Red", null, ChatColor.RED, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHBlue", " ⧫ ", "Blue", null, ChatColor.BLUE, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHGreen", " ⧫ ", "Green", null, ChatColor.GREEN, false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHYellow", " ⧫ ", "Yellow", null, ChatColor.YELLOW, false, true, NameTagVisibility.ALWAYS);
    }

    /**
     * This observer method activates when a player interacts with a "Class Kit" armor
     * stand in the KOTH lobby and assigns the player the respective kit
     */
    @EventHandler
    public void onKitSelect(PlayerInteractAtEntityEvent event) {
        Entity clicked = event.getRightClicked();
        // Hold the player entity
        Player MCPlayer = event.getPlayer();
        // Try to retrieve gamePlayer reference from this areaPlayers
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // Double check that gamePlayer is in KOTH lobby
        if (gamePlayer == null) return;

        //Check for valid click and for an armor stand interaction
        if (clicked.getType() == EntityType.ARMOR_STAND) {
            KOTHPlayer KOTHPlayer = (KOTHPlayer) gamePlayer;
            //Hold the location of armor stand "kit" to be selected
            String kitName = clicked.getName();
            //Check if the player has selected a valid kit entity and selects the specified class
            //Damage kits
            if (kitName.equals("§4Striker")) {       //Striker
                KOTHPlayer.setKit(new KOTHKitStriker(KOTHPlayer));
                MCPlayer.sendTitle(ChatColor.DARK_RED + "Striker", "Deal damage, and take it too");
                MCPlayer.setDisplayName(MCPlayer.getName() + ChatColor.translateAlternateColorCodes('&', " &7<&4&oStriker&7>"));
                MCPlayer.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&', "&7Selected Kit: <&4&oStriker&7>"));
            }
            if (kitName.equals("§4Orc")) {        //Orc

                MCPlayer.sendTitle(ChatColor.DARK_RED + "Orc", "Mean and green with a devastating axe");
            }
            if (kitName.equals("§4Pyro")) {        //Pyro

                MCPlayer.sendTitle(ChatColor.DARK_RED + "Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4Sayain")) {        //Saiyan

                MCPlayer.sendTitle(ChatColor.DARK_RED + "Sorry", "This class is not available yet");
            }
            //Tank kits
            if (kitName.equals("§1Knight")) {        //Kight

                MCPlayer.sendTitle(ChatColor.DARK_BLUE + "Knight", "Try pushing me off now");
            }
            if (kitName.equals("§1TMNT")) {        //TMNT

                MCPlayer.sendTitle(ChatColor.DARK_BLUE + "Sorry", "This class is not available yet");
            }
            if (kitName.equals("§1Trapper")) {        //Trapper

                MCPlayer.sendTitle(ChatColor.DARK_BLUE + "Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4CockNBalls")) {        //Number 4
                MCPlayer.sendTitle(ChatColor.DARK_BLUE + "Sorry", "This class is not available yet");
            }
            //Ranged kits
            if (kitName.equals("§2Archer")) {        //Archer

                MCPlayer.sendTitle(ChatColor.DARK_GREEN + "Archer", "Yeah, I shoot stuff");
            }
            if (kitName.equals("§2Sniper")) {        //Sniper

                MCPlayer.sendTitle(ChatColor.DARK_GREEN + "Sniper", "Lol, get OPed");
            }
            if (kitName.equals("§aIce Spirit")) {        //Ice Spirit

                MCPlayer.sendTitle(ChatColor.DARK_GREEN + "Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4CockNBalls")) {//Number 4
                MCPlayer.sendTitle(ChatColor.DARK_GREEN + "Sorry", "This class is not available yet");
            }
            //Magic kits
            if (kitName.equals("§dWarper")) {        //Warper

                MCPlayer.sendTitle(ChatColor.LIGHT_PURPLE + "Warper", "You're neither here nor there");
            }
            if (kitName.equals("§dClockmaster")) {        //Clock Master

                MCPlayer.sendTitle(ChatColor.LIGHT_PURPLE + "Sorry", "This class is not available yet");
            }
            if (kitName.equals("§dWizard")) {        //Wizard

                MCPlayer.sendTitle(ChatColor.LIGHT_PURPLE + "Sorry", "This class is not available yet");
            }
            if (kitName.equals("§dDruid")) {        //Druid

                MCPlayer.sendTitle(ChatColor.LIGHT_PURPLE + "Sorry", "This class is not available yet");
            }
            //Misc. kits
            if (kitName.equals("§3Fisherman")) {        //Fisherman

                MCPlayer.sendTitle(ChatColor.AQUA + "Fisherman", "Yank and smack, but don't tell your mom");
            }
            if (kitName.equals("§bOcean Man")) {        //Ocean man

                MCPlayer.sendTitle(ChatColor.AQUA + "Sorry", "This class is not available yet");
            }
            if (kitName.equals("§bBird Person")) {        //Bird Person

                MCPlayer.sendTitle(ChatColor.AQUA + "Sorry", "This class is not available yet");
            }
            if (kitName.equals("§bSpider")) {        //Spider Man

                MCPlayer.sendTitle(ChatColor.AQUA + "Sorry", "This class is not available yet");
            }
        }
    }

    //Map Selection


    // Gamemode selection (teams or no teams)

    /**
     * Sets the currently chosen gamemode to the specified gamemode
     *
     * @param selectedGamemode the gamemode to set the current, chosen gamemode to
     */
    public void setSelectedGamemode(String selectedGamemode) {
        this.selectedGamemode = selectedGamemode;
    }

    /**
     * Adds the specified gamePlayer to KOTHLobby's list of players. Also resets
     * the gamePlayer's current area, isInGame, and isGameReady
     *
     * @param gamePlayer the gamePlayer to be added to this area
     */
    @Override
    public void addPlayer(GamePlayer gamePlayer) {
        gamePlayer.setCurrentArea(this);
        gamePlayer.setIsInGame(false);
        gamePlayer.setIsGameReady(false);
        areaPlayers.put(gamePlayer.getPlayer().getName(), new KOTHPlayer(gamePlayer));
    }

    /**
     * If the given gamePlayer is not already in the KOTHLobby, they are removed
     * from their previous area and added to the KOTHLobby's collection of players.
     * This method also initializes a new KOTHLobbyHandler when one doesn't already
     * exist.
     *
     * @param gamePlayer object to be set to KOTHLobby
     */
    public static void sendPlayer(GamePlayer gamePlayer) {
        // If the player's current area is not KOTHLobby...
        if (!(gamePlayer.getCurrentArea().getAreaName().equals("KOTHLobby"))) {
            // Remove player from their current area
            gamePlayer.getCurrentArea().removePlayer(gamePlayer);
            // If a KOTHLobby already exists...
            if (getKOTHLobby() != null)
                // Add the player to that KOTHLobby
                getKOTHLobby().addPlayer(gamePlayer);
                // Otherwise...
            else {
                // Make a new KOTHLobby
                createKOTHLobby();
                // And add the player to the new KOTHLobby
                getKOTHLobby().addPlayer(gamePlayer);
            }
            Player MCPlayer = gamePlayer.getPlayer();
            MCPlayer.setDisplayName(MCPlayer.getName() + ChatColor.translateAlternateColorCodes('&', " &7<&4&oStriker&7>"));
            MCPlayer.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&', "&7Selected Kit: <&4&oStriker&7>"));
        }
    }

    /**
     * If there is at least one player ready in the KOTHLobby, a new KOTHGame
     * is started. All ready gamePlayers are removed from this lobby and put
     * inside the new KOTHGame's collection of players.
     */
    public void startNewGame() {
        // If a game is not already starting...
        if (!isStartingGame) {
            // Start countdown if there is a ready player
            if (getReadyPlayers().size() > 0) {
                // Update boolean
                isStartingGame = true;
                // Alert all players in the lobby that a new game is starting
                for (GamePlayer gamePlayer : this.areaPlayers.values()) {
                    gamePlayer.getPlayer().sendMessage(ChatColor.GREEN + "Starting a new KOTH game...");
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer(), Sound.BLOCK_BEACON_ACTIVATE, 5, 1);
                }

                // Create and store delayed task to ACTUALLY START a new game
                countdownTasks[0] = new DelayedTask(() -> {
                    // Create new KOTHGame
                    activeGames[0] = new KOTHGameHandler((MC_Minigames_Plugin) plugin, getReadyPlayers(), selectedGamemode, selectedMap);        // Change to insert into correct map slot
                    // Remove ready players from this lobby
                    for (GamePlayer gamePlayer : getReadyPlayers())
                        this.areaPlayers.remove(gamePlayer.getPlayer().getName());
                    // Reset boolean
                    isStartingGame = false;
                }, 20 * 11);  // Start after 11 seconds


                // Count down from 10 using delayed tasks
                long delay = 1;     // Separate variable for task delay time (seconds)
                for (int t = 10; t > 0; t--, delay++) {
                    int time = t;     // Stored cause lambda gets angry >:(
                    // Create and store new delayed task...
                    countdownTasks[t] = new DelayedTask(() -> {
                        // Cancel game start if there are no longer queued players
                        if (getReadyPlayers().size() < 1) {
                            cancelNewGame();
                            // Alert players
                            for (GamePlayer gamePlayer : this.areaPlayers.values())
                                gamePlayer.getPlayer().sendMessage(ChatColor.RED + "All players left the queue...");
                        }
                        // Otherwise...
                        else
                            // Display countdown to all ready players
                            for (GamePlayer gamePlayer : this.areaPlayers.values()) {
                                gamePlayer.getPlayer().sendTitle(ChatColor.YELLOW + "" + time, "");
                                gamePlayer.getPlayer().playSound(gamePlayer.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BIT, 5, 1);
                            }
                    }, 20 * delay);
                }
            }
            // If no players are ready
            else
                // Alert lobby players that someone must be ready to start a game
                for (GamePlayer gamePlayer : this.areaPlayers.values())
                    gamePlayer.getPlayer().sendMessage(ChatColor.RED + "Players must enter the queue to start a KOTH game!");
        }
        // If a game is already starting, cancel the currently starting game
        else
            cancelNewGame();
    }

    /**
     * Cancels the currently starting KOTH game, updating necessary fields, cancelling tasks,
     * and alerting lobby players.
     */
    public void cancelNewGame() {
        // Update boolean
        isStartingGame = false;
        // Cancel running delayed tasks
        for (DelayedTask task : countdownTasks)
            Bukkit.getScheduler().cancelTask(task.getId());
        // Alert all players that the game was cancelled
        for (GamePlayer gamePlayer : this.areaPlayers.values()) {
            gamePlayer.getPlayer().sendMessage(ChatColor.RED + "The starting KOTH game was cancelled");
            gamePlayer.getPlayer().clearTitle();    // Clear countdown title
            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer(), Sound.BLOCK_BEACON_DEACTIVATE, 5, 1);
        }
    }

    /**
     * Handles lobby hot bar menu items
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Setup
        Player MCPlayer = event.getPlayer();
        // Try to retrieve gamePlayer reference from this areaPlayers
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // Double check that gamePlayer is in KOTH lobby
        if (gamePlayer == null) return;
        Inventory inv = MCPlayer.getInventory();

        // For all players in the KOTH Lobby...
        // Detect when player right-clicks
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            // START/CANCEL NEW GAME BUTTON
            // Detect when player right-clicks a block
            if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && MCPlayer.getItemInHand().getItemMeta() == null) {
                // Define button location
                Location KOTHStartButtonLoc = new Location(Bukkit.getWorld("world"), 8, -59, -631);
                // Detect click on button
                if (event.getClickedBlock().getLocation().equals(KOTHStartButtonLoc)) {
                    // If a game is not already starting
                    startNewGame();
                }
            }

            // ITEM FUNCTIONALITY
            // Detect click with an item
            if (MCPlayer.getItemInHand().getItemMeta() != null) {
                // QUEUE ITEM
                // Detect click with KOTH queue item
                if (MCPlayer.getItemInHand().getItemMeta().getDisplayName().equals(KOTHQueue.getItemMeta().getDisplayName())) {
                    new DelayedTask(() -> {
                        // Queue player
                        gamePlayer.setIsGameReady(true);
                        // Give glowing effect
                        MCPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true, false, false));
                        // Switch to dequeue item
                        inv.setItem(0, KOTHDequeue);
                        // Play sound
                        MCPlayer.playSound(MCPlayer, Sound.BLOCK_NOTE_BLOCK_CHIME, 5, 1.5f);
                    }, 3);
                }
                // Detect click with KOTH dequeue item
                else if (MCPlayer.getItemInHand().getItemMeta().getDisplayName().equals(KOTHDequeue.getItemMeta().getDisplayName())) {
                    new DelayedTask(() -> {
                        // Dequeue player
                        gamePlayer.setIsGameReady(false);
                        // Clear glowing potion effects
                        Collection<PotionEffect> effectsToClear = MCPlayer.getActivePotionEffects();
                        for (PotionEffect pE : effectsToClear)
                            MCPlayer.removePotionEffect(pE.getType());
                        // Switch to queue item
                        inv.setItem(0, KOTHQueue);
                        // Play sound
                        MCPlayer.playSound(MCPlayer, Sound.BLOCK_NOTE_BLOCK_COW_BELL, 5, .8f);
                    }, 3);
                }

                // TEAM SELECTOR
                // Detect click with KOTH team selector (none)
                else if (MCPlayer.getItemInHand().getItemMeta().getDisplayName().equals(KOTHTeamNone.getItemMeta().getDisplayName())) {
                    new DelayedTask(() -> {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("KOTHRed").addPlayer(MCPlayer);
                        // Switch to next item
                        inv.setItem(2, KOTHTeamRed);
                        // Play sound
                        MCPlayer.playSound(MCPlayer, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }, 3);
                }
                // Detect click with KOTH team selector (red)
                else if (MCPlayer.getItemInHand().getItemMeta().getDisplayName().equals(KOTHTeamRed.getItemMeta().getDisplayName())) {
                    new DelayedTask(() -> {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("KOTHBlue").addPlayer(MCPlayer);
                        // Switch to next item
                        inv.setItem(2, KOTHTeamBlue);
                        // Play sound
                        MCPlayer.playSound(MCPlayer, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }, 3);
                }
                // Detect click with KOTH team selector (blue)
                else if (MCPlayer.getItemInHand().getItemMeta().getDisplayName().equals(KOTHTeamBlue.getItemMeta().getDisplayName())) {
                    new DelayedTask(() -> {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("KOTHGreen").addPlayer(MCPlayer);
                        // Switch to next item
                        inv.setItem(2, KOTHTeamGreen);
                        // Play sound
                        MCPlayer.playSound(MCPlayer, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }, 3);
                }
                // Detect click with KOTH team selector (green)
                else if (MCPlayer.getItemInHand().getItemMeta().getDisplayName().equals(KOTHTeamGreen.getItemMeta().getDisplayName())) {
                    new DelayedTask(() -> {
                        // Put player on red team
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("KOTHYellow").addPlayer(MCPlayer);
                        // Switch to next item
                        inv.setItem(2, KOTHTeamYellow);
                        // Play sound
                        MCPlayer.playSound(MCPlayer, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }, 3);
                }
                // Detect click with KOTH team selector (yellow)
                else if (MCPlayer.getItemInHand().getItemMeta().getDisplayName().equals(KOTHTeamYellow.getItemMeta().getDisplayName())) {
                    new DelayedTask(() -> {
                        // Put player on red team
                        Tools.resetTeam(MCPlayer);
                        // Switch to next item
                        inv.setItem(2, KOTHTeamNone);
                        // Play sound
                        MCPlayer.playSound(MCPlayer, Sound.ITEM_ARMOR_EQUIP_LEATHER, 5, 1);
                    }, 3);
                }
            }
        }
    }

    /**
     * Returns a list of the KOTHLobby's players that are ready/queued
     */
    public ArrayList<GamePlayer> getReadyPlayers() {
        ArrayList<GamePlayer> readyPlayers = new ArrayList<>();
        for (GamePlayer gamePlayer : this.areaPlayers.values())
            if (gamePlayer.isGameReady())
                readyPlayers.add(gamePlayer);
        return readyPlayers;
    }

    /**
     * Returns the KOTHLobby's array of active games
     */
    public static KOTHGameHandler[] getActiveGames() {
        return activeGames;
    }

    /**
     * Provides functionality for portal returning players to main hub from KOTH lobby
     */
    @EventHandler
    public void returnPortal(PlayerMoveEvent event) {
        Player MCPlayer = event.getPlayer();
        // Try to retrieve gamePlayer reference from this areaPlayers
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // Double check that gamePlayer is in KOTH lobby
        if (gamePlayer == null) return;
        // Detect players in portal range and in KOTH lobby
        if (event.getTo().getY() < -56 && event.getTo().getY() > -63 &&
                event.getTo().getZ() < -594 && event.getTo().getZ() > -595 &&
                event.getTo().getX() < 11 && event.getTo().getX() > 5) {

            // If not troubleshooting...
            if (!(gamePlayer.isTroubleshooting()))
                // Prevent lobby item glitches
                MCPlayer.getInventory().clear();

            // Transport player to main hub
            MCPlayer.teleport(MainHubHandler.getLocation());       // Prevents duplicate sends
            MCPlayer.clearTitle();                      // Clear countdown title
            GeneralLobbyHandler.sendMainHub(gamePlayer);
        }
    }

    /**
     * Accessor method that returns the PlayerArea's location
     */
    public static Location getLocation() {
        return location;
    }

}

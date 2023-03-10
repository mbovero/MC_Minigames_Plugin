package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits.KitStriker;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import mc_minigames_plugin.mc_minigames_plugin.util.Locations;
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

import static mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler.*;
import static mc_minigames_plugin.mc_minigames_plugin.util.Tools.createItem;

/**
 *Class Description: Holds players in lobby and handles run time events for those players
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

    private String selectedGamemode;              // The KOTH gamemode that is currently selected
    private Location selectedMap;                 // The KOTH map that is currently selected
    private KOTHGameHandler[] activeGames;        // A list of the currently running KOTH games

    /**
     * Constructor that initiates this area's list of players, the area name, and the KOTH teams
     */
    public KOTHLobbyHandler (MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        // Create new list of players for this area
        areaPlayers = new ArrayList<>();
        areaName = "KOTHLobby";
        selectedGamemode = "default";
        selectedMap = Locations.KOTHCastleOfDreams;
        activeGames = new KOTHGameHandler[9];       // An array of the possible active games, each index correlates to a map
        // Create KOTH teams
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHRed", " ⧫ ", "Red", null, ChatColor.RED,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHBlue", " ⧫ ", "Blue", null, ChatColor.BLUE,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHGreen", " ⧫ ", "Green", null, ChatColor.GREEN,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHYellow", " ⧫ ", "Yellow", null, ChatColor.YELLOW,false, true, NameTagVisibility.ALWAYS);
    }

    /**
     * This observer method activates when a player interacts with a "Class Kit" armor
     * stand in the KOTH lobby and assigns the player the respective kit
     * @param event
     */
    @EventHandler
    public void onKitSelect (PlayerInteractAtEntityEvent event) {
        Entity clicked = event.getRightClicked();
        // Hold the player entity
        Player MCPlayer = event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // Find gamePlayer's area
        String currentArea = gamePlayer.getCurrentArea().getAreaName();

        //Check for valid click and for an armor stand interaction
        if (clicked.getType() == EntityType.ARMOR_STAND && currentArea.equals("KOTHLobby")) {
            KOTHPlayer KOTHPlayer = (KOTHPlayer)gamePlayer;
            //Hold the location of armor stand "kit" to be selected
            String kitName = clicked.getName();
            //Check if the player has selected a valid kit entity and selects the specified class
                //Damage kits
            if (kitName.equals("§4Striker"))  {       //Striker
                changeKitName(MCPlayer, "KOTH_kit_Striker");
                KOTHPlayer.setKit(new KitStriker(KOTHPlayer));
                MCPlayer.sendTitle("Striker", "Deal damage, and take it too");
            }
            if (kitName.equals("§4Orc")) {        //Orc
                changeKitName(MCPlayer, "KOTH_kit_Striker");
                MCPlayer.sendTitle("Orc", "Mean and green with a devastating axe");
            }
            if (kitName.equals("§4Pyro")) {        //Pyro
                changeKitName(MCPlayer, "KOTH_kit_Pyro");
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4Sayain")) {        //Saiyan
                changeKitName(MCPlayer, "KOTH_kit_Saiyan");
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
                //Tank kits
            if (kitName.equals("§1Knight")) {        //Kight
                changeKitName(MCPlayer, "KOTH_kit_Tank");
                MCPlayer.sendTitle("Knight", "Try pushing me off now");
            }
            if (kitName.equals("§1TMNT")) {        //TMNT
                changeKitName(MCPlayer, "KOTH_kit_TMNT");
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§1Trapper")) {        //Trapper
                changeKitName(MCPlayer, "KOTH_kit_Trapper ");
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4CockNBalls")) {        //Number 4
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
                //Ranged kits
            if (kitName.equals("§2Archer")) {        //Archer
                changeKitName(MCPlayer, "KOTH_kit_Archer");
                MCPlayer.sendTitle("Archer", "Yeah, I shoot stuff");
            }
            if (kitName.equals("§2Sniper")) {        //Sniper
                changeKitName(MCPlayer, "KOTH_kit_Sniper");
                MCPlayer.sendTitle("Sniper", "Lol, get OPed");
            }
            if (kitName.equals("§aIce Spirit")) {        //Ice Spirit
                changeKitName(MCPlayer, "KOTH_kit_IceSpirit");
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4CockNBalls")) {//Number 4
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
                //Magic kits
            if (kitName.equals("§dWarper")) {        //Warper
               changeKitName(MCPlayer, "KOTH_kit_Warper");
                MCPlayer.sendTitle("Warper", "You're neither here nor there");
            }
            if (kitName.equals("§dClockmaster")) {        //Clock Master
                changeKitName(MCPlayer, "KOTH_kit_ClockMaster");
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§dwizard")) {        //Wizard
                changeKitName(MCPlayer, "KOTH_kit_Wizard");
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§dDruid")) {        //Druid
                changeKitName(MCPlayer, "KOTH_kit_Druid");
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
                //Misc. kits
            if (kitName.equals("§3Fisherman")) {        //Fisherman
                changeKitName(MCPlayer, "KOTH_kit_Fisherman");
                MCPlayer.sendTitle("Sorry", "Yank and smack, but don't tell your mom");
            }
            if (kitName.equals("§bOcean Man")) {        //Ocean man
                changeKitName(MCPlayer, "KOTH_kit_OceanMan");
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§bBird Person")) {        //Bird Person
                changeKitName(MCPlayer, "KOTH_kit_BirdPerson");
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§bSpider")) {        //Spider Man
                changeKitName(MCPlayer, "KOTH_kit_SpiderMan");
                MCPlayer.sendTitle("Sorry", "This class is not available yet");
            }
        }
    }

    /**
     * Method to change the player's current kit into the selected kit
     * @param MCPlayer
     * @param kit
     */
    public void changeKitName(Player MCPlayer, String kit) {
        //Iterate through game players
        for (GamePlayer gamePlayer : areaPlayers) {
            //Check if the current player matches the list reference player
            if (gamePlayer.isPlayer(MCPlayer)) {
                //Type cast the list reference player into a KOTHPlayer
                KOTHPlayer referenceKOTH = (KOTHPlayer) gamePlayer;
                //Remove the currently held kit from the mcPlayer
                MCPlayer.removeScoreboardTag(referenceKOTH.getKitName());
                //Change the currently held kit from the KOTHPlayer to the new kit
                referenceKOTH.changePlayerKitName(kit);
                //Add the new kit tag to the mcPlayer
                MCPlayer.addScoreboardTag(kit);
                //Stop iterating
                break;
            }
        }
    }

    //Map Selection


    // Gamemode selection (teams or no teams)

    /**
     * Sets the currently chosen gamemode to the specified gamemode
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
        areaPlayers.add(new KOTHPlayer(gamePlayer));
    }

    /**
     * If the given gamePlayer is not already in the KOTHLobby, they are
     * removed from their previous area and placed in the list of KOTHLobby players.
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
        }
    }

    /**
     * If there is at least one player ready in the KOTHLobby, a new KOTHGame
     * is started. All ready gamePlayers are removed from this lobby and put
     * inside the KOTHGame's list of players.
     */
    public void startNewGame() {
        // Get list of ready players
        ArrayList<GamePlayer> readyPlayers = getReadyPlayers();
        // Start the game if there is a ready player
        if (readyPlayers.size() > 0) {
            for (GamePlayer gamePlayer : this.areaPlayers)
                gamePlayer.getPlayer().sendMessage("Starting a new KOTH game...");
            // Create new KOTHGame
            this.activeGames[0] = new KOTHGameHandler((MC_Minigames_Plugin) plugin, readyPlayers, selectedGamemode, selectedMap);        // Change to insert into correct map slot
            // Remove ready players from this lobby
            this.areaPlayers.removeAll(readyPlayers);
        }

    }

    /**
     * Handles lobby hot bar menu items
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Setup
        Player MCPlayer = event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // Find gamePlayer's area
        String currentArea = gamePlayer.getCurrentArea().getAreaName();
        Inventory inv = MCPlayer.getInventory();

        // For all players in the KOTH Lobby...
        if (currentArea.equals("KOTHLobby")) {

            // Detect when player right-clicks
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                // START BUTTON
                // Detect when player right-clicks a block
                if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && MCPlayer.getItemInHand().getItemMeta() == null) {
                    // Define button location
                    Location KOTHStartButtonLoc = new Location(Bukkit.getWorld("world"), 8, -59, -631);
                    // Detect click on button
                    if (event.getClickedBlock().getLocation().equals(KOTHStartButtonLoc)) {
                        // Create new KOTH game
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
    }

    /**
     * Returns a list of the KOTHLobby's players that are ready/queued
     */
    public ArrayList<GamePlayer> getReadyPlayers() {
        ArrayList<GamePlayer> readyPlayers = new ArrayList<>();
        for (GamePlayer gamePlayer : this.areaPlayers)
            if (gamePlayer.isGameReady())
                readyPlayers.add(gamePlayer);
        return readyPlayers;
    }

    /**
     * Returns the KOTHLobby's array of active games
     */
    public KOTHGameHandler[] getActiveGames() {
        return activeGames;
    }

    /**
     * Provides functionality for portal returning players to main hub from KOTH lobby
     */
    @EventHandler
    public void returnPortal(PlayerMoveEvent event) {
        Player MCPlayer = event.getPlayer();
        // Find the gamePlayer matching with the event's MCPlayer
        GamePlayer gamePlayer = findPlayer(MCPlayer);
        // Find gamePlayer's area
        String currentArea = gamePlayer.getCurrentArea().getAreaName();
        // Detect players in portal range and in KOTH lobby
        if (event.getTo().getY() < -56 && event.getTo().getY() > -63 &&
                event.getTo().getZ() < -594 && event.getTo().getZ() > -595 &&
                event.getTo().getX() < 11 && event.getTo().getX() > 5 &&
                currentArea.equals("KOTHLobby")) {

            // If not troubleshooting...
            if (!(gamePlayer.isTroubleshooting()))
                // Prevent lobby item glitches
                MCPlayer.getInventory().clear();

            // Transport player to main hub
            MCPlayer.teleport(Locations.mainHub);       // Prevents duplicate sends
            GeneralLobbyHandler.sendMainHub(gamePlayer);
        }
    }
}

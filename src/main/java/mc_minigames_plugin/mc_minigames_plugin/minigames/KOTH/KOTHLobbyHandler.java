package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
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
import org.checkerframework.checker.units.qual.K;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static mc_minigames_plugin.mc_minigames_plugin.util.Tools.createItem;

/**
 *Class Description: Holds players in lobby and handles run time events for those players
 *
 * @author Kirt Robinson
 * @version March 5, 2023
 */
public class KOTHLobbyHandler extends PlayerArea implements Listener {

// ITEMS ---------------------------------------------------------------------------------------------------------------
    // Lobby selector tool
    static ItemStack lobbySelector = createItem(new ItemStack(Material.COMPASS), "&aLobby Selector", "&fExplore our selection of games!");

    // KOTH lobby hot bar menu items
    static ItemStack KOTHQueue = createItem(new ItemStack(Material.GRAY_DYE), "&7Unready", "&fClick with this item to enter the KOTH queue!");
    static ItemStack KOTHDequeue = createItem(new ItemStack(Material.LIME_DYE), "&aReady", "&fClick with this item to leave the KOTH queue");

    static ItemStack KOTHTeamNone = createItem(new ItemStack(Material.LIGHT_GRAY_WOOL), "&7No Team", "&fClick with this item to change KOTH teams!");
    static ItemStack KOTHTeamRed = createItem(new ItemStack(Material.RED_WOOL), "&4Red Team", "&fClick with this item to change KOTH teams!");
    static ItemStack KOTHTeamBlue = createItem(new ItemStack(Material.BLUE_WOOL), "&1Blue Team", "&fClick with this item to change KOTH teams!");
    static ItemStack KOTHTeamGreen = createItem(new ItemStack(Material.LIME_WOOL), "&2Green Team", "&fClick with this item to change KOTH teams!");
    static ItemStack KOTHTeamYellow = createItem(new ItemStack(Material.YELLOW_WOOL), "&eYellow Team", "&fClick with this item to change KOTH teams!");
// ---------------------------------------------------------------------------------------------------------------------


    public KOTHLobbyHandler (MC_Minigames_Plugin plugin, Player player) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        //Add player to start of player list
        areaPlayers = new ArrayList<>();
        areaPlayers.add(new KOTHPlayer(player));
        // Create KOTH teams
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHRed", " ⧫ ", "Red", null, ChatColor.RED,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHBlue", " ⧫ ", "Blue", null, ChatColor.BLUE,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHGreen", " ⧫ ", "Green", null, ChatColor.GREEN,false, true, NameTagVisibility.ALWAYS);
        Tools.newTeam(Bukkit.getScoreboardManager().getMainScoreboard(), "KOTHYellow", " ⧫ ", "Yellow", null, ChatColor.YELLOW,false, true, NameTagVisibility.ALWAYS);
        }

    //KOTH LOBBY FEATURES-----------------------------------------------------------------------------------------------

    /**
     * Method adds a new KOTPlayer to the list of current KOTHPlayers
     * @param mcPlayer a reference to a minecraft players
     */
    public void addPlayer (Player mcPlayer) {areaPlayers.add(new KOTHPlayer(mcPlayer));}

    /**
     * This observer method activates when a player interacts with a "Class Kit" armor
     * stand in the KOTH lobby and assigns the player the respective kit
     * @param event
     */
    @EventHandler
    public void onKitSelect (PlayerInteractAtEntityEvent event) {
        Entity clicked = event.getRightClicked();
        //Hold the player entity
        Player player = event.getPlayer();
        Set<String> tags = event.getPlayer().getScoreboardTags();
        //Check for valid click and for an armor stand interaction
        if (clicked.getType() == EntityType.ARMOR_STAND && tags.contains("KOTHLobby")) {
            //Hold the location of armor stand "kit" to be selected
            String kitName = clicked.getName();
            //Check if the player has selected a valid kit entity and selects the specified class
                //Damage kits
            if (kitName.equals("§4Striker"))  {       //Striker
                changeKit(player, "KOTH_kit_Striker");
                player.sendTitle("Striker", "Deal damage, and take it too");
            }
            if (kitName.equals("§4Orc")) {        //Orc
                changeKit(player, "KOTH_kit_Striker");
                player.sendTitle("Orc", "Mean and green with a devastating axe");
            }
            if (kitName.equals("§4Pyro")) {        //Pyro
                changeKit(player, "KOTH_kit_Pyro");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4Sayain")) {        //Saiyan
                changeKit(player, "KOTH_kit_Saiyan");
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Tank kits
            if (kitName.equals("§1Knight")) {        //Kight
                changeKit(player, "KOTH_kit_Tank");
                player.sendTitle("Knight", "Try pushing me off now");
            }
            if (kitName.equals("§1TMNT")) {        //TMNT
                changeKit(player, "KOTH_kit_TMNT");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§1Trapper")) {        //Trapper
                changeKit(player, "KOTH_kit_Trapper ");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4CockNBalls")) {        //Number 4
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Ranged kits
            if (kitName.equals("§2Archer")) {        //Archer
                changeKit(player, "KOTH_kit_Archer");
                player.sendTitle("Archer", "Yeah, I shoot stuff");
            }
            if (kitName.equals("§2Sniper")) {        //Sniper
                changeKit(player, "KOTH_kit_Sniper");
                player.sendTitle("Sniper", "Lol, get OPed");
            }
            if (kitName.equals("§aIce Spirit")) {        //Ice Spirit
                changeKit(player, "KOTH_kit_IceSpirit");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4CockNBalls")) {//Number 4
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Magic kits
            if (kitName.equals("§dWarper")) {        //Warper
               changeKit(player, "KOTH_kit_Warper");
                player.sendTitle("Warper", "You're neither here nor there");
            }
            if (kitName.equals("§dClockmaster")) {        //Clock Master
                changeKit(player, "KOTH_kit_ClockMaster");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§dwizard")) {        //Wizard
                changeKit(player, "KOTH_kit_Wizard");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§dDruid")) {        //Druid
                changeKit(player, "KOTH_kit_Druid");
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Misc. kits
            if (kitName.equals("§3Fisherman")) {        //Fisherman
                changeKit(player, "KOTH_kit_Fisherman");
                player.sendTitle("Sorry", "Yank and smack, but don't tell your mom");
            }
            if (kitName.equals("§bOcean Man")) {        //Ocean man
                changeKit(player, "KOTH_kit_OceanMan");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§bBird Person")) {        //Bird Person
                changeKit(player, "KOTH_kit_BirdPerson");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§bSpider")) {        //Spider Man
                changeKit(player, "KOTH_kit_SpiderMan");
                player.sendTitle("Sorry", "This class is not available yet");
            }
        }
    }

    /**
     * Method to change the player's current kit into the selected kit
     * @param player
     * @param kit
     */
    public void changeKit (Player player, String kit) {
        //Iterate through game players
        for (GamePlayer reference : areaPlayers) {
            //Check if the current player matches the list reference player
            if (reference.getPlayer().getDisplayName().equals(player.getDisplayName())) {
                //Type cast the list reference player into a KOTHPlayer
                KOTHPlayer referenceKOTH = (KOTHPlayer) reference;
                //Remove the currently held kit from the mcPlayer
                player.removeScoreboardTag(referenceKOTH.getPlayerKit());
                //Change the currently held kit from the KOTHPlayer to the new kit
                referenceKOTH.changePlayerKit(kit);
                //Add the new kit tag to the mcPlayer
                player.addScoreboardTag(kit);
                //Stop iterating
                break;
            }
        }
    }

    //Map Selection


    //Gamemode selection (teams or no teams)

    //Team Selection

    //Enter Que/Ready up

    /**
     * Handles lobby hot bar menu items
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
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
                        // Give glowing effect
                        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,2147483647, 1, true, false, false));
                        // Switch to dequeue item
                        inv.setItem(0, KOTHDequeue);
                        // Play sound
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_CHIME, 5, 1.5f);
                    }
                    // Detect click with KOTH dequeue item
                    else if (player.getItemInHand().getItemMeta().getDisplayName().equals(KOTHDequeue.getItemMeta().getDisplayName())) {
                        // Dequeue player
                        player.removeScoreboardTag("KOTHQueued");
                        // Clear glowing potion effects
                        Collection<PotionEffect> effectsToClear = player.getActivePotionEffects();
                        for (PotionEffect pE : effectsToClear)
                            player.removePotionEffect(pE.getType());
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
    }

    /**
     * Provides functionality for portal returning players to main hub from KOTH lobby
     */
    @EventHandler
    public void returnPortal(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Set<String> tags = player.getScoreboardTags();
        // Detect players in portal range and in KOTH lobby
        if (event.getTo().getY() < -56 && event.getTo().getY() > -63 &&
                event.getTo().getZ() < -594 && event.getTo().getZ() > -595 &&
                event.getTo().getX() < 11 && event.getTo().getX() > 5 &&
                tags.contains("KOTHLobby")) {
            // Play portal sound at KOTH lobby to surrounding players
            for (Player p : Bukkit.getOnlinePlayers())
                if (p.getScoreboardTags().contains("notInGame"))
                    p.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, .8f);
            // Transport player to main hub
            GeneralLobbyHandler.sendMainHub(player);
            // Play portal sound at main hub to surrounding players
            for (Player p : Bukkit.getOnlinePlayers())
                if (p.getScoreboardTags().contains("notInGame"))
                    p.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, .8f);
        }
    }
}

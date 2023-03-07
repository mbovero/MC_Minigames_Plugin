package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.scoreboard.NameTagVisibility;
import org.checkerframework.checker.units.qual.K;

import java.util.ArrayList;
import java.util.Set;

/**
 *Class Description: Holds players in lobby and handles run time events for those players
 *
 * @author Kirt Robinson
 * @version March 5, 2023
 */
public class KOTHLobbyHandler extends PlayerArea implements Listener {



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
    public void onKitSelection (PlayerInteractAtEntityEvent event) {
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
                player.addScoreboardTag("KOTH_kit_Striker");
                player.sendTitle("Striker", "Deal damage, and take it too");
            }
            if (kitName.equals("§4Orc")) {        //Orc
                player.addScoreboardTag("KOTH_kit_Striker");
                player.sendTitle("Orc", "Mean and green with a devastating axe");
            }
            if (kitName.equals("§4Pyro")) {        //Pyro
                player.addScoreboardTag("KOTH_kit_Pyro");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4Sayain")) {        //Saiyan
                player.addScoreboardTag("KOTH_kit_Saiyan");
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Tank kits
            if (kitName.equals("§1Knight")) {        //Kight
                player.addScoreboardTag("KOTH_kit_Tank");
                player.sendTitle("Knight", "Try pushing me off now");
            }
            if (kitName.equals("§1TMNT")) {        //TMNT
                player.addScoreboardTag("KOTH_kit_TMNT");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§1Trapper")) {        //Trapper
                player.addScoreboardTag("KOTH_kit_Trapper ");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4CockNBalls")) {        //Number 4
                player.sendTitle("Sorry", "This class is not available yet");
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Ranged kits
            if (kitName.equals("§2Archer")) {        //Archer
                player.addScoreboardTag("KOTH_kit_Archer");
                player.sendTitle("Archer", "Yeah, I shoot stuff");
            }
            if (kitName.equals("§2Sniper")) {        //Sniper
                player.addScoreboardTag("KOTH_kit_Sniper");
                player.sendTitle("Sniper", "Lol, get OPed");
            }
            if (kitName.equals("§aIce Spirit")) {        //Ice Spirit
                player.addScoreboardTag("KOTH_kit_IceSpirit");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§4CockNBalls")) {//Number 4
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Magic kits
            if (kitName.equals("§dWarper")) {        //Warper
                player.addScoreboardTag("KOTH_kit_Warper");
                player.sendTitle("Warper", "You're neither here nor there");
            }
            if (kitName.equals("§dClockmaster")) {        //Clock Master
                player.addScoreboardTag("KOTH_kit_ClockMaster");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§dwizard")) {        //Wizard
                player.addScoreboardTag("KOTH_kit_Wizard");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§dDruid")) {        //Druid
                player.addScoreboardTag("KOTH_kit_Druid");
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Misc. kits
            if (kitName.equals("§3Fisherman")) {        //Fisherman
                player.addScoreboardTag("KOTH_kit_Fisherman");
                player.sendTitle("Sorry", "Yank and smack, but don't tell your mom");
            }
            if (kitName.equals("§bOcean Man")) {        //Ocean man
                player.addScoreboardTag("KOTH_kit_OceanMan");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§bBird Person")) {        //Bird Person
                player.addScoreboardTag("KOTH_kit_BirdPerson");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kitName.equals("§bSpider")) {        //Spider Man
                player.addScoreboardTag("KOTH_kit_SpiderMan");
                player.sendTitle("Sorry", "This class is not available yet");
            }
        }
    }
    public void changeKit (Player player, String kit) {

        player.addScoreboardTag(kit);
    }

    //Map Selection


    //Gamemode selection (teams or no teams)

    //Team Selection

    //Enter Que/Ready up


}

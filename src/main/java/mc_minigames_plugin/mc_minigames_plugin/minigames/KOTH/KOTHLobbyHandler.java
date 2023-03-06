package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

/**
 *Class Description: Holds players in lobby and handles run time events for those players
 *
 * @author Kirt Robinson
 * @version March 5, 2023
 */
public class KOTHLobbyHandler extends PlayerArea implements Listener {
    public KOTHLobbyHandler (MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //KOTH LOBBY FEATURES-----------------------------------------------------------------------------------------------
    /**
     * This observer method activates when a player interacts with a "Class Kit" armor
     * stand in the KOTH lobby and assigns the player the respective kit
     * @param event
     */
    public void onKitSelection (PlayerInteractAtEntityEvent event) {
        Entity clicked = event.getRightClicked();
        //Check for valid click and for an armor stand interaction
        if (clicked.getType() == EntityType.ARMOR_STAND) {
            //Hold the player entity
            Player player = event.getPlayer();
            //Hold the armor stand "class kit" to be selected
            Location kothKit = clicked.getLocation();
            //Check if the player has selected a valid kit entity and selects the specified class
                //Damage kits
            if (kothKit.getX() == 1 && kothKit.getZ() == -603)  {       //Striker
                player.addScoreboardTag("KOTH_kit_Striker");
                player.sendTitle("Striker", "Deal damage and take it too");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -599) {        //Orc
                player.addScoreboardTag("KOTH_kit_Striker");
                player.sendTitle("Orc", "Mean and green with a devastating axe");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -595) {        //Pyro
                player.addScoreboardTag("KOTH_kit_Pyro");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -591) {        //Saiyan
                player.addScoreboardTag("KOTH_kit_Saiyan");
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Tank kits
            if (kothKit.getX() == 5 && kothKit.getZ() == -603) {        //Kight
                player.addScoreboardTag("KOTH_kit_Tank");
                player.sendTitle("Knight", "Try pushing me off now");
            }
            if (kothKit.getX() == 5 && kothKit.getZ() == -599) {        //TMNT
                player.addScoreboardTag("KOTH_kit_TMNT");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kothKit.getX() == 5 && kothKit.getZ() == -595) {        //Trapper
                player.addScoreboardTag("KOTH_kit_Trapper ");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kothKit.getX() == 5 && kothKit.getZ() == -591) {        //Number 4
                player.sendTitle("Sorry", "This class is not available yet");
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Ranged kits
            if (kothKit.getX() == 1 && kothKit.getZ() == -603) {        //Archer
                player.addScoreboardTag("KOTH_kit_Archer");
                player.sendTitle("Archer", "Yeah, I shoot stuff");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -599) {        //Sniper
                player.addScoreboardTag("KOTH_kit_Sniper");
                player.sendTitle("Sniper", "Lol, get OPed");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -595) {        //Ice Spirit
                player.addScoreboardTag("KOTH_kit_IceSpirit");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -591) {//Number 4
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Magic kits
            if (kothKit.getX() == 1 && kothKit.getZ() == -603) {        //Warper
                player.addScoreboardTag("KOTH_kit_Warper");
                player.sendTitle("Warper", "You're neither here or there");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -599) {        //Clock Master
                player.addScoreboardTag("KOTH_kit_ClockMaster");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -595) {        //Wizard
                player.addScoreboardTag("KOTH_kit_Wizard");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -591) {        //Druid
                player.addScoreboardTag("KOTH_kit_Druid");
                player.sendTitle("Sorry", "This class is not available yet");
            }
                //Misc. kits
            if (kothKit.getX() == 1 && kothKit.getZ() == -603) {        //Fisherman
                player.addScoreboardTag("KOTH_kit_Fisherman");
                player.sendTitle("Sorry", "Yank and smack, but don't tell your mom");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -599) {        //Ocean man
                player.addScoreboardTag("KOTH_kit_OceanMan");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -595) {        //Bird Person
                player.addScoreboardTag("KOTH_kit_BirdPerson");
                player.sendTitle("Sorry", "This class is not available yet");
            }
            if (kothKit.getX() == 1 && kothKit.getZ() == -591) {        //Spider Man
                player.addScoreboardTag("KOTH_kit_SpiderMan");
                player.sendTitle("Sorry", "This class is not available yet");
            }
        }
    }

    //Map Selection


    //Gamemode selection (teams or no teams)

    //Team Selection

    //Enter Que/Ready up


}

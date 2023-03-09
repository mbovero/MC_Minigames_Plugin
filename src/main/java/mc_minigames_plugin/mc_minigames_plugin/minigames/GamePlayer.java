package mc_minigames_plugin.mc_minigames_plugin.minigames;

import mc_minigames_plugin.mc_minigames_plugin.util.TroubleshootUtil;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Class represents the abstract blueprint of a player object for a game to be tracked and managed
 * @author Kirt Robinson
 * @version March 6, 2023
 */
abstract public class GamePlayer {
    protected boolean gameReady; //identifies if the player is ready for the game to start

    protected boolean isTroubleShooting;      // Whether the player is troubleshooting or not
    protected boolean isInGame;               // Whether the player is in a game or not

    protected Player MCPlayer;

    protected PlayerArea currentArea;   // The area this player is currently in

    public GamePlayer (Player MCPlayer, PlayerArea currentArea) {
        this.MCPlayer = MCPlayer;
        this.currentArea = currentArea;
        // Set isTroubleShooting to false by default
        this.isTroubleShooting = false;
        // Iterate through stored troubleshooters config if not null
        List<String> troubleshooters = TroubleshootUtil.getTroubleshooters();
        if (troubleshooters != null)
            for (String troubleshooter : troubleshooters) {
                // If the player is found in the list, update their isTroubleShooting
                if (troubleshooter.equals(MCPlayer.getName())) {
                    this.isTroubleShooting = true;
                    break;
                }
            }
        this.isInGame = false;
        gameReady = false;
    }


    /**
     * Method compares a given minecraft player reference to this GamePlayer object and
     * returns the boolean value of that comparison.
     * @param MCPlayer
     * @return
     */
    public boolean isPlayer (Player MCPlayer) {
        return this.MCPlayer.getName().equals(MCPlayer.getName());
    }

    //Accessors --------------------------------------------------------------------------------------------------------

    /**
     * Accessor method for returning the gameReady value of this GamePlayer
     * @return boolean value for game readiness
     */
    public boolean isGameReady () {return gameReady;}

    /**
     * Accessor method for returning the current reference of a minecraft player
     * @return
     */
    public Player getPlayer () {return MCPlayer;}

    /**
     * Accessor method for returning the troubleShooting value of this GamePlayer
     * @return whether the player is troubleshooting or not.
     */
    public boolean isTroubleShooting() {
        return isTroubleShooting;
    }



    /**
     * Accessor method for returning the inGame value of this GamePlayer
     * @return whether the player is in a game or not.
     */
    public boolean isInGame() {
        return isInGame;
    }

    /**
     * Accessor method for returning the currentArea of this GamePlayer.
     * @return GamePlayer's currentArea
     */
    public PlayerArea getCurrentArea() {return currentArea;}

    //Mutators --------------------------------------------------------------------------------------------------------

    /**
     * Mutator method for changing the isInGame value of this GamePlayer
     */
    public void setIsInGame(boolean isInGame) {
        this.isInGame = isInGame;
    }

    /**
     * Mutator method for changing the troubleShooting value of this GamePlayer
     */
    public void setTroubleShooting(boolean isTroubleshooting) {
        this.isTroubleShooting = isTroubleshooting;
    }

    /**
     * Mutator method that changes the currentArea value of this GamePlayer
     * @param newArea
     */
    public void setCurrentArea (PlayerArea newArea) {this.currentArea = newArea;}
}

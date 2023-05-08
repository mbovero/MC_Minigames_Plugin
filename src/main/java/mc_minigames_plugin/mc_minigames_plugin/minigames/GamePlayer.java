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
    protected boolean isGameReady; //identifies if the player is ready for the game to start

    protected boolean isTroubleshooting;      // Whether the player is troubleshooting or not
    protected boolean isInGame;               // Whether the player is in a game or not

    protected Player MCPlayer;

    protected PlayerArea currentArea;   // The area this player is currently in

    public GamePlayer (Player MCPlayer, PlayerArea currentArea) {
        this.MCPlayer = MCPlayer;
        this.currentArea = currentArea;
        isTroubleshooting = false;     // Set isTroubleShooting to false by default
        // Iterate through stored troubleshooters config if not null
        List<String> troubleshooters = TroubleshootUtil.getTroubleshooters();
        if (troubleshooters != null)
            for (String troubleshooter : troubleshooters) {
                // If the player is found in the list, update their isTroubleshooting
                if (troubleshooter.equals(MCPlayer.getName())) {
                    isTroubleshooting = true;
                    break;
                }
            }
        isInGame = false;
        isGameReady = false;
    }

    public GamePlayer (GamePlayer gamePlayer) {
        MCPlayer = gamePlayer.getPlayer();
        currentArea = gamePlayer.getCurrentArea();
        // Set isTroubleShooting to false by default
        isTroubleshooting = gamePlayer.isTroubleshooting();
        isInGame = gamePlayer.isInGame;
        isGameReady = gamePlayer.isGameReady;
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
    public boolean isGameReady () {return isGameReady;}

    /**
     * Accessor method for returning the current reference of a minecraft player
     * @return minecraft player reference
     */
    public Player getPlayer () {return MCPlayer;}

    /**
     * Accessor method for returning the troubleShooting value of this GamePlayer
     * @return whether the player is troubleshooting or not.
     */
    public boolean isTroubleshooting() {
        return isTroubleshooting;
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
     * Mutator method for changing the isGameReady value of this GamePlayer
     */
    public void setIsGameReady(boolean isGameReady) {
        this.isGameReady = isGameReady;
    }

    /**
     * Mutator method for changing the MCPlayer value of this GamePlayer
     */
    public void setPlayer (Player MCPlayer) {this.MCPlayer = MCPlayer;}

    /**
     * Mutator method for changing the isInGame value of this GamePlayer
     */
    public void setIsInGame(boolean isInGame) {
        this.isInGame = isInGame;
    }

    /**
     * Mutator method for changing the troubleShooting value of this GamePlayer
     */
    public void setTroubleshooting(boolean isTroubleshooting) {
        this.isTroubleshooting = isTroubleshooting;
    }

    /**
     * Mutator method that changes the currentArea value of this GamePlayer
     */
    public void setCurrentArea (PlayerArea newArea) {this.currentArea = newArea;}
}

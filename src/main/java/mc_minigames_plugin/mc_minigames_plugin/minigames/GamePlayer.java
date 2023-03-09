package mc_minigames_plugin.mc_minigames_plugin.minigames;

import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Class represents the abstract blueprint of a player object for a game to be tracked and managed
 * @author Kirt Robinson
 * @version March 6, 2023
 */
abstract public class GamePlayer {
    protected boolean gameReady; //identifies if the player is ready for the game to start

    protected Player MCPlayer;

    public GamePlayer (Player MCPlayer) {
        gameReady = false;
        this.MCPlayer = MCPlayer;
    }

    /**
     * Method checks to see if a player is in a given lobby and returns the boolean state of that query.
     * @param MCPlayer
     * @param lobby
     * @return
     */
    public boolean isInGame (Player MCPlayer, String lobby) {
        Set<String> tags = MCPlayer.getScoreboardTags();
        for (String tag: tags) {if (tag.equals(lobby)) {return true;}}
        return false;
    }

    /**
     * Method compares a given minecraft player reference to this GamePlayer object and
     * returns the boolean value of that comparison.
     * @param MCPlayer
     * @return
     */
    public boolean isPlayer (Player MCPlayer) {return this.MCPlayer.getName().equals(MCPlayer.getName());}

    //Accessors --------------------------------------------------------------------------------------------------------

    /**
     * Accessor method for returning the gameReady value of a GamePlayer
     * @return boolean value for game readiness
     */
    public boolean isGameReady () {return gameReady;}

    /**
     * Accessor method for returning the current reference of a minecraft player
     * @return
     */
    public Player getPlayer () {return MCPlayer;}

}

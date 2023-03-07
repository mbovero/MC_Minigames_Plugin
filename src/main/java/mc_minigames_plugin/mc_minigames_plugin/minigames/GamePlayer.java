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

    protected Player player;

    public GamePlayer (Player player) {
        gameReady = false;
        this.player = player;
    }

    /**
     * Method checks to see if a player is in a given lobby and returns the boolean state of that query.
     * @param player
     * @param lobby
     * @return
     */
    public boolean isInGame (Player player, String lobby) {
        Set<String> tags = player.getScoreboardTags();
        for (String tag: tags) {if (tag.equals(lobby)) {return true;}}
        return false;
    }

    /**
     * Method accesses the player reference and adds a tag to that player.
     * @param tag
     */
    public void giveTag (String tag) {player.addScoreboardTag(tag);}

    /**
     * Method accesses the player reference and removes a tag from that player.
     * @param tag
     */
    public void removeTag (String tag) {player.removeScoreboardTag(tag);}

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
    public Player getPlayer () {return player;}

}

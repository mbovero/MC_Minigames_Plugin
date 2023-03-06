package mc_minigames_plugin.mc_minigames_plugin.minigames;

import org.bukkit.entity.Player;

import java.util.Set;

abstract public class GamePlayer {
    protected String playerName; //Player username for identification and management
    protected boolean gameReady; //identifies if the player is ready for the game to start


    public boolean isInGame (Player player, String lobby) {
        Set<String> tags = player.getScoreboardTags();
        for (String tag: tags) {if (tag.equals(lobby)) {return true;}}
        return false;
    }

    //Accessors --------------------------------------------------------------------------------------------------------

    /**
     * Accessor method for returning the gameReady value of a GamePlayer
     * @return boolean value for game readiness
     */
    public boolean isGameReady () {return gameReady;}

}

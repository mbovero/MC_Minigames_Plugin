package mc_minigames_plugin.mc_minigames_plugin.minigames;

import org.bukkit.entity.Player;

public class HubPlayer extends GamePlayer {
    //FIELDS

    /**
     * Called the very first time a player joins, converting the MCPlayer
     * into a GamePlayer object.
     */
    public HubPlayer(Player MCPlayer, PlayerArea currentArea) {
        super(MCPlayer, currentArea);
    }

    public HubPlayer(GamePlayer gamePlayer) {
        super(gamePlayer);
    }

    //METHODS

    //Basic item give methods

    //Mutators



}

package mc_minigames_plugin.mc_minigames_plugin.minigames;

import org.bukkit.entity.Player;

public class HubPlayer extends GamePlayer {
    //FIELDS

    public HubPlayer(Player MCPlayer, PlayerArea currentArea) {
        super(MCPlayer, currentArea);
        this.isTroubleShooting = false;
        this.isInGame = false;
    }

    //METHODS

    //Basic item give methods

    //Mutators



}

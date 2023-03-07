package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import org.bukkit.entity.Player;
import java.util.Set;

public class KOTHPlayer extends GamePlayer {
    //FIELDS
    String playerKit; //Hold player class
    //Hold player team??
    int killCount;//Hold player kill count

    public KOTHPlayer(Player player, String playerKit) {
        super(player);
        this.playerKit = playerKit;
    }

    //METHODS

        //Basic item give methods

        //Accessors


}

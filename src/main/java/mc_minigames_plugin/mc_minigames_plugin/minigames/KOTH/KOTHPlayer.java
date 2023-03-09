package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.PlayerArea;
import org.bukkit.entity.Player;

public class KOTHPlayer extends GamePlayer {
    //FIELDS
    String playerKit = "KOTH_kit_Striker"; // Hold player class
    // Hold player team??
    int killCount;  // Hold player kill count

    public KOTHPlayer(Player MCPlayer, PlayerArea currentArea) {
        super(MCPlayer, currentArea);
        this.playerKit = playerKit;
    }

    //METHODS

    //Basic item give methods

    //Mutators

    /**
     * Mutator method to change the playerKit field into the preferred kit value
     * @param kit to change into
     */
    public void changePlayerKit(String kit) {playerKit = kit;}

    //Accessors
    /**
     * Accessor method that returns the current held playerKit value
     * @return playerKit value
     */
    public String getPlayerKit () {return playerKit;}


}

package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits;

import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class Kit implements Listener {

    protected String kitName;
    protected GamePlayer gamePlayer;                   // Reference to the parent GamePlayer

    /**
     * Provides the kit's player with their basic gear load-out
     */
    public abstract void giveBasicGear();

    /**
     * Listener to detect the use of this kit's special items/abilities
     */
    @EventHandler
    abstract void onPlayerInteract(PlayerInteractEvent event);
    // Pass in reference of parent GamePlayer's MCPlayer
    // See if the player picked up by the event is the same as this object's MCPlayer reference


    // Kill reward 1
    public abstract void checkKillReward1();
    // Kill reward 2
    public abstract void checkKillReward2();
    // Kill reward 3
    public abstract void checkKillReward3();
    // Kill reward 4
    public abstract void checkKillReward4();

    public String getKitName() {
        return this.kitName;
    }
}

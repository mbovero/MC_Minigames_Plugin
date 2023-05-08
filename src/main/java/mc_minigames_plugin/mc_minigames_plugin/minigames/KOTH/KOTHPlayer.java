package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits.KOTHKit;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits.KOTHKitStriker;

public class KOTHPlayer extends GamePlayer {
    //FIELDS
    KOTHKit KOTHKit;            // Player kit object
    int kills1;         // Player kill count 1
    int kills2;         // Player kill count 2
    int kills3;         // Player kill count 3
    int kills4;         // Player kill count 4


    public KOTHPlayer(GamePlayer gamePlayer) {
        super(gamePlayer);
        this.KOTHKit = new KOTHKitStriker(this);    // Assign Striker kit by default
    }

    //METHODS

    /**
     * Used to increment this player's kills when they kill an enemy.
     * This method also checks to see if this player earned a kill reward.
     */
    public void updateKills() {
        this.kills1++;
        this.kills2++;
        this.kills3++;
        this.kills4++;
        this.checkKills();
    }

    /**
     * Used to check if this player has earned a kill reward.
     */
    public void checkKills() {
        this.KOTHKit.checkKillReward1();
        this.KOTHKit.checkKillReward2();
        this.KOTHKit.checkKillReward3();
        this.KOTHKit.checkKillReward4();
    }

    //Mutators

    /**
     * Changes this KOTHPlayer's kit object
     * @param KOTHKit this player's new kit
     */
    public void setKit(KOTHKit KOTHKit) {
        this.KOTHKit = KOTHKit;
    }

    /**
     * Sets this KOTHPlayer's first kill count to the specified value
     * @param kills1 new kill count
     */
    public void setKills1(int kills1) {
        this.kills1 = kills1;
    }

    /**
     * Sets this KOTHPlayer's second kill count to the specified value
     * @param kills2 new kill count
     */
    public void setKills2(int kills2) {
        this.kills2 = kills2;
    }

    /**
     * Sets this KOTHPlayer's third kill count to the specified value
     * @param kills3 new kill count
     */
    public void setKills3(int kills3) {
        this.kills3 = kills3;
    }

    /**
     * Sets this KOTHPlayer's fourth kill count to the specified value
     * @param kills4 new kill count
     */
    public void setKills4(int kills4) {
        this.kills4 = kills4;
    }


    //Accessors

    /**
     * Accessor method that returns this KOTHPlayer's kit object
     */
    public KOTHKit getKit() {
        return this.KOTHKit;
    }

    /**
     * Accessor method that returns this KOTHPlayer's first kill count
     */
    public int getKills1() {
        return kills1;
    }

    /**
     * Accessor method that returns this KOTHPlayer's second kill count
     */
    public int getKills2() {
        return kills2;
    }

    /**
     * Accessor method that returns this KOTHPlayer's third kill count
     */
    public int getKills3() {
        return kills3;
    }

    /**
     * Accessor method that returns this KOTHPlayer's fourth kill count
     */
    public int getKills4() {
        return kills4;
    }

}

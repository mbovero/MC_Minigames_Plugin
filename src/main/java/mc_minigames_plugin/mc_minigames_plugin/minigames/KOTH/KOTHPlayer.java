package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits.Kit;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits.KitStriker;

public class KOTHPlayer extends GamePlayer {
    //FIELDS
    String kitName;     // Player kit name          not needed?
    Kit kit;            // Player kit object
    int kills1;         // Player kill count 1
    int kills2;         // Player kill count 2
    int kills3;         // Player kill count 3
    int kills4;         // Player kill count 4


    public KOTHPlayer(GamePlayer gamePlayer) {
        super(gamePlayer);
        this.kitName = "KOTH_kit_Striker";            // Assign Striker kit name by default             not needed?
        this.kit = new KitStriker(this);    // Assign Striker kit by default
    }

    //METHODS

    /**
     * Used to increment this player's kills when they kill an enemy.
     * This method also checks to see if this player earned a kill reward.
     */
    public void updateKills() {
        this.kills1++;
        this.kit.checkKillReward1();
        this.kills2++;
        this.kit.checkKillReward2();
        this.kills3++;
        this.kit.checkKillReward3();
        this.kills4++;
        this.kit.checkKillReward4();
    }

    //Mutators

    /**
     * Mutator method to change the playerKitName field into the preferred kitName value
     * @param kitName name to change into
     */
    public void changePlayerKitName(String kitName) {
        this.kitName = kitName;
    }

    /**
     * Changes this KOTHPlayer's kit object
     * @param kit this player's new kit
     */
    public void setKit(Kit kit) {
        this.kit = kit;
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
     * Accessor method that returns this KOTHPlayer's kit name
     */
    public String getKitName() {
        return kitName;
    }

    /**
     * Accessor method that returns this KOTHPlayer's kit object
     */
    public Kit getKit() {
        return this.kit;
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

package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH;

import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits.Kit;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits.KitStriker;

public class KOTHPlayer extends GamePlayer {
    //FIELDS
    String kitName;     // Hold player kit name
    Kit kit;            // Hold player kit object
    int kills1;         // Hold player kill count 1
    int kills2;         // Hold player kill count 2
    int kills3;         // Hold player kill count 3
    int kills4;         // Hold player kill count 4


    public KOTHPlayer(GamePlayer gamePlayer) {
        super(gamePlayer);
        this.kitName = "KOTH_kit_Striker";            // Assign Striker kit name by default
        this.kit = new KitStriker(this);    // Assign Striker kit by default
    }

    //METHODS

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

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public void setKills1(int kills1) {
        this.kills1 = kills1;
    }

    public void setKills2(int kills2) {
        this.kills2 = kills2;
    }

    public void setKills3(int kills3) {
        this.kills3 = kills3;
    }

    public void setKills4(int kills4) {
        this.kills4 = kills4;
    }


    //Accessors
    /**
     * Accessor method that returns the current held playerKit value
     * @return playerKit value
     */
    public String getKitName() {
        return kitName;
    }

    public Kit getKit() {
        return this.kit;
    }

    public int getKills1() {
        return kills1;
    }

    public int getKills2() {
        return kills2;
    }

    public int getKills3() {
        return kills3;
    }

    public int getKills4() {
        return kills4;
    }

}

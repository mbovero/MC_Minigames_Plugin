package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits;

import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Class for holding and managing the KOTH Warper kit
 *
 * @author Kirt Robinson
 * @version May 9, 2023
 */
public class KOTHKitWarper extends KOTHKit {

    ItemStack helmetWarper = Tools.createItem(new ItemStack(Material.GOLDEN_HELMET), "§dWarper Helmet", true, "");
    ItemStack bootsWarper = Tools.createItem(new ItemStack(Material.GOLDEN_BOOTS), "§dWarper Boots", true, "");
    ItemStack shovelWarper = Tools.createItem(new ItemStack(Material.GOLDEN_SHOVEL), "§dWarper Shovel", true, "");


    public KOTHKitWarper (GamePlayer gameplayer) {
        this.kitName = "Stiker";
        shovelWarper.addEnchantment(Enchantment.KNOCKBACK, 2);
    }

    @Override
    public void giveBasicGear() {

    }

    @Override
    void onPlayerInteract(PlayerInteractEvent event) {

    }

    @Override
    public void checkKillReward1() {

    }

    @Override
    public void checkKillReward2() {

    }

    @Override
    public void checkKillReward3() {

    }

    @Override
    public void checkKillReward4() {

    }
}

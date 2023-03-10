package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits;

import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHPlayer;
import mc_minigames_plugin.mc_minigames_plugin.util.Tools;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

public class KitStriker extends Kit {

    ItemStack helmetStriker = Tools.createItem(new ItemStack(Material.NETHERITE_HELMET), "&cStriker Helmet", true, "");
    ItemStack chestplateStriker = Tools.createItem(new ItemStack(Material.LEATHER_CHESTPLATE), "&cStriker Chestplate", true, "");
    ItemStack swordStriker = Tools.createItem(new ItemStack(Material.WOODEN_SWORD), "&cStriker Sword", true, "");
    ItemStack slownessPotion = Tools.createItem(new ItemStack(Material.SPLASH_POTION), "&7Slowness Splash Pot", "");
    ItemStack resistancePotion = Tools.createItem(new ItemStack(Material.POTION), "&8Super Resistance Pot", "");

    public KitStriker(GamePlayer gamePlayer) {
        this.kitName = "Striker";
        this.gamePlayer = gamePlayer;
        // Sharp 4 on sword
        swordStriker.addEnchantment(Enchantment.DAMAGE_ALL, 4);
        // Slowness potion
        Tools.addPotionItemEffect(slownessPotion, PotionEffectType.SLOW, 400, 3, true);
        // Resistance potion
        Tools.addPotionItemEffect(resistancePotion, PotionEffectType.DAMAGE_RESISTANCE, 200, 2, true);
    }

    @Override
    public void giveBasicGear() {
        PlayerInventory inv = this.gamePlayer.getPlayer().getInventory();
        inv.clear();

        inv.setHelmet(helmetStriker);
        inv.setChestplate(chestplateStriker);
        inv.setItem(0, swordStriker);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Dash ability?
    }

    @Override
    public void checkKillReward1() {
        // If player reaches kill goal 1...
        if (((KOTHPlayer)this.gamePlayer).getKills1() >= 3) {
            // Reset correlating kill field
            ((KOTHPlayer)this.gamePlayer).setKills1(0);
            // Give kill reward
            PlayerInventory inv = this.gamePlayer.getPlayer().getInventory();
            inv.addItem(slownessPotion);
        }
    }

    @Override
    public void checkKillReward2() {
        // If player reaches kill goal 2...
        if (((KOTHPlayer)this.gamePlayer).getKills2() >= 5) {
            // Reset correlating kill field
            ((KOTHPlayer)this.gamePlayer).setKills2(0);
            // Give kill reward
            PlayerInventory inv = this.gamePlayer.getPlayer().getInventory();
            inv.addItem(resistancePotion);
        }
    }

    @Override
    public void checkKillReward3() {

    }

    @Override
    public void checkKillReward4() {

    }
}

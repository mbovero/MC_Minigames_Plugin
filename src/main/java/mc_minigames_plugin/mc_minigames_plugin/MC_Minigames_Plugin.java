package mc_minigames_plugin.mc_minigames_plugin;

import mc_minigames_plugin.mc_minigames_plugin.commands.*;
import mc_minigames_plugin.mc_minigames_plugin.handlers.TroubleshootingHandler;
import mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.util.ConfigUtil;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import mc_minigames_plugin.mc_minigames_plugin.util.SpawnUtil;
import mc_minigames_plugin.mc_minigames_plugin.util.TroubleshootUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * The main class running the MC Minigames Plugin.
 *
 * @author Kirt Robinson, Miles Bovero
 * @version March 6, 2023
 */
public final class MC_Minigames_Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Starting MC Minigames Plugin...");

        // Saves config file into plugins folder if it doesn't exist already
        this.saveDefaultConfig();


        // Get default config "config.yml"
//        FileConfiguration defaultConfig = this.getConfig();

        // Adds a list of names to the troubleshooting path (replacing previous values)
//        ArrayList<String> troubleshooters = new ArrayList<>();
//        troubleshooters.add("bigbudderbob");
//        troubleshooters.add("KirtRoboMan");
//        defaultConfig.addDefault("troubleshooting", troubleshooters);

        // Adds to the list of names in the troubleshooting path
//        List<String> troubleshooters2 = (List<String>) defaultConfig.get("troubleshooting");
//        troubleshooters2.add("LordKrandle");
//        defaultConfig.addDefault("troubleshooting", troubleshooters2);

        // Resets the troubleshooting path
//        defaultConfig.set("troubleshooting", null);

        // Save changes to config
//        defaultConfig.options().copyDefaults(true);
//        saveConfig();
//
//        Bukkit.getLogger().info(defaultConfig.getString("troubleshooting"));    // Print contents of troubleshooting path




        // Loops through config file and stores kit items
        List<String> kitItems = (List<String>)getConfig().getList("kit");
        for (String itemName : kitItems) {
            // Print kit items
            Bukkit.getLogger().info(itemName);
        }
        // Creates a new config file and writes to it
        ConfigUtil config = new ConfigUtil(this, "test.yml");
        config.getConfig().set("hello", "world");
        config.save();

        SpawnUtil spawnUtil = new SpawnUtil(this);

        // Register commands
    // Tutorial stuff
//        getCommand("menu").setExecutor(new Menu(this));
//        getCommand("spawn").setExecutor(new Spawn(spawnUtil));
//        getCommand("setSpawn").setExecutor(new SetSpawn(spawnUtil));

        getCommand("fly").setExecutor(new Fly());
        getCommand("hub").setExecutor(new Hub());
        getCommand("reset").setExecutor(new Reset());
        getCommand("getitem").setExecutor(new GetItem());
        getCommand("getdata").setExecutor(new GetData());
        getCommand("troubleshoot").setExecutor(new Troubleshoot(new TroubleshootUtil(this)));




        // Register new handler
        new TroubleshootingHandler(this);
        new DelayedTask(this);
        new GeneralLobbyHandler(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Shutting down...");
    }
}

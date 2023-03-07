package mc_minigames_plugin.mc_minigames_plugin;

import mc_minigames_plugin.mc_minigames_plugin.commands.*;
import mc_minigames_plugin.mc_minigames_plugin.handlers.TroubleshootingHandler;
import mc_minigames_plugin.mc_minigames_plugin.handlers.GameLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.handlers.PlayerHandler;
import mc_minigames_plugin.mc_minigames_plugin.handlers.TorchHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.util.ConfigUtil;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import mc_minigames_plugin.mc_minigames_plugin.util.SpawnUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class MC_Minigames_Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Starting MC Minigames Plugin...");

        // Saves config file into plugins folder if it doesn't exist already
        saveDefaultConfig();
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
        getCommand("fly").setExecutor(new Fly());
        getCommand("hub").setExecutor(new Hub());
        getCommand("menu").setExecutor(new Menu(this));
        getCommand("spawn").setExecutor(new Spawn(spawnUtil));
        getCommand("setSpawn").setExecutor(new SetSpawn(spawnUtil));
        getCommand("troubleshoot").setExecutor(new Troubleshoot());




        // Register new handler
        new TorchHandler(this);
        new TroubleshootingHandler(this);
        new PlayerHandler(this);
        new DelayedTask(this);
        new GameLobbyHandler(this);
        new KOTHLobbyHandler(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Shutting down...");
    }
}

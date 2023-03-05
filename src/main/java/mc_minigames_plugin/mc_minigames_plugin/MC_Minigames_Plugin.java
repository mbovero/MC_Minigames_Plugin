package mc_minigames_plugin.mc_minigames_plugin;

import mc_minigames_plugin.mc_minigames_plugin.commands.Fly;
import mc_minigames_plugin.mc_minigames_plugin.commands.Hub;
import mc_minigames_plugin.mc_minigames_plugin.commands.Menu;
import mc_minigames_plugin.mc_minigames_plugin.handlers.PlayerHandler;
import mc_minigames_plugin.mc_minigames_plugin.handlers.TorchHandler;
import mc_minigames_plugin.mc_minigames_plugin.util.ConfigUtil;
import mc_minigames_plugin.mc_minigames_plugin.util.DelayedTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class MC_Minigames_Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Hello World");

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

        // Register commands
        getCommand("fly").setExecutor(new Fly());
        getCommand("hub").setExecutor(new Hub());
        getCommand("menu").setExecutor(new Menu(this));


        // Register new handler
        new TorchHandler(this);
        new PlayerHandler(this);
        new DelayedTask(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Shutting down...");
    }
}

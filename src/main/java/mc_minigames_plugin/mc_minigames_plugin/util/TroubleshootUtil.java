package mc_minigames_plugin.mc_minigames_plugin.util;

import mc_minigames_plugin.mc_minigames_plugin.MC_Minigames_Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class TroubleshootUtil implements Listener {

    private final ConfigUtil config;
    private static List<String> troubleshooters;

    public TroubleshootUtil(MC_Minigames_Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        // Initiate config
        config = new ConfigUtil(plugin, "troubleshoot.yml");
        // Copy list of troubleshooters from config
        troubleshooters = (List<String>) config.getConfig().get("troubleshooters");
    }

    public void add(Player MCPlayer) {
        // If troubleshooters is null, make a new list
        troubleshooters = new ArrayList<>();
        // Add provided player to list of troubleshooters
        troubleshooters.add(MCPlayer.getName());
        // Update config
        config.getConfig().set("troubleshooters", troubleshooters);
        config.save();
    }

    public void remove(Player MCPlayer) {
        // Remove provided player to list of troubleshooters
        troubleshooters.remove(MCPlayer.getName());
        // Update config
        config.getConfig().set("troubleshooters", troubleshooters);
        config.save();
    }

    public static List<String> getTroubleshooters() {
        return troubleshooters;
    }
}

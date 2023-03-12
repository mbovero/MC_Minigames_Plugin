package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MapCastleOfDreams extends Map{

    public static Location mapLoc = new Location(Bukkit.getWorld("world"), -900, 12, 2, -180, 0);       // The location of the map used for game devs

    public MapCastleOfDreams() {
        this.mapName = "Castle Of Dreams";
        this.respawnLocs = new Location[] {
                new Location(Bukkit.getWorld("world"), -899, 12, 3, -180, 0),
                new Location (Bukkit.getWorld("world"), -902, 12, 0, -180, 0),
                new Location (Bukkit.getWorld("world"), -899, 12, -2, -180, 0),
                new Location (Bukkit.getWorld("world"), -896, 12, 0,-180, 0),
                new Location (Bukkit.getWorld("world"), -895, 12, 4, -180, 0),
                new Location (Bukkit.getWorld("world"), -903, 12, 4, -180, 0),
                new Location (Bukkit.getWorld("world"), -903, 12, -3, -180, 0),
                new Location (Bukkit.getWorld("world"), -895, 12, -3, -180, 0),
                new Location (Bukkit.getWorld("world"), -891, 12, 0, -180, 0),
                new Location (Bukkit.getWorld("world"), -899, 12, 8, -180, 0),
                new Location (Bukkit.getWorld("world"), -907, 12, 0, -180, 0),
                new Location (Bukkit.getWorld("world"), -899, 12, -7, -180, 0)};
        this.lowSWBound = new int[]{1, 2, 3};
        this.highNEBound = new int[]{1, 2, 3};
    }
}

package mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Maps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MapCastleOfDreams extends Map{

    public static Location mapLoc = new Location(Bukkit.getWorld("world"), -900, 12, 2, -180, 0);       // The location of the map used for game devs

    public MapCastleOfDreams() {
        this.mapName = "Castle Of Dreams";
        this.respawnLocs = new Location[] {
                new Location(Bukkit.getWorld("world"), -899.5, 12, 8.5, 180, 0),
                new Location (Bukkit.getWorld("world"), -903.5, 12, 4.5, 45, 0),
                new Location (Bukkit.getWorld("world"), -899.5, 12, 3.5, 180, 0),
                new Location (Bukkit.getWorld("world"), -895.5, 12, 4.5,-45, 0),
                new Location (Bukkit.getWorld("world"), -907.5, 12, 0.5, -90, 0),
                new Location (Bukkit.getWorld("world"), -902.5, 12, 0.5, 90, 0),
                new Location (Bukkit.getWorld("world"), -896.5, 12, 0.5, -90, 0),
                new Location (Bukkit.getWorld("world"), -891.5, 12, 0.5, 90, 0),
                new Location (Bukkit.getWorld("world"), -903.5, 12, -3.5, 135, 0),
                new Location (Bukkit.getWorld("world"), -899.5, 12, -2.5, 180, 0),
                new Location (Bukkit.getWorld("world"), -895.5, 12, -3.5, -135, 0),
                new Location (Bukkit.getWorld("world"), -899.5, 12, -7.5, 0, 0)};
        this.lowSWBound = new int[]{1, 2, 3};
        this.highNEBound = new int[]{1, 2, 3};
    }
}

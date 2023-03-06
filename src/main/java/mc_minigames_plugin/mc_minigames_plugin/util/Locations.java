package mc_minigames_plugin.mc_minigames_plugin.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Locations {
    // Lobbies
    public static Location mainHub = new Location(Bukkit.getWorld("world"), -15.5, -43, -17.5, -45, 15);
    public static Location KOTHLobby = new Location(Bukkit.getWorld("world"), 8, -60, -600, -180, 0);
    public static Location MMLobby = new Location(Bukkit.getWorld("world"), 0, -53, -1800, -180, 0);

    // KOTH maps
    public static Location KOTHTestPlace = new Location(Bukkit.getWorld("world"), 8, 12, -1200, -180, 0);
    public static Location KOTHCastleOfDreams = new Location(Bukkit.getWorld("world"), -900, 12, 2, -180, 0);
    public static Location KOTHRamenBowl = new Location(Bukkit.getWorld("world"), -751, 9, -1650, -180, 0);
    public static Location KOTHHills = new Location(Bukkit.getWorld("world"), -904, 18, -601, -180, 0);
}

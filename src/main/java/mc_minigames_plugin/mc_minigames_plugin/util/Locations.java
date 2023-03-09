package mc_minigames_plugin.mc_minigames_plugin.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Locations {
    // Lobbies ---------------------------------------------------------------------------------------------------------
    public static Location mainHub = new Location(Bukkit.getWorld("world"), -15.5, -43, -17.5, -45, 15);
    public static Location KOTHLobby = new Location(Bukkit.getWorld("world"), 8, -60, -600, -180, 0);
    public static Location MMLobby = new Location(Bukkit.getWorld("world"), 0, -53, -1800, -180, 0);

    // KOTH maps -------------------------------------------------------------------------------------------------------
    public static Location KOTHTestPlace = new Location(Bukkit.getWorld("world"), 8, 12, -1200, -180, 0);
    public static Location KOTHCastleOfDreams = new Location(Bukkit.getWorld("world"), -900, 12, 2, -180, 0);
        public static Location[] KOTHCastleOfDreamsSpawn = new Location[] { //Specific spawn locations
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

    public static Location KOTHRamenBowl = new Location(Bukkit.getWorld("world"), -751, 9, -1650, -180, 0);
        /*public static double[][] KOTHRamenBowlSpawn = new double[][] { //Spawn range
                {-747, 5, -1678}, {-752, 5, -1675}};*/
    public static Location KOTHHills = new Location(Bukkit.getWorld("world"), -904, 18, -601, -180, 0);
        /*public static double[][][] KOTHHillsSpawn = new double[][][] { //List of spawn ranges
                {{}, {}},
                {{}, {}},
                {{}, {}}}; */
}

package mc_minigames_plugin.mc_minigames_plugin.commands;

import mc_minigames_plugin.mc_minigames_plugin.handlers.GeneralLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.handlers.MainHubHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.GamePlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHLobbyHandler;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.KOTHPlayer;
import mc_minigames_plugin.mc_minigames_plugin.minigames.KOTH.Kits.KOTHKitStriker;
import mc_minigames_plugin.mc_minigames_plugin.minigames.MM.MMLobbyHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class providing functionality for the /setdata command. Allows in-game players to change data values of Java
 * GamePlayer objects.
 */
public class SetData implements CommandExecutor {

    // List of data types
    String list = ChatColor.AQUA + "isGameReady, isInGame, currentArea, KOTHKit, KOTHKills1, KOTHKills2, KOTHKills3, KOTHKills4";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // If no type, player, or data is specified
        if (args.length == 0) {
            // Send warning
            sender.sendMessage(ChatColor.RED + "Specify a data type to change. Type '/setdata list' to see a list of data types");
            return true;
        }

        // If no player or data is specified
        if (args.length == 1) {
            // Send list if specified
            if(args[0].equals("list")) {
                sender.sendMessage(list);
                return true;
            }
            // Otherwise, send warning
            sender.sendMessage(ChatColor.RED + "Specify a player and/or their new data value:\n/setdata <type> [username] <value>");
            return true;
        }

        // Initiate target
        Player MCPlayer = null;
        String value = null;

        // If only two arguments are specified...
        if (args.length == 2) {
            // Check/warn if second argument is a player (no value specified)
            if (Bukkit.getPlayerExact(args[1]) != null) {
                sender.sendMessage(ChatColor.RED + "Specify a new data value:\n/setdata <type> [username] <value>");
                return true;
            }
            // Send warning if the sender is not a player
            else if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Specify which player's data to change:\n/setdata <type> <username> <value>");
                return true;
            }
            // Otherwise, set target as the command sender and store new data value
            MCPlayer = (Player)sender;
            value = args[1].toLowerCase();
        }

        // If there are more than 2 args...  (previous statements did not run)
        if (MCPlayer == null) {
            // Set target to specified player
            MCPlayer = Bukkit.getPlayerExact(args[1]);
            // Store provided data value
            value = args[2].toLowerCase();
        }

        // Send warning if specified player was not found
        if (MCPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player " + args[1] + " could not be found");
            return true;
        }

        // Store specified data/value types
        String data = args[0].toLowerCase();
        // Locate gamePlayer
        GamePlayer gamePlayer = GeneralLobbyHandler.findPlayerGlobal(MCPlayer);
        // Message sender an error if gamePlayer could not be found
        if (gamePlayer == null) {
            sender.sendMessage(ChatColor.RED + "Could not find a gamePlayer associated with " + MCPlayer.getName());
            return true;
        }
        // Otherwise, set the specified data type to the specified value
        switch (data) {
            case "list":
                sender.sendMessage(list);
                break;
            case "isgameready":
                if (value.equals("true") || value.equals("t") || value.equals("1")) {
                    gamePlayer.setIsGameReady(true);
                    sender.sendMessage(ChatColor.GREEN + "Set " + MCPlayer.getName() + "'s isGameReady value to " + ChatColor.GRAY + "true");
                }
                else if (value.equals("false") || value.equals("f") || value.equals("0")) {
                    gamePlayer.setIsGameReady(false);
                    sender.sendMessage(ChatColor.GREEN + "Set " + MCPlayer.getName() + "'s isGameReady value to " + ChatColor.GRAY + "false");
                }
                else
                    sender.sendMessage(ChatColor.RED + "Specify a valid value: true, false, t, f, 1, 0");
                break;

            case "isingame":
                if (value.equals("true") || value.equals("t") || value.equals("1")) {
                    gamePlayer.setIsInGame(true);
                    sender.sendMessage(ChatColor.GREEN + "Set " + MCPlayer.getName() + "'s isInGame value to " + ChatColor.GRAY + "true");
                }
                else if (value.equals("false") || value.equals("f") || value.equals("0")) {
                    gamePlayer.setIsInGame(false);
                    sender.sendMessage(ChatColor.GREEN + "Set " + MCPlayer.getName() + "'s isInGame value to " + ChatColor.GRAY + "false");
                }
                else
                    sender.sendMessage(ChatColor.RED + "Specify a valid value: True, False, T, F, 1, 0");
                break;

                // DANGEROUS
//            case "MCPlayer":
//                if (Bukkit.getPlayerExact(value) != null) {
//                    gamePlayer.setPlayer(Bukkit.getPlayerExact(value));
//                    sender.sendMessage(ChatColor.GREEN + "Set " + MCPlayer.getName() + "'s MCPlayer value to " + ChatColor.GRAY + value);
//                }
//                else
//                    sender.sendMessage(ChatColor.RED + "Player " + value + " could not be found");
//                sender.sendMessage(ChatColor.GREEN + MCPlayer.getName() + "'s MCPlayer's name is " + ChatColor.GRAY + (gamePlayer.getPlayer().getName()));
//                break;

            case "currentarea":
                if (value.equals("mainhub")){
                    MainHubHandler.sendPlayer(gamePlayer);
                    sender.sendMessage(ChatColor.GREEN + "Sent " + MCPlayer.getName() + "'s GamePlayer object to " + ChatColor.GRAY + "MainHub");
                }
                else if (value.equals("kothlobby")) {
                    KOTHLobbyHandler.sendPlayer(gamePlayer);
                    sender.sendMessage(ChatColor.GREEN + "Sent " + MCPlayer.getName() + "'s GamePlayer object to " + ChatColor.GRAY + "KOTHLobby");
                }
                else if (value.equals("mmlobby")) {
                    MMLobbyHandler.sendPlayer(gamePlayer);
                    sender.sendMessage(ChatColor.GREEN + "Sent " + MCPlayer.getName() + "'s GamePlayer object to " + ChatColor.GRAY + "MMLobby");
                }
                else
                    sender.sendMessage(ChatColor.RED + "Specify a valid value: MainHub, KOTHLobby, MMLobby");
                break;

            case "kothkit":
                if(gamePlayer.getCurrentArea().getAreaName().equals("KOTHLobby") || gamePlayer.getCurrentArea().getAreaName().contains("KOTHGame")) {
                    KOTHPlayer KOTHPlayer = (KOTHPlayer) gamePlayer;
                    if (value.equals("striker")) {
                        KOTHPlayer.setKit(new KOTHKitStriker(KOTHPlayer));
                        sender.sendMessage(ChatColor.GREEN + "Set " + MCPlayer.getName() + "'s KOTHKit to " + ChatColor.GRAY + "Striker");
                    }
                    else
                        sender.sendMessage(ChatColor.RED + "Specify a valid value: Striker");
                }
                else
                    sender.sendMessage(ChatColor.RED + MCPlayer.getName() + " is not currently a KOTHPlayer");
                break;

            case "kothkills1":
                if(gamePlayer.getCurrentArea().getAreaName().equals("KOTHLobby") || gamePlayer.getCurrentArea().getAreaName().contains("KOTHGame")) {
                    KOTHPlayer KOTHPlayer = (KOTHPlayer) gamePlayer;
                    try {
                        int kills = Integer.parseInt(value);
                        KOTHPlayer.setKills1(kills);
                        KOTHPlayer.checkKills();
                        sender.sendMessage(ChatColor.GREEN + "Set " + MCPlayer.getName() + "'s KOTHKills1 to " + ChatColor.GRAY + value);
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "Specify a valid value: <int>");
                        break;
                    }
                }
                else
                    sender.sendMessage(ChatColor.RED + MCPlayer.getName() + " is not currently a KOTHPlayer");
                break;

            case "kothkills2":
                if(gamePlayer.getCurrentArea().getAreaName().equals("KOTHLobby") || gamePlayer.getCurrentArea().getAreaName().contains("KOTHGame")) {
                    KOTHPlayer KOTHPlayer = (KOTHPlayer) gamePlayer;
                    try {
                        int kills = Integer.parseInt(value);
                        KOTHPlayer.setKills2(kills);
                        KOTHPlayer.checkKills();
                        sender.sendMessage(ChatColor.GREEN + "Set " + MCPlayer.getName() + "'s KOTHKills2 to " + ChatColor.GRAY + value);
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "Specify a valid value: <int>");
                        break;
                    }
                }
                else
                    sender.sendMessage(ChatColor.RED + MCPlayer.getName() + " is not currently a KOTHPlayer");
                break;

            case "kothkills3":
                if(gamePlayer.getCurrentArea().getAreaName().equals("KOTHLobby") || gamePlayer.getCurrentArea().getAreaName().contains("KOTHGame")) {
                    KOTHPlayer KOTHPlayer = (KOTHPlayer) gamePlayer;
                    try {
                        int kills = Integer.parseInt(value);
                        KOTHPlayer.setKills3(kills);
                        KOTHPlayer.checkKills();
                        sender.sendMessage(ChatColor.GREEN + "Set " + MCPlayer.getName() + "'s KOTHKills3 to " + ChatColor.GRAY + value);
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "Specify a valid value: <int>");
                        break;
                    }
                }
                else
                    sender.sendMessage(ChatColor.RED + MCPlayer.getName() + " is not currently a KOTHPlayer");
                break;

            case "kothkills4":
                if(gamePlayer.getCurrentArea().getAreaName().equals("KOTHLobby") || gamePlayer.getCurrentArea().getAreaName().contains("KOTHGame")) {
                    KOTHPlayer KOTHPlayer = (KOTHPlayer) gamePlayer;
                    try {
                        int kills = Integer.parseInt(value);
                        KOTHPlayer.setKills4(kills);
                        KOTHPlayer.checkKills();
                        sender.sendMessage(ChatColor.GREEN + "Set " + MCPlayer.getName() + "'s KOTHKills4 to " + ChatColor.GRAY + value);
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "Specify a valid value: <int>");
                        break;
                    }
                }
                else
                    sender.sendMessage(ChatColor.RED + MCPlayer.getName() + " is not currently a KOTHPlayer");
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Specify a valid data type to receive. Type '/getdata list' to see a list of data types");
        }

        return true;
    }
}

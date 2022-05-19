package org.vexmi.escapadelabestia.cmds;

import org.bukkit.ChatColor;
import org.vexmi.escapadelabestia.EscapaBestia;
import org.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.managers.GameManager;
import org.vexmi.escapadelabestia.managers.InvManager;
import org.vexmi.escapadelabestia.utils.AdminCmd;
import org.vexmi.escapadelabestia.utils.ErrorCodes;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EBCmd implements CommandExecutor {

    private final EscapaBestia plugin;

    public EBCmd(EscapaBestia plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameManager gameM = new GameManager(plugin);
        AdminCmd adminCmd = new AdminCmd();
        InvManager invM = new InvManager(plugin);

        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!(args.length >= 2)) {
                    player.sendMessage(plugin.colorText("&cQue tal si pones como se va a llamar la partida? :D"));
                    return true;
                }
                EscapaBestiaPlayer eplayer = new EscapaBestiaPlayer(player);
                Bukkit.getConsoleSender().sendMessage(plugin.colorText(eplayer.getName()));
                Game game = new Game(args[1], 1, 10);
                plugin.addGame(game);
            } else if (args[0].equalsIgnoreCase("join")) {
                if (!(args.length >= 2)) {
                    player.sendMessage(plugin.colorText("&cTe recomiendo poner el nombre de la partida salu2"));
                    return true;
                } else {
                    Game game = plugin.getGame(args[1]);
                    if (game != null) {
                        if (game.isEnabled()) {
                            if (plugin.getPlayerGame(player.getName()) == null) {
                                if (!game.isPlaying()) {
                                    if (!game.isFull()) {
                                        ErrorCodes error = GameManager.playerJoin(game, player, plugin);

                                        player.sendMessage(error.getMessage());
                                    } else {
                                        player.sendMessage(ErrorCodes.GAME_IS_FULL.getMessage());
                                    }
                                } else {
                                    player.sendMessage(ErrorCodes.GAME_IS_PLAYING.getMessage());
                                }

                            } else {
                                player.sendMessage(ErrorCodes.PLAYER_ALREADY_IN_GAME.getMessage());
                            }
                        } else {
                            player.sendMessage(ErrorCodes.GAME_NOT_ENABLED.getMessage());
                        }
                    } else {
                        player.sendMessage(ErrorCodes.GAME_NOT_FOUND.getMessage());
                    }
                }
                return true;
            } else if (args[0].equalsIgnoreCase("admin")) {
                if (!(args.length >= 2)) {
                    player.sendMessage("tienes que poner que comando de admin quieres usar ._.");
                    return true;
                }
                return adminCmd.execute(args, player, plugin, gameM);
                /*String function = args[1];
                if(function.equalsIgnoreCase("setlobby")) {
                    if (!(args.length >= 3)) {
                        player.sendMessage("Tienes que poner de que partida quieres poner el lobby");
                    }
                    Game game = gameM.getGame(args[2]);
                    Location l = player.getLocation();
                    game.setLobby(l);
                    player.sendMessage("Lobby establecido");
                }*/
            } else if (args[0].equalsIgnoreCase("inv")) {
                invM.createJoinGamesInv(player, 0);
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                List<String> messages = new ArrayList<>();
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb create <partida>"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb join <partida>"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb inv"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb admin setlobby <partida>"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb admin setplayersspawn <partida>"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb admin setbestiaspawn <partida>"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb admin setchestlocation <partida>"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb admin setminplayers <partida> <minimojugadores>"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb admin setmaxplayers <partida> <maximojugadores>"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb admin setmaxtime <partida> <maxtime>"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb admin changename <partida> <nuevonombre>"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb admin forcestop <partida>"));
                messages.add(ChatColor.translateAlternateColorCodes('&', "/edlb admin spawnchests <partida>"));

                for(String msg : messages) {
                    player.sendMessage(msg);
                }

                return true;
            }
        }

        return false;
    }
}

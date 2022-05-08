package org.vexmi.escapadelabestia.cmds;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class EBCmd implements CommandExecutor {

    private EscapaBestia plugin;

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
        FileConfiguration messages = plugin.getMessages();
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
                                        player.sendMessage("5");
                                    }
                                } else {
                                    player.sendMessage("4");
                                }

                            } else {
                                player.sendMessage("3");
                            }
                        } else {
                            player.sendMessage("2");
                        }
                    } else {
                        player.sendMessage("1");
                    }
                }
            } else if (args[0].equalsIgnoreCase("admin")) {
                if (!(args.length >= 2)) {
                    player.sendMessage("tienes que poner que comando de admin quieres usar ._.");
                    return true;
                }
                adminCmd.execute(args, player, plugin, gameM);
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
            }
        }

        return true;
    }
}

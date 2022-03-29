package com.vexmi.escapadelabestia.cmds;

import com.vexmi.escapadelabestia.EscapaBestia;
import com.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import com.vexmi.escapadelabestia.classes.Game;
import com.vexmi.escapadelabestia.managers.GameManager;
import com.vexmi.escapadelabestia.utils.AdminCmd;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class EBCmd implements CommandExecutor
{
    private EscapaBestia plugin;
    public EBCmd(EscapaBestia plugin) { this.plugin = plugin; }
    private GameManager gameM = plugin.gameM;
    private AdminCmd adminCmd = new AdminCmd();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            return false;
        }
        Player player = (Player) sender;
        FileConfiguration messages = plugin.getMessages();
        if(args.length >= 1)
        {
            if(args[0].equalsIgnoreCase("create"))
            {
                if(!(args.length >= 2))
                {
                    player.sendMessage(plugin.colorText("&cQue tal si pones como se va a llamar la partida? :D"));
                    return true;
                }
                EscapaBestiaPlayer eplayer = new EscapaBestiaPlayer(player);
                Bukkit.getConsoleSender().sendMessage(plugin.colorText(eplayer.getName()));
                Game game = new Game(args[1], 1, 10);
                gameM.addGame(game);
            }
            else if (args[0].equalsIgnoreCase("join"))
            {
                if(!(args.length >= 2))
                {
                    player.sendMessage(plugin.colorText("&cTe recomiendo poner el nombre de la partida salu2"));
                    return true;
                }
                else
                {
                    Game game = gameM.getGame(args[1]);
                    player.sendMessage(game.getName());
                    if(game != null)
                    {
                        if(game.isEnabled())
                        {
                            if(gameM.getPlayerGame(player.getName()) == null)
                            {
                                if(!game.isPlaying())
                                {
                                    if(!game.isFull())
                                    {
                                        GameManager.playerJoin(game, player, plugin);
                                    }
                                    else
                                    {
                                        player.sendMessage("5");
                                    }
                                }else
                                {
                                    player.sendMessage("4");
                                }

                            }
                            else
                            {
                                player.sendMessage("3");
                            }
                        }
                        else
                        {
                            player.sendMessage("2");
                        }
                    }
                    else
                    {
                        player.sendMessage("1");
                    }
                }
            }
            else if(args[0].equalsIgnoreCase("admin"))
            {
                if(!(args.length >= 2))
                {
                    player.sendMessage("tienes que poner que comando de admin quieres usar ._.");
                    return true;
                }
                adminCmd.execute(args, player, plugin, gameM);
                /*String function = args[1];
                if(function.equalsIgnoreCase("setlobby")) {
                    if (!(args.length >= 3)) {
                        player.sendMessage("Tienes que poner de que partida quieres poner el lobby");
                        return false;
                    }
                    Game game = gameM.getGame(args[2]);
                    Location l = player.getLocation();
                    game.setLobby(l);
                    player.sendMessage("Lobby establecido");
                }*/
            }
        }
        return true;
    }
}

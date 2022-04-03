package com.vexmi.escapadelabestia.utils;

import com.vexmi.escapadelabestia.EscapaBestia;
import com.vexmi.escapadelabestia.classes.Game;
import com.vexmi.escapadelabestia.managers.GameManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AdminCmd
{
    public boolean execute(String[] args, Player player, EscapaBestia plugin, GameManager gameM)
    {
        if(args[1].equalsIgnoreCase("setlobby"))
        {
            if(!(args.length >= 3))
            {
                player.sendMessage("Tienes que poner la partida ._.");
                return true;
            }
            Game game = gameM.getGame(args[2]);
            if(game == null)
            {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            return setLobby(game, player, plugin);
        }
        else if(args[1].equalsIgnoreCase("setplayersspawn"))
        {
            if(!(args.length >= 3))
            {
                player.sendMessage("Tienes que poner la partida ._.");
                return true;
            }
            Game game = gameM.getGame(args[2]);
            if(game == null)
            {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            return setPlayersSpawn(game, player, plugin);
        }
        else if(args[1].equalsIgnoreCase("setbestiaspawn"))
        {
            if(!(args.length >= 3))
            {
                player.sendMessage("Tienes que poner la partida ._.");
                return true;
            }
            Game game = gameM.getGame(args[2]);
            if(game == null)
            {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            return setBestiaSpawn(game, player, plugin);
        }
        else if(args[1].equalsIgnoreCase("setminplayers"))
        {
            if(!(args.length >= 4))
            {
                player.sendMessage("Tienes que poner la partida y el número de jugadores mínimo ._.");
                return true;
            }
            Game game = gameM.getGame(args[2]);
            if(game == null)
            {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            try
            {
                int minPlayers = Integer.parseInt(args[3]);
                return setMinPlayers(game, player, plugin, minPlayers);
            }
            catch(NumberFormatException e)
            {
                player.sendMessage(plugin.colorText("&2El número debe ser un número ._."));
                e.printStackTrace();
                return true;
            }
        }
        else if(args[1].equalsIgnoreCase("setmaxplayers"))
        {
            if(!(args.length >= 4))
            {
                player.sendMessage("Tienes que poner la partida y el número de jugadores máximo ._.");
                return true;
            }
            Game game = gameM.getGame(args[2]);
            if(game == null)
            {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            try
            {
                int maxPlayers = Integer.parseInt(args[3]);
                return setMaxPlayers(game, player, plugin, maxPlayers);
            }
            catch(NumberFormatException e)
            {
                player.sendMessage(plugin.colorText("&2El número debe ser un número ._."));
                e.printStackTrace();
                return true;
            }
        }
        else if(args[1].equalsIgnoreCase("changename"))
        {
            if(!(args.length >= 4))
            {
                player.sendMessage("Tienes que poner la partida y el nuevo nombre ._.");
                return true;
            }
            Game game = gameM.getGame(args[2]);
            if(game == null)
            {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            return changeName(game, player, plugin, args[3]);
        }
        return true;
    }

    private boolean setLobby(Game game, Player player, EscapaBestia plugin)
    {
        Location l = player.getLocation();
        game.setLobby(l);
        player.sendMessage("Lobby establecido");
        return true;
    }

    private boolean setPlayersSpawn(Game game, Player player, EscapaBestia plugin)
    {
        Location l = player.getLocation();
        game.setPlayersSpawn(l);
        player.sendMessage("Lobby establecido");
        return true;
    }

    private boolean setBestiaSpawn(Game game, Player player, EscapaBestia plugin)
    {
        Location l = player.getLocation();
        game.setBestiaSpawn(l);
        player.sendMessage("Lobby establecido");
        return true;
    }

    private boolean setMinPlayers(Game game, Player player, EscapaBestia plugin, int minPlayers)
    {
        game.setMinPlayers(minPlayers);
        player.sendMessage("Mínimo de jugadores de la partida " + game.getName() + " para empezar la partida establecido a &a" + minPlayers);
        return true;
    }

    private boolean setMaxPlayers(Game game, Player player, EscapaBestia plugin, int maxPlayers)
    {
        game.setMinPlayers(maxPlayers);
        player.sendMessage("Máximo de jugadores de la partida " + game.getName() + " establecido a &a" + maxPlayers);
        return true;
    }

    private boolean changeName(Game game, Player player, EscapaBestia plugin, String newName)
    {
        String oldName = game.getName();
        game.setName(newName);
        player.sendMessage(plugin.colorText("&2Nombre de la partida &a" + oldName + "&2 cambiado a &a" + newName));
        return true;
    }
}
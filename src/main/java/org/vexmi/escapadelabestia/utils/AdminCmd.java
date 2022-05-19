package org.vexmi.escapadelabestia.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.vexmi.escapadelabestia.EscapaBestia;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.managers.ChestsManager;
import org.vexmi.escapadelabestia.managers.GameManager;

public class AdminCmd {

    public boolean execute(String[] args, Player player, EscapaBestia plugin, GameManager gameM) {

        if (args[1].equalsIgnoreCase("setlobby")) {
            if (!(args.length >= 3)) {
                player.sendMessage("Tienes que poner la partida ._.");
                return true;
            }
            Game game = plugin.getGame(args[2]);
            if (game == null) {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            return setLobby(game, player);
        }
        else if (args[1].equalsIgnoreCase("setplayersspawn")) {
            if (!(args.length >= 3)) {
                player.sendMessage("Tienes que poner la partida ._.");
                return true;
            }
            Game game = plugin.getGame(args[2]);
            if (game == null) {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            return setPlayersSpawn(game, player);
        }
        else if (args[1].equalsIgnoreCase("setbestiaspawn")) {
            if (!(args.length >= 3)) {
                player.sendMessage("Tienes que poner la partida ._.");
                return true;
            }
            Game game = plugin.getGame(args[2]);
            if (game == null) {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            return setBestiaSpawn(game, player);
        }
        else if (args[1].equalsIgnoreCase("setchestlocation")) {
            if (!(args.length >= 3)) {
                player.sendMessage("Tienes que poner la partida ._.");
                return true;
            }
            Game game = plugin.getGame(args[2]);
            if (game == null) {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            return setChestLocation(game, player, plugin);
        }
        else if (args[1].equalsIgnoreCase("setminplayers")) {
            if (!(args.length >= 4)) {
                player.sendMessage("Tienes que poner la partida y el número de jugadores mínimo ._.");
                return true;
            }
            Game game = plugin.getGame(args[2]);
            if (game == null) {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            try {
                int minPlayers = Integer.parseInt(args[3]);
                return setMinPlayers(game, player, minPlayers);
            } catch (NumberFormatException e) {
                player.sendMessage(plugin.colorText("&2El número debe ser un número ._."));
                e.printStackTrace();
                return true;
            }
        }
        else if (args[1].equalsIgnoreCase("setmaxplayers")) {
            if (!(args.length >= 4)) {
                player.sendMessage("Tienes que poner la partida y el número de jugadores máximo ._.");
                return true;
            }
            Game game = plugin.getGame(args[2]);
            if (game == null) {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            try {
                int maxPlayers = Integer.parseInt(args[3]);
                return setMaxPlayers(game, player, maxPlayers);
            } catch (NumberFormatException e) {
                player.sendMessage(plugin.colorText("&2El número debe ser un número ._."));
                e.printStackTrace();
                return true;
            }
        }
        else if (args[1].equalsIgnoreCase("changename")) {
            if (!(args.length >= 4)) {
                player.sendMessage("Tienes que poner la partida y el nuevo nombre ._.");
                return true;
            }
            Game game = plugin.getGame(args[2]);
            if (game == null) {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            return changeName(game, player, plugin, args[3]);
        }
        else if (args[1].equalsIgnoreCase("forcestop")) {
            if (!(args.length >= 3)) {
                player.sendMessage("Tienes que poner la partida._.");
                return true;
            }
            Game game = plugin.getGame(args[2]);
            if (game == null) {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            return forceStopGame(game, player, plugin);
        }
        else if (args[1].equalsIgnoreCase("spawnchests")) {
            if (!(args.length >= 3)) {
                player.sendMessage("Tienes que poner la partida._.");
                return true;
            }
            Game game = plugin.getGame(args[2]);
            if (game == null) {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            return spawnChests(game, player, plugin);
        }
        else if (args[1].equalsIgnoreCase("setmaxtime")) {
            if (!(args.length >= 4)) {
                player.sendMessage("Tienes que poner la partida y el tiempo máximo de esta ._.");
                return true;
            }
            Game game = plugin.getGame(args[2]);
            if (game == null) {
                player.sendMessage(plugin.colorText("&cEsa partida no existe!"));
                return true;
            }
            try {
                int maxTime = Integer.parseInt(args[3]);
                return setMaxTime(game, player, maxTime);
            } catch (NumberFormatException e) {
                player.sendMessage(plugin.colorText("&2El número debe ser un número ._."));
                return true;
            }
        }
        return true;
    }

    private boolean setLobby(@NotNull Game game, @NotNull Player player) {
        Location l = player.getLocation();
        game.setLobby(l);
        player.sendMessage("Lobby establecido");
        return true;
    }
    private boolean setPlayersSpawn(@NotNull Game game, @NotNull Player player) {
        Location l = player.getLocation();
        game.setPlayersSpawn(l);
        player.sendMessage("Spawn de jugadores establecido");
        return true;
    }
    private boolean setBestiaSpawn(@NotNull Game game, @NotNull Player player) {
        Location l = player.getLocation();
        game.setBestiaSpawn(l);
        player.sendMessage("Spawn de bestia establecido");
        return true;
    }
    private boolean setMinPlayers(@NotNull Game game, @NotNull Player player, int minPlayers) {
        game.setMinPlayers(minPlayers);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Mínimo de jugadores de la partida " + game.getName() + " para empezar la partida establecido a &a" + minPlayers));
        return true;
    }
    private boolean setMaxPlayers(@NotNull Game game, @NotNull Player player, int maxPlayers) {
        game.setMinPlayers(maxPlayers);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Máximo de jugadores de la partida " + game.getName() + " establecido a &a" + maxPlayers));
        return true;
    }
    private boolean changeName(@NotNull Game game, @NotNull Player player, @NotNull EscapaBestia plugin, String newName) {
        String oldName = game.getName();
        game.setName(newName);
        player.sendMessage(plugin.colorText("&2Nombre de la partida &a" + oldName + "&2 cambiado a &a" + newName));
        return true;
    }
    private boolean forceStopGame(@NotNull Game game, Player player, EscapaBestia plugin) {
        if (game.isPlaying()) {
            GameManager.finishGame(game, plugin, true);
            player.sendMessage("Juego parado con exito.");
            return true;
        }
        return false;
    }
    private boolean setChestLocation(@NotNull Game game, @NotNull Player player, EscapaBestia plugin) {
        Location l = player.getLocation();
        game.setChestsLocation(l);
        player.sendMessage("Localización de cofres establecido");
        return true;
    }
    private boolean spawnChests(Game game, @NotNull Player player, EscapaBestia plugin) {
        ChestsManager.spawnChests(game);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Cofres spawneados"));
        return true;
    }
    private boolean setMaxTime(@NotNull Game game, @NotNull Player player, int maxTime) {
        game.setMaxTime(maxTime);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Tiempo máximo de la partida &6" + game.getName() + " &2fue establecido a &6" + maxTime));
        return true;
    }
}
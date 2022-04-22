package com.vexmi.escapadelabestia.managers;

import com.vexmi.escapadelabestia.EscapaBestia;
import com.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import com.vexmi.escapadelabestia.classes.Game;
import com.vexmi.escapadelabestia.classes.GameState;
import com.vexmi.escapadelabestia.utils.ErrorCodes;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.logging.Level;

public class GameManager {
    private static EscapaBestia pluginStatic;
    private EscapaBestia plugin;

    public GameManager(EscapaBestia plugin, EscapaBestia pluginStatic) {
        GameManager.pluginStatic = pluginStatic;
        this.plugin = plugin;
    }

    private static CooldownManager cooldownM = new CooldownManager(pluginStatic);

    public static int playerJoin(Game game, Player player, EscapaBestia plugin) {
        for (Game game2 : plugin.games) {
            for (EscapaBestiaPlayer ePlayer : game2.getPlayers()) {
                if (ePlayer.getPlayer().equals(player)) {
                    return ErrorCodes.PLAYER_ALREADY_IN_GAME.getCode();
                }
            }
        }
        FileConfiguration messages = plugin.getMessages();
        EscapaBestiaPlayer pToAdd = new EscapaBestiaPlayer(player);
        game.addPlayer(pToAdd);
        ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
        String prefix = plugin.getConfig().getString("Config.prefix");
        for (EscapaBestiaPlayer p : players) {
            String path = messages.getString("Messages.OnPlayerJoin");
            String actualPlayers = String.valueOf(game.getActualPlayers());
            String maxPlayers = String.valueOf(game.getMaxPlayers());
            p.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', path)
                    .replaceAll("%player%", player.getName())
                    .replaceAll("%actualPlayers%", actualPlayers)
                    .replaceAll("%maxPlayers%", maxPlayers)
                    .replaceAll("%game%", game.getName()));
        }
        player.getInventory().clear();
        player.getEquipment().clear();
        player.getEquipment().setArmorContents(null);
        player.updateInventory();

        player.setGameMode(GameMode.SURVIVAL);
        player.setExp(0);
        player.setLevel(0);
        player.setFoodLevel(20);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFlying(false);
        player.setAllowFlight(false);
        for (PotionEffect e : player.getActivePotionEffects()) {
            player.removePotionEffect(e.getType());
        }

        if (game.getLobby() == null)
            return ErrorCodes.LOBBY_NOT_FOUND.getCode();
        else if (Bukkit.getWorld(game.getLobby().getWorld().getName()) == null)
            return ErrorCodes.WORLD_NOT_FOUND.getCode();
        else
            player.teleport(game.getLobby());

        if (game.getActualPlayers() >= game.getMinPlayers() && game.getState().equals(GameState.WAITING)) {
            //cooldown
            player.sendMessage(game.getState().getName());
            cooldownM.cooldownStartGame(game, plugin);
        }
        return ErrorCodes.GOOD.getCode();
    }

    public static int playerLeave(Game game, Player player, EscapaBestia plugin, boolean finishingGame, boolean closingServer) {
        EscapaBestiaPlayer edlbP = game.getPlayer(player.getName());
        ItemStack[] savedInventory = edlbP.getSaved().getInvSaved();
        ItemStack[] savedEquipment = edlbP.getSaved().getArmorSaved();
        GameMode savedGamemode = edlbP.getSaved().getGamemodeSaved();
        float savedXp = edlbP.getSaved().getXpSaved();
        int savedLevel = edlbP.getSaved().getLevelSaved();
        int savedFood = edlbP.getSaved().getFoodSaved();
        double savedHealth = edlbP.getSaved().getHealthSaved();
        double savedMaxHealth = edlbP.getSaved().getMaxHealthSaved();

        game.removePlayer(player.getName());

        if (!finishingGame) {
            FileConfiguration messages = plugin.getMessages();
            ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
            for (EscapaBestiaPlayer p : players) {
                String actualPlayers = String.valueOf(game.getActualPlayers());
                String maxPlayers = String.valueOf(game.getMaxPlayers());
                String path = messages.getString("Messages.PlayerLeaveGame");
                p.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', path)
                        .replaceAll("%player%", player.getName())
                        .replaceAll("%actualPlayers%", actualPlayers)
                        .replaceAll("%maxPlayers%", maxPlayers));
            }
        }

        //FileConfiguration mainlobby = plugin.getMainLobby();
        //World world = null;
        //if(Bukkit.getWorld(mainlobby.getString("MainLobby.world")) == null)
        //{
        //return 1; //El mundo no fue encontrado
        //}
        //else
        //world = Bukkit.getWorld(mainlobby.getString("MainLobby.world"));

        //double x = Double.valueOf(mainlobby.getString("MainLobby.x"));
        //double y = Double.valueOf(mainlobby.getString("MainLobby.y"));
        //double z = Double.valueOf(mainlobby.getString("MainLobby.z"));
        //float yaw = Float.valueOf(mainlobby.getString("MainLobby.yaw"));
        //float pitch = Float.valueOf(mainlobby.getString("MainLobby.pitch"));
        //Location l = new Location(world, x, y, z, yaw, pitch);
        //player.teleport(l);

        player.getInventory().setContents(savedInventory);
        player.getInventory().setArmorContents(savedEquipment);
        player.setGameMode(savedGamemode);
        player.setExp(savedXp);
        player.setLevel(savedLevel);
        player.setFoodLevel(savedFood);
        player.setHealth(savedHealth);
        player.setMaxHealth(savedMaxHealth);
        player.updateInventory();

        if (!closingServer) {
            //if(game.getActualPlayers() < game.getMinPlayers() && game.getState().equals(GameState.STARTING)) {
            //game.setState(GameState.WAITING);
            //}else if(game.getActualPlayers() <= 1 && game.getState().equals(GameState.PLAYING)) {
            //finishGame(game, plugin);
            //}else if((game.getTeam1().getQuantityPlayers() == 0 || game.getTeam2().getQuantityPlayers() == 0) &&
            //game.getState().equals(GameState.PLAYING)) {
            //finishGame(game, plugin);
            //}
        }
        return ErrorCodes.GOOD.getCode();
    }

    @SuppressWarnings("unlikely-arg-type")
    public void startGame(Game game) {
        for (EscapaBestiaPlayer p : game.getPlayers()) {
            p.getPlayer().sendMessage("Empieza");
        }
        game.setState(GameState.PLAYING);
        game.determineBestia();

        ArrayList<EscapaBestiaPlayer> players = (ArrayList<EscapaBestiaPlayer>) game.getPlayers();
        for (EscapaBestiaPlayer p : players) {
            FileConfiguration messages = plugin.getMessages();
            String path = messages.getString("Messages.StartGame");
            p.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', path));
        }

        CooldownManager cooldown = new CooldownManager(pluginStatic);
        cooldown.cooldownGame(game, pluginStatic);
    }

    private static int teleportPlayers(Game game) {
        if (game.getBestiaSpawn() == null)
            return ErrorCodes.NO_BESTIA_SPAWN.getCode();
        if (game.getPlayersSpawn() == null)
            return ErrorCodes.NO_PLAYERS_SPAWN.getCode();

        ArrayList<EscapaBestiaPlayer> players = (ArrayList<EscapaBestiaPlayer>) game.getPlayers();
        for (EscapaBestiaPlayer p : players) {
            if (p.isBestia()) {
                Player bestia = p.getPlayer();
                bestia.sendMessage("teletransportando al spawn de bestias");
                bestia.teleport(game.getBestiaSpawn());
                return ErrorCodes.GOOD.getCode();
            } else {
                Player player = p.getPlayer();
                player.sendMessage("teletransportando al spawn de jugadores");
                player.teleport(game.getPlayersSpawn());
                return ErrorCodes.GOOD.getCode();
            }
        }
        return ErrorCodes.UNKNOWN_ERROR.getCode();
    }

    public static void finishGame(Game game, EscapaBestia plugin, boolean bestiaWin) {
        game.setState(GameState.FINISHING);
        game.setGameFinishing(true);
        for (EscapaBestiaPlayer player : game.getPlayers()) {
            if (bestiaWin)
                player.getPlayer().sendMessage("La bestia gano");
            else
                player.getPlayer().sendMessage("Los jugadores ganaron");
        }
        game.getPlayers().removeAll(game.getPlayers());
        plugin.log(Level.INFO, game.getPlayers().toString());
    }
}

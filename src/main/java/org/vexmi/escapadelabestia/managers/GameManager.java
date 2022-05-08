package org.vexmi.escapadelabestia.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.PlayerInventory;
import org.vexmi.escapadelabestia.EscapaBestia;
import org.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.classes.GameState;
import org.vexmi.escapadelabestia.utils.ErrorCodes;
import org.vexmi.escapadelabestia.utils.Messages;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.logging.Level;

public class GameManager {
    private static EscapaBestia plugin;

    public GameManager(EscapaBestia plugin) {
        GameManager.plugin = plugin;
    }

    private static final CooldownManager cooldownM = new CooldownManager();

    public static ErrorCodes playerJoin(Game game, Player player, EscapaBestia plugin) {
        for (Game game2 : plugin.games) {
            for (EscapaBestiaPlayer ePlayer : game2.getPlayers()) {
                if (ePlayer.getPlayer().equals(player)) {
                    return ErrorCodes.PLAYER_ALREADY_IN_GAME;
                }
            }
        }
        EscapaBestiaPlayer pToAdd = new EscapaBestiaPlayer(player);
        game.addPlayer(pToAdd);
        ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
        for (EscapaBestiaPlayer p : players) {
            String actualPlayers = String.valueOf(game.getActualPlayers());
            String maxPlayers = String.valueOf(game.getMaxPlayers());
            p.getPlayer().sendMessage(Messages.OnPlayerJoin
                    .replaceAll("%player%", player.getName())
                    .replaceAll("%actualPlayers%", actualPlayers)
                    .replaceAll("%maxPlayers%", maxPlayers)
                    .replaceAll("%game%", game.getName()));
        }
        player.getInventory().clear();
        player.getEquipment().clear();
        player.getEquipment().setArmorContents(null);
        player.updateInventory();

        player.setGameMode(GameMode.ADVENTURE);
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
            return ErrorCodes.LOBBY_NOT_FOUND;
        else if (Bukkit.getWorld(game.getLobby().getWorld().getName()) == null)
            return ErrorCodes.WORLD_NOT_FOUND;
        else
            player.teleport(game.getLobby());

        if (game.getActualPlayers() >= game.getMinPlayers() && !game.getState().equals(GameState.PLAYING)) {
            //cooldown
            player.sendMessage(game.getState().getName());
            cooldownM.cooldownStartGame(game, plugin);
        }

        return ErrorCodes.GOOD;
    }

    public static ErrorCodes playerLeave(Game game, Player player, EscapaBestia plugin, boolean finishingGame, boolean closingServer) {
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
            ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
            for (EscapaBestiaPlayer p : players) {
                String actualPlayers = String.valueOf(game.getActualPlayers());
                String maxPlayers = String.valueOf(game.getMaxPlayers());
                p.getPlayer().sendMessage(Messages.PlayerLeaveGame
                        .replaceAll("%player%", player.getName())
                        .replaceAll("%actualPlayers%", actualPlayers)
                        .replaceAll("%maxPlayers%", maxPlayers));
            }
        }

        FileConfiguration mainlobby = plugin.getMainLobby();
        World world = null;
        if (Bukkit.getWorld(mainlobby.getString("MainLobby.world")) == null)
            return ErrorCodes.WORLD_NOT_FOUND;
        else
            world = Bukkit.getWorld(mainlobby.getString("MainLobby.world"));

        double x = Double.valueOf(mainlobby.getString("MainLobby.x"));
        double y = Double.valueOf(mainlobby.getString("MainLobby.y"));
        double z = Double.valueOf(mainlobby.getString("MainLobby.z"));
        float yaw = Float.valueOf(mainlobby.getString("MainLobby.yaw"));
        float pitch = Float.valueOf(mainlobby.getString("MainLobby.pitch"));
        Location l = new Location(world, x, y, z, yaw, pitch);
        player.teleport(l);

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
        return ErrorCodes.GOOD;
    }

    @SuppressWarnings("unlikely-arg-type")
    public void startGame(Game game) {
        for (EscapaBestiaPlayer p : game.getPlayers()) {
            p.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
        game.setState(GameState.PLAYING);
        game.determineBestia();

        game.getBestia().getPlayer().getInventory().clear();
        setBestiaInv(game.getBestia());

        teleportPlayers(game);
        ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
        for (EscapaBestiaPlayer p : players) {
            p.getPlayer().sendMessage(Messages.StartGame);
        }

        CooldownManager cooldown = new CooldownManager();
        cooldown.cooldownGame(game, plugin);
    }

    private static void setBestiaInv(EscapaBestiaPlayer bestia) {
        PlayerInventory inv = bestia.getPlayer().getInventory();

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1, (short) 0);
        ItemStack bow = new ItemStack(Material.BOW, 1, (short) 0);

        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1, (short) 0);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1, (short) 0);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1, (short) 0);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1, (short) 0);

        inv.setItem(0, sword);
        inv.setItem(1, bow);
        inv.setHelmet(helmet);
        inv.setChestplate(chestplate);
        inv.setLeggings(leggings);
        inv.setBoots(boots);
    }

    private static ErrorCodes teleportPlayers(Game game) {
        if (game.getBestiaSpawn() == null)
            return ErrorCodes.NO_BESTIA_SPAWN;
        if (game.getPlayersSpawn() == null)
            return ErrorCodes.NO_PLAYERS_SPAWN;

        ArrayList<EscapaBestiaPlayer> players = (ArrayList<EscapaBestiaPlayer>) game.getPlayers();
        for (EscapaBestiaPlayer p : players) {
            if (p.isBestia()) {
                Player bestia = p.getPlayer();
                bestia.sendMessage("teletransportando al spawn de bestias");
                bestia.teleport(game.getBestiaSpawn());
                return ErrorCodes.GOOD;
            } else {
                Player player = p.getPlayer();
                player.sendMessage("teletransportando al spawn de jugadores");
                player.teleport(game.getPlayersSpawn());
                return ErrorCodes.GOOD;
            }
        }
        return ErrorCodes.UNKNOWN_ERROR;
    }

    public static int teleportBestiaToPlayersSpawn(Game game) {
        if (game.getPlayersSpawn() == null)
            return ErrorCodes.NO_PLAYERS_SPAWN.getCode();

        try {
            game.getBestia().getPlayer().teleport(game.getPlayersSpawn());

            return ErrorCodes.GOOD.getCode();
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCodes.UNKNOWN_ERROR.getCode();
        }
    }

    public static void finishGame(Game game, EscapaBestia plugin, boolean bestiaWin) {
        game.setState(GameState.FINISHING);
        game.setGameFinishing(true);
        cooldownM.cooldownFinishGame(game, plugin);
        for (EscapaBestiaPlayer player : game.getPlayers()) {
            if (bestiaWin)
                player.getPlayer().sendMessage(Messages.BestiaWin);
            else
                player.getPlayer().sendMessage(Messages.PlayersWin);
        }
        game.getPlayers().removeAll(game.getPlayers());
//        plugin.log(Level.INFO, game.getPlayers().toString());
        game.setState(GameState.WAITING);
    }
}

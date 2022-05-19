package org.vexmi.escapadelabestia.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
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

    public static ErrorCodes playerJoin(Game game, Player player, @NotNull EscapaBestia plugin) {
        for (Game g : plugin.games) {
            for (EscapaBestiaPlayer ePlayer : g.getPlayers()) {
                if (ePlayer.getPlayer().equals(player)) {
                    return ErrorCodes.PLAYER_ALREADY_IN_GAME;
                }
            }
        }

        if (game.getLobby() == null)
            return ErrorCodes.LOBBY_NOT_FOUND;
        else if (Bukkit.getWorld(game.getLobby().getWorld().getName()) == null)
            return ErrorCodes.WORLD_NOT_FOUND;

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

        player.teleport(game.getLobby());

        if (game.getActualPlayers() >= game.getMinPlayers() && game.getState().equals(GameState.WAITING)) {
            //cooldown
            player.sendMessage(game.getState().getName());
            cooldownM.cooldownStartGame(game, plugin);
        }

        return ErrorCodes.GOOD;
    }

    public static ErrorCodes playerLeave(@NotNull Game game, @NotNull Player player, EscapaBestia plugin, boolean finishingGame, boolean closingServer) {
        EscapaBestiaPlayer edlbP = game.getPlayer(player.getName());
        assert edlbP != null;
        ItemStack[] savedInventory = edlbP.getSaved().getInvSaved();
        ItemStack[] savedEquipment = edlbP.getSaved().getArmorSaved();
        GameMode savedGamemode = edlbP.getSaved().getGamemodeSaved();
        float savedXp = edlbP.getSaved().getXpSaved();
        int savedLevel = edlbP.getSaved().getLevelSaved();
        int savedFood = edlbP.getSaved().getFoodSaved();
        double savedHealth = edlbP.getSaved().getHealthSaved();
        double savedMaxHealth = edlbP.getSaved().getMaxHealthSaved();

        game.removePlayer(edlbP);

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
        World world;
        if (Bukkit.getWorld(mainlobby.getString("MainLobby.world")) == null)
            return ErrorCodes.WORLD_NOT_FOUND;
        else
            world = Bukkit.getWorld(mainlobby.getString("MainLobby.world"));

        double x = Double.parseDouble(mainlobby.getString("MainLobby.x"));
        double y = Double.parseDouble(mainlobby.getString("MainLobby.y"));
        double z = Double.parseDouble(mainlobby.getString("MainLobby.z"));
        float yaw = Float.parseFloat(mainlobby.getString("MainLobby.yaw"));
        float pitch = Float.parseFloat(mainlobby.getString("MainLobby.pitch"));
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
    public void startGame(@NotNull Game game) {
        ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
        for (EscapaBestiaPlayer p : players) {
            p.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
        game.setState(GameState.PLAYING);
        game.determineBestia();

        assert game.getBestia() != null;
        game.getBestia().getPlayer().getInventory().clear();
        setBestiaInv(game.getBestia());

        ErrorCodes tpError = teleportPlayers(game);
        if(tpError.getMessage() != null) {
            for (EscapaBestiaPlayer p : players) {
                p.getPlayer().sendMessage(tpError.getMessage());
            }
        }

        for (EscapaBestiaPlayer p : players) {
            p.getPlayer().sendMessage(Messages.StartGame);
        }

        cooldownM.cooldownGame(game, plugin);
    }

    protected static void setBestiaInv(@NotNull EscapaBestiaPlayer bestia) {
        PlayerInventory inv = bestia.getPlayer().getInventory();

        inv.setItem(0, new ItemStack(Material.DIAMOND_SWORD, 1, (short) 0));
        inv.setItem(1, new ItemStack(Material.BOW, 1, (short) 0));
        inv.setItem(17, new ItemStack(Material.ARROW, 64, (short) 0));

        inv.setHelmet(new ItemStack(Material.DIAMOND_HELMET, 1, (short) 0));
        inv.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE, 1, (short) 0));
        inv.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS, 1, (short) 0));
        inv.setBoots(new ItemStack(Material.DIAMOND_BOOTS, 1, (short) 0));
    }

    protected static ErrorCodes teleportPlayers(@NotNull Game game) {
        if (game.getBestiaSpawn() == null)
            return ErrorCodes.NO_BESTIA_SPAWN;
        if (game.getPlayersSpawn() == null)
            return ErrorCodes.NO_PLAYERS_SPAWN;

        ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
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

    public static ErrorCodes teleportBestiaToPlayersSpawn(@NotNull Game game) {
        if (game.getPlayersSpawn() == null)
            return ErrorCodes.NO_PLAYERS_SPAWN;

        try {
            assert game.getBestia() != null;
            game.getBestia().getPlayer().teleport(game.getPlayersSpawn());

            return ErrorCodes.GOOD;
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCodes.UNKNOWN_ERROR;
        }
    }

    public static void finishGame(@NotNull Game game, EscapaBestia plugin, boolean bestiaWin) {
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

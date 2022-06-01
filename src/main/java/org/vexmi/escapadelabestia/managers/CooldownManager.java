package org.vexmi.escapadelabestia.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.vexmi.escapadelabestia.utils.ActionBarAPI;
import org.vexmi.escapadelabestia.EscapaBestia;
import org.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.classes.GameState;
import org.vexmi.escapadelabestia.utils.Messages;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class CooldownManager {

    int startTaskID;
    int gameTaskID;
    int finishTaskID;
    int particleTaskID;
    int time;

    int bestiaTime;

    void cooldownStartGame(Game game, EscapaBestia plugin) {
        game.setState(GameState.STARTING);
        this.time = 10;
        FileConfiguration messages = plugin.getMessages();
        String path = messages.getString("Messages.GameStartCooldown");
        String timePath = String.valueOf(this.time);
        ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
        for (EscapaBestiaPlayer player : players) {
            player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', path).replaceAll("%time%", timePath));
            player.getPlayer().sendMessage(game.getState().getName());
        }

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        startTaskID = scheduler.scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
            public void run() {
                if (!executeStartGame(game, plugin)) {
                    Bukkit.getScheduler().cancelTask(startTaskID);
                }
            }
        }, 0L, 20L);
    }

    protected boolean executeStartGame(Game game, EscapaBestia plugin) {
        if (game != null && game.getState().equals(GameState.STARTING)) {
            if (time <= 5 && time > 0) {
                ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
                for (EscapaBestiaPlayer player : players) {
                    player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.GameStartCooldown).replaceAll("%time%", String.valueOf(this.time)));
                    player.getPlayer().sendTitle(String.valueOf(this.time), "");
                }
                time--;
                return true;
            } else if (time <= 0) {
                new GameManager(plugin).startGame(game);
                return false;
            } else {
                time--;
                return true;
            }
        } else {
//            assert game != null;
//            ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
//            for (EscapaBestiaPlayer player : players) {
//                player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.NotEnoughPlayers));
//            }
            return true;
        }
    }

    void cooldownGame(Game game, EscapaBestia plugin) {
        this.time = game.getMaxTime();
        this.bestiaTime = 10;

        game.setTime(this.time);

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        particleTaskID = scheduler.scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                assert game.getBestia() != null;
                Location l = game.getBestia().getPlayer().getLocation().clone();
                l.setY(l.getY() + 2.5D);
                new ParticleBuilder(ParticleEffect.FLAME, l)
                        .setSpeed(0.005F)
                        .setAmount(25)
                        .display();
            }
        }, 0L, 5L);
        gameTaskID = scheduler.scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
            public void run() {
                if (!executeGame(game, plugin)) {
                    scheduler.cancelTask(particleTaskID);
                    scheduler.cancelTask(gameTaskID);
                }
            }
        }, 0L, 20L);
    }

    protected boolean executeGame(@NotNull Game game, @NotNull EscapaBestia plugin) {
        if (game.getState().equals(GameState.PLAYING)) {
            if (time <= 0) {
                GameManager.finishGame(game, plugin, true);
                return false;
            } else if (bestiaTime > 0) {
                for (EscapaBestiaPlayer ep : game.getPlayers()) {
                    new ActionBarAPI().sendActionBar(ep.getPlayer(), ChatColor.translateAlternateColorCodes('&', Messages.BestiaCooldown
                            .replaceAll("%bestiaTime%", String.valueOf(bestiaTime))));
//                    ep.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.BestiaCooldown
//                            .replaceAll("%bestiaTime%", String.valueOf(bestiaTime))));
                }
                bestiaTime--;
                game.decreaseTime();
                time--;
            } else if (bestiaTime == 0) {
                GameManager.teleportBestiaToPlayersSpawn(game);
                for (EscapaBestiaPlayer ep : game.getPlayers()) {
                    ep.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.BestiaCooldown
                            .replaceAll("%bestiaTime%", String.valueOf(bestiaTime))));
                    ep.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.BestiaLiberada
                            .replaceAll("%time%", String.valueOf(this.time))));
                }
                game.decreaseTime();
                time--;
                bestiaTime--;
            } else if (time == game.getMaxTime()) {
                for (EscapaBestiaPlayer ep : game.getPlayers()) {
                    ep.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.BestiaCooldown)
                            .replaceAll("%bestiaTime%", String.valueOf(bestiaTime)));
                }
                game.decreaseTime();
                time--;
            } else {
                game.decreaseTime();
                time--;
            }
        } else {
            return false;
        }
//        Location l = game.getBestia().getPlayer().getLocation().clone();
//        l.setY(l.getY() + 2.5D);
//        new ParticleBuilder(ParticleEffect.FLAME, l)
//                .setSpeed(0.005F)
//                .setAmount(25)
//                .display();
        return true;
    }

    void cooldownFinishGame(@NotNull Game game, @NotNull EscapaBestia plugin) {
        this.time = 10;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        finishTaskID = scheduler.scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
            public void run() {
                if (!executeFinishGame(game, plugin)) {
                    Bukkit.getScheduler().cancelTask(finishTaskID);
                }
            }
        }, 0L, 20L);
    }

    protected boolean executeFinishGame(Game game, EscapaBestia plugin) {
        if (game != null && game.getState().equals(GameState.FINISHING)) {
            if (time == 0) {
                new ScoreboardManager(game).hideScoreboard();
                List<String> onWinCmds = plugin.getConfig().getStringList("Config.onWinCmds");
                for (String cmd : onWinCmds) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);
                }
                return false;
            } else {
                game.decreaseTime();
                time--;
                return true;
            }
        } else {
            return false;
        }
    }
}

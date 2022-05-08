package org.vexmi.escapadelabestia.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.vexmi.escapadelabestia.EscapaBestia;
import org.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.classes.GameState;
import org.vexmi.escapadelabestia.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class CooldownManager {

    int startTaskID;
    int gameTaskID;
    int finishTaskID;
    int particleTaskID;
    int time;

    int bestiaTime;

    public void cooldownStartGame(Game game, EscapaBestia plugin) {
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
        startTaskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if (!executeStartGame(game, plugin)) {
                    Bukkit.getScheduler().cancelTask(startTaskID);
                }
            }
        }, 0L, 20L);
    }

    @SuppressWarnings("unlikely-arg-type")
    protected boolean executeStartGame(Game game, EscapaBestia plugin) {
        if (game != null && game.getState().equals(GameState.STARTING)) {
            if (time <= 5 && time > 0) {
                ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
                for (EscapaBestiaPlayer player : players) {
                    player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.GameStartCooldown).replaceAll("%time%", String.valueOf(this.time)));
                }
                game.decreaseTime();
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

    public void cooldownGame(Game game, EscapaBestia plugin) {
        this.time = game.getMaxTime();
        this.bestiaTime = 10;
        game.setTime(this.time);

//        Effect effect = new BleedEffect(EscapaBestia.em);
//        assert game.getBestia() != null;
//        effect.setEntity(game.getBestia().getPlayer());
//        Location l = game.getBestia().getPlayer().getLocation().clone();
//        l.setY(l.getY() + 2);
//        effect.setLocation(l);
//        effect.start();

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        particleTaskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                Location l = game.getBestia().getPlayer().getLocation().clone();
                l.setY(l.getY() + 2.5D);
                new ParticleBuilder(ParticleEffect.FLAME, l)
                        .setSpeed(0.005F)
                        .setAmount(25)
                        .display();
            }
        }, 0L, 5L);
        gameTaskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if (!executeGame(game, plugin)) {
                    Bukkit.getScheduler().cancelTask(gameTaskID);
                }
            }
        }, 0L, 20L);
    }

    protected boolean executeGame(@NotNull Game game, @NotNull EscapaBestia plugin) {
//        assert game.getBestia() != null;
//        Location pL = game.getBestia().getPlayer().getLocation().clone();
//        pL.setY(pL.getY() + 2);
//        Location effectL = pL;
//        effect.setLocation(effectL);
//        effect.start();

        if (game.getState().equals(GameState.PLAYING)) {
            if (time <= 0) {
//                effect.cancel();
                Bukkit.getScheduler().cancelTask(particleTaskID);
                GameManager.finishGame(game, plugin, true);
                return false;
            } else if (bestiaTime > 0) {
                for(EscapaBestiaPlayer ep : game.getPlayers()) {
                    ep.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.BestiaCooldown
                            .replaceAll("%bestiaTime%", String.valueOf(bestiaTime))));
                }
                bestiaTime--;
                game.decreaseTime();
                time--;
                return true;
            } else if (bestiaTime == 0) {
                GameManager.teleportBestiaToPlayersSpawn(game);
                for(EscapaBestiaPlayer ep : game.getPlayers()) {
                    ep.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.BestiaCooldown
                            .replaceAll("%bestiaTime%", String.valueOf(bestiaTime))));
                    ep.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.BestiaLiberada
                            .replaceAll("%time%", String.valueOf(this.time))));
                }
                game.decreaseTime();
                time--;
                bestiaTime--;
                return true;
            } else if (time == game.getMaxTime()) {
                for(EscapaBestiaPlayer ep : game.getPlayers()) {
                    ep.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.BestiaCooldown)
                            .replaceAll("%bestiaTime%", String.valueOf(bestiaTime)));
                }
                game.decreaseTime();
                time--;
                return true;
            } else {
                game.decreaseTime();
                time--;
                return true;
            }
        } else {
            return false;
        }
    }

    public void cooldownFinishGame(@NotNull Game game, @NotNull EscapaBestia plugin) {
        this.time = 10;
        game.setTime(this.time);
        ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
        for (EscapaBestiaPlayer player : players) {
            String timePath = String.valueOf(this.time);
            player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.GameStartCooldown)
                    .replaceAll("%time%", timePath)
                    .replaceAll("%game%", game.getName()));
        }

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        finishTaskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if (!executeFinishGame(game, plugin)) {
                    Bukkit.getScheduler().cancelTask(finishTaskID);
                    return;
                }
            }
        }, 0L, 20L);
    }

    protected boolean executeFinishGame(Game game, EscapaBestia plugin) {
        if (game != null && game.getState().equals(GameState.FINISHING)) {
            game.decreaseTime();
            if (time == 0) {
                List<String> onWinCmds = plugin.getConfig().getStringList("Config.onWinCmds");
                for (String cmd : onWinCmds) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);
                }
                return false;
            } else {
                time--;
                return true;
            }
        } else {
            return false;
        }
    }
}

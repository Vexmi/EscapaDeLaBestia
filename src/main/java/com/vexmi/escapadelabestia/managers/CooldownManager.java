package com.vexmi.escapadelabestia.managers;

import java.util.ArrayList;
import java.util.List;

import com.vexmi.escapadelabestia.EscapaBestia;
import com.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import com.vexmi.escapadelabestia.classes.Game;
import com.vexmi.escapadelabestia.classes.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

public class CooldownManager {

    int taskID;
    int time;
    private EscapaBestia plugin;
    public CooldownManager(EscapaBestia plugin) {
        this.plugin = plugin;
    }

    public void cooldownStartGame(Game game, EscapaBestia plugin) {
        game.setState(GameState.STARTING);
        this.time = 10;
        FileConfiguration messages = plugin.getMessages();
        String path = messages.getString("Messages.GameStartCooldown");
        String timePath = String.valueOf(this.time);
        ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
        for(EscapaBestiaPlayer player : players) {
            player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', path).replaceAll("%time%", timePath));
            player.getPlayer().sendMessage(game.getState().getName());
        }

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if(!executeStartGame(game, plugin)) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    return;
                }
            }
        }, 0L, 20L);
    }

    @SuppressWarnings("unlikely-arg-type")
    protected boolean executeStartGame(Game game, EscapaBestia plugin) {
        if(game != null && game.getState().equals(GameState.STARTING)) {
            if(time <= 5 && time > 0) {
                FileConfiguration messages = plugin.getMessages();
                ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
                for(int i=0;i<players.size();i++) {
                    String path = messages.getString("Messages.GameStartCooldown");
                    String timePath = String.valueOf(this.time);
                    players.get(i).getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', path).replaceAll("%time%", timePath));
                }
                game.decreaseTime();
                time--;
                return true;
            }else if(time <= 0) {
                GameManager.startGame(game);
                return false;
            }else {
                time--;
                return true;
            }
        }else {
            FileConfiguration messages = plugin.getMessages();
            FileConfiguration config = plugin.getConfig();
            ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
            for(int i=0;i<players.size();i++) {
                String path = messages.getString("Messages.NotEnoughPlayers");
                players.get(i).getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', path));
            }
            return false;
        }
    }

    public void cooldownGame(Game game, EscapaBestia plugin) {
        this.time = game.getMaxTime();
        game.setTime(this.time);
        FileConfiguration messages = plugin.getMessages();
        ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
        for(int i=0;i<players.size();i++) {
            String path = messages.getString("Messages.GameStartCooldown");
            String timePath = String.valueOf(this.time);
            players.get(i).getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', path).replaceAll("%time%", timePath));
        }

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if(!executeGame(game, plugin)) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    return;
                }
            }
        }, 0L, 20L);
    }

    protected boolean executeGame(Game game, EscapaBestia plugin) {
        if(game != null && game.getState().equals(GameState.PLAYING)) {
            game.decreaseTime();
            if(time <= 0) {
                GameManager.finishGame(game, plugin, true);
                return false;
            }else {
                time--;
                return true;
            }
        }else {
            return false;
        }
    }

    public void cooldownFinishGame(Game game, final EscapaBestiaPlayer bestia, EscapaBestia plugin) {
        this.time = 10;
        game.setTime(this.time);
        FileConfiguration messages = plugin.getMessages();
        ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
        for(int i=0;i<players.size();i++) {
            String path = messages.getString("Messages.GameStartCooldown");
            String timePath = String.valueOf(this.time);
            players.get(i).getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', path)
                    .replaceAll("%time%", timePath)
                    .replaceAll("%game%", game.getName()));
        }

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if(!executeFinishGame(game, plugin)) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    return;
                }
            }
        }, 0L, 20L);
    }

    @SuppressWarnings("null")
    protected boolean executeFinishGame(Game game, EscapaBestia plugin) {
        if(game != null && game.getState().equals(GameState.FINISHING)) {
            game.decreaseTime();
            if(time == 0) {
                List<String> onWinCmds = plugin.getConfig().getStringList("Config.onWinCmds");
                for(String cmd : onWinCmds)
                {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);
                }
                return false;
            }else {
                time--;
                return true;
            }
        }else {
            return false;
        }
    }
}

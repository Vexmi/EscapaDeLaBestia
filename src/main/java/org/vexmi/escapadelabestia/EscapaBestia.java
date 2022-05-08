package org.vexmi.escapadelabestia;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.classes.GameState;
import org.vexmi.escapadelabestia.cmds.EBCmd;
import org.vexmi.escapadelabestia.events.GameEvents;
import org.vexmi.escapadelabestia.events.InvEvents;
import org.vexmi.escapadelabestia.utils.Config;
import org.vexmi.escapadelabestia.utils.Messages;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Plugin de Escapa de La Bestia para Vexmi MC
 * | Started 24/03/2022
 *
 * @author Adrigamer2950
 * @version 1.0.0-devbuild
 * @since 1.0.0-devbuild
 */
public class EscapaBestia extends JavaPlugin {

    public static PluginManager pm = Bukkit.getServer().getPluginManager();

    private FileConfiguration messages = null;
    private File messagesFile = null;
    private FileConfiguration fileGames = null;
    private File gamesFile = null;
    private FileConfiguration mainlobby = null;
    private File mainlobbyFile = null;
    public ArrayList<Game> games;

    private static EscapaBestia plugin;

    public static ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    public String colorText(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public void onEnable() {
        plugin = this;
        games = new ArrayList<Game>();
        registerConfig();
        Config.init(this);
        registerMessages();
        Messages.init(this);
        //registerMainLobby();
        registerGames();
        registerEvents();
        registerCommands();
        loadGames();

        if (pm.getPlugin("PlaceholderAPI") != null)
            new PlaceHolderAPIExpansion(this).register();

        log(Level.INFO, colorText("Plugin Enabled Successfully"));
    }

    @Override
    public void onDisable() {
        saveGames();
        //saveMainLobby();
        log(Level.INFO, colorText("&4Plugin Disabled Successfully"));
    }

    public void log(Level level, Object msg) {
        getLogger().log(level, String.valueOf(msg));
    }

    public void logException(Level level, String msg, Exception ex) {
        getLogger().log(level, msg, ex);
    }

    public void saveGames() {
        FileConfiguration games = getGamesFile();
        games.set("Games", null);
        for (Game g : this.games) {
            String name = g.getName();
            games.set("Games." + name + ".min_players", g.getMinPlayers());
            games.set("Games." + name + ".max_players", g.getMaxPlayers());
            games.set("Games." + name + ".time", g.getMaxTime());
            Location llobby = g.getLobby();
            if (llobby != null) {
                games.set("Games." + name + ".lobby.world", llobby.getWorld().getName());
                games.set("Games." + name + ".lobby.x", llobby.getX());
                games.set("Games." + name + ".lobby.y", llobby.getY());
                games.set("Games." + name + ".lobby.z", llobby.getZ());
                games.set("Games." + name + ".lobby.yaw", llobby.getYaw());
                games.set("Games." + name + ".lobby.pitch", llobby.getPitch());
            }

            Location lSpawnBestia = g.getBestiaSpawn();
            if (lSpawnBestia != null) {
                games.set("Games." + name + ".bestia.world", lSpawnBestia.getWorld().getName());
                games.set("Games." + name + ".bestia.x", lSpawnBestia.getX());
                games.set("Games." + name + ".bestia.y", lSpawnBestia.getY());
                games.set("Games." + name + ".bestia.z", lSpawnBestia.getZ());
                games.set("Games." + name + ".bestia.yaw", lSpawnBestia.getYaw());
                games.set("Games." + name + ".bestia.pitch", lSpawnBestia.getPitch());
            }

            Location lSpawnPlayers = g.getPlayersSpawn();
            if (lSpawnPlayers != null) {
                games.set("Games." + name + ".players.world", lSpawnPlayers.getWorld().getName());
                games.set("Games." + name + ".players.x", lSpawnPlayers.getX());
                games.set("Games." + name + ".players.y", lSpawnPlayers.getY());
                games.set("Games." + name + ".players.z", lSpawnPlayers.getZ());
                games.set("Games." + name + ".players.yaw", lSpawnPlayers.getYaw());
                games.set("Games." + name + ".players.pitch", lSpawnPlayers.getPitch());
            }

            if (g.getState().equals(GameState.DISABLED))
                games.set("Games." + name + ".enabled", false);
            else
                games.set("Games." + name + ".enabled", true);
        }
        saveGamesFile();
    }

    public void loadGames() {
        FileConfiguration gamesC = getGamesFile();
        if (gamesC.contains("Games")) {
            for (String nameGame : gamesC.getConfigurationSection("Games").getKeys(false)) {
                int minPlayers = Integer.parseInt(gamesC.getString("Games." + nameGame + ".min_players"));
                int maxPlayers = Integer.parseInt(gamesC.getString("Games." + nameGame + ".max_players"));
                int time = Integer.parseInt(gamesC.getString("Games." + nameGame + ".time"));

                Location lLobby = null;
                if (gamesC.contains("Games." + nameGame + ".lobby")) {
                    double xLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".lobby.x"));
                    double yLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".lobby.y"));
                    double zLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".lobby.z"));
                    float yawLobby = Float.parseFloat(gamesC.getString("Games." + nameGame + ".lobby.yaw"));
                    float pitchLobby = Float.parseFloat(gamesC.getString("Games." + nameGame + ".lobby.pitch"));
                    World worldLobby = Bukkit.getWorld(gamesC.getString("Games." + nameGame + ".lobby.world"));
                    if (worldLobby == null) {
                        log(Level.SEVERE, "lobby world for game '" + nameGame + "' is null!");
                    } else {
                        lLobby = new Location(worldLobby, xLobby, yLobby, zLobby, yawLobby, pitchLobby);
                    }
                }

                Location lPSpawn = null;
                if (gamesC.contains("Games." + nameGame + ".players")) {
                    double xLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".players.x"));
                    double yLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".players.y"));
                    double zLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".players.z"));
                    float yawLobby = Float.parseFloat(gamesC.getString("Games." + nameGame + ".players.yaw"));
                    float pitchLobby = Float.parseFloat(gamesC.getString("Games." + nameGame + ".players.pitch"));
                    World worldLobby = Bukkit.getWorld(gamesC.getString("Games." + nameGame + ".players.world"));
                    if (worldLobby == null) {
                        log(Level.SEVERE, "player world for game '" + nameGame + "' is null!");
                    } else {
                        lPSpawn = new Location(worldLobby, xLobby, yLobby, zLobby, yawLobby, pitchLobby);
                    }
                }

                Location lBSpawn = null;
                if (gamesC.contains("Games." + nameGame + ".bestia")) {
                    double xLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".bestia.x"));
                    double yLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".bestia.y"));
                    double zLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".bestia.z"));
                    float yawLobby = Float.parseFloat(gamesC.getString("Games." + nameGame + ".bestia.yaw"));
                    float pitchLobby = Float.parseFloat(gamesC.getString("Games." + nameGame + ".bestia.pitch"));
                    World worldLobby = Bukkit.getWorld(gamesC.getString("Games." + nameGame + ".bestia.world"));
                    if (worldLobby == null) {
                        log(Level.SEVERE, "bestia world for game '" + nameGame + "' is null!");
                    } else {
                        lBSpawn = new Location(worldLobby, xLobby, yLobby, zLobby, yawLobby, pitchLobby);
                    }
                }

                Game game = new Game(nameGame);
                game.setMaxPlayers(maxPlayers);
                game.setMinPlayers(minPlayers);
                game.setLobby(lLobby);
                game.setPlayersSpawn(lPSpawn);
                game.setBestiaSpawn(lBSpawn);
                game.setName(nameGame);
                game.setMaxTime(time);

                boolean enabled = gamesC.getBoolean("Games." + nameGame + ".enabled");
                if (!enabled)
                    game.setState(GameState.DISABLED);
                else
                    game.setState(GameState.WAITING);

                games.add(game);
            }
        }
    }

    public ArrayList<Game> getGames() {
        return this.games;
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public void removeGame(String name) {
        for (Game game : this.games) {
            if (game.getName().equals(name)) {
                this.games.remove(game);
            }
        }
    }

    public Game getGame(String name) {
        for (Game game : this.games) {
            if (game.getName().equals(name)) {
                return game;
            }
        }
        return null;
    }

    public Game getPlayerGame(String playerName) {
        log(Level.INFO, 1);
        for (Game game : this.games) {
            log(Level.INFO, 2);
            ArrayList<EscapaBestiaPlayer> players = game.getPlayers();
            for (EscapaBestiaPlayer player : players) {
                log(Level.INFO, 3);
                if (player.getPlayer().getName().equals(playerName)) {
                    log(Level.INFO, 4);
                    return game;
                }
            }
        }
        return null;
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InvEvents(this), this);
        pm.registerEvents(new GameEvents(this), this);
    }

    private void registerCommands() {
        getCommand("edlb").setExecutor(new EBCmd(this));
    }

    private void registerConfig() {
        File config = new File(this.getDataFolder(), "config.yml");
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

    public FileConfiguration getMessages() {
        if (messages == null) {
            reloadMessages();
        }
        return messages;
    }

    public void reloadMessages() {
        if (messages == null) {
            messagesFile = new File(getDataFolder(), "messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                messages.setDefaults(defConfig);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void saveMessages() {
        try {
            messages.save(messagesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerMessages() {
        messagesFile = new File(this.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            this.getMessages().options().copyDefaults(true);
            saveMessages();
        }
    }

    public FileConfiguration getGamesFile() {
        if (fileGames == null) {
            reloadGames();
        }
        return fileGames;
    }

    public void reloadGames() {
        if (fileGames == null) {
            gamesFile = new File(getDataFolder(), "games.yml");
        }
        fileGames = YamlConfiguration.loadConfiguration(gamesFile);
        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(this.getResource("games.yml"), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                fileGames.setDefaults(defConfig);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void saveGamesFile() {
        try {
            fileGames.save(gamesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerGames() {
        gamesFile = new File(this.getDataFolder(), "games.yml");
        if (!gamesFile.exists()) {
            this.getGamesFile().options().copyDefaults(true);
            saveGamesFile();
        }
    }

    public FileConfiguration getMainLobby() {
        if (messages == null) {
            reloadMessages();
        }
        return messages;
    }

    public void reloadMainLobby() {
        if (mainlobby == null) {
            mainlobbyFile = new File(getDataFolder(), "mainlobby.yml");
        }
        mainlobby = YamlConfiguration.loadConfiguration(mainlobbyFile);
        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(this.getResource("mainlobby.yml"), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                mainlobby.setDefaults(defConfig);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void saveMainLobby() {
        try {
            mainlobby.save(mainlobbyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerMainLobby() {
        mainlobbyFile = new File(this.getDataFolder(), "mainlobby.yml");
        if (!mainlobbyFile.exists()) {
            this.getMainLobby().options().copyDefaults(true);
            saveMainLobby();
        }
    }
}

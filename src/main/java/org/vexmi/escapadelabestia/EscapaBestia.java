package org.vexmi.escapadelabestia;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.classes.GameState;
import org.vexmi.escapadelabestia.cmds.EBCmd;
import org.vexmi.escapadelabestia.events.GameEvents;
import org.vexmi.escapadelabestia.events.InvEvents;
import org.vexmi.escapadelabestia.managers.SignsManager;
import org.vexmi.escapadelabestia.utils.Config;
import org.vexmi.escapadelabestia.utils.Messages;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
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
    private FileConfiguration signs = null;
    private File signsFile = null;
    public ArrayList<Game> games;

    private static EscapaBestia plugin;
    SignsManager signsM = new SignsManager();

    public static EscapaBestia getPlugin() {
        return plugin;
    }

    public String colorText(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public void onEnable() {
        plugin = this;
        games = new ArrayList<>();
        registerConfig();
        Config.init(this);
        registerMessages();
        Messages.init(this);
        registerSignsFile();
        //registerMainLobby();
        registerGames();
        registerEvents();
        registerCommands();

        loadGames();
        loadSigns();

        if (pm.getPlugin("PlaceholderAPI") != null)
            new PlaceHolderAPIExpansion(this).register();

        log(Level.INFO, colorText("Plugin Enabled Successfully"));
    }

    @Override
    public void onDisable() {
        saveGamesFile();
        saveMessages();
        saveConfig();

        try {
            signs.save(signsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveGames();
        saveSigns();
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
        for (Game g : this.games) {
            String nameGame = g.getName();
            games.set("Games." + nameGame + ".min_players", g.getMinPlayers());
            games.set("Games." + nameGame + ".max_players", g.getMaxPlayers());
            games.set("Games." + nameGame + ".time", g.getMaxTime());
            Location llobby = g.getLobby();
            if (llobby != null) {
                games.set("Games." + nameGame + ".lobby.world", llobby.getWorld().getName());
                games.set("Games." + nameGame + ".lobby.x", llobby.getX());
                games.set("Games." + nameGame + ".lobby.y", llobby.getY());
                games.set("Games." + nameGame + ".lobby.z", llobby.getZ());
                games.set("Games." + nameGame + ".lobby.yaw", llobby.getYaw());
                games.set("Games." + nameGame + ".lobby.pitch", llobby.getPitch());
            }

            Location lSpawnBestia = g.getBestiaSpawn();
            if (lSpawnBestia != null) {
                games.set("Games." + nameGame + ".bestia.world", lSpawnBestia.getWorld().getName());
                games.set("Games." + nameGame + ".bestia.x", lSpawnBestia.getX());
                games.set("Games." + nameGame + ".bestia.y", lSpawnBestia.getY());
                games.set("Games." + nameGame + ".bestia.z", lSpawnBestia.getZ());
                games.set("Games." + nameGame + ".bestia.yaw", lSpawnBestia.getYaw());
                games.set("Games." + nameGame + ".bestia.pitch", lSpawnBestia.getPitch());
            }

            Location lSpawnPlayers = g.getPlayersSpawn();
            if (lSpawnPlayers != null) {
                games.set("Games." + nameGame + ".players.world", lSpawnPlayers.getWorld().getName());
                games.set("Games." + nameGame + ".players.x", lSpawnPlayers.getX());
                games.set("Games." + nameGame + ".players.y", lSpawnPlayers.getY());
                games.set("Games." + nameGame + ".players.z", lSpawnPlayers.getZ());
                games.set("Games." + nameGame + ".players.yaw", lSpawnPlayers.getYaw());
                games.set("Games." + nameGame + ".players.pitch", lSpawnPlayers.getPitch());
            }

            Location lSpawnChest = g.getChestsLocation();
            if (lSpawnChest != null) {
                games.set("Games." + nameGame + ".chests.world", lSpawnChest.getWorld().getName());
                games.set("Games." + nameGame + ".chests.x", lSpawnChest.getX());
                games.set("Games." + nameGame + ".chests.y", lSpawnChest.getY());
                games.set("Games." + nameGame + ".chests.z", lSpawnChest.getZ());
                games.set("Games." + nameGame + ".chests.yaw", lSpawnChest.getYaw());
                games.set("Games." + nameGame + ".chests.pitch", lSpawnChest.getPitch());
            }

            if (g.getState().equals(GameState.DISABLED))
                games.set("Games." + nameGame + ".enabled", false);
            else
                games.set("Games." + nameGame + ".enabled", true);
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
                    World worldLobby = getServer().getWorld(gamesC.getString("Games." + nameGame + ".lobby.world"));
                    if (worldLobby == null) {
                        log(Level.SEVERE, "Lobby world for game '" + nameGame + "' is null!");
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
                    World worldLobby = getServer().getWorld(gamesC.getString("Games." + nameGame + ".players.world"));
                    if (worldLobby == null) {
                        log(Level.SEVERE, "Player world for game '" + nameGame + "' is null!");
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
                    World worldLobby = getServer().getWorld(gamesC.getString("Games." + nameGame + ".bestia.world"));
                    if (worldLobby == null) {
                        log(Level.SEVERE, "Bestia world for game '" + nameGame + "' is null!");
                    } else {
                        lBSpawn = new Location(worldLobby, xLobby, yLobby, zLobby, yawLobby, pitchLobby);
                    }
                }

                Location lSpawnChest = null;
                if (gamesC.contains("Games." + nameGame + ".chests")) {
                    double xLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".chests.x"));
                    double yLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".chests.y"));
                    double zLobby = Double.parseDouble(gamesC.getString("Games." + nameGame + ".chests.z"));
                    float yawLobby = Float.parseFloat(gamesC.getString("Games." + nameGame + ".chests.yaw"));
                    float pitchLobby = Float.parseFloat(gamesC.getString("Games." + nameGame + ".chests.pitch"));
                    World worldLobby = getServer().getWorld(gamesC.getString("Games." + nameGame + ".chests.world"));
                    if (worldLobby == null) {
                        log(Level.SEVERE, "Chest world for game '" + nameGame + "' is null!");
                    } else {
                        lSpawnChest = new Location(worldLobby, xLobby, yLobby, zLobby, yawLobby, pitchLobby);
                    }
                }

                Game game = new Game(nameGame);
                game.setMaxPlayers(maxPlayers);
                game.setMinPlayers(minPlayers);
                if (lLobby != null) game.setLobby(lLobby);
                if (lPSpawn != null) game.setPlayersSpawn(lPSpawn);
                if (lBSpawn != null) game.setBestiaSpawn(lBSpawn);
                if (lSpawnChest != null) game.setChestsLocation(lSpawnChest);
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
        this.games.removeIf(game -> game.getName().equals(name));
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
        pm.registerEvents(signsM, this);
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
        defConfigStream = new InputStreamReader(this.getResource("messages.yml"), StandardCharsets.UTF_8);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            messages.setDefaults(defConfig);
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
        defConfigStream = new InputStreamReader(this.getResource("games.yml"), StandardCharsets.UTF_8);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileGames.setDefaults(defConfig);
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
        if (mainlobby == null) {
            reloadMainLobby();
        }
        return mainlobby;
    }

    public void reloadMainLobby() {
        if (mainlobby == null) {
            mainlobbyFile = new File(getDataFolder(), "mainlobby.yml");
        }
        mainlobby = YamlConfiguration.loadConfiguration(mainlobbyFile);
        Reader defConfigStream;
        defConfigStream = new InputStreamReader(this.getResource("mainlobby.yml"), StandardCharsets.UTF_8);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            mainlobby.setDefaults(defConfig);
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

    private void saveSigns() {
        for (Map.Entry<Location, Game> entry : signsM.gameSigns.entrySet()) {
            Game game = entry.getValue();
            Location location = entry.getKey();

            getSignsFile().set("Signs." + game.getName() + ".world", location.getWorld());
            getSignsFile().set("Signs." + game.getName() + ".x", location.getX());
            getSignsFile().set("Signs." + game.getName() + ".y", location.getY());
            getSignsFile().set("Signs." + game.getName() + ".z", location.getZ());
            getSignsFile().set("Signs." + game.getName() + ".yaw", location.getYaw());
            getSignsFile().set("Signs." + game.getName() + ".pitch", location.getPitch());

            getSignsFile().set("Signs." + game.getName() + ".type", "games");
        }

        for (Map.Entry<Location, Game> entry : signsM.chestSigns.entrySet()) {
            Game game = entry.getValue();
            Location location = entry.getKey();

            signs.set("Signs." + game.getName() + ".world", location.getWorld());
            signs.set("Signs." + game.getName() + ".x", location.getX());
            signs.set("Signs." + game.getName() + ".y", location.getY());
            signs.set("Signs." + game.getName() + ".z", location.getZ());
            signs.set("Signs." + game.getName() + ".yaw", location.getYaw());
            signs.set("Signs." + game.getName() + ".pitch", location.getPitch());

            signs.set("Signs." + game.getName() + ".type", "chests");
        }
    }

    private void loadSigns() {
        if (!signs.contains("Signs")) return;
        for (String nameGame : signs.getConfigurationSection("Signs").getKeys(false)) {
            Location l = null;
            double xLobby = Double.parseDouble(signs.getString("Signs." + nameGame + ".x"));
            double yLobby = Double.parseDouble(signs.getString("Signs." + nameGame + ".y"));
            double zLobby = Double.parseDouble(signs.getString("Signs." + nameGame + ".z"));
            float yawLobby = Float.parseFloat(signs.getString("Signs." + nameGame + ".yaw"));
            float pitchLobby = Float.parseFloat(signs.getString("Signs." + nameGame + ".pitch"));
            World worldLobby = getServer().getWorld(signs.getString("Signs." + nameGame + ".world"));
            if (worldLobby == null) {
                log(Level.SEVERE, "World for the sign of game '" + nameGame + "' is null!");
            } else {
                l = new Location(worldLobby, xLobby, yLobby, zLobby, yawLobby, pitchLobby);
            }

            if (getSignsFile().getString("Signs." + nameGame + ".type").equals("games"))
                signsM.gameSigns.put(l, getGame(nameGame));
            else
                signsM.chestSigns.put(l, getGame(nameGame));
        }
    }

    private void registerSignsFile() {
        signsFile = new File(this.getDataFolder(), "signs.yml");
        if (!signsFile.exists()) {
            this.getSignsFile().options().copyDefaults(true);
            try {
                signs.save(signsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getSignsFile() {
        if (signs == null) {
            reloadSignsFile();
        }
        return signs;
    }

    public void reloadSignsFile() {
        if (signs == null) {
            signsFile = new File(getDataFolder(), "signs.yml");
        }
        signs = YamlConfiguration.loadConfiguration(signsFile);
        Reader defConfigStream;
        defConfigStream = new InputStreamReader(this.getResource("signs.yml"), StandardCharsets.UTF_8);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            signs.setDefaults(defConfig);
        }
    }
}

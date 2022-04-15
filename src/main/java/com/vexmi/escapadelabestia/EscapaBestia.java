package com.vexmi.escapadelabestia;

import com.vexmi.escapadelabestia.classes.Game;
import com.vexmi.escapadelabestia.classes.GameState;
import com.vexmi.escapadelabestia.cmds.EBCmd;
import com.vexmi.escapadelabestia.events.InvEvents;
import com.vexmi.escapadelabestia.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;

/*
 * Plugin de Escapa de La Bestia para Vexmi MC
 * Started 24/03/2022
 * @author Adrigamer2950
 */
public class EscapaBestia extends JavaPlugin
{
    PluginDescriptionFile pdffile = getDescription();
    public String version = pdffile.getVersion();
    public String name = ChatColor.RED+"["+ChatColor.GREEN+pdffile.getName()+ ChatColor.BLUE+"]";
    private FileConfiguration messages = null;
    private File messagesFile = null;
    private FileConfiguration fileGames = null;
    private File gamesFile = null;
    private FileConfiguration mainlobby = null;
    private File mainlobbyFile = null;
    public ArrayList<Game> games;
    public GameManager gameM = new GameManager(this);

    public String colorText(String text)
    {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public void onEnable()
    {
        games = new ArrayList<Game>();
        registerConfig();
        registerMessages();
        //registerMainLobby();
        registerGames();
        registerEvents();
        registerCommands();
        loadGames();

        Bukkit.getConsoleSender().sendMessage(colorText(name + " &2Plugin Enabled Successfully"));
    }

    @Override
    public void onDisable()
    {
        saveGames();
        //saveMainLobby();
        Bukkit.getConsoleSender().sendMessage(colorText(name + " &4Plugin Disabled Successfully"));
    }

    public void saveGames() {
        FileConfiguration games = getGamesFile();
        games.set("Games", null);
        for(Game g : this.games) {
            String name = g.getName();
            games.set("Games."+name+".min_players", g.getMinPlayers());
            games.set("Games."+name+".max_players", g.getMaxPlayers());
            games.set("Games."+name+".time", g.getMaxTime());
            Location llobby = g.getLobby();
            if(llobby != null) {
                games.set("Games."+name+".lobby.world", llobby.getWorld().getName());
                games.set("Games."+name+".lobby.x", llobby.getX());
                games.set("Games."+name+".lobby.y", llobby.getY());
                games.set("Games."+name+".lobby.z", llobby.getZ());
                games.set("Games."+name+".lobby.yaw", llobby.getYaw());
                games.set("Games."+name+".lobby.pitch", llobby.getPitch());
            }

            Location lSpawnBestia = g.getBestiaSpawn();
            if(lSpawnBestia != null) {
                games.set("Games."+name+".bestia.Spawn.world", lSpawnBestia.getWorld().getName());
                games.set("Games."+name+".bestia.Spawn.x", lSpawnBestia.getX());
                games.set("Games."+name+".bestia.Spawn.y", lSpawnBestia.getY());
                games.set("Games."+name+".bestia.Spawn.z", lSpawnBestia.getZ());
                games.set("Games."+name+".bestia.Spawn.yaw", lSpawnBestia.getYaw());
                games.set("Games."+name+".bestia.Spawn.pitch", lSpawnBestia.getPitch());
            }

            Location lSpawnPlayers = g.getPlayersSpawn();
            if(lSpawnPlayers != null) {
                games.set("Games."+name+".players.Spawn.world", lSpawnPlayers.getWorld().getName());
                games.set("Games."+name+".players.Spawn.x", lSpawnPlayers.getX());
                games.set("Games."+name+".players.Spawn.y", lSpawnPlayers.getY());
                games.set("Games."+name+".players.Spawn.z", lSpawnPlayers.getZ());
                games.set("Games."+name+".players.Spawn.yaw", lSpawnPlayers.getYaw());
                games.set("Games."+name+".players.Spawn.pitch", lSpawnPlayers.getPitch());
            }

            games.set("Games."+name+".enabled", g.getState().equals(GameState.DISABLED) ? false : true);
        }
        saveGamesFile();
    }

    @SuppressWarnings("unlikely-arg-type")
    public void loadGames() {
        FileConfiguration gamesC = getGamesFile();
        if(gamesC.contains("Games")) {
            for(String nameGame : gamesC.getConfigurationSection("Games").getKeys(false)) {
                int minPlayers = Integer.valueOf(gamesC.getString("Games."+nameGame+".min_players"));
                int maxPlayers = Integer.valueOf(gamesC.getString("Games."+nameGame+".max_players"));
                int time = Integer.valueOf(gamesC.getString("Games."+nameGame+".time"));

                Location lLobby = null;
                if(gamesC.contains("Games."+nameGame+".lobby")) {
                    double xLobby = Double.valueOf(gamesC.getString("Games."+nameGame+".lobby.x"));
                    double yLobby = Double.valueOf(gamesC.getString("Games."+nameGame+".lobby.y"));
                    double zLobby = Double.valueOf(gamesC.getString("Games."+nameGame+".lobby.z"));
                    float yawLobby = Float.valueOf(gamesC.getString("Games."+nameGame+".lobby.yaw"));
                    float pitchLobby = Float.valueOf(gamesC.getString("Games."+nameGame+".lobby.pitch"));
                    World worldLobby = Bukkit.getWorld(gamesC.getString("Games."+nameGame+".lobby.yaw"));
                    lLobby = new Location(worldLobby, xLobby, yLobby, zLobby, yawLobby, pitchLobby);
                }

                Location lPSpawn = null;
                if(games.contains("Games."+nameGame+".players.spawn")) {
                    double xLobby = Double.valueOf(gamesC.getString("Games."+nameGame+".players.spawn.x"));
                    double yLobby = Double.valueOf(gamesC.getString("Games."+nameGame+".players.spawn.y"));
                    double zLobby = Double.valueOf(gamesC.getString("Games."+nameGame+".players.spawn.z"));
                    float yawLobby = Float.valueOf(gamesC.getString("Games."+nameGame+".players.spawn.yaw"));
                    float pitchLobby = Float.valueOf(gamesC.getString("Games."+nameGame+".players.spawn.pitch"));
                    World worldLobby = Bukkit.getWorld(gamesC.getString("Games."+nameGame+".players.spawn.yaw"));
                    lPSpawn = new Location(worldLobby, xLobby, yLobby, zLobby, yawLobby, pitchLobby);
                }

                Location lBSpawn = null;
                if(games.contains("Games."+nameGame+".bestia.spawn")) {
                    double xLobby = Double.valueOf(gamesC.getString("Games."+nameGame+".bestia.spawn.x"));
                    double yLobby = Double.valueOf(gamesC.getString("Games."+nameGame+".bestia.spawn.y"));
                    double zLobby = Double.valueOf(gamesC.getString("Games."+nameGame+".bestia.spawn.z"));
                    float yawLobby = Float.valueOf(gamesC.getString("Games."+nameGame+".bestia.spawn.yaw"));
                    float pitchLobby = Float.valueOf(gamesC.getString("Games."+nameGame+".bestia.spawn.pitch"));
                    World worldLobby = Bukkit.getWorld(gamesC.getString("Games."+nameGame+".bestia.spawn.yaw"));
                    lBSpawn = new Location(worldLobby, xLobby, yLobby, zLobby, yawLobby, pitchLobby);
                }

                Game game = new Game(nameGame);
                game.setMaxPlayers(maxPlayers);
                game.setMinPlayers(minPlayers);
                game.setLobby(lLobby);
                game.setPlayersSpawn(lPSpawn);
                game.setBestiaSpawn(lBSpawn);
                game.setName(nameGame);
                game.setMaxTime(time);

                String enabledS = gamesC.getString("Games."+nameGame+".enabled");
                boolean enabled = enabledS.equals(true);
                if(!enabled) {
                    game.setState(GameState.DISABLED);
                }else {
                    game.setState(GameState.WAITING);
                }

                games.add(game);
            }
        }
    }

    private void registerEvents()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InvEvents(this), this);
    }

    private void registerCommands()
    {
        getCommand("edlb").setExecutor(new EBCmd(this));
    }

    private void registerConfig()
    {
        File config = new File(this.getDataFolder(),"config.yml");
        if(!config.exists()){
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

    public FileConfiguration getMessages(){
        if(messages == null){
            reloadMessages();
        }
        return messages;
    }

    public void reloadMessages(){
        if(messages == null){
            messagesFile = new File(getDataFolder(),"messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        Reader defConfigStream;
        try{
            defConfigStream = new InputStreamReader(this.getResource("messages.yml"),"UTF8");
            if(defConfigStream != null){
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                messages.setDefaults(defConfig);
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public void saveMessages(){
        try{
            messages.save(messagesFile);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void registerMessages(){
        messagesFile = new File(this.getDataFolder(),"messages.yml");
        if(!messagesFile.exists()){
            this.getMessages().options().copyDefaults(true);
            saveMessages();
        }
    }

    public FileConfiguration getGamesFile(){
        if(fileGames == null){
            reloadGames();
        }
        return fileGames;
    }

    public void reloadGames(){
        if(fileGames == null){
            gamesFile = new File(getDataFolder(),"games.yml");
        }
        fileGames = YamlConfiguration.loadConfiguration(gamesFile);
        Reader defConfigStream;
        try{
            defConfigStream = new InputStreamReader(this.getResource("games.yml"),"UTF8");
            if(defConfigStream != null){
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                fileGames.setDefaults(defConfig);
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public void saveGamesFile(){
        try{
            fileGames.save(gamesFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void registerGames(){
        gamesFile = new File(this.getDataFolder(),"games.yml");
        if(!gamesFile.exists()){
            this.getGamesFile().options().copyDefaults(true);
            saveGamesFile();
        }
    }

    public FileConfiguration getMainLobby(){
        if(messages == null){
            reloadMessages();
        }
        return messages;
    }

    public void reloadMainLobby(){
        if(mainlobby == null){
            mainlobbyFile = new File(getDataFolder(),"mainlobby.yml");
        }
        mainlobby = YamlConfiguration.loadConfiguration(mainlobbyFile);
        Reader defConfigStream;
        try{
            defConfigStream = new InputStreamReader(this.getResource("mainlobby.yml"),"UTF8");
            if(defConfigStream != null){
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                mainlobby.setDefaults(defConfig);
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public void saveMainLobby(){
        try{
            mainlobby.save(mainlobbyFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void registerMainLobby(){
        mainlobbyFile = new File(this.getDataFolder(),"mainlobby.yml");
        if(!mainlobbyFile.exists()){
            this.getMainLobby().options().copyDefaults(true);
            saveMainLobby();
        }
    }
}

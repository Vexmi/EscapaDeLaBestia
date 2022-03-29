package com.vexmi.escapadelabestia;

import com.vexmi.escapadelabestia.classes.Game;
import com.vexmi.escapadelabestia.cmds.EBCmd;
import com.vexmi.escapadelabestia.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;

/*
 * Plugin de Escapa de La Bestia para Vexmi MC
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
    public static ArrayList<Game> games = new ArrayList<Game>();
    public GameManager gameM = new GameManager(this);

    public String colorText(String text)
    {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public void onEnable()
    {
        Bukkit.getConsoleSender().sendMessage(colorText(name + " &2Plugin Enabled Successfully"));

        registerEvents();
        registerCommands();
        registerConfig();
        registerMessages();
        registerMainLobby();
        registerGames();
    }

    @Override
    public void onDisable()
    {
        Bukkit.getConsoleSender().sendMessage(colorText(name + " &4Plugin Disabled Successfully"));
        gameM.saveGames();
        saveMainLobby();
    }

    private void registerEvents()
    {
        PluginManager pm = getServer().getPluginManager();
    }

    private void registerCommands()
    {
        getCommand("edlb").setExecutor(new EBCmd(this));
    }

    public void registerConfig()
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

    public void registerMessages(){
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

    public void registerGames(){
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

    public void registerMainLobby(){
        mainlobbyFile = new File(this.getDataFolder(),"mainlobby.yml");
        if(!mainlobbyFile.exists()){
            this.getMainLobby().options().copyDefaults(true);
            saveMainLobby();
        }
    }
}

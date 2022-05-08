package org.vexmi.escapadelabestia.utils;

import org.vexmi.escapadelabestia.EscapaBestia;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Messages {

    public static String NoPermissions;
    public static String OnPlayerJoin;
    public static String GameAlreadyExists;
    public static String CreatedGame;
    public static String GameNotExists;
    public static String GameNotActivated;
    public static String GameIsAlreadyEnabled;
    public static String GameIsAlreadyDisabled;
    public static String PlayerIsInGame;
    public static String PlayerIsntInGame;
    public static String TheGameHasStarted;
    public static String DeletedGame;
    public static String PluginReloaded;
    public static String CreatedLobby;
    public static String SetLobbyCommandHelp;
    public static String SetBestiaSpawnCommandHelp;
    public static String SetPlayersSpawnCommandHelp;
    public static String SetMaxPlayersHelp;
    public static String SetMinPlayersHelp;
    public static String NotNumberValid;
    public static String SetMaxPlayers;
    public static String SetMinPlayers;
    public static String SetTimeHelp;
    public static String SetTime;
    public static String SetMainLobbyCommandHelp;
    public static String MainLobbyNotExists;
    public static String GameIsFull;
    public static String NeedToCreateLobby;
    public static String PlayerJoinGame;
    public static String PlayerLeaveGame;
    public static String GameStartCooldown;
    public static String GameFinishCooldown;
    public static String BestiaCooldown;
    public static String BestiaLiberada;
    public static String NotEnoughPlayers;
    public static String StartGame;
    public static String BestiaWin;
    public static String PlayersWin;
    public static String PlayerDeath;
    public static String PlayerKill;
    public static String PlayerDead;
    public static String BestiaDead;
    public static String PlayerDeadByBestia;

//  ErrorCodes Messages
    public static String ErrorCodes_GOOD = null;
    public static String ErrorCodes_WORLD_NOT_FOUND = ChatColor.translateAlternateColorCodes('&', "&cError! El mundo de esa partida no existe o no es encontrado.");
    public static String ErrorCodes_LOBBY_NOT_FOUND = ChatColor.translateAlternateColorCodes('&', "&cError! EL lobby de esa partida no existe o no fue encontrado.");

    public static void init(EscapaBestia plugin) {
        FileConfiguration messages = plugin.getMessages();

        NoPermissions = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.NoPermissions"));
        OnPlayerJoin = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.OnPlayerJoin"));
        GameAlreadyExists = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.GameAlreadyExists"));
        CreatedGame = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.CreatedGame"));
        GameNotExists = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.GameNotExists"));
        GameNotActivated = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.GameNotActivated"));
        GameIsAlreadyEnabled = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.GameIsAlreadyEnabled"));
        GameIsAlreadyDisabled = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.GameIsAlreadyDisabled"));
        PlayerIsInGame = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PlayerIsInGame"));
        PlayerIsntInGame = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PlayerIsntInGame"));
        TheGameHasStarted = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.TheGameHasStarted"));
        DeletedGame = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.DeletedGame"));
        PluginReloaded = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PluginReloaded"));
        CreatedLobby = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.CreatedLobby"));
        SetLobbyCommandHelp = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.SetLobbyCommandHelp"));
        SetBestiaSpawnCommandHelp = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.SetBestiaSpawnCommandHelp"));
        SetPlayersSpawnCommandHelp = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.SetPlayersSpawnCommandHelp"));
        SetMaxPlayersHelp = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.SetMaxPlayersHelp"));
        SetMinPlayersHelp = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.SetMinPlayersHelp"));
        NotNumberValid = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.NotNumberValid"));
        SetMaxPlayers = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.SetMaxPlayers"));
        SetMinPlayers = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.SetMinPlayers"));
        SetTimeHelp = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.SetTimeHelp"));
        SetTime = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.SetTime"));
        SetMainLobbyCommandHelp = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.SetMainLobbyCommandHelp"));
        MainLobbyNotExists = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.MainLobbyNotExists"));
        GameIsFull = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.GameIsFull"));
        NeedToCreateLobby = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.NeedToCreateLobby"));
        PlayerJoinGame = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PlayerJoinGame"));
        PlayerLeaveGame = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PlayerLeaveGame"));
        GameStartCooldown = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.GameStartCooldown"));
        GameFinishCooldown = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.GameFinishCooldown"));
        BestiaCooldown = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.BestiaCooldown"));
        BestiaLiberada = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.BestiaLiberada"));
        NotEnoughPlayers = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.NotEnoughPlayers"));
        StartGame = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.StartGame"));
        BestiaWin = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.BestiaWin"));
        PlayersWin = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PlayersWin"));
        PlayerDeath = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PlayerDeath"));
        PlayerKill = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PlayerKill"));
        PlayerDead = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PlayerDead"));
        BestiaDead = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.BestiaDead"));
        PlayerDeadByBestia = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.PlayerDeadByBestia"));
    }
}

package com.vexmi.escapadelabestia.classes;

import org.bukkit.Location;

import java.util.ArrayList;

public class Game
{
    private ArrayList<EscapaBestiaPlayer> players = new ArrayList<EscapaBestiaPlayer>();
    private EscapaBestiaPlayer bestia;
    private String name;
    private int minPlayers;
    private int maxPlayers;
    private int actualPlayers;
    private GameState state;
    private Location lobby;
    private Location bestiaSpawn;
    private Location playersSpawn;
    private int time;
    private int maxTime;
    private boolean isGameFinishing;

    public Game(String name)
    {
        this(name, 1, 8);
    }

    public Game(String name, int minPlayers, int maxPlayers)
    {
        this.name = name;
        this.setMinPlayers(minPlayers);
        this.setMaxPlayers(maxPlayers);
        this.setActualPlayers(0);
        this.setState(GameState.WAITING);
        this.time = 0;
        this.maxTime = 60;
        this.isGameFinishing = false;
    }

    public boolean isGameFinishing()
    {
        return isGameFinishing;
    }

    public void setGameFinishing(boolean isGameFinishing)
    {
        this.isGameFinishing = isGameFinishing;
    }

    public Location getBestiaSpawn()
    {
        return bestiaSpawn;
    }

    public Location getPlayersSpawn()
    {
        return playersSpawn;
    }

    public void setBestiaSpawn(Location bestiaSpawn)
    {
        this.bestiaSpawn = bestiaSpawn;
    }

    public void setPlayersSpawn(Location playersSpawn)
    {
        this.playersSpawn = playersSpawn;
    }

    public ArrayList<EscapaBestiaPlayer> getPlayers()
    {
        return players;
    }

    public EscapaBestiaPlayer getPlayer(String player) {
        ArrayList<EscapaBestiaPlayer> players = getPlayers();
        for(EscapaBestiaPlayer p : players) {
            if(p.getPlayer().getName().equals(player)){
                return p;
            }
        }
        return null;
    }

    public boolean addPlayer(EscapaBestiaPlayer player)
    {
        for(EscapaBestiaPlayer p : players)
        {
            if (p.getName().equals(player.getName()))
            {
                return false;
            }
        }
        players.add(player);
        this.setActualPlayers((this.getActualPlayers() == this.getMaxPlayers()) ? this.getActualPlayers() : this.getActualPlayers() + 1);
        return true;
    }

    public boolean removePlayer(EscapaBestiaPlayer player)
    {
        for(EscapaBestiaPlayer p : players)
        {
            if(p.equals(player))
            {
                return false;
            }
        }
        players.remove(player);
        this.setActualPlayers((this.getActualPlayers() == 0) ? this.getActualPlayers() : this.getActualPlayers() - 1);
        return true;
    }

    public boolean removePlayer(String player)
    {
        for(EscapaBestiaPlayer p : players)
        {
            if(p.getName().equals(player))
            {
                players.remove(p);
                this.setActualPlayers((this.getActualPlayers() == 0) ? this.getActualPlayers() : this.getActualPlayers() - 1);
                return true;
            }
        }
        return false;
    }

    public EscapaBestiaPlayer determineBestia()
    {
        int random = (int)(Math.random() * players.size());
        EscapaBestiaPlayer bestia = players.get(random);
        this.bestia = bestia;
        return bestia;
    }

    public EscapaBestiaPlayer getBestia()
    {
        return bestia;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public void decreaseTime() {
        this.time--;
    }

    public void increaseTime() {
        this.time++;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getActualPlayers() {
        return actualPlayers;
    }

    public void setActualPlayers(int actualPlayers) {
        this.actualPlayers = actualPlayers;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public boolean isPlaying() {
        if(state.equals(GameState.WAITING) || state.equals(GameState.DISABLED) || state.equals(GameState.STARTING)) {
            return false;
        }else {
            return true;
        }
    }

    public boolean isFull() {
        if(this.actualPlayers == this.maxPlayers) {
            return true;
        }else {
            return false;
        }
    }

    public boolean isEnabled() {
        if(!state.equals(GameState.DISABLED)) {
            return true;
        }else {
            return false;
        }
    }

    public Location getLobby() {
        return this.lobby;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
    }
}

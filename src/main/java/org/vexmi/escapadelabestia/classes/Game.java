package org.vexmi.escapadelabestia.classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Game {

    private final ArrayList<EscapaBestiaPlayer> players = new ArrayList<>();
    private EscapaBestiaPlayer bestia = null;
    private String name;
    private int minPlayers;
    private int maxPlayers;
    private int actualPlayers;
    private GameState state = null;
    private Location lobby = null;
    private Location bestiaSpawn = null;
    private Location playersSpawn = null;
    private int time;
    private int maxTime;
    private boolean isGameFinishing;

    public Game(String name) {
        this(name, 1, 8);
    }

    public Game(String name, int minPlayers, int maxPlayers) {
        this.name = name;
        this.setMinPlayers(minPlayers);
        this.setMaxPlayers(maxPlayers);
        this.setActualPlayers(0);
        this.setState(GameState.WAITING);
        this.time = 0;
        this.maxTime = 60;
        this.isGameFinishing = false;
    }

    public PlayerInventory getBestiaInv() {
        PlayerInventory inv = (PlayerInventory) Bukkit.createInventory(new InvOwner(null), InventoryType.PLAYER);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1, (short) 0);
        ItemStack bow = new ItemStack(Material.BOW, 1, (short) 0);

        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1, (short) 0);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1, (short) 0);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1, (short) 0);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1, (short) 0);

        inv.setItem(0, sword);
        inv.setItem(1, bow);
        inv.setItem(103, helmet);
        inv.setItem(102, chestplate);
        inv.setItem(101, leggings);
        inv.setItem(100, boots);

        return inv;
    }

    public boolean isGameFinishing() {
        return isGameFinishing;
    }

    public void setGameFinishing(boolean isGameFinishing) {
        this.isGameFinishing = isGameFinishing;
    }

    @Nullable
    public Location getBestiaSpawn() {
        return this.bestiaSpawn;
    }

    @Nullable
    public Location getPlayersSpawn() {
        return this.playersSpawn;
    }

    public void setBestiaSpawn(@NotNull Location bestiaSpawn) {
        this.bestiaSpawn = bestiaSpawn;
    }

    public void setPlayersSpawn(@NotNull Location playersSpawn) {
        this.playersSpawn = playersSpawn;
    }

    @NotNull
    public ArrayList<EscapaBestiaPlayer> getPlayers() {
        return players;
    }

    @Nullable
    public EscapaBestiaPlayer getPlayer(@NotNull String player) {
        ArrayList<EscapaBestiaPlayer> players = getPlayers();
        for (EscapaBestiaPlayer p : players) {
            if (p.getPlayer().getName().equals(player)) {
                return p;
            }
        }
        return null;
    }

    public boolean addPlayer(@NotNull EscapaBestiaPlayer player) {
        for (EscapaBestiaPlayer p : players) {
            if (p.getName().equals(player.getName()))
                return false;
        }
        players.add(player);
        this.setActualPlayers((this.getActualPlayers() == this.getMaxPlayers()) ? this.getActualPlayers() : this.getActualPlayers() + 1);
        return true;
    }

    public boolean removePlayer(@NotNull EscapaBestiaPlayer player) {
        for (EscapaBestiaPlayer p : players) {
            if (p.equals(player)) {
                return false;
            }
        }
        players.remove(player);
        this.setActualPlayers((this.getActualPlayers() == 0) ? this.getActualPlayers() : this.getActualPlayers() - 1);
        return true;
    }

    public boolean removePlayer(@NotNull String player) {
        for (EscapaBestiaPlayer p : players) {
            if (p.getName().equals(player)) {
                players.remove(p);
                this.setActualPlayers((this.getActualPlayers() == 0) ? this.getActualPlayers() : this.getActualPlayers() - 1);
                return true;
            }
        }
        return false;
    }

    public void determineBestia() {
        int random = (int) (Math.random() * players.size());
        EscapaBestiaPlayer bestia = players.get(random);
        bestia.setBestia(true);
        this.bestia = bestia;
    }

    @Nullable
    public EscapaBestiaPlayer getBestia() {
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

    public void setName(String newName) {
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

    @Nullable
    public GameState getState() {
        return state;
    }

    public void setState(@NotNull GameState state) {
        this.state = state;
    }

    public boolean isPlaying() {
        if (state.equals(GameState.WAITING) || state.equals(GameState.DISABLED) || state.equals(GameState.STARTING)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isFull() {
        return (this.actualPlayers == this.maxPlayers);
    }

    public boolean isEnabled() {
        return !state.equals(GameState.DISABLED);
    }

    @Nullable
    public Location getLobby() {
        return this.lobby;
    }

    public void setLobby(@NotNull Location lobby) {
        this.lobby = lobby;
    }
}

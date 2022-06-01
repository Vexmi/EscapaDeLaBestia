package org.vexmi.escapadelabestia.classes;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final ArrayList<EscapaBestiaPlayer> players = new ArrayList<>();
    private EscapaBestiaPlayer bestia = null;
    @NotNull private String name;
    private int minPlayers;
    private int maxPlayers;
    private int actualPlayers;
    private int deadPlayers;
    private GameState state = null;
    private Location lobby = null;
    private Location bestiaSpawn = null;
    private Location playersSpawn = null;
    private Location chestsLocation = null;
    private int time;
    private int maxTime;
    private boolean isGameFinishing;

    public Game(String name) {
        this(name, 4, 12);
    }

    public Game(@NotNull String name, int minPlayers, int maxPlayers) {
        this.name = name;
        this.setMinPlayers(minPlayers);
        this.setMaxPlayers(maxPlayers);
        this.setActualPlayers(0);

        this.setState(GameState.WAITING);
        this.time = 0;
        this.maxTime = 60;
        this.isGameFinishing = false;
    }

    public int getDeadPlayers() {
        List<EscapaBestiaPlayer> deadPlayers = new ArrayList<>();
        for(EscapaBestiaPlayer eplayer : players) {
            if(eplayer.isDead()) {
                deadPlayers.add(eplayer);
            }
        }

        return deadPlayers.size();
    }



    public Location getChestsLocation() {
        return this.chestsLocation;
    }

    public void setChestsLocation(@NotNull Location chestsLocation) {
        this.chestsLocation = chestsLocation;
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

    public void addPlayer(@NotNull EscapaBestiaPlayer player) {
        for (EscapaBestiaPlayer p : players) {
            if (p.getName().equals(player.getName()))
                return;
        }
        players.add(player);
        this.setActualPlayers((this.getActualPlayers() == this.getMaxPlayers()) ? this.getActualPlayers() : this.getActualPlayers() + 1);
    }

    public void removePlayer(@NotNull EscapaBestiaPlayer player) {
        for (EscapaBestiaPlayer p : players) {
            if (p.equals(player)) {
                return;
            }
        }
        players.remove(player);
        this.setActualPlayers((this.getActualPlayers() == 0) ? this.getActualPlayers() : this.getActualPlayers() - 1);
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

    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String newName) {
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

    @NotNull
    public GameState getState() {
        if(state == null) {
            return GameState.DISABLED;
        }

        return state;
    }

    public void setState(@NotNull GameState state) {
        this.state = state;
    }

    public boolean isPlaying() {
        return !state.equals(GameState.WAITING) && !state.equals(GameState.DISABLED) && !state.equals(GameState.STARTING);
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

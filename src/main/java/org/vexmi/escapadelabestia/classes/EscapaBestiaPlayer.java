package org.vexmi.escapadelabestia.classes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class EscapaBestiaPlayer {
    private Player player;
    private PlayerInventory inventory;
    private boolean isBestia;
    private ItemsSaved saved;
    private boolean isDead;

    public EscapaBestiaPlayer(Player player) {
        this(player, false);
    }

    public EscapaBestiaPlayer(Player player, boolean isBestia) {
        this.player = player;
        this.inventory = player.getInventory();
        this.isBestia = isBestia;
        this.saved = new ItemsSaved(player.getInventory().getContents(), player.getInventory().getArmorContents(), player.getGameMode(),
                player.getExp(), player.getLevel(), player.getFoodLevel(), player.getHealth(), player.getMaxHealth());
    }

    public ItemsSaved getSaved() {
        return this.saved;
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return player.getName();
    }

    public boolean isBestia() {
        return isBestia;
    }

    public void setBestia(boolean bestia) {
        isBestia = bestia;
    }

    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        this.isDead = dead;
    }
}

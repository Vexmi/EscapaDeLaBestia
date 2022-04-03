package com.vexmi.escapadelabestia.classes;

import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

public class ItemsSaved {

    private ItemStack[] invSaved;
    private ItemStack[] armorSaved;
    private GameMode gamemodeSaved;
    private float xpSaved;
    private int levelSaved;
    private int foodSaved;
    private double healthSaved;
    private double maxHealthSaved;
    public ItemsSaved(ItemStack[] invSaved, ItemStack[] armorSaved, GameMode gamemodeSaved, float xpSaved,
                      int levelSaved, int foodSaved, double healthSaved, double maxHealthSaved) {
        super();
        this.invSaved = invSaved;
        this.armorSaved = armorSaved;
        this.gamemodeSaved = gamemodeSaved;
        this.xpSaved = xpSaved;
        this.levelSaved = levelSaved;
        this.foodSaved = foodSaved;
        this.healthSaved = healthSaved;
        this.maxHealthSaved = maxHealthSaved;
    }

    public ItemStack[] getInvSaved() {
        return invSaved;
    }

    public void setInvSaved(ItemStack[] invSaved) {
        this.invSaved = invSaved;
    }

    public ItemStack[] getArmorSaved() {
        return armorSaved;
    }

    public void setArmorSaved(ItemStack[] armorSaved) {
        this.armorSaved = armorSaved;
    }

    public GameMode getGamemodeSaved() {
        return gamemodeSaved;
    }

    public void setGamemodeSaved(GameMode gamemodeSaved) {
        this.gamemodeSaved = gamemodeSaved;
    }

    public float getXpSaved() {
        return xpSaved;
    }

    public void setXpSaved(float xpSaved) {
        this.xpSaved = xpSaved;
    }

    public int getLevelSaved() {
        return levelSaved;
    }

    public void setLevelSaved(int levelSaved) {
        this.levelSaved = levelSaved;
    }

    public int getFoodSaved() {
        return foodSaved;
    }

    public void setFoodSaved(int foodSaved) {
        this.foodSaved = foodSaved;
    }

    public double getHealthSaved() {
        return healthSaved;
    }

    public void setHealthSaved(double healthSaved) {
        this.healthSaved = healthSaved;
    }

    public double getMaxHealthSaved() {
        return maxHealthSaved;
    }

    public void setMaxHealthSaved(double maxHealthSaved) {
        this.maxHealthSaved = maxHealthSaved;
    }


}

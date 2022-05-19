package org.vexmi.escapadelabestia.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.vexmi.escapadelabestia.classes.Game;

import java.util.ArrayList;
import java.util.List;

public class ChestsManager {

    public static void spawnChests(Game game) {
        double x = game.getChestsLocation().getX();
        double y = game.getChestsLocation().getY();
        double z = game.getChestsLocation().getZ();

        List<Block> chests = new ArrayList<>();

        game.getChestsLocation().getWorld().getBlockAt((int) x + 2, (int) y, (int) z).setType(Material.CHEST);
        chests.add(game.getChestsLocation().getWorld().getBlockAt((int) x + 2, (int) y, (int) z));

        game.getChestsLocation().getWorld().getBlockAt((int) x + 2, (int) y, (int) z + 2).setType(Material.CHEST);
        chests.add(game.getChestsLocation().getWorld().getBlockAt((int) x + 2, (int) y, (int) z + 2));

        game.getChestsLocation().getWorld().getBlockAt((int) x + 2, (int) y, (int) z - 2).setType(Material.CHEST);
        chests.add(game.getChestsLocation().getWorld().getBlockAt((int) x + 2, (int) y, (int) z - 2));

        for(Block block : chests) {
            Chest chest = (Chest) block.getState();
            chest.getBlockInventory().setContents(getChestsInventory().getContents());
            chest.update();
        }
    }

    static Inventory getChestsInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, "Cofres para chetarte uwu");

        inv.setItem(0, new ItemStack(Material.DIAMOND_HELMET, 1));
        inv.setItem(1, new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
        inv.setItem(2, new ItemStack(Material.DIAMOND_LEGGINGS, 1));
        inv.setItem(3, new ItemStack(Material.DIAMOND_BOOTS, 1));

        inv.setItem(18, new ItemStack(Material.DIAMOND_SWORD, 1));
        inv.setItem(19, new ItemStack(Material.BOW, 1));
        inv.setItem(20, new ItemStack(Material.ARROW, 64));

        return inv;
    }
}

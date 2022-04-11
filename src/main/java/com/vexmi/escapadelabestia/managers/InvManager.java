package com.vexmi.escapadelabestia.managers;

import com.vexmi.escapadelabestia.EscapaBestia;
import com.vexmi.escapadelabestia.classes.Game;
import com.vexmi.escapadelabestia.classes.GameState;
import com.vexmi.escapadelabestia.classes.InvOwner;
import com.vexmi.escapadelabestia.utils.ErrorCodes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class InvManager
{
    private EscapaBestia plugin;
    public InvManager(EscapaBestia plugin)
    {
        this.plugin = plugin;
    }
    private GameManager gameM = new GameManager(plugin);

    public int createJoinGamesInv(Player player, int actualPage)
    {
        if(plugin == null)
        {
            return ErrorCodes.UNKNOWN_ERROR.getCode();
        }
        FileConfiguration config = plugin.getConfig();
        String invName = config.getString("Config.joinGamesInv.name");
        String fillGlassM = config.getString("Config.joinGamesInv.fill-glass.material");
        int fillGlassAmount = config.getInt("Config.joinGamesInv.fill-glass.amount");
        int fillGlassDatatag = config.getInt("Config.joinGamesInv.fill-glass.datatag");
        if(Material.getMaterial(fillGlassM) == null)
        {
            return ErrorCodes.MATERIAL_NOT_FOUND.getCode();
        }
        InvOwner holder = new InvOwner(null);
        Inventory joinGamesInv = Bukkit.createInventory(holder, 54, ChatColor.translateAlternateColorCodes('&', invName));
        ItemStack item = new ItemStack(Material.getMaterial(fillGlassM), fillGlassAmount, (short) fillGlassDatatag);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r"));
        item.setItemMeta(meta);
        for(int i = 0 ; i <= 8 ; i++)
        {
            joinGamesInv.setItem(i, item);
        }
        for(int i = 45 ; i <= 53 ; i++)
        {
            joinGamesInv.setItem(i, item);
        }
        for(int i = 9 ; i <= 17 ; i++)
        {
            joinGamesInv.setItem(i, item);
        }
        for(int i = 36 ; i <= 45 ; i++)
        {
            joinGamesInv.setItem(i, item);
        }

        joinGamesInv.setItem(18, item);
        joinGamesInv.setItem(27, item);
        joinGamesInv.setItem(19, item);
        joinGamesInv.setItem(28, item);

        joinGamesInv.setItem(26, item);
        joinGamesInv.setItem(35, item);
        joinGamesInv.setItem(25, item);
        joinGamesInv.setItem(34, item);

        List<Game> gamesList = (ArrayList<Game>) gameM.getGames();

        int pos = 0;
        for(Game game : gamesList)
        {
            if(pos == 0)
            {
                pos = 20;
            }
            else if(pos == 25)
            {
                pos = 29;
            }
            else if(pos == 34)
            {
                break;
            }
            GameState state = game.getState();
            ItemStack stack = state.getItem();
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a" + game.getName()));
            List<String> lore = new ArrayList<String>();
            lore.add(state.getName());
            lore.add(game.getActualPlayers() + " / " + game.getMaxPlayers() + " jugadores");
            stackMeta.setLore(lore);
            stack.setItemMeta(stackMeta);

            joinGamesInv.setItem(pos, state.getItem());
            pos++;
        }

        player.openInventory(joinGamesInv);

        return ErrorCodes.GOOD.getCode();
    }
}

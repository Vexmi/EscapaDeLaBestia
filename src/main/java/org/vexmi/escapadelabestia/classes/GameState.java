package org.vexmi.escapadelabestia.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum GameState
{
    DISABLED("DISABLED", new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 0, ChatColor.GRAY),
    WAITING("WAITING", new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 1, ChatColor.DARK_GRAY),
    STARTING("STARTING", new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 2, ChatColor.GOLD),
    PLAYING("PLAYING", new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 3, ChatColor.GREEN),
    FINISHING("FINISHING", new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 4, ChatColor.RED);

    private static final GameState[] META_LOOKUP = new GameState[values().length];
    private final ItemStack item;
    private final int meta;
    private final String name, unlocalizedName;
    private final ChatColor color;

    GameState(String name, ItemStack item, int meta, ChatColor color)
    {
        this(name, name, item, meta, ChatColor.GRAY);
    }

    GameState(String name, String unlocalizedName, ItemStack item, int meta, ChatColor color)
    {
        this.item = item;
        this.meta = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
        this.color = color;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    public int getMeta()
    {
        return this.meta;
    }

    public String getName()
    {
        return this.name;
    }

    public String getUnlocalizedName()
    {
        return this.unlocalizedName;
    }

    public static GameState byMetadata(int meta)
    {
        return META_LOOKUP[meta];
    }

    public ChatColor getColor() {
        return this.color;
    }

    static
    {
        for(GameState gamestate : values())
        {
            META_LOOKUP[gamestate.getMeta()] = gamestate;
        }
    }
}

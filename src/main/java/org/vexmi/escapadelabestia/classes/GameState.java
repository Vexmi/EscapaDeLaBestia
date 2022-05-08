package org.vexmi.escapadelabestia.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum GameState
{
    DISABLED("DISABLED", new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 0),
    WAITING("WAITING", new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 1),
    STARTING("STARTING", new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 2),
    PLAYING("PLAYING", new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 3),
    FINISHING("FINISHING", new ItemStack(Material.STAINED_CLAY, 1, (short) 15), 4);

    private static final GameState[] META_LOOKUP = new GameState[values().length];
    private final ItemStack item;
    private final int meta;
    private final String name, unlocalizedName;

    private GameState(String name, ItemStack item, int meta)
    {
        this(name, name, item, meta);
    }

    private GameState(String name, String unlocalizedName, ItemStack item, int meta)
    {
        this.item = item;
        this.meta = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
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

    static
    {
        for(GameState gamestate : values())
        {
            META_LOOKUP[gamestate.getMeta()] = gamestate;
        }
    }
}

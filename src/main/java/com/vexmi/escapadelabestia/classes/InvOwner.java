package com.vexmi.escapadelabestia.classes;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Esta clase sirve para identificar inventarios del plugin y los de otro plugin/vanilla
 */
public class InvOwner implements InventoryHolder
{
    private Inventory inv;
    public InvOwner(Inventory inv)
    {
        this.inv = inv;
    }

    @Override
    public Inventory getInventory()
    {
        return inv;
    }

    public void setInventory(Inventory inv)
    {
        this.inv = inv;
    }
}

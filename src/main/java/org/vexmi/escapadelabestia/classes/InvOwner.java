package org.vexmi.escapadelabestia.classes;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Esta clase sirve para identificar los inventarios del plugin
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

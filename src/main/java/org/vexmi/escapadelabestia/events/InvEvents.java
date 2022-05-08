package org.vexmi.escapadelabestia.events;

import org.vexmi.escapadelabestia.EscapaBestia;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.classes.InvOwner;
import org.vexmi.escapadelabestia.managers.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.vexmi.escapadelabestia.utils.ErrorCodes;

public class InvEvents implements Listener {
    private EscapaBestia plugin;

    public InvEvents(EscapaBestia plugin) {
        this.plugin = plugin;
    }

    private GameManager gameM = new GameManager(plugin);

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof InvOwner) {
            Player player = (Player) event.getWhoClicked();
            Inventory inv = event.getInventory();
            String joinGamesInv = plugin.getConfig().getString("Config.joinGamesInv.name");
            if (ChatColor.stripColor(inv.getName()).equals(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', joinGamesInv)))) {
                event.setCancelled(true);
                String glassM = plugin.getConfig().getString("Config.joinGamesInv.fill-glass.material");
                if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.getMaterial(glassM))
                    return;
                else {
                    ItemStack stack = event.getCurrentItem();
                    String gameName = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
                    if (plugin.getGame(gameName) != null) {
                        Game game = plugin.getGame(gameName);
                        if (game.isEnabled())
                            if (plugin.getPlayerGame(player.getName()) == null)
                                if (!game.isPlaying())
                                    if (!game.isFull())
                                        if(GameManager.playerJoin(game, player, plugin).getCode() != 0)
                                            player.sendMessage(GameManager.playerJoin(game, player, plugin).getMessage());
                    } else {
                        player.closeInventory();
                        player.sendMessage(ErrorCodes.UNKNOWN_ERROR.getMessage());
                    }
                }
            }
        }
    }
}

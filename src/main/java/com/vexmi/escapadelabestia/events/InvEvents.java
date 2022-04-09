package com.vexmi.escapadelabestia.events;

import com.vexmi.escapadelabestia.EscapaBestia;
import com.vexmi.escapadelabestia.classes.Game;
import com.vexmi.escapadelabestia.classes.InvOwner;
import com.vexmi.escapadelabestia.managers.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvEvents implements Listener
{
    private EscapaBestia plugin;
    public InvEvents(EscapaBestia plugin)
    {
        this.plugin = plugin;
    }
    private GameManager gameM = new GameManager(plugin);

    @EventHandler
    public void onInvClick(InventoryClickEvent event)
    {
        if(event.getInventory().getHolder() instanceof InvOwner)
        {
            Player player = (Player) event.getWhoClicked();
            Inventory inv = event.getInventory();
            String joinGamesInv = plugin.getConfig().getString("Config.joinGamesInv.name");
            player.sendMessage(inv.getName());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', joinGamesInv));
            if(ChatColor.stripColor(inv.getName()).equals(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', joinGamesInv))))
            {
                event.setCancelled(true);
                String glassM = plugin.getConfig().getString("Config.joinGamesInv.fill-glass.material");
                if(event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.getMaterial(glassM))
                {
                    return;
                }
                else
                {
                    ItemStack stack = event.getCurrentItem();
                    String gameName = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
                    if(gameM.getGame(gameName) != null)
                    {
                        Game game = gameM.getGame(gameName);
                        if(game.isEnabled())
                        {
                            if(gameM.getPlayerGame(player.getName()) == null)
                            {
                                if(!game.isPlaying())
                                {
                                    if(!game.isFull())
                                    {
                                        if(GameManager.playerJoin(game, player, plugin) == 0)
                                        {
                                            return;
                                        }
                                        else if(GameManager.playerJoin(game, player, plugin) == 1)
                                        {
                                            player.sendMessage(plugin.colorText("&cError! El mundo de esa partida no existe o no es encontrado."));
                                            return;
                                        }
                                        else if(GameManager.playerJoin(game, player, plugin) == 2)
                                        {
                                            player.sendMessage(plugin.colorText("&cError! EL lobby de esa partida no existe o no fue encontrado."));
                                            return;
                                        }
                                    }
                                    else
                                    {
                                        player.sendMessage("5");
                                    }
                                }else
                                {
                                    player.sendMessage("4");
                                }

                            }
                            else
                            {
                                player.sendMessage("3");
                            }
                        }
                    }
                    else
                    {
                        player.closeInventory();
                        player.sendMessage("Error!");

                        return;
                    }
                }
            }
            else
            {
                event.getWhoClicked().sendMessage("2");
            }
        }
        else
        {
            event.getWhoClicked().sendMessage("1");
        }
    }
}

package com.vexmi.escapadelabestia.events;

import com.vexmi.escapadelabestia.EscapaBestia;
import com.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import com.vexmi.escapadelabestia.classes.Game;
import com.vexmi.escapadelabestia.managers.GameManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GameEvents implements Listener
{
    private EscapaBestia plugin;
    public GameEvents(EscapaBestia plugin)
    {
        this.plugin = plugin;
    }
    private GameManager gameM = new GameManager(plugin);

    @EventHandler()
    public void onEntityDamage(EntityDamageByEntityEvent e)
    {
        Entity entity = e.getEntity();
        Entity damagedEntity = e.getDamager();
        if((entity instanceof Player) && (damagedEntity instanceof Player))
        {
            Player player = (Player) entity;
            Player damagedPlayer = (Player) damagedEntity;
            if(gameM.getPlayerGame(player.getName()) != null)
            {
                Game game = gameM.getPlayerGame(player.getName());
                if(game.isGameFinishing())
                {
                    e.setCancelled(true);
                }
                else
                {
                    EscapaBestiaPlayer ePlayer = game.getPlayer(player.getName());
                    EscapaBestiaPlayer eBestia = game.getBestia();
                    if(damagedPlayer != eBestia)
                    {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}

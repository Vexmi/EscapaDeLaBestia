package org.vexmi.escapadelabestia.events;

import org.vexmi.escapadelabestia.EscapaBestia;
import org.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.managers.GameManager;
import org.vexmi.escapadelabestia.utils.GameUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.logging.Level;

public class GameEvents implements Listener {

    private EscapaBestia plugin;

    public GameEvents(EscapaBestia plugin) {
        this.plugin = plugin;
    }

    private final GameManager gameM = new GameManager(plugin);

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if ((e.getEntity() instanceof Player)) {
            Player player = (Player) e.getDamager();
            Player damagedPlayer = (Player) e.getEntity();
            if (plugin.getPlayerGame(damagedPlayer.getName()) != null) {
                Game game = plugin.getPlayerGame(damagedPlayer.getName());
                if (game.isGameFinishing()) {
                    e.setCancelled(true);
                } else {
                    EscapaBestiaPlayer ePlayer = game.getPlayer(damagedPlayer.getName());
                    EscapaBestiaPlayer eBestia = game.getBestia();
                    if (eBestia.getPlayer() == player)
                        if (ePlayer == eBestia)
                            e.setCancelled(true);
                    else
                        e.setCancelled(true);
                }
            }
        }
    }
//
//    @EventHandler(priority = EventPriority.HIGH)
//    public void onEntityDamage(EntityDamageEvent e) {
//        if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
//            Player player = (Player) e.getEntity();
//
//        }
//    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = e.getEntity().getKiller();
            Player killed = (Player) e.getEntity();

            if ((plugin.getPlayerGame(killed.getName()) != null)) {
                Game game = plugin.getPlayerGame(killed.getName());
                EscapaBestiaPlayer eKilled = game.getPlayer(killed.getName());
                EscapaBestiaPlayer eKiller = game.getPlayer(player.getName());
                EscapaBestiaPlayer eBestia = game.getBestia();
                if (eKiller == eBestia)
                    GameUtils.sendKillMsgToAllPlayers(game, gameM, eKilled, eKiller, plugin);
                else if (eKiller == null && player == null)
                    GameUtils.sendKillMsgToAllPlayers(game, gameM, eKilled, null, plugin);
                else
                    GameUtils.sendKillMsgToAllPlayers(game, gameM, eKilled, null, plugin);

                eKilled.setDead(true);
            }
        }
    }
}

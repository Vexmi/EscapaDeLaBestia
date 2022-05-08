package org.vexmi.escapadelabestia.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.vexmi.escapadelabestia.EscapaBestia;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.managers.GameManager;

public class GeneralListener implements Listener {

    private EscapaBestia plugin;
    public GeneralListener(EscapaBestia plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if(plugin.getPlayerGame(e.getPlayer().getName()) != null) {
            Game game = plugin.getPlayerGame(e.getPlayer().getName());
            GameManager.playerLeave(game, e.getPlayer(), plugin, false, false);
        }
    }
}

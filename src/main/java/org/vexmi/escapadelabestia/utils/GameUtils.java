package org.vexmi.escapadelabestia.utils;

import org.vexmi.escapadelabestia.EscapaBestia;
import org.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.managers.GameManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GameUtils {

    public static void sendKillMsgToAllPlayers(@NotNull Game game, @NotNull GameManager manager, @NotNull EscapaBestiaPlayer killed, @Nullable EscapaBestiaPlayer killer, @NotNull EscapaBestia plugin) {
        if (killer == null) {
            String message = Messages.PlayerDead.replaceAll("%player%", killed.getPlayer().getName());

            String replacedMessage = message;
            if(plugin.getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null) {
                replacedMessage = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(killed.getPlayer(), message);
            }

            for (EscapaBestiaPlayer p : game.getPlayers()) {
                p.getPlayer().sendMessage(replacedMessage);
            }
        } else {
            String message = Messages.PlayerDeadByBestia
                    .replaceAll("%player%", killed.getPlayer().getName())
                    .replaceAll("%bestia%", killer.getPlayer().getName());

            String replacedMessage = message;
            if(plugin.getServer().getPluginManager().getPlugin("PlaceHolderAPI") != null) {
                replacedMessage = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(killed.getPlayer(), message);
            }

            for (EscapaBestiaPlayer p : game.getPlayers()) {
                p.getPlayer().sendMessage(replacedMessage);
            }
        }
    }
}

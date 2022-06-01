package org.vexmi.escapadelabestia.managers;

import dev.jcsoftware.jscoreboards.JGlobalMethodBasedScoreboard;
import org.bukkit.ChatColor;
import org.vexmi.escapadelabestia.classes.EscapaBestiaPlayer;
import org.vexmi.escapadelabestia.classes.Game;

import java.util.ArrayList;
import java.util.List;

class ScoreboardManager {

     private final JGlobalMethodBasedScoreboard scoreboard;
     private final Game game;

     public ScoreboardManager(Game game) {
          scoreboard = new JGlobalMethodBasedScoreboard();
          this.game = game;
     }

     public void showScoreboard() {
          List<String> lines = new ArrayList<>();
          lines.add(" ");
          lines.add(ChatColor.translateAlternateColorCodes('&', "&2Mapa: &6" + game.getName()));
          lines.add(" ");
          assert game.getBestia() != null;
          lines.add(ChatColor.translateAlternateColorCodes('&', "&2Bestia: &6" + game.getBestia().getPlayer().getDisplayName()));
          lines.add(" ");
          lines.add(ChatColor.translateAlternateColorCodes('&', "*la ip del server*"));

          scoreboard.setTitle(ChatColor.translateAlternateColorCodes('&', "&6Escapa de la Bestia"));
          scoreboard.setLines(lines);

          for(EscapaBestiaPlayer eplayer : game.getPlayers()) {
               scoreboard.addPlayer(eplayer.getPlayer());
               scoreboard.updateScoreboard();
          }
     }

     public void hideScoreboard() {
          for(EscapaBestiaPlayer eplayer : game.getPlayers()) {
               scoreboard.removePlayer(eplayer.getPlayer());
               scoreboard.updateScoreboard();
          }
          scoreboard.destroy();
     }
}

package org.vexmi.escapadelabestia.managers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.vexmi.escapadelabestia.EscapaBestia;
import org.vexmi.escapadelabestia.classes.Game;
import org.vexmi.escapadelabestia.utils.ErrorCodes;

import java.util.HashMap;
import java.util.logging.Level;

public class SignsManager implements Listener {

    public HashMap<Location, Game> chestSigns = new HashMap<>();
    public HashMap<Location, Game> gameSigns = new HashMap<>();

    void createChestsTPSign(SignChangeEvent e) {
        if (EscapaBestia.getPlugin().getGame(e.getLine(2)) != null) {
            chestSigns.put(e.getBlock().getLocation(), EscapaBestia.getPlugin().getGame(e.getLine(2)));
            e.setLine(0, ChatColor.translateAlternateColorCodes('&', e.getLine(0)));
            e.setLine(1, ChatColor.translateAlternateColorCodes('&', "&2Para ir a los"));
            e.setLine(2, ChatColor.translateAlternateColorCodes('&', "&2cofres haz"));
            e.setLine(3, ChatColor.translateAlternateColorCodes('&', "&2click aqui."));
        }
    }

    void createGameJoinSign(SignChangeEvent e) {
        if (EscapaBestia.getPlugin().getGame(e.getLine(1)) != null) {
            Game game = EscapaBestia.getPlugin().getGame(e.getLine(1));
            gameSigns.put(e.getBlock().getLocation(), game);
            e.setLine(0, ChatColor.translateAlternateColorCodes('&', "&6EDLB"));
            e.setLine(1, ChatColor.translateAlternateColorCodes('&', "&1&l" + e.getLine(1)));
            e.setLine(2, ChatColor.translateAlternateColorCodes('&', game.getState().getColor() + game.getState().getName()));
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent e) {
        if (e.getLine(0) == null) return;
        if (e.getLine(1) == null) return;
        if (e.getLine(2) == null) return;

        if (e.getLine(0).equals("[EDLB]")) {
            if (e.getLine(1).equals("Chests")) {
                createChestsTPSign(e);
            } else {
                createGameJoinSign(e);
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Player p = e.getPlayer();
            EscapaBestia.getPlugin().log(Level.INFO, "asd1");
            if (chestSigns.get(e.getClickedBlock().getLocation()) != null) {
                if(EscapaBestia.getPlugin().getGame(chestSigns.get(e.getClickedBlock().getLocation()).getName()) != null) {
                    Game game = EscapaBestia.getPlugin().getGame(chestSigns.get(e.getClickedBlock().getLocation()).getName());
                    assert game.getChestsLocation() != null;
                    p.teleport(game.getChestsLocation());
                }
            } else {
                EscapaBestia.getPlugin().log(Level.INFO, "asd2");
                if(gameSigns.get(e.getClickedBlock().getLocation()) != null) {
                    if (EscapaBestia.getPlugin().getGame(gameSigns.get(e.getClickedBlock().getLocation()).getName()) != null) {
                        Game game = EscapaBestia.getPlugin().getGame(gameSigns.get(e.getClickedBlock().getLocation()).getName());
                        if (game != null) {
                            if (game.isEnabled()) {
                                if (EscapaBestia.getPlugin().getPlayerGame(p.getName()) == null) {
                                    if (!game.isPlaying()) {
                                        if (!game.isFull()) {
                                            ErrorCodes error = GameManager.playerJoin(game, p, EscapaBestia.getPlugin());

                                            p.sendMessage(error.getMessage());
                                        } else {
                                            p.sendMessage(ErrorCodes.GAME_IS_FULL.getMessage());
                                        }
                                    } else {
                                        p.sendMessage(ErrorCodes.GAME_IS_PLAYING.getMessage());
                                    }

                                } else {
                                    p.sendMessage(ErrorCodes.PLAYER_ALREADY_IN_GAME.getMessage());
                                }
                            } else {
                                p.sendMessage(ErrorCodes.GAME_NOT_ENABLED.getMessage());
                            }
                        } else {
                            p.sendMessage(ErrorCodes.GAME_NOT_FOUND.getMessage());
                        }
                    }
                }
            }
        }
    }
}

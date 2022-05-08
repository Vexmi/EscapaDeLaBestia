package org.vexmi.escapadelabestia.utils;

import org.vexmi.escapadelabestia.EscapaBestia;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {

    public static List<String> onWinCmds;

    public static String joinGamesInv_name;
    public static String joinGamesInv_fillglass_material;
    public static String joinGamesInv_fillglass_amount;
    public static String joinGamesInv_fillglass_datatag;

    public static void init(EscapaBestia plugin) {
        FileConfiguration config = plugin.getConfig();

        onWinCmds = config.getStringList("Config.onWinCmds");

        joinGamesInv_name = config.getString("Config.joinGamesInv.name");
        joinGamesInv_fillglass_material = config.getString("Config.joinGamesInv.fill-glass.material");
        joinGamesInv_fillglass_amount = config.getString("Config.joinGamesInv.fill-glass.amount");
        joinGamesInv_fillglass_datatag = config.getString("Config.joinGamesInv.fill-glass.datatag");
    }
}

package org.vexmi.escapadelabestia.utils;

import org.fusesource.jansi.Ansi;

public class Colors {

    public static String translateColors(String message) {
        if(message == null) return "";

        message = message.replaceAll("&0", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString())
                .replaceAll("&1", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString())
                .replaceAll("&2", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString())
                .replaceAll("&3", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString())
                .replaceAll("&4", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString())
                .replaceAll("&5", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString())
                .replaceAll("&6", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString())
                .replaceAll("&7", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString())
                .replaceAll("&8", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString())
                .replaceAll("&9", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString())
                .replaceAll("&a", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString())
                .replaceAll("&b", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString())
                .replaceAll("&c", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString())
                .replaceAll("&d", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString())
                .replaceAll("&e", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString())
                .replaceAll("&f", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString())
                .replaceAll("&k", Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString())
                .replaceAll("&l", Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString())
                .replaceAll("&m", Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString())
                .replaceAll("&n", Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString())
                .replaceAll("&o", Ansi.ansi().a(Ansi.Attribute.ITALIC).toString())
                .replaceAll("&r", Ansi.ansi().a(Ansi.Attribute.RESET).toString());

        return message + Ansi.ansi().reset().toString();
    }
}

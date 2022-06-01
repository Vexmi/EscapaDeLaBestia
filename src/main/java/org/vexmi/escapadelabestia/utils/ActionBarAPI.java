package org.vexmi.escapadelabestia.utils;

import com.connorlinfoot.actionbarapi.ActionBarMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.vexmi.escapadelabestia.EscapaBestia;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActionBarAPI {

    private static String nmsver;

    public ActionBarAPI() {
        nmsver = EscapaBestia.getPlugin().getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
    }

    public void sendActionBar(Player player, String message) {
        if (player.isOnline()) {
            ActionBarMessageEvent actionBarMessageEvent = new ActionBarMessageEvent(player, message);
            Bukkit.getPluginManager().callEvent(actionBarMessageEvent);
            if (!actionBarMessageEvent.isCancelled()) {
                try {
                    Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
                    Object craftPlayer = craftPlayerClass.cast(player);
                    Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
                    Class<?> packetClass = Class.forName("net.minecraft.server." + nmsver + ".Packet");
                    Object packet;
                    Class chatComponentTextClass;
                    Class iChatBaseComponentClass;
                    Object chatComponentText;

                    chatComponentTextClass = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
                    iChatBaseComponentClass = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");

                    try {
                        Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsver + ".ChatMessageType");
                        Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                        Object chatMessageType = null;
                        Object[] var13 = chatMessageTypes;
                        int var14 = chatMessageTypes.length;

                        for(int var15 = 0; var15 < var14; ++var15) {
                            Object obj = var13[var15];
                            if (obj.toString().equals("GAME_INFO")) {
                                chatMessageType = obj;
                            }
                        }

                        chatComponentText = chatComponentTextClass.getConstructor(String.class).newInstance(message);
                        packet = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, chatMessageTypeClass).newInstance(chatComponentText, chatMessageType);
                    } catch (ClassNotFoundException var17) {
                        chatComponentText = chatComponentTextClass.getConstructor(String.class).newInstance(message);
                        packet = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, Byte.TYPE).newInstance(chatComponentText, Byte.parseByte(String.valueOf(2)));
                    }

                    Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
                    Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
                    Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
                    chatComponentText = playerConnectionField.get(craftPlayerHandle);
                    Method sendPacketMethod = chatComponentText.getClass().getDeclaredMethod("sendPacket", packetClass);
                    sendPacketMethod.invoke(chatComponentText, packet);
                } catch (Exception var18) {
                    var18.printStackTrace();
                }

            }
        }
    }
}

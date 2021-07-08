package main.java.com.irfandumanx.apiawipeout.misc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import io.papermc.lib.PaperLib;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

public class Utils {
    private static final String NMS_VERSION;
    private static final boolean IS_1_8, IS_1_9, IS_1_10;
    private static final Random random;

    private static Class<?> chatPacket, chatComponent, chatBaseComponent;
    private static Constructor<?> titleConstructor, chatComponentConstructor, chatPacketConstructor;
    private static Method chatTitleMethod, chatComponentMethod;
    private static Object titleObject, subtitleObject;

    static {
        NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        IS_1_8 = NMS_VERSION.startsWith("v1_8_R");
        IS_1_9 = NMS_VERSION.startsWith("v1_9_R");
        IS_1_10 = NMS_VERSION.startsWith("v1_10_R");
        random = new Random();

        try {
            if (IS_1_8 || IS_1_9 || IS_1_10) {
                chatTitleMethod = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class);
                titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class,
                        int.class);
                titleObject = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                subtitleObject = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            }

            if (IS_1_8 || IS_1_9) {
                boolean is_1_8_R1 = NMS_VERSION.equalsIgnoreCase("v1_8_R1");
                if (is_1_8_R1) {
                    chatComponent = getNMSClass("ChatSerializer");
                    chatComponentMethod = chatComponent.getDeclaredMethod("a", String.class);
                } else {
                    chatComponent = getNMSClass("ChatComponentText");
                }
                chatComponentConstructor = chatComponent.getConstructor(new Class[]{String.class});
                chatPacket = getNMSClass("PacketPlayOutChat");
                chatBaseComponent = getNMSClass("IChatBaseComponent");
                chatPacketConstructor = chatPacket.getConstructor(new Class[]{chatBaseComponent, Byte.TYPE});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNmsVersion() {
        return NMS_VERSION;
    }

    public static int getInt(String intString, int defaultreturn) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException nfe) {
            return defaultreturn;
        }
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (IS_1_8 || IS_1_9 || IS_1_10) {
            try {
                Object chatTitle = chatTitleMethod.invoke(null, "{\"text\": \"" + title + "\"}");
                Object titlePacket = titleConstructor.newInstance(titleObject, chatTitle, fadeIn, stay, fadeOut);

                Object subtitleTitle = chatTitleMethod.invoke(null, "{\"text\": \"" + subtitle + "\"}");
                Object subtitlePacket = titleConstructor.newInstance(subtitleObject, subtitleTitle, fadeIn, stay, fadeOut);

                sendPacket(player, titlePacket);
                sendPacket(player, subtitlePacket);
            } catch (Exception var11) {
                var11.printStackTrace();
            }
        } else {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public static void clearTitle(Player player) {
        sendTitle(player, "", "", 0, 0, 0);
    }

    public static void sendActionbar(Player player, String message) {
        if (!IS_1_8 && !IS_1_9) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            return;
        }

        try {
            Object object = chatComponentMethod == null ? chatComponentConstructor.newInstance(message)
                    : chatBaseComponent.cast(chatComponentMethod.invoke(chatComponent, "{'text': '" + message + "'}"));
            Object packetPlayOutChat = chatPacketConstructor.newInstance(object, (byte) 2);

            sendPacket(player, packetPlayOutChat);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object getHandle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = getHandle.getClass().getField("playerConnection").get(getHandle);
            playerConnection.getClass().getMethod("sendPacket", new Class[]{getNMSClass("Packet")}).invoke(playerConnection, new Object[]{packet});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + NMS_VERSION + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getCraftBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + NMS_VERSION + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getRegionNMSClass(String name) {
        try {
            return Class.forName("main.java.com.irfandumanx.apiawipeout." + (isLegacy() ? "v1_12_R1." : "v1_16_R3.") + name).getConstructor(ApiaWipeout.class).newInstance(ApiaWipeout.getInstance());
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object asyncBlocksSet1_12_2(Block b, int blockId, byte data, boolean applyPhysics) {
        try {
            Object object = Class.forName("main.java.com.irfandumanx.apiawipeout.v1_12_R1.misc.AsyncBlocksSet").newInstance();
            return object.getClass().getMethod("setBlockSuperFast", Block.class, int.class, byte.class, boolean.class)
                    .invoke(object, b, blockId, data, applyPhysics);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object asyncBlocksSet1_16_5(Location location, Material material, boolean applyPhysics) {
        try {
            Object object = Class.forName("main.java.com.irfandumanx.apiawipeout.v1_16_R3.misc.AsyncBlocksSet").newInstance();
            return object.getClass().getMethod("setBlockSuperFast", Location.class, Material.class, boolean.class)
                    .invoke(object, location, material, applyPhysics);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPrettyStringFromLocation(final Location location) {
        return "(" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")";
    }

    public static boolean isLegacy() {
        return Integer.parseInt(NMS_VERSION.replace("v", "").replace("_", "").replaceAll("(?<=R).*$", "").replace("R", "")) < 113;
    }

    public static boolean isPaper() {
        return PaperLib.isPaper();
    }

    public static void teleport(Entity entity, Location location) {
        PaperLib.teleportAsync(entity, location);
    }

    public static double distanceSquared(@NotNull Location armorStandLocation, Location playerLocation) {
        return Math.abs(armorStandLocation.getY() - playerLocation.getY()) <= 1.5 ? NumberConversions.square(playerLocation.getX() - ((armorStandLocation.getX() < 0 ? -.2 : .2) + armorStandLocation.getX())) + NumberConversions.square(playerLocation.getZ() - ((armorStandLocation.getZ() < 0 ? -.2 : .2) + armorStandLocation.getZ())) : 0.257;
    }

    public static int getRandomNumber(int maxValue) {
        return random.nextInt(maxValue);
    }
}
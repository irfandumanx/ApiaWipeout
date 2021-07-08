package main.java.com.irfandumanx.apiawipeout.v1_16_R3.misc;

import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import org.bukkit.Location;
import org.bukkit.Material;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AsyncBlocksSet {
    private static final Map<Material, Object> blocks = new HashMap<>();
    private static Class<?> blockPosition, iBlockData;
    private static Method worldGetHandle, getBlock, setTypeAndData, getBlockData;
    private static Constructor<?> blockPositionConstructor;

    static {
        try {
            blockPosition = Utils.getNMSClass("BlockPosition");
            iBlockData = Utils.getNMSClass("IBlockData");

            worldGetHandle = Utils.getCraftBukkitClass("CraftWorld").getMethod("getHandle", new Class[0]);
            getBlock = Utils.getCraftBukkitClass("util.CraftMagicNumbers").getMethod("getBlock", Material.class);

            getBlockData = Utils.getNMSClass("Block").getMethod("getBlockData", new Class[0]);
            setTypeAndData = Utils.getNMSClass("WorldServer").getMethod("setTypeAndData", blockPosition, iBlockData, int.class);

            blockPositionConstructor = blockPosition.getConstructor(int.class, int.class, int.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void setBlockSuperFast(Location location, Material type, boolean applyPhysics) {
        try {
            Object nmsWorld = worldGetHandle.invoke(location.getWorld(), new Object[0]);
            Object bp = blockPositionConstructor.newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            Object ibd = blocks.get(type);

            if (ibd == null) {
                ibd = getBlockData.invoke(getBlock.invoke(null, type), new Object[0]);
                blocks.put(type, ibd);
            }
            setTypeAndData.invoke(nmsWorld, bp, ibd, applyPhysics ? 3 : 2);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

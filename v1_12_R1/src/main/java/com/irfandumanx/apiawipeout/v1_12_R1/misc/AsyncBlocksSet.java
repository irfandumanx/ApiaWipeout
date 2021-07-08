package main.java.com.irfandumanx.apiawipeout.v1_12_R1.misc;

import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import org.bukkit.block.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AsyncBlocksSet {
    private static final Map<Integer, Object> blocks = new HashMap<>();
    private static Class<?> blockPositionClass, iBlockDataClass;
    private static Method worldGetHandle, getChunkAt, getByCombinedId, setTypeAndData, chunkA;
    private static Constructor<?> blockPositionConstructor;
    static {
        try {
            blockPositionClass = Utils.getNMSClass("BlockPosition");
            iBlockDataClass = Utils.getNMSClass("IBlockData");

            Class<?> nmsWorld = Utils.getNMSClass("WorldServer");

            worldGetHandle = Utils.getCraftBukkitClass("CraftWorld").getMethod("getHandle", new Class[0]);
            getChunkAt = nmsWorld.getMethod("getChunkAt", int.class, int.class);
            getByCombinedId = Utils.getNMSClass("Block").getMethod("getByCombinedId", int.class);
            setTypeAndData = nmsWorld.getMethod("setTypeAndData", blockPositionClass, iBlockDataClass, int.class);
            chunkA = Utils.getNMSClass("Chunk").getMethod("a", blockPositionClass, iBlockDataClass);

            blockPositionConstructor = blockPositionClass.getConstructor(int.class, int.class, int.class);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void setBlockSuperFast(Block b, int blockId, byte data, boolean applyPhysics) {
        try {
            Object nmsWorld = worldGetHandle.invoke(b.getWorld(), new Object[0]);
            Object nmsChunk = getChunkAt.invoke(nmsWorld, b.getX() >> 4, b.getZ() >> 4);
            Object blockPosition = blockPositionConstructor.newInstance(b.getX(), b.getY(), b.getZ());
            int combined = blockId + (data << 12);
            Object iBlockData = blocks.get(combined);
            if(iBlockData == null) {
                iBlockData = getByCombinedId.invoke(null, combined);
                blocks.put(combined, iBlockData);
            }
            setTypeAndData.invoke(nmsWorld, blockPosition, iBlockData, applyPhysics ? 3 : 2);
            chunkA.invoke(nmsChunk, blockPosition, iBlockData);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

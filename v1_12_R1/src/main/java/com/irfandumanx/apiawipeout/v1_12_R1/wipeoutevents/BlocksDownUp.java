package main.java.com.irfandumanx.apiawipeout.v1_12_R1.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import main.java.com.irfandumanx.apiawipeout.v1_12_R1.misc.AsyncBlocksSet;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlocksDownUp extends Region {
    private final Map<Location, ItemStack> oldBlock = new HashMap<>();
    private final AtomicBoolean isPutBlocks = new AtomicBoolean(true);

    public BlocksDownUp(ApiaWipeout instance) {
        super(instance);
    }

    @Override
    public void start() {
        down();
    }

    @Override
    public Map<Location, ItemStack> getOldBlock() {
        return oldBlock;
    }

    @SuppressWarnings("deprecation")
    public void down() {
        getInstance().getServer().getScheduler().runTaskAsynchronously(getInstance(), () -> {
            int counter = Settings.WIPEOUTWAITDOWNTIME.getInt();
            if (getCoords().isEmpty()) return;
            for (int maxY = getCoords().get(4); maxY >= getCoords().get(1); maxY--) {
                for (int maxX = getCoords().get(3); maxX >= getCoords().get(0); maxX--) {
                    for (int maxZ = getCoords().get(5); maxZ >= getCoords().get(2); maxZ--) {
                        final int maxZZ = maxZ;
                        final int maxXX = maxX;
                        final int maxYY = maxY;
                        getInstance().getServer().getScheduler().runTaskLater(getInstance(), () -> {
                            Location location = new Location(getWorld(), maxXX, maxYY, maxZZ);
                            if (isPutBlocks.get()) oldBlock.put(location, new ItemStack(location.getBlock().getType(), 1, location.getBlock().getData()));
                            AsyncBlocksSet.setBlockSuperFast(location.getBlock(), 0, (byte) 0, false);
                            if (maxYY == getCoords().get(1) && maxXX == getCoords().get(0) && maxZZ == getCoords().get(2)) up();
                        }, (long) Settings.WIPEOUTDOWNSPEED.getInt() * counter);
                    }
                }
                counter++;
            }
            getInstance().getServer().getScheduler().runTaskLater(getInstance(), () -> isPutBlocks.set(false),(long) Settings.WIPEOUTDOWNSPEED.getInt() * counter + 1);
        });
    }

    @SuppressWarnings("deprecation")
    public void up() {
        getInstance().getServer().getScheduler().runTaskAsynchronously(getInstance(), () -> {
            int counter = Settings.WIPEOUTWAITUPTIME.getInt();
            if (getCoords().isEmpty()) return;
            for (int minY = getCoords().get(1); minY <= getCoords().get(4); minY++) {
                for (int minX = getCoords().get(0); minX <= getCoords().get(3); minX++) {
                    for (int minZ = getCoords().get(2); minZ <= getCoords().get(5); minZ++) {
                        final int minZZ = minZ;
                        final int minXX = minX;
                        final int minYY = minY;
                        getInstance().getServer().getScheduler().runTaskLater(getInstance(), () -> {
                            Location location = new Location(getWorld(), minXX, minYY, minZZ);
                            ItemStack itemStack = oldBlock.get(location);
                            AsyncBlocksSet.setBlockSuperFast(location.getBlock(), itemStack.getTypeId(), itemStack.getData().getData(), false);
                            if (minYY == getCoords().get(4) && minXX == getCoords().get(3) && minZZ == getCoords().get(5)) down();
                        }, ((long) Settings.WIPEOUTUPSPEED.getInt() * counter));
                    }
                }
                counter++;
            }
        });
    }
}

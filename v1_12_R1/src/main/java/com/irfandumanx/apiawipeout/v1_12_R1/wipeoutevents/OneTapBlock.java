package main.java.com.irfandumanx.apiawipeout.v1_12_R1.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import main.java.com.irfandumanx.apiawipeout.v1_12_R1.misc.AsyncBlocksSet;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class OneTapBlock extends Region {
    private final Map<Location, ItemStack> oldBlock = new HashMap<>();

    public OneTapBlock(ApiaWipeout instance) {
        super(instance);
    }

    @Override
    public void start() {
        oneTap();
    }

    @Override
    public Map<Location, ItemStack> getOldBlock() {
        return oldBlock;
    }

    @SuppressWarnings("deprecation")
    public void oneTap() {
        getInstance().getServer().getScheduler().runTaskAsynchronously(getInstance(), () -> {
            if (getCoords().isEmpty()) return;
            for (int maxY = getCoords().get(4); maxY >= getCoords().get(1); maxY--) {
                for (int maxX = getCoords().get(3); maxX >= getCoords().get(0); maxX--) {
                    for (int maxZ = getCoords().get(5); maxZ >= getCoords().get(2); maxZ--) {
                        final int maxZZ = maxZ;
                        final int maxXX = maxX;
                        final int maxYY = maxY;
                        getInstance().getServer().getScheduler().runTaskTimer(getInstance(), () -> {
                            Location location = new Location(getWorld(), maxXX, maxYY, maxZZ);
                            Block block = location.getBlock();
                            ItemStack itemStack = new ItemStack(block.getType(), 1, block.getData());
                            oldBlock.put(location, itemStack);
                            AsyncBlocksSet.setBlockSuperFast(block, 0, (byte) 0, false);
                            getInstance().getServer().getScheduler().runTaskLater(getInstance(), () -> AsyncBlocksSet.setBlockSuperFast(block, itemStack.getTypeId(), itemStack.getData().getData(), false), Settings.WIPEOUTONETAPREPLACE.getInt());
                        }, 20,Settings.WIPEOUTONETAPREMOVE.getInt());
                    }
                }
            }
        });
    }

}

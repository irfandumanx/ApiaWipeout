package main.java.com.irfandumanx.apiawipeout.v1_16_R3.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import main.java.com.irfandumanx.apiawipeout.v1_16_R3.misc.AsyncBlocksSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class StartBlock extends Region {
    private final Map<Location, ItemStack> oldBlock = new HashMap<>();

    public StartBlock(ApiaWipeout instance) {
        super(instance);
    }

    @Override
    public void start() {
        startBlock();
    }

    @Override
    public Map<Location, ItemStack> getOldBlock() {
        return oldBlock;
    }

    public void startBlock() {
        getInstance().getServer().getScheduler().runTaskAsynchronously(getInstance(), () -> {
            if (getCoords().isEmpty()) return;
            for (int maxY = getCoords().get(4); maxY >= getCoords().get(1); maxY--) {
                for (int maxX = getCoords().get(3); maxX >= getCoords().get(0); maxX--) {
                    for (int maxZ = getCoords().get(5); maxZ >= getCoords().get(2); maxZ--) {
                        Location location = new Location(getWorld(), maxX, maxY, maxZ);
                        oldBlock.put(location, new ItemStack(location.getBlock().getType(), 1));
                        getInstance().getServer().getScheduler().runTask(getInstance(), () -> AsyncBlocksSet.setBlockSuperFast(location, Material.AIR, false));
                    }
                }
            }
        });
    }
}

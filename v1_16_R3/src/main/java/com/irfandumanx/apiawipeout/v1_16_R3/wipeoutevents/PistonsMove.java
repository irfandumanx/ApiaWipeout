package main.java.com.irfandumanx.apiawipeout.v1_16_R3.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import main.java.com.irfandumanx.apiawipeout.v1_16_R3.misc.AsyncBlocksSet;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.APistonsMove;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Piston;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PistonsMove extends APistonsMove {
    private final Map<Location, ItemStack> oldBlock = new HashMap<>();
    public PistonsMove(ApiaWipeout instance) {
        super(instance);
    }

    @Override
    public void start() {
        piston();
    }

    @Override
    public void stop() {
        getInstance().getServer().getScheduler().cancelTasks(getInstance());
        oldBlock.forEach((k,v) -> AsyncBlocksSet.setBlockSuperFast(k, v.getType(), true));
    }

    @Override
    public Map<Location, ItemStack> getOldBlock() {
        return null;
    }

    public void piston() {
        Block block = getBlockLocations().getFirst().getBlock();

        if (!(block.getBlockData() instanceof Piston)) return;
        Piston piston = (Piston) block.getState().getBlockData();
        Block block1 = block.getRelative(piston.getFacing().getOppositeFace());
        ItemStack itemStack = new ItemStack(block1.getType(), 1);

        getInstance().getServer().getScheduler().runTaskTimer(getInstance(), () -> {
            if (Settings.WIPEOUTPISTONEXTENSIONCHANCE.getInt() < Utils.getRandomNumber(100)) return;
            oldBlock.put(block1.getLocation(), itemStack);
            AsyncBlocksSet.setBlockSuperFast(block1.getLocation(), Material.REDSTONE_BLOCK, true);

            getInstance().getServer().getScheduler().runTaskLater(getInstance(), () -> AsyncBlocksSet.setBlockSuperFast(block1.getLocation(), itemStack.getType(), true), Settings.WIPEOUTPISTONRETRACTTIME.getInt());
        }, Settings.WIPEOUTPISTONSSTARTTIME.getInt(), Settings.WIPEOUTPISTONSEXTENSIONTIME.getInt());
    }
}

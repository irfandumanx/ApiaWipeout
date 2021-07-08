package main.java.com.irfandumanx.apiawipeout.v1_12_R1.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import main.java.com.irfandumanx.apiawipeout.v1_12_R1.misc.AsyncBlocksSet;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.APistonsMove;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.PistonBaseMaterial;

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

    @SuppressWarnings("deprecation")
    @Override
    public void stop() {
        getInstance().getServer().getScheduler().cancelTasks(getInstance());
        oldBlock.forEach((k,v) -> AsyncBlocksSet.setBlockSuperFast(k.getBlock(), v.getTypeId(), v.getData().getData(), true));
    }

    @Override
    public Map<Location, ItemStack> getOldBlock() {
        return null;
    }

    @SuppressWarnings("deprecation")
    public void piston() {
        Block block = getBlockLocations().getFirst().getBlock();
        PistonBaseMaterial pistonBaseMaterial = (PistonBaseMaterial) block.getState().getData();
        Block block1 = block.getRelative(pistonBaseMaterial.getFacing().getOppositeFace());
        ItemStack itemStack = new ItemStack(block1.getType(), 1, block1.getData());

        getInstance().getServer().getScheduler().runTaskTimer(getInstance(), () -> {
            if (Settings.WIPEOUTPISTONEXTENSIONCHANCE.getInt() < Utils.getRandomNumber(100)) return;
            oldBlock.put(block1.getLocation(), itemStack);
            block1.setType(Material.REDSTONE_BLOCK);

            getInstance().getServer().getScheduler().runTaskLater(getInstance(), () -> {
                block1.setType(itemStack.getType());
                block1.setData(itemStack.getData().getData());
            }, Settings.WIPEOUTPISTONRETRACTTIME.getInt());
        }, Settings.WIPEOUTPISTONSSTARTTIME.getInt(), Settings.WIPEOUTPISTONSEXTENSIONTIME.getInt());
    }
}

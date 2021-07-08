package main.java.com.irfandumanx.apiawipeout.v1_16_R3.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.v1_16_R3.misc.AsyncBlocksSet;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.AEscalatorRamp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class EscalatorRamp extends AEscalatorRamp {
    private final Map<Location, ItemStack> oldBlock = new LinkedHashMap<>();
    private Map<Location, ItemStack> oldBlockSource;
    private RampType rampType;
    private int numberOfBlock;

    public EscalatorRamp(ApiaWipeout instance) {
        super(instance);
    }

    @Override
    public void start() {
        ramp();
    }

    @Override
    public Map<Location, ItemStack> getOldBlock() {
        oldBlock.keySet().forEach(key -> AsyncBlocksSet.setBlockSuperFast(key.getBlock().getLocation(), Material.AIR, false));
        return oldBlockSource;
    }

    public void ramp() {
        getInstance().getServer().getScheduler().runTaskAsynchronously(getInstance(), () -> {
            if (getCoords().isEmpty()) return;
            oldBlock.clear();
            for (int maxZ = getCoords().get(5); maxZ >= getCoords().get(2); maxZ--) {
                for (int maxY = getCoords().get(4); maxY >= getCoords().get(1); maxY--) {
                    for (int maxX = getCoords().get(3); maxX >= getCoords().get(0); maxX--) {
                        Location location = new Location(getWorld(), maxX, maxY, maxZ);
                        oldBlock.put(location, new ItemStack(location.getBlock().getType(), 1));
                    }
                }
            }
            oldBlockSource = new LinkedHashMap<>();
            oldBlock.forEach((k,v) -> oldBlockSource.put(k.clone(), v.clone()));

            if (rampType == RampType.SOUTH) south();
            else if (rampType == RampType.NORTH) north();
            else if (rampType == RampType.EAST) east();
            else if (rampType == RampType.WEST) west();
        });
    }

    public void south() {
        int i = 1;
        while (i <= numberOfBlock) {
            getInstance().getServer().getScheduler().runTaskLater(getInstance(), () -> oldBlock.forEach((k, v) -> {
                AsyncBlocksSet.setBlockSuperFast(k.getBlock().getLocation(), Material.AIR, false);
                k.add(0, 0, 1);
                AsyncBlocksSet.setBlockSuperFast(k.getBlock().getLocation(), v.getType(), false);
            }), (long) Settings.WIPEOUTRAMPMOVESPEED.getInt() * i);
            i++;
        }
        getInstance().getServer().getScheduler().runTaskLaterAsynchronously(getInstance(), this::north, (long) numberOfBlock * Settings.WIPEOUTRAMPMOVESPEED.getInt() + Settings.WIPEOUTRAMPWAITTIME.getInt());
    }

    public void north() {
        int i = 1;
        while (i <= numberOfBlock) {
            getInstance().getServer().getScheduler().runTaskLater(getInstance(), new Runnable() {
                int minValue = Integer.MIN_VALUE;
                @Override
                public void run() {
                    oldBlock.forEach((k, v) -> {
                        if (k.getBlockZ() > minValue) minValue = k.getBlockZ();
                        if (k.getBlockZ() == minValue) AsyncBlocksSet.setBlockSuperFast(k.getBlock().getLocation(), Material.AIR, false);
                        k.add(0, 0, -1);
                        AsyncBlocksSet.setBlockSuperFast(k.getBlock().getLocation(), v.getType(), false);
                    });
                }
            }, (long) Settings.WIPEOUTRAMPMOVESPEED.getInt() * i);
            i++;
        }
        getInstance().getServer().getScheduler().runTaskLaterAsynchronously(getInstance(), this::south, (long) numberOfBlock * Settings.WIPEOUTRAMPMOVESPEED.getInt() + Settings.WIPEOUTRAMPWAITTIME.getInt());
    }

    public void east() {
        int i = 1;
        while (i <= numberOfBlock) {
            getInstance().getServer().getScheduler().runTaskLater(getInstance(), () -> oldBlock.forEach((k, v) -> {
                AsyncBlocksSet.setBlockSuperFast(k.getBlock().getLocation(), Material.AIR, false);
                k.add(1, 0, 0);
                AsyncBlocksSet.setBlockSuperFast(k.getBlock().getLocation(), v.getType(), false);
            }), (long) Settings.WIPEOUTRAMPMOVESPEED.getInt() * i);
            i++;
        }
        getInstance().getServer().getScheduler().runTaskLaterAsynchronously(getInstance(), this::west, (long) numberOfBlock * Settings.WIPEOUTRAMPMOVESPEED.getInt() + Settings.WIPEOUTRAMPWAITTIME.getInt());
    }

    public void west() {
        int i = 1;
        while (i <= numberOfBlock) {
            getInstance().getServer().getScheduler().runTaskLater(getInstance(), new Runnable() {
                int minValue = Integer.MIN_VALUE;
                @Override
                public void run() {
                    oldBlock.forEach((k, v) -> {
                        if (k.getBlockX() > minValue) minValue = k.getBlockX();
                        if (k.getBlockX() == minValue) AsyncBlocksSet.setBlockSuperFast(k.getBlock().getLocation(), Material.AIR, false);
                        k.add(-1, 0, 0);
                        AsyncBlocksSet.setBlockSuperFast(k.getBlock().getLocation(), v.getType(), false);
                    });
                }
            }, (long) Settings.WIPEOUTRAMPMOVESPEED.getInt() * i);
            i++;
        }
        getInstance().getServer().getScheduler().runTaskLaterAsynchronously(getInstance(), this::east, (long) numberOfBlock * Settings.WIPEOUTRAMPMOVESPEED.getInt() + Settings.WIPEOUTRAMPWAITTIME.getInt());
    }

    @Override
    public RampType getRampType() {
        return rampType;
    }
    @Override
    public void setRampType(RampType rampType) {
        this.rampType = rampType;
    }
    @Override
    public int getNumberOfBlock() {
        return numberOfBlock;
    }
    @Override
    public void setNumberOfBlock(int numberOfBlock) {
        this.numberOfBlock = numberOfBlock;
    }
}
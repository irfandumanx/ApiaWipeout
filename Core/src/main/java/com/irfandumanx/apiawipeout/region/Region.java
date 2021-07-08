package main.java.com.irfandumanx.apiawipeout.region;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Pair;
import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Region {
    private ApiaWipeout instance;
    private World world;
    private Pair<Location, Location> blockLocations;
    private List<Integer> coords;
    private Location checkPoint;

    public Region(ApiaWipeout instance) {
        this.instance = instance;
        blockLocations = new Pair<>();
        coords = new LinkedList<>();
    }

    public Pair<Location, Location> getBlockLocations() {
        return blockLocations;
    }

    public World getWorld() {
        return world;
    }

    public List<Integer> getCoords() {
        return coords;
    }

    public Location getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(Location checkPoint) {
        this.checkPoint = checkPoint;
    }

    public void setMinMaxLoc() {
        coords.add(Math.min(blockLocations.getFirst().getBlockX(), blockLocations.getSecond().getBlockX()));
        coords.add(Math.min(blockLocations.getFirst().getBlockY(), blockLocations.getSecond().getBlockY()));
        coords.add(Math.min(blockLocations.getFirst().getBlockZ(), blockLocations.getSecond().getBlockZ()));
        coords.add(Math.max(blockLocations.getFirst().getBlockX(), blockLocations.getSecond().getBlockX()));
        coords.add(Math.max(blockLocations.getFirst().getBlockY(), blockLocations.getSecond().getBlockY()));
        coords.add(Math.max(blockLocations.getFirst().getBlockZ(), blockLocations.getSecond().getBlockZ()));
        world = blockLocations.getFirst().getWorld();
    }

    public abstract void start();

    @SuppressWarnings("deprecation")
    public void stop() {
        instance.getServer().getScheduler().cancelTasks(instance);
        if (getOldBlock() != null) {
            if ((Utils.isLegacy())) getOldBlock().forEach((k, v) -> Utils.asyncBlocksSet1_12_2(k.getBlock(), v.getTypeId(), v.getData().getData(), false));
            else getOldBlock().forEach((k, v) -> Utils.asyncBlocksSet1_16_5(k.getBlock().getLocation(), v.getType(), false));
        }
    }

    public abstract Map<Location, ItemStack> getOldBlock();

    public ApiaWipeout getInstance() {return instance;}
}

package main.java.com.irfandumanx.apiawipeout.managers;

import main.java.com.irfandumanx.apiawipeout.Game;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.commands.SetCommand;
import main.java.com.irfandumanx.apiawipeout.misc.Pair;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import main.java.com.irfandumanx.apiawipeout.region.CheckPoint;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

public class RegionManager {
    private final ApiaWipeout instance;
    private final SetCommand setCommandInstance;
    private final Map<Integer, Region> regions = new HashMap<>();

    public RegionManager(ApiaWipeout instance, SetCommand setCommandInstance) {
        this.instance = instance;
        this.setCommandInstance = setCommandInstance;
    }

    public void loadRegion(int id) {
        boolean anyChanges = false;
        FileConfiguration regionC = instance.getFileManager().getRegionC();
        Region region = null;
        String regionType = regionC.getString(id + ".type");
        switch (regionType) {
            case "DOWNUP":
                region = (Region) Utils.getRegionNMSClass("wipeoutevents.BlocksDownUp");
                break;
            case "FINISH":
                region = new Finish(instance);
                break;
            case "PISTON":
                region = (APistonsMove) Utils.getRegionNMSClass("wipeoutevents.PistonsMove");
                break;
            case "FLOW":
                region = (Region) Utils.getRegionNMSClass("wipeoutevents.WaterFlow");
                break;
            case "STARTLINE":
                region = (Region) Utils.getRegionNMSClass("wipeoutevents.StartBlock");
                break;
            case "ONETAP":
                region = (Region) Utils.getRegionNMSClass("wipeoutevents.OneTapBlock");
                break;
            case "WATER":
                region = new WaterFall(instance);
                break;
            case "FALL":
                region = new FallBlock(instance);
                break;
            case "THROW":
                region = new ThrowBlock(instance);
                break;
            case "RAMP":
                region = (AEscalatorRamp) Utils.getRegionNMSClass("wipeoutevents.EscalatorRamp");
                break;
            case "ROTROD":
                region = (ARotrod) Utils.getRegionNMSClass("wipeoutevents.Rotrod");
                break;
        }

        if (Settings.WIPEOUTDEBUGMODE.getBoolean()) instance.getLogger().info("Region type : " + regionType + "\nBlock - 1 " + regionC.getString(id + ".block.1") + "\nBlock - 2 " + regionC.getString(id + ".block.2") + "\nWith method Block - 1 : " + instance.getFileManager().getLocationFromString(regionC.getString(id + ".block.1")) + "\nWith method Block - 2 : " + instance.getFileManager().getLocationFromString(regionC.getString(id + ".block.2")));

        if (regionC.isSet(id + ".block")) {
            region.getBlockLocations().setFirst(instance.getFileManager().getLocationFromString(regionC.getString(id + ".block.1")));
            region.getBlockLocations().setSecond(instance.getFileManager().getLocationFromString(regionC.getString(id + ".block.2")));
        }

        if (region instanceof WaterFall) {
            if (regionC.isSet(id + ".checkpoint"))
                region.setCheckPoint(instance.getFileManager().getLocationFromString(regionC.getString(id + ".checkpoint.location")));
            else if (setCommandInstance.getCheckPointLocation() == null) {
                region.setCheckPoint(Game.getBaseLocation());
                anyChanges = true;
            } else {
                region.setCheckPoint(setCommandInstance.getCheckPointLocation());
                anyChanges = true;
            }
        }

        if (region instanceof ThrowBlock) {
            if (regionC.isSet(id + ".power"))
                ((ThrowBlock) region).setThrowPower(regionC.getInt(id + ".power"));
            else {
                ((ThrowBlock) region).setThrowPower(setCommandInstance.getThrowPower());
                anyChanges = true;
            }
        }

        if (region instanceof AEscalatorRamp) {
            if (regionC.isSet(id + ".goblock") && regionC.isSet(id + ".direction")) {
                ((AEscalatorRamp) region).setNumberOfBlock(regionC.getInt(id + ".goblock"));
                ((AEscalatorRamp) region).setRampType(AEscalatorRamp.RampType.valueOf(regionC.getString(id + ".direction")));
            }
            else {
                ((AEscalatorRamp) region).setNumberOfBlock(setCommandInstance.getNumberOfBlock());
                ((AEscalatorRamp) region).setRampType(setCommandInstance.getRampType());
                anyChanges = true;
            }
        }

        if (region instanceof ARotrod) {
            if (regionC.isSet(id + ".material") && regionC.isSet(id + ".size") && regionC.isSet(id + ".radius") && regionC.isSet(id + ".direction")) {
                String[] splitItemStack = regionC.getString(id + ".material").split(";");
                if (splitItemStack.length == 2) ((ARotrod) region).setItemStack(new ItemStack(Material.getMaterial(splitItemStack[0].toUpperCase()), 1, Short.parseShort(splitItemStack[1])));
                else ((ARotrod) region).setItemStack(new ItemStack(Material.getMaterial(splitItemStack[0].toUpperCase()), 1));

                ((ARotrod) region).setSize(regionC.getInt(id + ".size"));
                ((ARotrod) region).setRadius(regionC.getInt(id + ".radius"));
                ((ARotrod) region).setType(regionC.getString(id + ".direction"));
            }
            else {
                ((ARotrod) region).setItemStack(setCommandInstance.getRotrodItemStack());
                ((ARotrod) region).setSize(setCommandInstance.getRotrodSize());
                ((ARotrod) region).setRadius(setCommandInstance.getRotrodRadius());
                ((ARotrod) region).setType(setCommandInstance.getRotrodType());
                anyChanges = true;
            }
        }

        region.setMinMaxLoc();
        regions.put(id, region);
        if (anyChanges) saveRegion(id, region);
    }

    public void loadRegions() {
        for (String id : instance.getFileManager().getRegionC().getConfigurationSection("").getKeys(false)) {
            loadRegion(Integer.parseInt(id));
        }
    }

    public void createRegion(String type, Location location) {
        FileConfiguration regionC = instance.getFileManager().getRegionC();
        int id = getEmptyID();
        regionC.set(id + ".type", type);
        regionC.set(id + ".block.1", instance.getFileManager().getStringFromLocation(location));
        regionC.set(id + ".block.2", instance.getFileManager().getStringFromLocation(location));
        try {
            regionC.save(instance.getFileManager().getRegion());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        loadRegion(id);
    }

    public void createRegion(String type, UUID uuid) {
        FileConfiguration regionC = instance.getFileManager().getRegionC();
        int id = getEmptyID();
        Pair<Location, Location> locationPair = instance.getSelectorManager().getPosOfPlayer(uuid);
        regionC.set(id + ".type", type);
        regionC.set(id + ".block.1", instance.getFileManager().getStringFromLocation(locationPair.getFirst()));
        regionC.set(id + ".block.2", instance.getFileManager().getStringFromLocation(locationPair.getSecond()));
        try {
            regionC.save(instance.getFileManager().getRegion());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        loadRegion(id);
    }

    public void deleteRegion(int id) {
        regions.remove(id);
        instance.getFileManager().getRegionC().set("" + id, null);
        try {
            instance.getFileManager().getRegionC().save(instance.getFileManager().getRegion());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private int getEmptyID() {
        int i = 0;
        do {
            i++;
        } while (instance.getFileManager().getRegionC().isSet("" + i));

        return i;
    }

    @SuppressWarnings("deprecation")
    public void saveRegion(int id, Region region) {
        FileConfiguration regionC = instance.getFileManager().getRegionC();
        if (region != null) {
            if (region.getBlockLocations().getFirst() != null)
                regionC.set(id + ".block.1", instance.getFileManager().getStringFromLocation(region.getBlockLocations().getFirst()));

            if (region.getBlockLocations().getSecond() != null)
                regionC.set(id + ".block.2", instance.getFileManager().getStringFromLocation(region.getBlockLocations().getSecond()));

            if (region instanceof WaterFall && region.getCheckPoint() != null) {
                CheckPoint checkPoint = instance.getCheckPointManager().getCheckPointWithLocation(region.getCheckPoint());
                if (checkPoint != null) {
                    regionC.set(id + ".checkpoint.name", checkPoint.getName());
                    regionC.set(id + ".checkpoint.location", instance.getFileManager().getStringFromLocation(checkPoint.getLocation()));
                }
            }

            if (region instanceof ThrowBlock && ((ThrowBlock) region).getThrowPower() != 0) {
                regionC.set(id + ".power", ((ThrowBlock) region).getThrowPower());
            }
            if (region instanceof AEscalatorRamp && ((AEscalatorRamp) region).getRampType() != null) {
                regionC.set(id + ".goblock", ((AEscalatorRamp) region).getNumberOfBlock());
                regionC.set(id + ".direction", ((AEscalatorRamp) region).getRampType() + "");
            }
            if (region instanceof ARotrod) {
                if (!Utils.isLegacy()) regionC.set(id + ".material", ((ARotrod) region).getItemStack().getType() + "");
                else {
                    if (((ARotrod) region).getItemStack().getData().getData() == 0) regionC.set(id + ".material", ((ARotrod) region).getItemStack().getType() + "");
                    else regionC.set(id + ".material", ((ARotrod) region).getItemStack().getType() + ";" + ((ARotrod) region).getItemStack().getData().getData());
                }
                regionC.set(id + ".size", ((ARotrod) region).getSize());
                regionC.set(id + ".radius", ((ARotrod) region).getRadius());
                regionC.set(id + ".direction", ((ARotrod) region).getType());
            }
        }

        try {
            regionC.save(instance.getFileManager().getRegion());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Region getRegion(Location location) {
        for (Region region : regions.values()) {
            if (!region.getWorld().getName().equals(location.getWorld().getName())) continue;
            List<Integer> regionCoords = region.getCoords();
            if ((regionCoords.get(0) <= location.getBlockX() && regionCoords.get(3) >= location.getBlockX())
                    && (regionCoords.get(1) <= location.getBlockY() && regionCoords.get(4) >= location.getBlockY())
                    && (regionCoords.get(2) <= location.getBlockZ() && regionCoords.get(5) >= location.getBlockZ()))
                return region;
        }
        return null;
    }

    public int getRegionWithID(Location location) {
        for (Map.Entry<Integer,Region> entry : regions.entrySet()) {
            if (!entry.getValue().getWorld().getName().equals(location.getWorld().getName())) continue;
            List<Integer> regionCoords = entry.getValue().getCoords();
            if ((regionCoords.get(0) <= location.getBlockX() && regionCoords.get(3) >= location.getBlockX())
                    && (regionCoords.get(1) <= location.getBlockY() && regionCoords.get(4) >= location.getBlockY())
                    && (regionCoords.get(2) <= location.getBlockZ() && regionCoords.get(5) >= location.getBlockZ()))
                return entry.getKey();
        }
        return 0;
    }

    public Region getRegionLoop(List<Object> coords) {
        for (Region region : regions.values()) {
            if (!coords.get(6).equals(region.getWorld()) || !coords.get(7).equals(region.getWorld())) continue;
            if (region instanceof WaterFall) continue;
            List<Integer> intCoords = new LinkedList<>();
            for (int i = 0; i <= 5; i++) intCoords.add((Integer) coords.get(i));
            List<Integer> regionCoords = region.getCoords();
            for (int maxY = regionCoords.get(4); maxY >= regionCoords.get(1); maxY--) {
                if (intCoords.get(4) >= maxY && intCoords.get(1) <= maxY) {
                    for (int maxX = regionCoords.get(3); maxX >= regionCoords.get(0); maxX--) {
                        if (intCoords.get(3) >= maxX && intCoords.get(0) <= maxX) {
                            for (int maxZ = regionCoords.get(5); maxZ >= regionCoords.get(2); maxZ--) {
                                if (intCoords.get(5) >= maxZ && intCoords.get(2) <= maxZ) return region;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<Object> setRegionLoopMinMax(Location location1, Location location2) {
        List<Object> coords = new LinkedList<>();
        coords.add(Math.min(location1.getBlockX(), location2.getBlockX()));
        coords.add(Math.min(location1.getBlockY(), location2.getBlockY()));
        coords.add(Math.min(location1.getBlockZ(), location2.getBlockZ()));
        coords.add(Math.max(location1.getBlockX(), location2.getBlockX()));
        coords.add(Math.max(location1.getBlockY(), location2.getBlockY()));
        coords.add(Math.max(location1.getBlockZ(), location2.getBlockZ()));
        coords.add(location1.getWorld());
        coords.add(location2.getWorld());
        return coords;
    }

    public Map<Integer, Region> getRegions() {
        return regions;
    }
}

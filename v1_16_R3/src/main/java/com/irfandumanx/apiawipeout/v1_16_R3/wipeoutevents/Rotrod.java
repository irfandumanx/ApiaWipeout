package main.java.com.irfandumanx.apiawipeout.v1_16_R3.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.ARotrod;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class  Rotrod extends ARotrod {
    private List<ArmorStand> armorStandList;
    private static final double CIRCLE = Math.toRadians(360);
    private double angle = 0;
    private Location location;
    private int size;
    private int radius;
    private String type;
    private ItemStack itemStack;

    public Rotrod(ApiaWipeout instance) {
        super(instance);
        armorStandList = new ArrayList<>();
    }

    @Override
    public List<ArmorStand> armorStandList() {
        return armorStandList;
    }

    @Override
    public void start() {
        location = getBlockLocations().getFirst().clone().add(.5, 0, .5);
        getInstance().getServer().getScheduler().runTask(getInstance(), this::setArmorStands);
        if (type.equals("+")) rotateClockWiseReverse();
        else rotateClockWise();
    }

    @Override
    public Map<Location, ItemStack> getOldBlock() {
        armorStandList.forEach(Entity::remove);
        armorStandList = new ArrayList<>();
        return null;
    }

    @SuppressWarnings("deprecation")
    public void createArmorStand(Location location) {
        ArmorStand armorStand = (ArmorStand) getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setHelmet(itemStack);
        armorStandList.add(armorStand);
    }

    public void setArmorStands() {
        if (armorStandList.isEmpty()) {
            for (int i = 0; i < size; i++) {
                createArmorStand(location);
            }
        }
    }

    public void runRotateClockWise() {
        angle += 0.17;
        angle = angle % CIRCLE;

        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
        double power = 2.5;

        for (int i = size - 1; i >= 0; i--) {
            ArmorStand armorStand = armorStandList.get(i);
            double x = (radius + (i * .6)) * cosAngle;
            double z = (radius + (i * .6)) * sinAngle;
            Utils.teleport(armorStand, location.clone().add(x, 0, z));
            for (Player player : getInstance().getServer().getOnlinePlayers()) {
                if (player.getWorld() != location.getWorld()) continue;
                if (Utils.distanceSquared(armorStand.getLocation(), player.getLocation()) < 0.257) player.setVelocity(new Vector(Math.sin(angle - .5) * power , 0, Math.cos(angle - .5) * power));
            }
        }
    }

    public void runRotateClockWiseReverse() {
        angle += 0.17;
        angle = angle % CIRCLE;

        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
        double power = 2.5;

        for (int i = size - 1; i >= 0; i--) {
            ArmorStand armorStand = armorStandList.get(i);
            double x = (radius + (i * .6)) * sinAngle;
            double z = (radius + (i * .6)) * cosAngle;
            Utils.teleport(armorStand, location.clone().add(x, 0, z));
            for (Player player : getInstance().getServer().getOnlinePlayers()) {
                if (player.getWorld() != location.getWorld()) continue;
                if (Utils.distanceSquared(armorStand.getLocation(), player.getLocation()) < 0.257) player.setVelocity(new Vector(Math.sin(angle - .5) * power , 0, Math.cos(angle - .5) * power));
            }
        }
    }

    public void rotateClockWise() {
        if (Utils.isPaper()) getInstance().getServer().getScheduler().runTaskTimerAsynchronously(getInstance(), this::runRotateClockWise, 5, Settings.WIPEOUT_ROTROD_ROTATE_SPEED.getInt());
        else getInstance().getServer().getScheduler().runTaskTimer(getInstance(), this::runRotateClockWise, 5, Settings.WIPEOUT_ROTROD_ROTATE_SPEED.getInt());
    }

    public void rotateClockWiseReverse() {
        if (Utils.isPaper()) getInstance().getServer().getScheduler().runTaskTimerAsynchronously(getInstance(), this::runRotateClockWiseReverse, 5, Settings.WIPEOUT_ROTROD_ROTATE_SPEED.getInt());
        else getInstance().getServer().getScheduler().runTaskTimer(getInstance(), this::runRotateClockWiseReverse, 5, Settings.WIPEOUT_ROTROD_ROTATE_SPEED.getInt());
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
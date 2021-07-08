package main.java.com.irfandumanx.apiawipeout.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class ARotrod extends Region {
    public ARotrod(ApiaWipeout instance) {
        super(instance);
    }

    public abstract List<ArmorStand> armorStandList();
    public abstract ItemStack getItemStack();
    public abstract void setItemStack(ItemStack itemStack);
    public abstract int getSize();
    public abstract void setSize(int size);
    public abstract int getRadius();
    public abstract void setRadius(int radius);
    public abstract String getType();
    public abstract void setType(String type);
}

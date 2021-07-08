package main.java.com.irfandumanx.apiawipeout.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class WaterFall extends Region {

    public WaterFall(ApiaWipeout instance) {
        super(instance);
    }

    @Override
    public void start() {
        //ApiaTeam
    }

    @Override
    public Map<Location, ItemStack> getOldBlock() {
        return null;
    }
}

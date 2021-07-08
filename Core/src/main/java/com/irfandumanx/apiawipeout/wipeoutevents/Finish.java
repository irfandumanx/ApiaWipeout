package main.java.com.irfandumanx.apiawipeout.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Finish extends Region {

    public Finish(ApiaWipeout instance) {
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

package main.java.com.irfandumanx.apiawipeout.wipeoutevents;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ThrowBlock extends Region {
    private int throwPower;

    public int getThrowPower() {
        return throwPower;
    }

    public void setThrowPower(Integer throwPower) {
        this.throwPower = throwPower;
    }

    public ThrowBlock(ApiaWipeout instance) {
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

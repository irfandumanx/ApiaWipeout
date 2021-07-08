package main.java.com.irfandumanx.apiawipeout.listeners;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.ARotrod;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class ArmorStandListener implements Listener {
    private final ApiaWipeout instance;

    public ArmorStandListener(ApiaWipeout instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClickArmorStand(PlayerArmorStandManipulateEvent event) {
        for (Region region : instance.getRegionManager().getRegions().values()) {
            if (!(region instanceof ARotrod)) continue;
            if (((ARotrod) region).armorStandList().contains(event.getRightClicked())) event.setCancelled(true);
        }
    }

}

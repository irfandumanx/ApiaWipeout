package main.java.com.irfandumanx.apiawipeout.listeners;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.Game;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.APistonsMove;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.HashSet;
import java.util.Set;

public class PistonListener implements Listener {

    private final ApiaWipeout instance;
    private final Set<Location> pistons;

    public PistonListener(ApiaWipeout instance) {
        this.instance = instance;
        pistons = new HashSet<>();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void pistonRetract(BlockPistonRetractEvent event) {
        if (Game.getGameState() == Game.WAITING) return;
        Region region = instance.getRegionManager().getRegion(event.getBlock().getLocation());
        if (region instanceof APistonsMove) {
            pistons.add(event.getBlock().getLocation());
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void pistonRetract2(BlockPistonRetractEvent event) {
        if (Game.getGameState() == Game.WAITING) return;
        if (pistons.contains(event.getBlock().getLocation())) {
            pistons.remove(event.getBlock().getLocation());
            event.setCancelled(false);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void pistonExtend(BlockPistonExtendEvent event) {
        if (Game.getGameState() == Game.WAITING) return;
        Region region = instance.getRegionManager().getRegion(event.getBlock().getLocation());
        if (region instanceof APistonsMove) {
            pistons.add(event.getBlock().getLocation());
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void pistonExtend2(BlockPistonExtendEvent event) {
        if (Game.getGameState() == Game.WAITING) return;
        if (pistons.contains(event.getBlock().getLocation())) {
            pistons.remove(event.getBlock().getLocation());
            event.setCancelled(false);
        }
    }
}

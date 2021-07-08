package main.java.com.irfandumanx.apiawipeout.v1_16_R3.listeners;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.Game;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import main.java.com.irfandumanx.apiawipeout.v1_16_R3.misc.AsyncBlocksSet;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.FallBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.function.Consumer;

public class BlockFallListener implements Listener {

    private final ApiaWipeout instance;

    public BlockFallListener(ApiaWipeout instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (Game.getGameState() == Game.WAITING || (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ())) return;
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
            Region region = instance.getRegionManager().getRegion(block.getLocation());
            Consumer<Region> regionConsumer = region1 -> {
                if (region instanceof FallBlock && block.getType() != Material.AIR) {
                    instance.getServer().getScheduler().runTaskLater(instance, () -> {
                        BlockData blockData = block.getBlockData();
                        AsyncBlocksSet.setBlockSuperFast(block.getLocation(), Material.AIR, false);
                        FallingBlock fallingBlock = event.getPlayer().getWorld().spawnFallingBlock(block.getLocation().add(.5, .5, .5), blockData);
                        fallingBlock.setDropItem(false);
                        fallingBlock.setHurtEntities(false);
                        fallingBlock.setMetadata("ApiaWipeout", new FixedMetadataValue(instance, true));
                        instance.getServer().getScheduler().runTaskLater(instance, () -> AsyncBlocksSet.setBlockSuperFast(block.getLocation(), blockData.getMaterial(), false), Settings.WIPEOUTFALLBLOCKREPLACE.getInt());
                    }, Settings.WIPEOUTFALLBLOCKSPEED.getInt());
                }
            };
           regionConsumer.accept(region);
        });
    }

    @EventHandler
    public void ignoreFallBlock(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK && event.getEntity().hasMetadata("ApiaWipeout")) event.setCancelled(true);
        if (event.getEntityType() == EntityType.FALLING_BLOCK && instance.getRegionManager().getRegion(event.getBlock().getLocation()) != null) {
            event.setCancelled(true);
            event.getBlock().getState().update(true, false);
        }
    }

}

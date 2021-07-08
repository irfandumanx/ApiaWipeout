package main.java.com.irfandumanx.apiawipeout.v1_12_R1.listeners;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Item;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Pair;
import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

public class ItemClickListener implements Listener {
    private final ApiaWipeout instance;
    private long timer = 0;

    public ItemClickListener(ApiaWipeout instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEvent event) {
        Item.SelectorType selectorType = instance.getItemManager().getType(event.getItem());
        if (selectorType != null) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            if (player.isSneaking() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                if (System.currentTimeMillis() - timer < 100) return;
                timer = System.currentTimeMillis();
                Item.SelectorType nextSelectorType = instance.getItemManager().getTypeNext(selectorType);

                instance.getItemManager().removeItem(player, selectorType);
                instance.getItemManager().setItem();
                instance.getItemManager().addItem(player, instance.getItemManager().getTypeNext(selectorType));
                Messages.ITEM_TYPE_CHANGED.sendTitle(Arrays.asList("%type%", nextSelectorType.typeName), player);
            } else if (selectorType == Item.SelectorType.NORMAL) {
                if (event.getClickedBlock() == null) return;
                Pair<Location, Location> pos = instance.getSelectorManager().setMultiPos(player.getUniqueId(), event.getAction(), event.getClickedBlock().getLocation());
                Messages.SETSELECTORLOCATION.sendPlayers(Arrays.asList("%first%", pos.getFirst() != null ? Utils.getPrettyStringFromLocation(pos.getFirst()) : "-", "%second%",
                        pos.getSecond() != null ? Utils.getPrettyStringFromLocation(pos.getSecond()) : "-"), event.getPlayer());
            } else if (selectorType == Item.SelectorType.PISTON) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (event.getClickedBlock().getType() == Material.PISTON_STICKY_BASE) {
                        Location location1 = event.getClickedBlock().getLocation();
                        for (Region region : instance.getRegionManager().getRegions().values()) {
                            if (region.getBlockLocations().getFirst().equals(location1) && region.getBlockLocations().getSecond().equals(location1)) {
                                Messages.THISISALREADYREGION.sendPlayers(null, player);
                                return;
                            }
                        }
                        instance.getRegionManager().createRegion("PISTON", location1);
                        Messages.PISTONSET.sendPlayers(Arrays.asList("%x%", "" + location1.getX()
                                , "%y%", "" + location1.getY()
                                , "%z%", "" + location1.getZ()), player);
                    } else Messages.NOTASTICKYPISTON.sendPlayers(null, player);
                }
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getClickedBlock().getType() == Material.PISTON_STICKY_BASE) {
                        int id = instance.getRegionManager().getRegionWithID(event.getClickedBlock().getLocation());
                        if (id != 0) {
                            instance.getRegionManager().deleteRegion(id);
                            Messages.REGIONDELETED.sendPlayers(null, player);
                        } else Messages.CANTFOUNDREGION.sendPlayers(null, player);
                    } else Messages.NOTASTICKYPISTON.sendPlayers(null, player);
                }
            }
        }
    }
}

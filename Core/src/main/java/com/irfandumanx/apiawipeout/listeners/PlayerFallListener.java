package main.java.com.irfandumanx.apiawipeout.listeners;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.Game;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.WaterFall;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlayerFallListener implements Listener {
    private final ApiaWipeout instance;
    private int fall;
    private Map<Player, Integer> fallPlayerCount;
    private Map<Player, Long> playerTimeMap;


    public PlayerFallListener(ApiaWipeout instance) {
        this.instance = instance;
        fall = Settings.WIPEOUTLIMITEDFALLCOUNT.getInt();
        fallPlayerCount = new HashMap<>();
        playerTimeMap = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (Game.getGameState() == Game.WAITING || (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ())) return;
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            Player player = event.getPlayer();
            Region region = instance.getRegionManager().getRegion(player.getLocation());
            if (region instanceof WaterFall && region.getCheckPoint() != null && player.getLocation().getBlock().isLiquid()) {
                if (playerTimeMap.containsKey(player)) return;
                long time = System.currentTimeMillis();
                playerTimeMap.put(player, time);
                instance.getServer().getScheduler().runTaskLater(instance, () -> {
                    if (playerTimeMap.containsKey(player) && playerTimeMap.get(player) == time) playerTimeMap.remove(player);
                }, 20);
                if (!fallPlayerCount.containsKey(player))
                    fallPlayerCount.put(player, fall);
                if (fall == 0) {
                    instance.getServer().getScheduler().runTask(instance, () -> player.teleport(region.getCheckPoint()));
                    return;
                }
                if (fallPlayerCount.get(player) != 0) {
                    fallPlayerCount.replace(player, fallPlayerCount.get(player) - 1);
                    Messages.FALLCOUNTMESSAGE.sendPlayers(Arrays.asList("%limited_fall_count%", "" + fallPlayerCount.get(player)), player);
                    instance.getServer().getScheduler().runTask(instance, () -> player.teleport(region.getCheckPoint()));
                } else {
                    fallPlayerCount.replace(player, fall);
                    Messages.FALLOVERMESSAGE.sendPlayers(null, player);
                    if (Game.getBaseLocation() != null) instance.getServer().getScheduler().runTask(instance, () -> player.teleport(Game.getBaseLocation()));
                }
            }
        });
    }

    public Map<Player, Integer> getFallPlayerCount() {
        return fallPlayerCount;
    }
}

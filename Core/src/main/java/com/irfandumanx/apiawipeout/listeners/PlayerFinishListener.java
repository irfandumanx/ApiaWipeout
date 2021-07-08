package main.java.com.irfandumanx.apiawipeout.listeners;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.Game;
import main.java.com.irfandumanx.apiawipeout.commands.StartCommand;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.Finish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;

public class PlayerFinishListener implements Listener {
    private final ApiaWipeout instance;

    public PlayerFinishListener(ApiaWipeout instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (Game.getGameState() == Game.WAITING || (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ())) return;
        Player player = event.getPlayer();
        Region region = instance.getRegionManager().getRegion(player.getLocation());
        if (region instanceof Finish) {
            if (instance.getPlayerManager().getAwardedPlayers().containsKey(player)) {
                if (Settings.WIPEOUTCANTAKEPRIZEAGAIN.getBoolean()) instance.getPlayerManager().givePrizePlayer(player, instance.getPlayerManager().getPlayerRank(player) + "");
                Messages.ALREADYFINISHEDWIPEOUT.sendPlayers(Arrays.asList("%rank%", "" + instance.getPlayerManager().getPlayerRank(player)), player);
                if (Game.getBaseLocation() != null)
                    player.teleport(Game.getBaseLocation());
                return;
            }

            long finishTime = System.currentTimeMillis();
            instance.getPlayerManager().getFinishTime().put(finishTime - StartCommand.getStartTime(), player);
            if (instance.getHologramManager() != null && instance.getHologramManager().getHolograms() != null)
                instance.getHologramManager().updateHologram();

            instance.getPlayerManager().givePrizePlayer(player, instance.getPlayerManager().getPlayerRank(player) + "");
            if (Game.getBaseLocation() != null)
                player.teleport(Game.getBaseLocation());
        }

    }
}

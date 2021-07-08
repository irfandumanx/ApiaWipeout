package main.java.com.irfandumanx.apiawipeout.listeners;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.Game;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.ThrowBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PlayerThrowingListener implements Listener {
    private final ApiaWipeout instance;

    public PlayerThrowingListener(ApiaWipeout instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (Game.getGameState() == Game.WAITING || (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ())) return;

        Player player = event.getPlayer();

        Region region = instance.getRegionManager().getRegion(player.getLocation());
        if (!(region instanceof ThrowBlock)) return;

        if (((ThrowBlock) region).getThrowPower() != 0 && player.isOnGround()) {
            if (Settings.WIPEOUTGETBLIND.getBoolean()) player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
                player.setVelocity(new Vector(0, (((ThrowBlock) region).getThrowPower() / 4.0), 0));
        }

    }
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player &&
                event.getCause() == EntityDamageEvent.DamageCause.FALL &&
                instance.getRegionManager().getRegion(event.getEntity().getLocation()) != null)
            event.setCancelled(true);
    }

}

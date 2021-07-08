package main.java.com.irfandumanx.apiawipeout.hologram;

import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import com.gmail.filoghost.holographicdisplays.object.line.CraftTouchSlimeLine;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class HologramClickListener implements Listener {
    private final ApiaWipeout instance;

    private Map<Player, Long> antiClickSpam;

    public HologramClickListener(ApiaWipeout instance) {
        this.instance = instance;
        antiClickSpam = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onSlimeInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.SLIME) {
            return;
        }

        Player clicker = event.getPlayer();
        if (clicker.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        NMSEntityBase entityBase = HolographicDisplays.getNMSManager().getNMSEntityBase(event.getRightClicked());
        if (entityBase == null || !(entityBase.getHologramLine() instanceof CraftTouchSlimeLine)) {
            return;
        }

        CraftTouchSlimeLine touchSlime = (CraftTouchSlimeLine) entityBase.getHologramLine();
        if (touchSlime.getTouchablePiece().getTouchHandler() == null) {
            return;
        }

        Long lastClick = antiClickSpam.get(clicker);
        if (lastClick != null && System.currentTimeMillis() - lastClick.longValue() < 100) {
            return;
        }

        antiClickSpam.put(event.getPlayer(), System.currentTimeMillis());

        try {
            instance.getHologramManager().updateHologram(event.getPlayer(), instance.getHologramManager().getCurrentPage().get(event.getPlayer()) + (event.getPlayer().isSneaking() ? -1 : 1));
            event.setCancelled(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

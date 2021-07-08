package main.java.com.irfandumanx.apiawipeout.listeners;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {
    private final ApiaWipeout instance;

    public JoinQuitListener(ApiaWipeout instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (instance.getHologramManager() != null && instance.getHologramManager().getHologramLocation() != null && !instance.getHologramManager().getHolograms().containsKey(player))
            instance.getHologramManager().createHologram(player);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (instance.getHologramManager() != null) instance.getHologramManager().delete(event.getPlayer());
    }
}

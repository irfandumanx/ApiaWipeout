package main.java.com.irfandumanx.apiawipeout.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.Game;
import main.java.com.irfandumanx.apiawipeout.dates.DateTimer;
import main.java.com.irfandumanx.apiawipeout.listeners.PlayerFallListener;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class StopCommand implements ICommand {
    public final String mainCommand = "stop";
    public final Set<String> alternativeCommands = Sets.newHashSet(Settings.WIPEOUT_ARGUMENT_STOP.getStringList());
    private final ApiaWipeout instance;

    public StopCommand(ApiaWipeout instance) {
        this.instance = instance;
    }

    @Override
    public boolean applyConditions(CommandSender sender, String[] args) {
        if (!sender.hasPermission("wipeout.admin")) {
            Messages.NOPERM.sendPlayers(null, sender);
            return false;
        }
        if (Game.getGameState() != Game.GAME) {
            Messages.WIPEOUTALREADYNOTSTARTED.sendPlayers(null, sender);
            return false;
        }
        return true;
    }

    @Override
    public void apply(CommandSender sender, String[] args) {
        instance.getRegionManager().getRegions().values().forEach(Region::stop);
        instance.getServer().getScheduler().runTaskLaterAsynchronously(instance, () -> Game.setGameState(Game.WAITING), 15);
        instance.getPlayerManager().getFinishTime().clear();
        instance.getPlayerManager().getAwardedPlayers().clear();
        instance.getPlayerFallListener().getFallPlayerCount().clear();
        if (instance.getHologramManager() != null && instance.getHologramManager().getHolograms() != null)
            instance.getHologramManager().updateHologram();
        Messages.WIPEOUTSTOPPED.sendPlayers(null, sender);
        new DateTimer(instance);
    }

    @Override
    public String getMainCommand() {
        return mainCommand;
    }

    @Override
    public Set<String> getAlternativeCommands() {
        return alternativeCommands;
    }

}

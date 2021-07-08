package main.java.com.irfandumanx.apiawipeout.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.Game;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import org.bukkit.command.CommandSender;

import java.util.Set;
import java.util.stream.Collectors;

public class StartCommand implements ICommand {
    public final String mainCommand = "start";
    public final Set<String> alternativeCommands = Sets.newHashSet(Settings.WIPEOUT_ARGUMENT_START.getStringList());
    private static long startTime;
    private final ApiaWipeout instance;

    public StartCommand(ApiaWipeout instance) {
        this.instance = instance;
    }

    @Override
    public boolean applyConditions(CommandSender sender, String[] args) {
        if (!sender.hasPermission("wipeout.admin")) {
            Messages.NOPERM.sendPlayers(null, sender);
            return false;
        }
        if (Game.getGameState() == Game.GAME) {
            Messages.WIPEOUTALREADYSTARTED.sendPlayers(null, sender);
            return false;
        }
        return true;
    }

    @Override
    public void apply(CommandSender sender, String[] args) {
        instance.getRegionManager().getRegions().values().forEach(Region::start);
        Game.setGameState(Game.GAME);
        instance.getServer().broadcastMessage(Messages.WIPEOUTSTARTED.getString());
        startTime = System.currentTimeMillis();
        Game.finishTimer(Settings.WIPEOUT_STOP_TIME_NOTIFICATION.getStringList().stream().map(Integer::parseInt).collect(Collectors.toSet()));
    }

    @Override
    public String getMainCommand() {
        return mainCommand;
    }

    @Override
    public Set<String> getAlternativeCommands() {
        return alternativeCommands;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime(long startTime) {
        StartCommand.startTime = startTime;
    }
}

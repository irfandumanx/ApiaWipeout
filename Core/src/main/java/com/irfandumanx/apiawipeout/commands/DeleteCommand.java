package main.java.com.irfandumanx.apiawipeout.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Pair;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DeleteCommand implements ICommand {
    public final String mainCommand = "delete";
    public final Set<String> alternativeCommands = Sets.newHashSet(Settings.WIPEOUT_ARGUMENT_DELETE.getStringList());
    private final ApiaWipeout instance;
    private Pair<Location, Location> locationPair;

    public DeleteCommand(ApiaWipeout instance) {
        this.instance = instance;
    }

    @Override
    public boolean applyConditions(CommandSender sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            instance.getLogger().info(Messages.ONLYPLAYER.getString());
            return false;
        }
        if (!sender.hasPermission("wipeout.admin")) {
            Messages.NOPERM.sendPlayers(null, sender);
            return false;
        }
        locationPair = instance.getSelectorManager().getPosOfPlayer(((Player)sender).getUniqueId());
        if (locationPair == null || locationPair.getFirst() == null || locationPair.getSecond() == null) {
            Messages.SETLOCATIONFORCONTUNUE.sendPlayers(null, sender);
            return false;
        }
        return true;
    }

    @Override
    public void apply(CommandSender sender, String[] args) {
        boolean isFoundRegion = false;
        for (Map.Entry<Integer, Region> entry : new HashMap<>(instance.getRegionManager().getRegions()).entrySet()) {
            if ((locationPair.getFirst().equals(entry.getValue().getBlockLocations().getFirst())
                    || locationPair.getFirst().equals(entry.getValue().getBlockLocations().getSecond()))
                    && (locationPair.getSecond().equals(entry.getValue().getBlockLocations().getSecond())
                    || locationPair.getSecond().equals(entry.getValue().getBlockLocations().getFirst()))) {
                instance.getRegionManager().deleteRegion(entry.getKey());
                Messages.REGIONDELETED.sendPlayers(null, sender);
                isFoundRegion = true;
                break;
            }
        }
        if (!isFoundRegion) Messages.CANTFOUNDREGION.sendPlayers(null, sender);
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

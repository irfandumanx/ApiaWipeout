package main.java.com.irfandumanx.apiawipeout.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.region.CheckPoint;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CheckPointCommand implements ICommand {
    public final String mainCommand = "checkpoint";
    public final Set<String> alternativeCommands = Sets.newHashSet(Settings.WIPEOUT_ARGUMENT_CHECKPOINT.getStringList());
    private final ApiaWipeout instance;

    public CheckPointCommand(ApiaWipeout instance) {
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
        if (args.length == 1 || args.length == 2) {
            Messages.HELPMESSAGE.getStringList().forEach(sender::sendMessage);
            return false;
        }
        return true;
    }

    @Override
    public void apply(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args[1].equalsIgnoreCase("create")) {
            for (CheckPoint checkPoint : instance.getCheckPointManager().getCheckPoints()) {
                if (args[2].equals(checkPoint.getName())) Messages.CHECKPOINTNAMEALREADYTOOK.sendPlayers(Arrays.asList("%checkpoint_name%", checkPoint.getName()), sender);
                if (player.getLocation().getBlockX() == checkPoint.getLocation().getBlockX() &&
                        player.getLocation().getBlockY() == checkPoint.getLocation().getBlockY() &&
                        player.getLocation().getBlockZ() == checkPoint.getLocation().getBlockZ()) {
                    Messages.CHECKPOINTLOCATIONALREADYTOOK.sendPlayers(Arrays.asList("%checkpoint_name%", checkPoint.getName()), player);
                }
            }
            instance.getCheckPointManager().createCheckPoint(args[2], player.getLocation());
            Messages.CHECKPOINTCREATED.sendPlayers(Arrays.asList(
                    "%checkpoint_name%", args[2]
                    , "%x%", "" + player.getLocation().getBlockX()
                    , "%y%", "" + player.getLocation().getBlockY()
                    , "%z%", "" + player.getLocation().getBlockZ())
                    , player);
        } else if (args[1].equalsIgnoreCase("delete")) {
            CheckPoint checkPoint = instance.getCheckPointManager().getCheckPointWithName(args[2]);
            if (checkPoint == null) {
                Messages.NOFOUNDCHECKPOINT.sendPlayers(Arrays.asList("%checkpoint_name%", args[2]), player);
                return;
            }
            boolean isHaveRegion = false;
            int i = 0;
            for (Map.Entry<Integer, Region> entry : new HashMap<>(instance.getRegionManager().getRegions()).entrySet()) {
                if (entry.getValue().getCheckPoint() == checkPoint.getLocation()) {
                    instance.getRegionManager().deleteRegion(entry.getKey());
                    i++;
                    isHaveRegion = true;
                }
            }
            instance.getCheckPointManager().deleteCheckPoint(checkPoint);
            if (isHaveRegion) Messages.WIPEOUT_DELETED_REGION_WITH_CHECKPOINT.sendPlayers(Arrays.asList("%checkpoint_name%", args[2], "%region_count%", i + ""), player);
            else Messages.CHECKPOINTDELETED.sendPlayers(Arrays.asList("%checkpoint_name%", args[2]), player);
        }
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

package main.java.com.irfandumanx.apiawipeout.v1_12_R1.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.commands.ICommand;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class ShowRegionCommand implements ICommand {
    public final String mainCommand = "showregion";
    public final Set<String> alternativeCommands = Sets.newHashSet(Settings.WIPEOUT_ARGUMENT_SHOWREGION.getStringList());
    private final ApiaWipeout instance;

    public ShowRegionCommand(ApiaWipeout instance) {
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
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void apply(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        instance.getRegionManager().getRegions().values().forEach(v -> {
            player.sendBlockChange(v.getBlockLocations().getFirst(), Material.GOLD_BLOCK, (byte) 0);
            player.sendBlockChange(v.getBlockLocations().getSecond(), Material.GOLD_BLOCK, (byte) 0);
            instance.getServer().getScheduler().runTaskLater(instance, () -> {
                player.sendBlockChange(v.getBlockLocations().getFirst(), v.getBlockLocations().getFirst().getBlock().getType(), v.getBlockLocations().getFirst().getBlock().getState().getData().getData());
                player.sendBlockChange(v.getBlockLocations().getSecond(), v.getBlockLocations().getSecond().getBlock().getType(), v.getBlockLocations().getSecond().getBlock().getState().getData().getData());
            }, 200);
        });
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

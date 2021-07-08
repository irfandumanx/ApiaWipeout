package main.java.com.irfandumanx.apiawipeout.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class HologramCommand implements ICommand {
    public final String mainCommand = "hologram";
    public final Set<String> alternativeCommands = Sets.newHashSet(Settings.WIPEOUT_ARGUMENT_HOLOGRAM.getStringList());
    private final ApiaWipeout instance;

    public HologramCommand(ApiaWipeout instance) {
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
        if (instance.getHologramManager() == null) {
            sender.sendMessage("§8[§cError§8] §fHolographicDisplays not found");
            return false;
        }
        if (args.length == 1) {
            Messages.HELPMESSAGE.getStringList().forEach(sender::sendMessage);
            return false;
        }
        return true;
    }

    @Override
    public void apply(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args[1].equalsIgnoreCase("create")) {
            instance.getHologramManager().setHologramLocation(player.getLocation());
            instance.getServer().getOnlinePlayers().forEach(player1 -> instance.getHologramManager().createHologram(player1));
            Messages.CREATEHOLOGRAM.sendPlayers(null, player);
        } else if (args[1].equalsIgnoreCase("delete")) {
            instance.getHologramManager().deleteHologram(player);
            instance.getHologramManager().setHologramLocation(null);
        }

        instance.getFileManager().getGameCFile().set("hologramlocation", null);
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

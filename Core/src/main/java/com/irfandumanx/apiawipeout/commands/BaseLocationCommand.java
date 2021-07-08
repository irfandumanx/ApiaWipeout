package main.java.com.irfandumanx.apiawipeout.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.Game;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class BaseLocationCommand implements ICommand {
    public final String mainCommand = "baselocation";
    public final Set<String> alternativeCommands = Sets.newHashSet(Settings.WIPEOUT_ARGUMENT_BASELOCATION.getStringList());
    private final ApiaWipeout instance;

    public BaseLocationCommand(ApiaWipeout instance) {
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
        if (args.length == 1) {
            Messages.HELPMESSAGE.getStringList().forEach(sender::sendMessage);
            return false;
        }
        return true;
    }

    @Override
    public void apply(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args[1].equalsIgnoreCase("set")) {
            Messages.SETBASELOCATION.sendPlayers(null, player);
            Game.setBaseLocation(player.getLocation());
        }
        if (args[1].equalsIgnoreCase("delete")) {
            Messages.DELBASELOCATION.sendPlayers(null, player);
            Game.setBaseLocation(null);
            instance.getFileManager().getGameCFile().set("baselocation", null);
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

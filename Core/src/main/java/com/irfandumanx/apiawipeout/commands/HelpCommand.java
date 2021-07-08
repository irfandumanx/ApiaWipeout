package main.java.com.irfandumanx.apiawipeout.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class HelpCommand implements ICommand {
    public final String mainCommand = "help";
    public final Set<String> alternativeCommands = Sets.newHashSet(Settings.WIPEOUT_ARGUMENT_HELP.getStringList());
    private final ApiaWipeout instance;

    public HelpCommand(ApiaWipeout instance) {
        this.instance = instance;
    }

    @Override
    public boolean applyConditions(CommandSender sender, String[] args) {
        if (!sender.hasPermission("wipeout.admin")) {
            Messages.NOPERM.sendPlayers(null, sender);
            return false;
        }
        return true;
    }

    @Override
    public void apply(CommandSender sender, String[] args) {
        Messages.HELPMESSAGE.getStringList().forEach(sender::sendMessage);
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

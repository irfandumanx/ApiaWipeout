package main.java.com.irfandumanx.apiawipeout.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class MainCommand implements ICommand {
    public final String mainCommand = "";
    public final Set<String> alternativeCommands = Sets.newHashSet();
    private final ApiaWipeout instance;

    public MainCommand(ApiaWipeout instance) {
        this.instance = instance;
    }

    @Override
    public boolean applyConditions(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Messages.HELPMESSAGE.getStringList().forEach(sender::sendMessage);
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

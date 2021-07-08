package main.java.com.irfandumanx.apiawipeout.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class ReloadCommand implements ICommand {
    public final String mainCommand = "reload";
    public final Set<String> alternativeCommands = Sets.newHashSet(Settings.WIPEOUT_ARGUMENT_RELOAD.getStringList());
    private final ApiaWipeout instance;

    public ReloadCommand(ApiaWipeout instance) {
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
        instance.getFileManager().reloadSystem();
        Messages.RELOAD.sendPlayers(null, sender);
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

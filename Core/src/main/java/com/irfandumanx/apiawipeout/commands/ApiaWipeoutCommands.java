package main.java.com.irfandumanx.apiawipeout.commands;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ApiaWipeoutCommands implements CommandExecutor {
    private final ApiaWipeout instance;

    public ApiaWipeoutCommands(ApiaWipeout instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string).append(" ");
        }

        ICommand iCommand = instance.getCommandManager().getCommand(stringBuilder.toString().trim());
        if (iCommand.applyConditions(commandSender, strings)) {
            iCommand.apply(commandSender, strings);
            return true;
        }
        return false;
    }
}

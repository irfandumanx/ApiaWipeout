package main.java.com.irfandumanx.apiawipeout.commands;

import org.bukkit.command.CommandSender;

import java.util.Set;

public interface ICommand {
    boolean applyConditions(CommandSender sender, String[] args);
    void apply(CommandSender sender, String[] args);
    String getMainCommand();
    Set<String> getAlternativeCommands();
}

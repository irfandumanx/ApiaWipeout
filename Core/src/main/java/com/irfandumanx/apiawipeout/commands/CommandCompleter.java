package main.java.com.irfandumanx.apiawipeout.commands;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandCompleter implements TabCompleter {

    private final ApiaWipeout instance;

    public CommandCompleter(ApiaWipeout instance) {
        this.instance = instance;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (command.getName().equalsIgnoreCase("wipeout")) {
            if (player.isOp()) {
                List<String> completedCommands = new ArrayList<>();
                completedCommands.add("reload");
                completedCommands.add("selector");
                completedCommands.add("set");
                completedCommands.add("delete");
                completedCommands.add("start");
                completedCommands.add("stop");
                completedCommands.add("checkpoint");
                completedCommands.add("baselocation");
                completedCommands.add("hologram");
                completedCommands.add("starttimer");
                completedCommands.add("stoptimer");
                completedCommands.add("showregion");
                completedCommands.add("help");

                List<String> forResult = new ArrayList<>();
                List<String> resultOfCommand = new ArrayList<>();
                if (strings.length == 1) {
                    for (String commands : completedCommands) {
                        if (commands.startsWith(strings[0]))
                            resultOfCommand.add(commands);
                    }
                }
                if (strings.length == 2 && strings[0].equalsIgnoreCase("set")) {
                    forResult.add("startline");
                    forResult.add("downup");
                    forResult.add("fall");
                    forResult.add("flow");
                    forResult.add("onetap");
                    forResult.add("throw");
                    forResult.add("water");
                    forResult.add("finish");
                    forResult.add("ramp");
                    forResult.add("rotrod");
                    for (String commands : forResult) {
                        if (commands.startsWith(strings[1]))
                            resultOfCommand.add(commands);
                    }
                }

                if (strings.length == 2 && strings[0].equalsIgnoreCase("checkpoint")) {
                    forResult.add("create");
                    forResult.add("delete");
                    for (String commands : forResult) {
                        if (commands.startsWith(strings[1]))
                            resultOfCommand.add(commands);
                    }
                }

                if (strings.length == 2 && strings[0].equalsIgnoreCase("baselocation")) {
                    forResult.add("set");
                    forResult.add("delete");
                    for (String commands : forResult) {
                        if (commands.startsWith(strings[1]))
                            resultOfCommand.add(commands);
                    }
                }

                if (strings.length == 2 && strings[0].equalsIgnoreCase("hologram")) {
                    forResult.add("create");
                    forResult.add("delete");
                    for (String commands : forResult) {
                        if (commands.startsWith(strings[1]))
                            resultOfCommand.add(commands);
                    }
                }

                if (strings.length == 3 && strings[1].equalsIgnoreCase("ramp")) {
                    forResult.add("SOUTH");
                    forResult.add("NORTH");
                    forResult.add("EAST");
                    forResult.add("WEST");
                    for (String commands : forResult) {
                        if (commands.startsWith(strings[2]))
                            resultOfCommand.add(commands);
                    }
                }

                if (strings.length == 3 && strings[1].equalsIgnoreCase("rotrod")) {
                    for (Material materialName : Material.values()) {
                        forResult.add(materialName.name());
                    }

                    for (String commands : forResult) {
                        if (commands.startsWith(strings[2].toUpperCase()))
                            resultOfCommand.add(commands);
                    }
                }

                if (strings.length == 6 && strings[1].equalsIgnoreCase("rotrod")) {
                    forResult.add("-");
                    forResult.add("+");
                    for (String commands : forResult) {
                        if (commands.startsWith(strings[5]))
                            resultOfCommand.add(commands);
                    }
                }

                return resultOfCommand;
            }
        }
        return null;
    }
}

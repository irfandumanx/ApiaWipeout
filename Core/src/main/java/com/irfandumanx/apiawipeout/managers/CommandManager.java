package main.java.com.irfandumanx.apiawipeout.managers;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.commands.*;
import main.java.com.irfandumanx.apiawipeout.misc.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;


/*


                                                                               02.06.2021 07.44
                                                                Komut eşleşme algoritması üst düzeye çıkarılacak
                                                 Komutlarla iletişim CommandManagerdaki instance sayesinde olacak (Status Code: 200)
                                                                               /\ irfandumanx /\


 */


public class CommandManager {
    private ICommand mainCommand, helpCommand, reloadCommand, startCommand, showRegionCommand, stopCommand, selectorCommand,
            setCommand, deleteCommand, checkPointCommand, hologramCommand, baseLocationCommand, startTimerCommand, stopTimerCommand;

    private final List<ICommand> commands;

    public CommandManager(ApiaWipeout instance) {
        mainCommand = new MainCommand(instance);
        helpCommand = new HelpCommand(instance);
        reloadCommand = new ReloadCommand(instance);
        startCommand = new StartCommand(instance);
        stopCommand = new StopCommand(instance);
        selectorCommand = new SelectorCommand(instance);
        setCommand = new SetCommand(instance);
        deleteCommand = new DeleteCommand(instance);
        checkPointCommand = new CheckPointCommand(instance);
        hologramCommand = new HologramCommand(instance);
        try {
            showRegionCommand = Utils.isLegacy() ? (ICommand) Class.forName("main.java.com.irfandumanx.apiawipeout.v1_12_R1.commands.ShowRegionCommand").getConstructor(ApiaWipeout.class).newInstance(instance) : (ICommand) Class.forName("main.java.com.irfandumanx.apiawipeout.v1_16_R3.commands.ShowRegionCommand").getConstructor(ApiaWipeout.class).newInstance(instance);
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        baseLocationCommand = new BaseLocationCommand(instance);
        startTimerCommand = new StartTimerCommand(instance);
        stopTimerCommand = new StopTimerCommand(instance);

        commands = Arrays.asList(mainCommand, helpCommand, reloadCommand, startCommand, stopCommand, selectorCommand, setCommand,
                deleteCommand, checkPointCommand, hologramCommand, showRegionCommand, baseLocationCommand, startTimerCommand, stopTimerCommand);
    }

    public ICommand getCommand(String mainCommand) {
        //return commands.stream().filter(command -> command.getMainCommand().contains(mainCommand) || command.getAlternativeCommands().stream().anyMatch(mainCommand::contains)).findFirst().orElse(this.mainCommand);
        if (mainCommand.startsWith("set") || setCommand.getAlternativeCommands().stream().anyMatch(mainCommand::startsWith)) return setCommand;
        else if (mainCommand.startsWith("checkpoint") || checkPointCommand.getAlternativeCommands().stream().anyMatch(mainCommand::startsWith)) return checkPointCommand;
        else if (mainCommand.startsWith("hologram") || hologramCommand.getAlternativeCommands().stream().anyMatch(mainCommand::startsWith)) return hologramCommand;
        else if (mainCommand.startsWith("baselocation") || baseLocationCommand.getAlternativeCommands().stream().anyMatch(mainCommand::startsWith)) return baseLocationCommand;
        else return commands.stream().filter(command -> command.getMainCommand().equalsIgnoreCase(mainCommand) || command.getAlternativeCommands().stream().anyMatch(mainCommand::equalsIgnoreCase)).findFirst().orElse(this.mainCommand);
    }

    public ICommand getStartCommand() {
        return startCommand;
    }

    public ICommand getStartTimerCommand() {
        return startTimerCommand;
    }

    public ICommand getSetCommand() {
        return setCommand;
    }

    public ICommand getStopCommand() { return stopCommand; }
}

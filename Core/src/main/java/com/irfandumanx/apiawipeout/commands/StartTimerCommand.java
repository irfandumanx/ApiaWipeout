package main.java.com.irfandumanx.apiawipeout.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.Game;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class StartTimerCommand implements ICommand {
    public final String mainCommand = "starttimer";
    public final Set<String> alternativeCommands = Sets.newHashSet(Settings.WIPEOUT_ARGUMENT_STARTTIMER.getStringList());
    public static int timer;
    public static boolean timerIsStart = false;
    private final ApiaWipeout instance;

    public StartTimerCommand(ApiaWipeout instance) {
        this.instance = instance;
    }

    @Override
    public boolean applyConditions(CommandSender sender, String[] args) {
        if (!sender.hasPermission("wipeout.admin")) {
            Messages.NOPERM.sendPlayers(null, sender);
            return false;
        }
        if (timerIsStart) {
            Messages.STARTERALREADYSTART.sendPlayers(null, sender);
            return false;
        }
        if (Game.getGameState() == Game.GAME) {
            Messages.WIPEOUTALREADYSTARTED.sendPlayers(null, sender);
            return false;
        }
        return true;
    }

    @Override
    public void apply(CommandSender sender, String[] args) {
        String titleMessage = Messages.WIPEOUT_START_MESSAGE_TITLE.getString();
        String subTitleMessage = Messages.WIPEOUT_START_MESSAGE_SUBTITLE.getString();
        String actionBar = Messages.WIPEOUT_START_MESSAGE_ACTIONBAR.getString();
        String broadcast = Messages.WIPEOUT_START_MESSAGE_BROADCAST.getString();

        timer = Settings.WIPEOUTSTARTTIMER.getInt();
        Messages.STARTERSTARTMESSAGE.sendPlayers(null, sender);
        timerIsStart = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (timer == -1)
                    cancel();

                else if (timer == 0) {
                    instance.getServer().getScheduler().runTask(instance, () -> new StartCommand(instance).apply(null, null));
                    instance.getServer().getOnlinePlayers().forEach(player -> {
                        Utils.clearTitle(player);
                        Messages.WIPEOUTSTARTED.sendPlayers(null, player);
                    });
                    timerIsStart = false;
                    cancel();
                } else {
                    for (String number : Settings.WIPEOUTSTARTSTARTTIMERNOTIFICATION.getStringList()) {
                        if (timer == Integer.parseInt(number)) {
                            if (timer > 60) {
                                instance.getServer().getOnlinePlayers().forEach(player -> {
                                    if (!titleMessage.isEmpty()) Utils.sendTitle(player, titleMessage.replace("%starttime%", (timer / 60) + Messages.MINUTE.getString() + " " + (timer % 60) + Messages.SECOND.getString()), subTitleMessage.replace("%starttime%", (timer / 60) + Messages.MINUTE.getString() + " " + (timer % 60) + Messages.SECOND.getString()), 10, 20, 10);
                                    if (!actionBar.isEmpty()) Utils.sendActionbar(player, actionBar.replace("%starttime%", (timer / 60) + Messages.MINUTE.getString() + " " + (timer % 60) + Messages.SECOND.getString()));
                                    if (!broadcast.isEmpty()) instance.getServer().broadcastMessage(broadcast.replace("%starttime%", (timer / 60) + Messages.MINUTE.getString() + " " + (timer % 60) + Messages.SECOND.getString()));
                                });
                            }
                            else {
                                instance.getServer().getOnlinePlayers().forEach(player -> {
                                    if (!titleMessage.isEmpty()) Utils.sendTitle(player, titleMessage.replace("%starttime%", timer + Messages.SECOND.getString()), subTitleMessage.replace("%starttime%", timer + Messages.SECOND.getString()), 10, 20, 10);
                                    if (!actionBar.isEmpty()) Utils.sendActionbar(player, actionBar.replace("%starttime%", timer + Messages.SECOND.getString()));
                                    if (!broadcast.isEmpty()) instance.getServer().broadcastMessage(broadcast.replace("%starttime%", timer + Messages.SECOND.getString()));
                                });
                            }
                        }
                    }
                    timer--;
                }
            }
        }.runTaskTimerAsynchronously(instance, 20, 20);
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

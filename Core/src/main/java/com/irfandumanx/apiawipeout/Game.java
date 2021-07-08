package main.java.com.irfandumanx.apiawipeout;

import main.java.com.irfandumanx.apiawipeout.commands.StartCommand;
import main.java.com.irfandumanx.apiawipeout.commands.StopCommand;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/*


                                                                       02.06.2021 10:33
                           GameManager classı olacak ve static olmayacak. (Sonradan Not -> Farklı bir şey yapmam gerekti ama üşendim.)
                                                  10 dakika 0 saniye kaldı mesajlarında saniye 0 ise gözükmeyecek (Status Code: 200)
                                                                       /\ irfandumanx /\


 */


public enum Game {
    WAITING, GAME;

    private static ApiaWipeout instance = ApiaWipeout.getInstance();
    private static Long wipeoutReverseTimer;
    public static Game game;
    private static Location baseLocation;

    public static Game getGameState() {
        return game;
    }
    public static void setGameState(Game game) {
        Game.game = game;
    }

    public static Location getBaseLocation() {
        return baseLocation;
    }

    public static void setBaseLocation(Location baseLocation) {
        Game.baseLocation = baseLocation;
    }

    public static void loadFile() {
        if (instance.getFileManager().getGameFile().exists()) {
            FileConfiguration gameC = instance.getFileManager().getGameCFile();
            if (gameC.isSet("baselocation")) Game.setBaseLocation(instance.getFileManager().getLocationFromString(gameC.getString("baselocation")));
            if (instance.getHologramManager() != null && gameC.isSet("hologramlocation")) instance.getHologramManager().setHologramLocation(instance.getFileManager().getLocationFromString(gameC.getString("hologramlocation")));
        }
     }

    public static void saveFile() {
        FileConfiguration gameC = instance.getFileManager().getGameCFile();
        if (Game.getBaseLocation() != null) gameC.set("baselocation", instance.getFileManager().getStringFromLocation(Game.getBaseLocation()));
        if (instance.getHologramManager() != null) gameC.set("hologramlocation", instance.getFileManager().getStringFromLocation(instance.getHologramManager().getHologramLocation()));
        try {
            gameC.save(instance.getFileManager().getGameFile());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void finishTimer(Set<Integer> noticeTimes) {
        instance.getServer().getScheduler().runTaskTimerAsynchronously(instance, () -> {
            if (Game.getGameState() == Game.WAITING) return;
            wipeoutReverseTimer = (Settings.WIPEOUTGAMETIME.getInt() * 60L) - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - StartCommand.getStartTime());
            for (int noticeTime : noticeTimes) {
                if (wipeoutReverseTimer == noticeTime) {
                    if (wipeoutReverseTimer > 60) {
                        int remainingSeconds = (int) (wipeoutReverseTimer % 60);
                        instance.getServer().broadcastMessage(Messages.TIMEFINISHWIPEOUT.getString().replace("%finishtime%", (wipeoutReverseTimer / 60) + Messages.MINUTE.getString() + (remainingSeconds == 0 ? "" : " " + remainingSeconds + Messages.SECOND.getString())));
                    } else instance.getServer().broadcastMessage(Messages.TIMEFINISHWIPEOUT.getString().replace("%finishtime%", wipeoutReverseTimer + Messages.SECOND.getString()));
                }
            }
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - StartCommand.getStartTime()) >= Settings.WIPEOUTGAMETIME.getInt() * 60L) {
                instance.getServer().getScheduler().runTask(instance, () -> instance.getCommandManager().getStopCommand().apply(null, null));
                instance.getServer().broadcastMessage(Messages.WIPEOUTFINISHED.getString());
            }
        }, 20, 20);
    }

    public static ApiaWipeout getInstance() {
        return instance;
    }
}



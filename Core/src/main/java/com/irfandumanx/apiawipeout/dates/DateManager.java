package main.java.com.irfandumanx.apiawipeout.dates;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class DateManager {
    private final ApiaWipeout instance;
    private static Set<WipeoutDate> wipeoutDates = Sets.newHashSet();

    public DateManager(ApiaWipeout instance) {
        this.instance = instance;
        loadWipeoutDates();
    }

    public void loadWipeoutDates() {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            FileConfiguration configC = instance.getFileManager().getConfigC();
            for (String name : instance.getFileManager().getConfigC().getConfigurationSection("wipeout_dates").getKeys(false)) {
                if (name.equals("enabled")) continue;
                String day = configC.getString("wipeout_dates." + name + ".day").toUpperCase();
                int hour = configC.getInt("wipeout_dates." + name + ".hour");
                int minute = configC.getInt("wipeout_dates." + name + ".minute");
                if (!DateTimer.ALL_DAYS.contains(day)) {
                    instance.getLogger().info("§4Incorrect input about day for " + name + " Available days: " + String.join(" ", DateTimer.ALL_DAYS));
                    continue;
                }
                if (hour < 0 || hour > 23) {
                    instance.getLogger().info("§4Incorrect input about hour for " + name + ". Please use number between 0 and 23.");
                    continue;
                }
                if (minute < 0 || minute > 59) {
                    instance.getLogger().info("§4Incorrect input about minute for " + name + ". Please use number between 0 and 60.");
                    continue;
                }
                WipeoutDate wipeoutDate = new WipeoutDate(name, day, hour, minute, configC.getString("wipeout_dates." + name + ".type"));
                wipeoutDates.add(wipeoutDate);
            }
        });
    }

    public static Set<WipeoutDate> getWipeoutDates() {
        return wipeoutDates;
    }
}

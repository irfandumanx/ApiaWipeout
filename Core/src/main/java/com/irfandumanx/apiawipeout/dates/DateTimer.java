package main.java.com.irfandumanx.apiawipeout.dates;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.commands.StartCommand;
import main.java.com.irfandumanx.apiawipeout.commands.StartTimerCommand;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public class DateTimer {
    private final ApiaWipeout instance;
    public static final Set<String> ALL_DAYS = Sets.newHashSet("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "WEEKDAY", "WEEKEND", "EVERYDAY");
    private long last;

    public DateTimer(ApiaWipeout instance) {
        this.instance = instance;
        timer();
    }

    public void timer() {
        instance.getServer().getScheduler().runTaskTimerAsynchronously(instance, () -> {
            if (Settings.WIPEOUTDATESENABLED.getBoolean()) {
                if (last != 0 && System.currentTimeMillis() - last < 60000) return;
                String currentDay = DayOfWeek.from(LocalDate.now()).name();
                boolean isWeekend = isWeekend(currentDay);
                boolean isWeekday = isWeekday(currentDay);
                for (WipeoutDate date : DateManager.getWipeoutDates()) {
                    if (Settings.WIPEOUTDEBUGMODE.getBoolean())
                        instance.getLogger().info("Current Day: " + currentDay + "\nisWeekday: " + isWeekday + "\nisWeekend: " + isWeekend + "\nDate day: " + date.getDay() + "\nDate hour: " + date.getHour() + "\nDate minute: " + date.getMinute());
                    if (!date.getDay().equals(currentDay) && !date.getDay().equals("EVERYDAY") && (!date.getDay().equals("WEEKDAY") || !isWeekday) && (!date.getDay().equals("WEEKEND") || !isWeekend)) continue;
                    LocalTime localTime = LocalTime.of(date.getHour(), date.getMinute());
                    LocalTime nowTime = LocalTime.now();
                    long until = nowTime.until(localTime, ChronoUnit.SECONDS);
                    if (Settings.WIPEOUTDEBUGMODE.getBoolean())
                        instance.getLogger().info("No problem, i passed." + "\nPlugin time: " + localTime + "\nServer time: " + nowTime + "\nUntil: " + until);
                    if (until > 0 || until < -45) continue;
                    last = System.currentTimeMillis();
                    if (date.getType().equalsIgnoreCase("START")) instance.getCommandManager().getStartCommand().apply(null, null); else instance.getCommandManager().getStartTimerCommand().apply(null, null);
                }
            }
        }, 20, 20);
    }

    public boolean isWeekend(String currentDay) {
        return currentDay.equals("SATURDAY") || currentDay.equals("SUNDAY");
    }

    public boolean isWeekday(String currentDay) { return currentDay.equals("MONDAY") || currentDay.equals("TUESDAY") || currentDay.equals("WEDNESDAY") || currentDay.equals("THURSDAY") || currentDay.equals("FRIDAY"); }
}

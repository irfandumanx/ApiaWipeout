package main.java.com.irfandumanx.apiawipeout.misc;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public enum Settings {
    WIPEOUTTYPENORMAL("type_normal"),
    WIPEOUTTYPEPISTON("type_piston"),
    WIPEOUTUPSPEED("animation.up_speed"),
    WIPEOUTDOWNSPEED("animation.down_speed"),
    WIPEOUTWAITDOWNTIME("animation.wait_down_time"),
    WIPEOUTWAITUPTIME("animation.wait_up_time"),
    WIPEOUTFALLBLOCKSPEED("animation.fall_block_speed"),
    WIPEOUTFALLBLOCKREPLACE("animation.fall_block_replace"),
    WIPEOUTPISTONRETRACTTIME("animation.piston_retract_time"),
    WIPEOUTPISTONEXTENSIONCHANCE("animation.piston_extension_chance"),
    WIPEOUTPISTONSSTARTTIME("animation.pistons_start_time"),
    WIPEOUTPISTONSEXTENSIONTIME("animation.piston_extension_time"),
    WIPEOUTLIMITEDFALLCOUNT("limited_fall_count"),
    WIPEOUTFLOWSTARTTIME("animation.water_flow_start_time"),
    WIPEOUTWATERPULLTIME("animation.water_pull_time"),
    WIPEOUTWATERREFLOWTIME("animation.water_reflow_time"),
    WIPEOUTONETAPREPLACE("animation.onetap_block_replace"),
    WIPEOUTONETAPREMOVE("animation.onetap_block_remove"),
    WIPEOUTRAMPMOVESPEED("animation.ramp_move_speed"),
    WIPEOUTRAMPWAITTIME("animation.ramp_wait_time"),
    WIPEOUT_ROTROD_ROTATE_SPEED("animation.rotrod_rotate_speed"),
    WIPEOUTCANTAKEPRIZEAGAIN("can_take_prize_again"),
    WIPEOUTGETBLIND("get_blind_when_jumping"),
    WIPEOUTGAMETIME("wipeout_game_time"),
    WIPEOUTSTARTTIMER("wipeout_command_start_time"),
    WIPEOUTSTARTSTARTTIMERNOTIFICATION("wipeout_start_time_notification"),
    WIPEOUTDATESENABLED("wipeout_dates.enabled"),
    WIPEOUTDEBUGMODE("debug"),
    WIPEOUT_ARGUMENT_RELOAD("commands.reload"),
    WIPEOUT_ARGUMENT_SHOWREGION("commands.showregion"),
    WIPEOUT_ARGUMENT_BASELOCATION("commands.baselocation"),
    WIPEOUT_ARGUMENT_CHECKPOINT("commands.checkpoint"),
    WIPEOUT_ARGUMENT_DELETE("commands.delete"),
    WIPEOUT_ARGUMENT_HELP("commands.help"),
    WIPEOUT_ARGUMENT_HOLOGRAM("commands.hologram"),
    WIPEOUT_ARGUMENT_SELECTOR("commands.selector"),
    WIPEOUT_ARGUMENT_SET("commands.set"),
    WIPEOUT_ARGUMENT_START("commands.start"),
    WIPEOUT_ARGUMENT_STARTTIMER("commands.starttimer"),
    WIPEOUT_ARGUMENT_STOP("commands.stop"),
    WIPEOUT_ARGUMENT_STOPTIMER("commands.stoptimer"),
    WIPEOUT_STOP_TIME_NOTIFICATION("wipeout_stop_time_notification"),
    ;

    private static FileConfiguration lang;
    private final String path;

    private Settings(String path) {
        this.path = path;
    }

    public static void setFile(FileConfiguration file) {
        Settings.lang = file;
    }

    public static FileConfiguration getFile() {
        if (Settings.lang != null) {
            return lang;
        }
        return null;
    }

    public String getPath() {
        return this.path;
    }

    public boolean getBoolean() {
        return lang == null ? null : Boolean.valueOf(lang.getBoolean(this.path));
    }

    public double getDouble() {
        return lang == null ? null : Double.valueOf(lang.getDouble(this.path));
    }

    public int getInt() {
        return lang == null ? null : Integer.valueOf(lang.getInt(this.path));
    }

    public String getString() {
        return lang == null ? null : ChatColor.translateAlternateColorCodes((char) '&', lang.getString(this.path));
    }

    public List<String> getStringList() {
        return lang == null ? null : lang.getStringList(this.path).stream().map(line -> ChatColor.translateAlternateColorCodes((char) '&', line)).collect(Collectors.toList());
    }

    public List<Integer> getIntList() {
        return lang == null ? null : lang.getStringList(this.path).stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    public String toString() {
        return lang == null ? null : lang.getString(this.path);
    }

    public static FileConfiguration getLang() {
        return lang;
    }
}

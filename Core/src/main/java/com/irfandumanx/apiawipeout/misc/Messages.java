package main.java.com.irfandumanx.apiawipeout.misc;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public enum Messages {
    NOPERM("messages.no_perm"),
    RELOAD("messages.reload"),
    HELPMESSAGE("messages.help_messages"),
    ONLYPLAYER("messages.only_player"),
    GIVENSELECTOR("messages.given_selector"),
    SETSELECTORLOCATION("messages.set_selector_location"),
    WIPEOUTALREADYSTARTED("messages.game_already_started"),
    WIPEOUTSTARTED("messages.game_started"),
    WIPEOUTSTOPPED("messages.game_stopped"),
    CANTFOUNDREGION("messages.cant_found_region"),
    REGIONDELETED("messages.region_deleted"),
    WIPEOUTREGIONSET("messages.region_set"),
    WIPEOUTALREADYNOTSTARTED("messages.already_not_started"),
    ITEM_TYPE_CHANGED("messages.item_type_changed"),
    PISTONSET("messages.piston_set"),
    NOTASTICKYPISTON("messages.not_a_sticky_piston"),
    THISISALREADYREGION("messages.this_already_region"),
    CHECKPOINTCREATED("messages.checkpoint_created"),
    CHECKPOINTDELETED("messages.checkpoint_deleted"),
    WIPEOUTSETWITHCHECKPOINT("messages.region_set_with_checkpoint"),
    CHECKPOINTLOCATIONALREADYTOOK("messages.checkpoint_location_already_took"),
    CHECKPOINTNAMEALREADYTOOK("messages.checkpoint_name_already_took"),
    NOFOUNDCHECKPOINT("messages.no_found_checkpoint"),
    SETBASELOCATION("messages.baselocation_set"),
    DELBASELOCATION("messages.baselocation_deleted"),
    FALLCOUNTMESSAGE("messages.fall_message"),
    FALLOVERMESSAGE("messages.fall_over_message"),
    NOTHROWPOWER("messages.no_found_throw_power"),
    SETLOCATIONFORCONTUNUE("messages.set_location_for_continue"),
    REGIONSETWITHPOWER("messages.region_set_with_power"),
    TYPES("messages.types"),
    CREATEHOLOGRAM("messages.create_hologram"),
    DELETEHOLOGRAM("messages.delete_hologram"),
    DELETENEARHOLOGRAM("messages.delete_near_hologram"),
    ALREADYFINISHEDWIPEOUT("messages.you_already_finished_wipeout"),
    WAITPLAYER("messages.waiting_player"),
    HAVETOSETBASELOC("messages.have_to_set_baselocation"),
    TIMEFINISHWIPEOUT("messages.time_finish_wipeout"),
    WIPEOUTFINISHED("messages.wipeout_finished"),
    MINUTE("messages.minute"),
    SECOND("messages.second"),
    STARTERSTARTMESSAGE("messages.start_time_message"),
    STARTERALREADYSTART("messages.already_timer_started"),
    STOPPEDTIMER("messages.timer_stopped"),
    TIMERALREADYNOTSTARTED("messages.timer_already_not_started"),
    REGIONSETWITHBLOCKNUMBERANDDIRECTION("messages.region_set_with_blocknumber_and_direction"),
    REGIONUNSETBLOCKNUMBER("messages.region_ramp_unset_blocknumber"),
    REGIONUNSETDIRECTION("messages.region_ramp_unset_direction"),
    WIPEOUT_HOLOGRAM_PAGE("messages.wipeout_hologram_page"),
    WIPEOUT_HOLOGRAM_PLAYER_LIMIT("messages.wipeout_hologram_player_limit"),
    WIPEOUT_HOLOGRAM_CHANGE_MESSAGE("messages.wipeout_hologram_change_message"),
    WIPEOUT_HOLOGRAM_FORMAT("messages.wipeout_hologram_format"),
    WIPEOUT_PAGE_FORMAT("messages.wipeout_hologram_page_format"),
    WIPEOUT_ON_PAGE_COLOR("messages.wipeout_hologram_on_page_color"),
    WIPEOUT_NOT_ON_PAGE_COLOR("messages.wipeout_hologram_not_on_page_color"),
    WIPEOUT_START_MESSAGE_TITLE("messages.start_time_messages.title"),
    WIPEOUT_START_MESSAGE_SUBTITLE("messages.start_time_messages.subtitle"),
    WIPEOUT_START_MESSAGE_ACTIONBAR("messages.start_time_messages.actionbar"),
    WIPEOUT_START_MESSAGE_BROADCAST("messages.start_time_messages.broadcast"),
    WIPEOUT_DELETED_REGION_WITH_CHECKPOINT("messages.deleted_region_with_checkpoint"),
    WIPEOUT_MISSING_ARGUMENT("messages.missing_argument"),
    WIPEOUT_REGION_SET_ROTROD("messages.region_set_rotrod"),
    ;

    private static FileConfiguration lang;
    private final String path;

    private Messages(String path) {
        this.path = path;
    }

    public static void setFile(FileConfiguration file) {
        Messages.lang = file;
    }

    public static FileConfiguration getFile() {
        if (Messages.lang != null) {
            return lang;
        }
        return null;
    }

    public String getPath() {
        return this.path;
    }

    public String getString() {
        return lang == null ? null : ChatColor.translateAlternateColorCodes('&', lang.getString(this.path));
    }

    public int getInt() {
        return lang == null ? null : Integer.valueOf(lang.getInt(this.path));
    }

    public List<String> getStringList() {
        return lang == null ? null : lang.getStringList(this.path).stream().map(line -> ChatColor.translateAlternateColorCodes((char) '&', line)).collect(Collectors.toList());
    }

    public void sendPlayers(List<String> string, CommandSender... players) {
        if (string == null) {
            sendMessage(players, getString());
        }
        else {
            String message = getString();
            for (int index = 0; index < string.size(); index++) {
                message = message.replace(string.get(index), string.get(++index));
            }
            sendMessage(players, message);
        }

    }

    public void sendTitle(List<String> string, Player... players) {
        if (string == null) {
            sendTitle2(ChatColor.translateAlternateColorCodes('&', lang.getString(getPath() + ".title")), ChatColor.translateAlternateColorCodes('&', lang.getString(getPath() + ".subtitle")), players);
        }
        else {
            String message = ChatColor.translateAlternateColorCodes('&', lang.getString(getPath() + ".title"));
            String message2 = ChatColor.translateAlternateColorCodes('&', lang.getString(getPath() + ".subtitle"));
            for (int index = 0; index < string.size(); index++) {
                int index2 = index + 1;
                message = message.replace(string.get(index), string.get(index2));
                message2 = message2.replace(string.get(index), string.get(index2));
                index++;
            }
            sendTitle2(message, message2, players);
        }

    }

    private void sendTitle2(String title, String subtitle, Player... players) {
        for (Player player : players) {
            if (player != null)
                Utils.sendTitle(player, ChatColor.translateAlternateColorCodes('&', title),
                        ChatColor.translateAlternateColorCodes('&', subtitle), 10, 20, 20);
        }
    }

    public void sendActionBar(List<String> string, Player... players) {
        if (string == null) {
            sendActionBar2(getString(), players);
        }
        else {
            String message = getString();
            for (int index = 0; index < string.size(); index++) {
                message = message.replace(string.get(index), string.get(++index));
            }
            sendActionBar2(message, players);
        }

    }

    private void sendActionBar2(String message, Player... players) {
        for (Player player : players) {
            if (player != null)
                Utils.sendActionbar(player, message);
        }
    }

    private void sendMessage(CommandSender[] players, String message) {
        for (CommandSender player : players) {
            if (player == null) continue;
            player.sendMessage(message);
        }
    }

    public String toString() {
        return lang == null ? null : lang.getString(this.path);
    }
}

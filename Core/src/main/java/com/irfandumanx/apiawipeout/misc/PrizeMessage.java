package main.java.com.irfandumanx.apiawipeout.misc;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PrizeMessage {

    private final ApiaWipeout instance;

    public PrizeMessage(ApiaWipeout instance) {
        this.instance = instance;
    }

    public void sendPlayers(List<String> string, String pureMessage, CommandSender... players) {
        if (string == null) {
            sendMessage(players, pureMessage);
        }
        else {
            String message = pureMessage;
            for (int index = 0; index < string.size(); index++) {
                message = message.replace(string.get(index), string.get(++index));
            }
            sendMessage(players, message);
        }
    }

    public void sendTitle(List<String> string, String title, String subTitle, Player... players) {
        if (string == null) {
            sendTitle2(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subTitle), players);
        }
        else {
            String message = ChatColor.translateAlternateColorCodes('&', title);
            String message2 = ChatColor.translateAlternateColorCodes('&', subTitle);
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
                    ChatColor.translateAlternateColorCodes('&', subtitle), 10, 70, 20);
        }
    }

    public void sendActionBar(List<String> string, String pureMessage, Player... players) {
        if (string == null) {
            sendActionBar2(pureMessage, players);
        }
        else {
            String message = pureMessage;
            for (int index = 0; index < string.size(); index++) {
                message = message.replace(string.get(index), string.get(++index));
            }
            sendActionBar2(message, players);
        }

    }

    private void sendActionBar2(String message, Player... players) {
        for (Player player : players) {
            if (player != null)
                Utils.sendActionbar(player, ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    private void sendMessage(CommandSender[] players, String message) {
        for (CommandSender player : players) {
            if (player == null) continue;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}

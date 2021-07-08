package main.java.com.irfandumanx.apiawipeout.managers;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PlayerManager {
    private final ApiaWipeout instance;

    private Map<Long, Player> finishTime;
    private Map<Player, Boolean> awardedPlayers;
    private List<Player> players;

    public PlayerManager(ApiaWipeout instance) {
        this.instance = instance;
        finishTime = new TreeMap<>();
        awardedPlayers = new HashMap<>();
    }

    public List<Player> getTopPlayers() {
        players = new LinkedList<>();
        finishTime.forEach((k, v) -> players.add(v));
        return players;
    }

    public Long getPlayerFinishTime(Player player) {
        Long playerFinishTime = null;
        for (Map.Entry<Long, Player> entry : finishTime.entrySet()) {
            if (player.getUniqueId().equals(entry.getValue().getUniqueId()))
                playerFinishTime = entry.getKey();
        }
        return playerFinishTime;
    }

    public Integer getPlayerRank(Player player) {
        int i = 0;
        for (Map.Entry<Long, Player> entry : finishTime.entrySet()) {
            i++;
            if (player.getUniqueId().equals(entry.getValue().getUniqueId()))
                return i;
        }
        return null;
    }

    public String getPlayerFinishTimeWithFormat(Player player) {
        if (player == null)
            return "00:00:00";
        Long playerFinishTime = getPlayerFinishTime(player);
        if (playerFinishTime != null)
            return DateTimeFormatter.ofPattern("HH:mm:ss").format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(playerFinishTime), ZoneOffset.UTC));
        return null;
    }

    public void givePrizePlayer(Player player, String rank) {
        if (!instance.getFileManager().getConfigC().isSet("prizes." + rank) || awardedPlayers.getOrDefault(player, false)) rank = "default";
        String rankNumber = instance.getPlayerManager().getPlayerRank(player) + "";
        for (String prize : instance.getFileManager().getConfigC().getStringList("prizes." + rank)) {
            String[] prizeSplit = prize.split(";");
            if (prizeSplit[0].equalsIgnoreCase("TITLE")) {
                if (prizeSplit.length == 3)
                    instance.getPrizeMessage().sendTitle(Arrays.asList("%player%", player.getName(), "%rank%", rankNumber), prizeSplit[1], prizeSplit[2], player);
                else if (prizeSplit.length == 2)
                    instance.getPrizeMessage().sendTitle(Arrays.asList("%player%", player.getName(), "%rank%", rankNumber), prizeSplit[1], "", player);
            }
            if (prizeSplit[0].equalsIgnoreCase("ACTIONBAR"))
                instance.getPrizeMessage().sendActionBar(Arrays.asList("%player%", player.getName(), "%rank%", rankNumber), prizeSplit[1], player);

            if (prizeSplit[0].equalsIgnoreCase("BROADCAST") && !awardedPlayers.getOrDefault(player, false))
                instance.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', prizeSplit[1]).replace("%player%", player.getName()).replace("%rank%", rankNumber));

            if (prizeSplit[0].equalsIgnoreCase("MESSAGE") && !awardedPlayers.getOrDefault(player, false))
                instance.getPrizeMessage().sendPlayers(Arrays.asList("%player%", player.getName(), "%rank%", rankNumber), prizeSplit[1], player);

            if (prizeSplit[0].equalsIgnoreCase("COMMAND"))
                instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), prizeSplit[1].replace("%player%", player.getName()));
        }
        awardedPlayers.put(player, true);
    }

    public Map<Long, Player> getFinishTime() {
        return finishTime;
    }

    public Map<Player, Boolean> getAwardedPlayers() {
        return awardedPlayers;
    }
}

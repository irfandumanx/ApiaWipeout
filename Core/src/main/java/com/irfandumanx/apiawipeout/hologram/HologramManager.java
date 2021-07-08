package main.java.com.irfandumanx.apiawipeout.hologram;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HologramManager {
    private final ApiaWipeout instance;
    private Location hologramLocation;
    private Map<Player, Hologram> holograms;
    private Map<Player, Integer> currentPage;
    private int limit;
    private int page;

    public HologramManager(ApiaWipeout instance) {
        this.instance = instance;
        holograms = new HashMap<>();
        currentPage = new HashMap<>();
        limit = Messages.WIPEOUT_HOLOGRAM_PLAYER_LIMIT.getInt();
        page = Messages.WIPEOUT_HOLOGRAM_PAGE.getInt();
    }

    public String getFormattedPage(int page) {
        String[] pageFormat = Messages.WIPEOUT_PAGE_FORMAT.getString().trim().split(" ");
        page++;

        String returnText;
        if (Utils.isLegacy()) {
            if (page >= 12) returnText = "    " + pageFormat[0] + " ";
            else if (page >= 8) returnText = "   " + pageFormat[0] + " ";
            else returnText = "  " + pageFormat[0] + " ";
        } else returnText = pageFormat[0] + " ";

        if (this.page <= 5) {
            for (int i = 1; i <= this.page; i++) {
                returnText += (i == page ? Messages.WIPEOUT_ON_PAGE_COLOR.getString() : Messages.WIPEOUT_NOT_ON_PAGE_COLOR.getString()) + i + (i == this.page ? "" : " ");
            }
        } else if (page < 3) {
            for (int i = 1; i <= 5; i++) {
                returnText += (i == page ? Messages.WIPEOUT_ON_PAGE_COLOR.getString() : Messages.WIPEOUT_NOT_ON_PAGE_COLOR.getString()) + i + (i == 5 ? "" : " ");
            }
        } else {
            if (this.page - page < 3) {
                for (int i = this.page - 4; i <= this.page; i++) {
                    returnText += (i == page ? Messages.WIPEOUT_ON_PAGE_COLOR.getString() : Messages.WIPEOUT_NOT_ON_PAGE_COLOR.getString()) + i + (i == this.page ? "" : " ");
                }
            } else {
                for (int i = page - 2; i <= page + 2; i++) {
                    returnText += (i == page ? Messages.WIPEOUT_ON_PAGE_COLOR.getString() : Messages.WIPEOUT_NOT_ON_PAGE_COLOR.getString()) + i + (i == page + 2 ? "" : " ");
                }
            }
        }
        return returnText + " " + pageFormat[1];
    }

    public void createHologram(Player player) {
        if (hologramLocation == null) setHologramLocation(player.getLocation());
        if (!holograms.containsKey(player)) currentPage.put(player, 0);
        int i = currentPage.get(player) * limit;
        Hologram hologram = HologramsAPI.createHologram(instance, hologramLocation);
        hologram.getVisibilityManager().setVisibleByDefault(false);
        hologram.getVisibilityManager().showTo(player);
        holograms.put(player, hologram);
        while (i < (currentPage.get(player) * limit) + limit) {
            Player player1 = i < instance.getPlayerManager().getTopPlayers().size() ? instance.getPlayerManager().getTopPlayers().get(i) : null;
            String playerName = i < instance.getPlayerManager().getTopPlayers().size() ? instance.getPlayerManager().getTopPlayers().get(i).getName() : Messages.WAITPLAYER.getString();
            if (!playerName.isEmpty())
                hologram.appendTextLine(Messages.WIPEOUT_HOLOGRAM_FORMAT.getString().replace("%rank%", i + 1 + "").replace("%player%", playerName).replace("%player_time%", instance.getPlayerManager().getPlayerFinishTimeWithFormat(player1)));
            i++;
        }
        hologram.appendTextLine("");
        hologram.appendTextLine(Messages.WIPEOUT_HOLOGRAM_CHANGE_MESSAGE.getString()).setTouchHandler(player2 -> {
        });
        hologram.appendTextLine(getFormattedPage(currentPage.get(player)));
    }

    public void updateHologram(Player player, int page) {
        Hologram hologram = holograms.get(player);
        if (hologram == null) return;
        hologram.clearLines();
        currentPage.put(player, page);
        if (currentPage.get(player) >= this.page) currentPage.put(player, 0);
        else if (currentPage.get(player) < 0) currentPage.put(player, this.page - 1);
        int i = currentPage.get(player) * limit;
        while (i < (currentPage.get(player) * limit) + limit) {
            Player player1 = i < instance.getPlayerManager().getTopPlayers().size() ? instance.getPlayerManager().getTopPlayers().get(i) : null;
            String playerName = i < instance.getPlayerManager().getTopPlayers().size() ? instance.getPlayerManager().getTopPlayers().get(i).getName() : Messages.WAITPLAYER.getString();
            if (!playerName.isEmpty())
                hologram.appendTextLine(Messages.WIPEOUT_HOLOGRAM_FORMAT.getString().replace("%rank%", i + 1 + "").replace("%player%", playerName).replace("%player_time%", instance.getPlayerManager().getPlayerFinishTimeWithFormat(player1)));
            i++;
        }
        hologram.appendTextLine("");
        hologram.appendTextLine(Messages.WIPEOUT_HOLOGRAM_CHANGE_MESSAGE.getString()).setTouchHandler(player2 -> {
        });
        hologram.appendTextLine(getFormattedPage(currentPage.get(player)));
    }

    public void updateHologram() {
        for (Map.Entry<Player, Hologram> entry : holograms.entrySet()) {
            int i = (currentPage.get(entry.getKey())) * limit;
            entry.getValue().clearLines();
            while (i < ((currentPage.get(entry.getKey())) * limit) + limit) {
                Player player1 = i < instance.getPlayerManager().getTopPlayers().size() ? instance.getPlayerManager().getTopPlayers().get(i) : null;
                String playerName = i < instance.getPlayerManager().getTopPlayers().size() ? instance.getPlayerManager().getTopPlayers().get(i).getName() : Messages.WAITPLAYER.getString();
                if (!playerName.isEmpty())
                    entry.getValue().appendTextLine(Messages.WIPEOUT_HOLOGRAM_FORMAT.getString().replace("%rank%", i + 1 + "").replace("%player%", playerName).replace("%player_time%", instance.getPlayerManager().getPlayerFinishTimeWithFormat(player1)));
                i++;
            }
            entry.getValue().appendTextLine("");
            entry.getValue().appendTextLine(Messages.WIPEOUT_HOLOGRAM_CHANGE_MESSAGE.getString()).setTouchHandler(player2 -> {
            });
            entry.getValue().appendTextLine(getFormattedPage(currentPage.get(entry.getKey())));
        }
    }

    public void delete(Player player) {
        Hologram hologram = holograms.get(player);
        if(hologram == null) return;
        holograms.remove(player);
        hologram.delete();
    }

    public void deleteHologram(Player player) {
        boolean isRemove = false;
        for (Map.Entry<Player, Hologram> entry : new HashMap<>(holograms).entrySet()) {
            if (player.getLocation().distance(entry.getValue().getLocation()) <= 4) {
                entry.getValue().delete();
                holograms.remove(entry.getKey());
                isRemove = true;
            }
        }
        if (isRemove) Messages.DELETEHOLOGRAM.sendPlayers(null, player);
        else Messages.DELETENEARHOLOGRAM.sendPlayers(null, player);
    }

    public Map<Player, Hologram> getHolograms() {
        return holograms;
    }

    public Location getHologramLocation() {
        return hologramLocation;
    }

    public void setHologramLocation(Location hologramLocation) {
        this.hologramLocation = hologramLocation;
    }

    public Map<Player, Integer> getCurrentPage() {
        return currentPage;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

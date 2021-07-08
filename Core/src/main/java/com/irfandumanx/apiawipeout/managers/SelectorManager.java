package main.java.com.irfandumanx.apiawipeout.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.misc.Pair;
import org.bukkit.Location;
import org.bukkit.event.block.Action;


public class SelectorManager {
    private Map<UUID, Pair<Location, Location>> multiPosOfPlayer;

    public SelectorManager(ApiaWipeout instance) {
        multiPosOfPlayer = new HashMap<>();
    }

    public Pair<Location,Location> setMultiPos(UUID uuid, Action type, Location newPos) {
        Pair<Location, Location> pos = multiPosOfPlayer.getOrDefault(uuid, new Pair<>());
        if(type == Action.LEFT_CLICK_BLOCK) {
            pos.setFirst(newPos);
            multiPosOfPlayer.put(uuid, pos);
        }
        else if(type == Action.RIGHT_CLICK_BLOCK) {
            pos.setSecond(newPos);
            multiPosOfPlayer.put(uuid, pos);
        }
        return pos;
    }

    public Pair<Location, Location> getPosOfPlayer(UUID uuid){
        return multiPosOfPlayer.get(uuid);
    }

    public void clearPosOfPlayer(UUID uuid) {
        if(multiPosOfPlayer.containsKey(uuid)) multiPosOfPlayer.remove(uuid);
    }
}
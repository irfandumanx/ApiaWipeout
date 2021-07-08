package main.java.com.irfandumanx.apiawipeout.managers;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.region.CheckPoint;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheckPointManager {
    private final ApiaWipeout instance;
    private Location location;
    private List<CheckPoint> checkPoints = new ArrayList<>();

    public CheckPointManager(ApiaWipeout instance) {
        this.instance = instance;
    }

    public void loadCheckPoint(int id) {
        FileConfiguration checkPointC = instance.getFileManager().getCheckPointC();
        CheckPoint checkPoint = new CheckPoint(checkPointC.getString(id + ".name"), id, location);
        if (checkPointC.isSet(id + ".location"))
            checkPoint.setLocation(instance.getFileManager().getLocationFromString(checkPointC.getString(id + ".location")));

        checkPoints.add(checkPoint);
    }

    public void loadCheckPoints() {
        for (int i = 0; i < 50; i++) {
            if (instance.getFileManager().getCheckPointC().isSet("" + i)) {
                loadCheckPoint(i);
            }
        }
    }

    public void createCheckPoint(String name, Location location) {
        FileConfiguration checkPointC = instance.getFileManager().getCheckPointC();
        Integer id = getEmptyID();
        if (id != null) {
            this.location = location;
            checkPointC.set(id + ".name", name);
            checkPointC.options().copyDefaults(true);

            try {
                checkPointC.save(instance.getFileManager().getCheckPoint());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            loadCheckPoint(id);
        }
    }

    private Integer getEmptyID() {
        for (int i = 0; i < 50; i++) {
            if (!(instance.getFileManager().getCheckPointC().isSet("" + i))) {
                return i;
            }
        }
        return null;
    }

    public void saveCheckPoint(CheckPoint checkPoint) {
        FileConfiguration checkPointC = instance.getFileManager().getCheckPointC();
        if (checkPoint != null) {
            checkPointC.set("" + checkPoint.getId(), null);
            checkPointC.set(checkPoint.getId() + ".name", checkPoint.getName());
            if (checkPoint.getLocation() != null)
                checkPointC.set(checkPoint.getId() + ".location", instance.getFileManager().getStringFromLocation(checkPoint.getLocation()));

        }
        try {
            checkPointC.save(instance.getFileManager().getCheckPoint());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void deleteCheckPoint(CheckPoint checkPoint) {
        if (checkPoint != null) {
            checkPoints.remove(checkPoint);
            instance.getFileManager().getCheckPointC().set("" + checkPoint.getId(), null);
            try {
                instance.getFileManager().getCheckPointC().save(instance.getFileManager().getCheckPoint());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public CheckPoint getCheckPointWithName(String name) {
        for (CheckPoint checkPoint : checkPoints) {
            if (checkPoint.getName().equals(name)) {
                return checkPoint;
            }
        }
        return null;
    }

    public CheckPoint getCheckPointWithLocation(Location location) {
        for (CheckPoint checkPoint : checkPoints) {
            if (checkPoint.getLocation().equals(location)) return checkPoint;
        }
        return null;
    }

    public List<CheckPoint> getCheckPoints() {
        return checkPoints;
    }
}

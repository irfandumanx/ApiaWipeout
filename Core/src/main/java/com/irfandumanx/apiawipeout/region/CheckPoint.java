package main.java.com.irfandumanx.apiawipeout.region;
import org.bukkit.Location;

public class CheckPoint {
    private String name;
    private Location location;
    private int id;

    public CheckPoint(String name, int id, Location location) {
        this.name = name;
        this.id = id;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }
}

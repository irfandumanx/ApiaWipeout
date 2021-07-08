package main.java.com.irfandumanx.apiawipeout.dates;

public class WipeoutDate {
    private String name, day, type;
    private int hour, minute;

    public WipeoutDate(String name, String day, int hour, int minute, String type) {
        this.name = name;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.type = type;
    }

    public String getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getType() {
        return type;
    }
}

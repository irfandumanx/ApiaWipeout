package main.java.com.irfandumanx.apiawipeout.managers;

import com.google.common.base.Charsets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.dates.DateManager;
import main.java.com.irfandumanx.apiawipeout.misc.Item;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class FileManager {

    private final ApiaWipeout instance;
    public File messages, config, region, game, checkPoint;
    public FileConfiguration messagesC, configC, regionC, gameC, checkPointC;

    public FileManager(ApiaWipeout instance) {
        this.instance = instance;
        initialize();
        checkUpgradedPath();
    }

    public void initialize() {
        messages = new File(instance.getDataFolder(), "messages.yml");
        messages.getParentFile().mkdir();

        if (!messages.exists()) {
            try {
                messages.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            instance.saveResource("messages.yml", true);
        }
        messagesC = YamlConfiguration.loadConfiguration(messages);
        Messages.setFile(messagesC);

        config = new File(instance.getDataFolder(), "config.yml");
        if (!config.exists()) {
            try {
                config.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            instance.saveResource("config.yml", true);
        }
        configC = YamlConfiguration.loadConfiguration(config);
        Settings.setFile(configC);

        region = new File(instance.getDataFolder(), "region.yml");
        regionC = YamlConfiguration.loadConfiguration(region);

        game = new File(instance.getDataFolder(), "game.yml");
        gameC = YamlConfiguration.loadConfiguration(game);

        checkPoint = new File(instance.getDataFolder(), "checkpoints.yml");
        checkPointC = YamlConfiguration.loadConfiguration(checkPoint);

        if (Settings.WIPEOUTDEBUGMODE.getBoolean()) {
            instance.getLogger().info("Data folder : " + instance.getDataFolder());
            instance.getLogger().info("Checkpoint folder : " + checkPoint.exists());
            instance.getLogger().info("Game folder : " + game.exists());
            instance.getLogger().info("Region folder : " + region.exists());
            instance.getLogger().info("Config folder : " + config.exists());
            instance.getLogger().info("Message folder : " + messages.exists());
        }
    }

    public void reloadLang() {
        FileConfiguration newMessage = YamlConfiguration.loadConfiguration(messages);

        final InputStream defLangStream = instance.getResource("messages.yml");
        if (defLangStream == null) {
            return;
        }
        newMessage.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defLangStream, Charsets.UTF_8)));
    }

    public void reloadConfig() {
        FileConfiguration newConfig = YamlConfiguration.loadConfiguration(config);

        final InputStream defConfigStream = instance.getResource("config.yml");
        if (defConfigStream == null) {
            return;
        }
        newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    public void reloadSystem() {
        initialize();
        reloadLang();
        reloadConfig();
        Messages.setFile(instance.getFileManager().messagesC);
        Settings.setFile(instance.getFileManager().configC);
        new DateManager(instance);
        Arrays.stream(Item.SelectorType.values()).forEach(value -> value.typeName = Settings.getLang().getString("type_" + value.toString().toLowerCase()).replace("&", "§"));
        instance.getItemManager().setItem();
        if (instance.getHologramManager() != null && instance.getHologramManager().getHolograms() != null) {
            instance.getHologramManager().setLimit(Messages.WIPEOUT_HOLOGRAM_PLAYER_LIMIT.getInt());
            instance.getHologramManager().setPage(Messages.WIPEOUT_HOLOGRAM_PAGE.getInt());
            instance.getHologramManager().updateHologram();
        }
    }

    public void checkUpgradedPath() {
        YamlConfiguration configResource = YamlConfiguration.loadConfiguration(new InputStreamReader(instance.getResource("config.yml")));
        YamlConfiguration messageResource = YamlConfiguration.loadConfiguration(new InputStreamReader(instance.getResource("messages.yml")));
        for (String path : configC.getConfigurationSection("").getKeys(true)) {
            if (path.startsWith("wipeout_dates.") || path.startsWith("prizes.") || path.startsWith("commands."))
                continue;
            if (!configResource.isSet(path)) configC.set(path, null);
        }
        for (String path : messagesC.getConfigurationSection("").getKeys(true)) {
            if (!messageResource.isSet(path)) messagesC.set(path, null);
        }
        for (String path : configResource.getConfigurationSection("").getKeys(true)) {
            if (path.startsWith("wipeout_dates.") || path.startsWith("prizes.") || path.startsWith("commands."))
                continue;
            if (!configC.isSet(path)) configC.set(path, configResource.get(path));
        }
        for (String path : messageResource.getConfigurationSection("").getKeys(true)) {
            if (!messagesC.isSet(path)) messagesC.set(path, messageResource.get(path));
        }
        try {
            configC.save(config);
            messagesC.save(messages);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public String getStringFromLocation(final Location location) {
        String loc = new String();
        loc = location == null ? "" : location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ();
        loc = location.getYaw() != 0.0 ? loc + ":yaw=" + location.getYaw() : loc;
        loc = location.getPitch() != 0.0 ? loc + ":pitch=" + location.getPitch() : loc;
        return loc;
    }

    public Location getLocationFromString(final String location) {
        if (location == null || location.trim() == "") return null;
        final String[] split = location.split(":");
        final World world = instance.getServer().getWorld(split[0]);
        if (world == null) return null;
        final double x = Double.parseDouble(split[1]);
        final double y = Double.parseDouble(split[2]);
        final double z = Double.parseDouble(split[3]);
        if (split.length == 4) {
            return new Location(world, x, y, z);
        } else if (split.length > 4) {
            final float yaw = (float) (split[4].contains("yaw") ? Float.parseFloat(split[4].replace("yaw=", "")) : 0.0);
            final float pitch = (float) (split.length > 5 && split[5] != null && split[5].contains("pitch") ? Float.parseFloat(split[5].replace("pitch=", "")) : split[4].contains("pitch") ? Float.parseFloat(split[5].replace("pitch=", "")) : 0.0);
            return new Location(world, x, y, z, yaw, pitch);
        }
        return null;
    }

    public File getRegion() {
        return region;
    }

    public FileConfiguration getRegionC() {
        return regionC;
    }

    public File getGameFile() {
        return game;
    }

    public FileConfiguration getGameCFile() {
        return gameC;
    }

    public File getCheckPoint() {
        return checkPoint;
    }

    public FileConfiguration getCheckPointC() {
        return checkPointC;
    }

    public File getConfig() {
        return config;
    }

    public FileConfiguration getConfigC() {
        return configC;
    }
}


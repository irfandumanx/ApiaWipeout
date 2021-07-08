package main.java.com.irfandumanx.apiawipeout;

import main.java.com.irfandumanx.apiawipeout.commands.ApiaWipeoutCommands;
import main.java.com.irfandumanx.apiawipeout.commands.CommandCompleter;
import main.java.com.irfandumanx.apiawipeout.commands.SetCommand;
import main.java.com.irfandumanx.apiawipeout.dates.DateManager;
import main.java.com.irfandumanx.apiawipeout.dates.DateTimer;
import main.java.com.irfandumanx.apiawipeout.hologram.HologramClickListener;
import main.java.com.irfandumanx.apiawipeout.hologram.HologramManager;
import main.java.com.irfandumanx.apiawipeout.listeners.*;
import main.java.com.irfandumanx.apiawipeout.managers.*;
import main.java.com.irfandumanx.apiawipeout.misc.Item;
import main.java.com.irfandumanx.apiawipeout.misc.PrizeMessage;
import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import main.java.com.irfandumanx.apiawipeout.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class ApiaWipeout extends JavaPlugin {
    private static ApiaWipeout instance;
    private FileManager fileManager;
    private Item item;
    private RegionManager regionManager;
    private CheckPointManager checkPointManager;
    private PlayerManager playerManager;
    private HologramManager hologramManager;
    private PrizeMessage prizeMessage;
    private CommandManager commandManager;
    private SelectorManager selectorManager;
    private PlayerFallListener playerFallListener;

    public void onEnable() {
        instance = this;
        fileManager = new FileManager(instance);
        new DateManager(instance);
        new DateTimer(instance);
        prizeMessage = new PrizeMessage(instance);
        commandManager = new CommandManager(instance);
        regionManager = new RegionManager(instance, ((SetCommand) commandManager.getSetCommand()).getSetCommandInstance());
        checkPointManager = new CheckPointManager(instance);
        playerManager = new PlayerManager(instance);
        selectorManager = new SelectorManager(instance);
        if (getServer().getPluginManager().isPluginEnabled("HolographicDisplays"))
            hologramManager = new HologramManager(instance);

        Listener blockFallListener = null;
        Listener itemClickListener = null;

        try {
            switch (Utils.getNmsVersion()) {
                case "v1_8_R3":
                case "v1_9_R1":
                case "v1_9_R2":
                case "v1_10_R1":
                case "v1_11_R1":
                case "v1_12_R1":
                    item = (Item) Class.forName("main.java.com.irfandumanx.apiawipeout.v1_12_R1.misc.Item").getConstructor(ApiaWipeout.class).newInstance(instance);
                    blockFallListener = (Listener) Class.forName("main.java.com.irfandumanx.apiawipeout.v1_12_R1.listeners.BlockFallListener").getConstructor(ApiaWipeout.class).newInstance(instance);
                    itemClickListener = (Listener) Class.forName("main.java.com.irfandumanx.apiawipeout.v1_12_R1.listeners.ItemClickListener").getConstructor(ApiaWipeout.class).newInstance(instance);
                    break;
                default:
                    item = (Item) Class.forName("main.java.com.irfandumanx.apiawipeout.v1_16_R3.misc.Item").getConstructor(ApiaWipeout.class).newInstance(instance);
                    blockFallListener = (Listener) Class.forName("main.java.com.irfandumanx.apiawipeout.v1_16_R3.listeners.BlockFallListener").getConstructor(ApiaWipeout.class).newInstance(instance);
                    itemClickListener = (Listener) Class.forName("main.java.com.irfandumanx.apiawipeout.v1_16_R3.listeners.ItemClickListener").getConstructor(ApiaWipeout.class).newInstance(instance);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }


        getServer().getScheduler().runTaskAsynchronously(instance, () -> {
            discordHook();
            Game.loadFile();
            checkPointManager.loadCheckPoints();
            regionManager.loadRegions();
        });

        Game.setGameState(Game.WAITING);

        playerFallListener = new PlayerFallListener(instance);
        getServer().getPluginManager().registerEvents(itemClickListener, this);
        getServer().getPluginManager().registerEvents(blockFallListener, this);
        getServer().getPluginManager().registerEvents(playerFallListener, this);
        getServer().getPluginManager().registerEvents(new PlayerThrowingListener(instance), this);
        getServer().getPluginManager().registerEvents(new PlayerFinishListener(instance), this);
        getServer().getPluginManager().registerEvents(new PistonListener(instance), this);
        getServer().getPluginManager().registerEvents(new JoinQuitListener(instance), this);
        getServer().getPluginManager().registerEvents(new HologramClickListener(instance), this);
        getServer().getPluginManager().registerEvents(new ArmorStandListener(instance), this);

        getCommand("wipeout").setExecutor(new ApiaWipeoutCommands(instance));
        getCommand("wipeout").setTabCompleter(new CommandCompleter(instance));

        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[ApiaWipeout] ApiaWipeout v2.0 enabled. This plugin created by ApiaTeam");
    }

    public void onDisable() {
        regionManager.getRegions().values().forEach(Region::stop);
        checkPointManager.getCheckPoints().forEach(checkPoint -> checkPointManager.saveCheckPoint(checkPoint));
        Game.saveFile();
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ApiaWipeout] ApiaWipeout v2.0 disabled. This plugin created by ApiaTeam");
    }

    public void discordHook() {
        DiscordManager webhook = new DiscordManager();
        String buyer = "irfandumanx#4451";
        boolean blacklist = blacklist(buyer);
        String IP = getIP();
        if (IP != null) {
            DiscordManager.EmbedObject embedObject = new DiscordManager.EmbedObject();
            webhook.setUsername("ApiaTeam Bot #2");
            webhook.setTts(true);
            try {
                if (!blacklist) embedObject.setColor(Color.RED);
                else embedObject.setColor(Color.CYAN);
                webhook.addEmbed(embedObject
                        .addField("Name", buyer, true)
                        .addField("Blacklist", !blacklist + "", false)
                        .addField("Eklenti", getDescription().getName(), false)
                        .addField("Sürüm", getDescription().getVersion(), false)
                        .addField("IP", getIP(), false));
                webhook.execute();
            } catch (IOException exception) {
            }
        }

        if (!blacklist) {
            getLogger().info("§c[ApiaWipeout] Lisans problemi yaşıyorsunuz. İnternet bağlantınız kopuk değilse bizimle iletişime geçiniz. Discord grubumuz: discord.gg/g74rBT4hZN");
            Bukkit.getScheduler().cancelTasks(instance);
            getServer().getPluginManager().disablePlugin(instance);
        }
    }

    public String getIP() {
        try {
            URLConnection localURLConnection = new URL("https://checkip.amazonaws.com").openConnection();
            localURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
            localURLConnection.connect();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localURLConnection.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder localStringBuilder = new StringBuilder();
            String str1;
            while((str1 = localBufferedReader.readLine()) != null) {
                localStringBuilder.append(str1);
            }
            return localStringBuilder.toString();
        }catch (Exception exception) {
            return null;
        }
    }

    private boolean blacklist(String name) {
        try {
            URLConnection localURLConnection = new URL("").openConnection();
            localURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
            localURLConnection.connect();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localURLConnection.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder localStringBuilder = new StringBuilder();
            String str1 = "";
            while((str1 = localBufferedReader.readLine()) != null) {
                localStringBuilder.append(str1);
            }
            String str2 = localStringBuilder.toString();
            if(str2.contains(name)) {
                return false;
            }
        } catch(IOException e) {
            return false;
        }
        return true;
    }

    public static ApiaWipeout getInstance() {
        return instance;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public Item getItemManager() {
        return item;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public CheckPointManager getCheckPointManager() {
        return checkPointManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public PrizeMessage getPrizeMessage() {
        return prizeMessage;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public SelectorManager getSelectorManager() {
        return selectorManager;
    }

    public PlayerFallListener getPlayerFallListener() {
        return playerFallListener;
    }
}

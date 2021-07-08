package main.java.com.irfandumanx.apiawipeout.commands;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import main.java.com.irfandumanx.apiawipeout.Game;
import main.java.com.irfandumanx.apiawipeout.misc.Messages;
import main.java.com.irfandumanx.apiawipeout.misc.Pair;
import main.java.com.irfandumanx.apiawipeout.misc.Settings;
import main.java.com.irfandumanx.apiawipeout.misc.Utils;
import main.java.com.irfandumanx.apiawipeout.wipeoutevents.AEscalatorRamp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class SetCommand implements ICommand {
    public final String mainCommand = "set";
    public final Set<String> alternativeCommands = Sets.newHashSet(Settings.WIPEOUT_ARGUMENT_SET.getStringList());
    private SetCommand setCommandInstance;
    private int throwPower;
    private Location checkPointLocation;
    private int numberOfBlock;
    private AEscalatorRamp.RampType rampType;
    private int rotrodRadius;
    private int rotrodSize;
    private ItemStack rotrodItemStack;
    private String rotrodType;
    private final ApiaWipeout instance;
    private Pair<Location, Location> locationPair;

    public SetCommand(ApiaWipeout instance) {
        setCommandInstance = this;
        this.instance = instance;
    }

    @Override
    public boolean applyConditions(CommandSender sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            instance.getLogger().info(Messages.ONLYPLAYER.getString());
            return false;
        }
        if (!sender.hasPermission("wipeout.admin")) {
            Messages.NOPERM.sendPlayers(null, sender);
            return false;
        }
        locationPair = instance.getSelectorManager().getPosOfPlayer(((Player) sender).getUniqueId());
        if (locationPair == null || locationPair.getFirst() == null || locationPair.getSecond() == null) {
            Messages.SETLOCATIONFORCONTUNUE.sendPlayers(null, sender);
            return false;
        }
        if (args.length == 1) {
            Messages.TYPES.sendPlayers(null, sender);
            return false;
        }

        Set<String> availableRegions = Sets.newHashSet("RAMP", "WATER", "STARTLINE", "DOWNUP", "FALL", "FLOW", "ONETAP", "THROW", "FINISH", "ROTROD");
        boolean isAvailable = false;
        for (String availableRegion : availableRegions) {
            if (args[1].equalsIgnoreCase(availableRegion)) {
                isAvailable = true;
                break;
            }
        }
        if (!isAvailable) {
            Messages.TYPES.sendPlayers(null, sender);
            return false;
        }

        if (!args[1].equalsIgnoreCase("WATER") && instance.getRegionManager().getRegionLoop(instance.getRegionManager().setRegionLoopMinMax(locationPair.getFirst(), locationPair.getSecond())) != null) {
            Messages.THISISALREADYREGION.sendPlayers(null, sender);
            return false;
        }
        return true;
    }

    @Override
    public void apply(CommandSender sender, String[] args) {
        if (!args[1].equalsIgnoreCase("THROW") && !args[1].equalsIgnoreCase("WATER") && !args[1].equalsIgnoreCase("RAMP") && !args[1].equalsIgnoreCase("ROTROD")) {
            instance.getRegionManager().createRegion(args[1].toUpperCase(), ((Player) sender).getUniqueId());
            Messages.WIPEOUTREGIONSET.sendPlayers(Arrays.asList("%region_type%", args[1].toUpperCase()), sender);
        } else if (args[1].equalsIgnoreCase("THROW")) {
            if (args.length == 3) {
                throwPower = Integer.parseInt(args[2]);
                Messages.REGIONSETWITHPOWER.sendPlayers(Arrays.asList("%region_type%", args[1], "%region_power%", args[2]), sender);
                instance.getRegionManager().createRegion(args[1].toUpperCase(), ((Player) sender).getUniqueId());
            } else
                Messages.NOTHROWPOWER.sendPlayers(null, sender);
        } else if (args[1].equalsIgnoreCase("WATER")) {
            if (args.length == 3) {
                AtomicBoolean isFoundCheckpoint = new AtomicBoolean(false);
                instance.getCheckPointManager().getCheckPoints().forEach(checkPoint -> {
                    if (args[2].equals(checkPoint.getName())) {
                        checkPointLocation = checkPoint.getLocation();
                        isFoundCheckpoint.set(true);
                    }
                });
                if (!isFoundCheckpoint.get()) {
                    Messages.NOFOUNDCHECKPOINT.sendPlayers(Arrays.asList("%checkpoint_name%", args[2]), sender);
                    return;
                }
                instance.getRegionManager().createRegion(args[1].toUpperCase(), ((Player) sender).getUniqueId());
                Messages.WIPEOUTSETWITHCHECKPOINT.sendPlayers(Arrays.asList("%region_type%", args[1].toUpperCase(), "%checkpoint_name%", args[2]), sender);
                return;
            }
            if (Game.getBaseLocation() != null)
                checkPointLocation = Game.getBaseLocation();
            else {
                Messages.HAVETOSETBASELOC.sendPlayers(null, sender);
                return;
            }
            instance.getRegionManager().createRegion(args[1].toUpperCase(), ((Player) sender).getUniqueId());
            Messages.WIPEOUTREGIONSET.sendPlayers(Arrays.asList("%region_type%", args[1].toUpperCase()), sender);
        } else if (args[1].equalsIgnoreCase("RAMP")) {
            if (args.length >= 3 && Arrays.stream(AEscalatorRamp.RampType.values()).anyMatch(type -> type.toString().equals(args[2].toUpperCase())))
                rampType = AEscalatorRamp.RampType.valueOf(args[2].toUpperCase());
            else {
                Messages.REGIONUNSETDIRECTION.sendPlayers(null, sender);
                return;
            }

            if (args.length == 4) numberOfBlock = Utils.getInt(args[3], 0);
            else {
                Messages.REGIONUNSETBLOCKNUMBER.sendPlayers(null, sender);
                return;
            }

            instance.getRegionManager().createRegion(args[1].toUpperCase(), ((Player) sender).getUniqueId());
            Messages.REGIONSETWITHBLOCKNUMBERANDDIRECTION.sendPlayers(Arrays.asList("%region_type%", args[1].toUpperCase(), "%region_block%", args[2], "%region_direction%", args[3].toUpperCase()), sender);
        } else if (args[1].equalsIgnoreCase("ROTROD")) {
            if (args.length != 6) {
                Messages.WIPEOUT_MISSING_ARGUMENT.sendPlayers(null, sender);
                return;
            }

            String[] splitItemStack = args[2].split(";");
            rotrodItemStack = new ItemStack(Material.getMaterial(splitItemStack[0].toUpperCase()), 1, splitItemStack.length == 2 ? Short.parseShort(splitItemStack[1]) : 0);

            rotrodSize = Utils.getInt(args[3], 0);
            rotrodRadius = Utils.getInt(args[4], 0);
            rotrodType = args[5];

            instance.getRegionManager().createRegion(args[1].toUpperCase(), ((Player) sender).getUniqueId());
            Messages.WIPEOUT_REGION_SET_ROTROD.sendPlayers(Arrays.asList("%region_type%", args[1].toUpperCase(), "%region_item%", args[2], "%region_size%", args[3], "%region_radius%", args[4], "%region_direction%", args[5]), sender);
        }
    }

    @Override
    public String getMainCommand() {
        return mainCommand;
    }

    @Override
    public Set<String> getAlternativeCommands() {
        return alternativeCommands;
    }

    public SetCommand getSetCommandInstance() {
        return setCommandInstance;
    }

    public int getThrowPower() {
        return throwPower;
    }

    public void setThrowPower(int throwPower) {
        this.throwPower = throwPower;
    }

    public Location getCheckPointLocation() {
        return checkPointLocation;
    }

    public int getNumberOfBlock() {
        return numberOfBlock;
    }

    public AEscalatorRamp.RampType getRampType() {
        return rampType;
    }

    public void setRampType(AEscalatorRamp.RampType rampType) {
        this.rampType = rampType;
    }

    public int getRotrodRadius() {
        return rotrodRadius;
    }

    public int getRotrodSize() {
        return rotrodSize;
    }

    public ItemStack getRotrodItemStack() {
        return rotrodItemStack;
    }

    public String getRotrodType() {
        return rotrodType;
    }
}

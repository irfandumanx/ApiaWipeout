package main.java.com.irfandumanx.apiawipeout.misc;

import com.google.common.collect.Sets;
import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class Item {
    private final ApiaWipeout instance;
    protected ItemStack normalItemStack;
    protected ItemStack pistonItemStack;
    protected Set<ItemFlag> flags;

    public Item(ApiaWipeout instance) {
        this.instance = instance;
        flags = Sets.newHashSet(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        setItem();
    }

    public ItemStack getItem(SelectorType selectorType) {
        if (selectorType == SelectorType.NORMAL) return normalItemStack;
        else if (selectorType == SelectorType.PISTON) return pistonItemStack;
        return null;
    }

    public void addItem(Player player, SelectorType selectorType) {
        player.getInventory().addItem(getItem(selectorType));
    }

    public void removeItem(Player player, SelectorType selectorType) {
        player.getInventory().remove(getItem(selectorType));
    }

    public void setItem() {
        FileConfiguration config = instance.getFileManager().getConfigC();
        for (SelectorType selectorType : SelectorType.values()) {
            String path = "selector_" + selectorType.toString().toLowerCase();
            setItem(
                    Material.valueOf(config.getString(path + ".type")),
                    config.getInt(path + ".data"),
                    config.getString(path + ".name"),
                    config.getStringList(path + ".lore"),
                    config.getBoolean(path + ".glow"),
                    selectorType
            );
        }
    }

    public abstract void setItem(Material material, int data, String name, List<String> lore, boolean glow, SelectorType selectorType);

    public SelectorType getType(ItemStack itemStack) {
        for (SelectorType selectorType : SelectorType.values()) {
            if (getItem(selectorType).isSimilar(itemStack)) return selectorType;
        }
        return null;
    }

    public SelectorType getTypeNext(SelectorType selectorType) {
        SelectorType next = null;
        for (SelectorType selectorType1 : SelectorType.values()) {
            if (selectorType == selectorType1) {
                next = selectorType1;
                continue;
            }
            if (next != null) return selectorType1;
        }
        if (next == selectorType && next != null) return Arrays.asList(SelectorType.values()).get(0);
        return null;
    }

    public enum SelectorType {
        NORMAL(Settings.WIPEOUTTYPENORMAL.getString()),
        PISTON(Settings.WIPEOUTTYPEPISTON.getString());

        public String typeName;

        private SelectorType(String typeName) {
            this.typeName = typeName;
        }
    }

}
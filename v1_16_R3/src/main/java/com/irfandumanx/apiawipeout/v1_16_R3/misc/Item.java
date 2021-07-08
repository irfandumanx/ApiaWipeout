package main.java.com.irfandumanx.apiawipeout.v1_16_R3.misc;

import main.java.com.irfandumanx.apiawipeout.ApiaWipeout;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class Item extends main.java.com.irfandumanx.apiawipeout.misc.Item {
    public Item(ApiaWipeout instance) {
        super(instance);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setItem(Material material, int data, String name, List<String> lore, boolean glow, SelectorType selectorType) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (name != null) itemMeta.setDisplayName(name.replace("%type%", selectorType.typeName).replace("&", "§"));
        if (lore != null) itemMeta.setLore(lore.stream().map(line -> line.replace("%type%", selectorType.typeName).replace("&", "§")).collect(Collectors.toList()));
        itemMeta.setUnbreakable(true);
        if (glow) itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        flags.forEach(itemMeta::addItemFlags);

        itemStack.setItemMeta(itemMeta);
        if (selectorType == SelectorType.NORMAL) normalItemStack = itemStack;
        else if (selectorType == SelectorType.PISTON) pistonItemStack = itemStack;
    }
}
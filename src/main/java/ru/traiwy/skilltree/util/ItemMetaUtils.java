package ru.traiwy.skilltree.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemMetaUtils {
    public static void applyItemMeta(ItemStack item, String name, List<String> lore){
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}

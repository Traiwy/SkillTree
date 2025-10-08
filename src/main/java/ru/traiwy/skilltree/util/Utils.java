package ru.traiwy.skilltree.util;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class Utils {
    public static final int[] SLOTS_PANEL = {
        27, 28, 29, 30, 31, 32, 33, 34, 35,
        45, 46, 47, 48, 49, 50, 51, 52,53,
    };

    public static void fillPanelSlots(Inventory inventory, ItemStack panel) {
        for (int i = 0; i < SLOTS_PANEL.length; i++) {
            inventory.setItem(SLOTS_PANEL[i], panel);
        }
    }
}

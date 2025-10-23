package ru.traiwy.skilltree.util;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import ru.traiwy.skilltree.manager.ConfigManager;

public class YLevelUtils {

    public static boolean isValidYLevel(Block block, ConfigManager.Challenge challenge) {
        Object yLevelObj = challenge.getSettings().get("yLevel");
        if (yLevelObj instanceof Number yLevel) {
           return block.getY() <= yLevel.intValue();

        }
        return true;
    }
}

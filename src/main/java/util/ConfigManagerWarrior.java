package util;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigManagerWarrior {
    private final JavaPlugin plugin;
    private  File config;
    public ConfigManagerWarrior(JavaPlugin plugin){
        this.plugin = plugin;
    }
    public static void load(){
        config = new File(plugin.getDataFolder(), "achievementsWarrior");
    }


}

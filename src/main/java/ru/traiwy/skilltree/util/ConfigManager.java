package ru.traiwy.skilltree.util;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Slf4j
public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private final String configName;

    public ConfigManager(JavaPlugin plugin, String configName) {
        this.plugin = plugin;
        this.configName = configName;
    }

    public static void load(FileConfiguration config){
        final var mysql = config.getConfigurationSection("mysql");
        if (mysql == null) {
            throw new IllegalStateException("Error load my sqlSection");
        } else {
            loadMysql(mysql);
        }

    }

    private static void loadMysql(ConfigurationSection sqlSection){
        MySQL.HOST = sqlSection.getString("host");
        MySQL.PORT = sqlSection.getInt("port");
        MySQL.USER = sqlSection.getString("user");
        MySQL.USER = sqlSection.getString("password");
        MySQL.DATABASE = sqlSection.getString("database");
    }


    public static class MySQL{
        public static String HOST;
        public static int PORT;
        public static String USER;
        public static String PASSWORD;
        public static String DATABASE;
    }

}

package ru.traiwy.skilltree.manager;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.enums.Skill;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;


@Slf4j
public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration mainConfig;
    private FileConfiguration bdConfig;

    private final List<GUI.TASK> warriorTask = new ArrayList<>();
    private final List<GUI.TASK> alchemistTask = new ArrayList<>();
    private final List<GUI.TASK> farmerTask = new ArrayList<>();

    public ConfigManager(JavaPlugin plugin, FileConfiguration file) {
        this.plugin = plugin;
    }

    public void load(FileConfiguration config){
        try{
            loadMainConfig();
            loadDataBaseConfig();
            parseGui(mainConfig);
        } catch (Exception e) {
             throw new IllegalStateException("Ошибка загрузки конфигурации", e);
        }
    }
    public void loadMainConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.mainConfig = plugin.getConfig();

        if (mainConfig == null) {
            throw new IllegalStateException("Основной config.yml не загружен!");
        }
    }

    public void loadDataBaseConfig() {
        File dbFile = new File(plugin.getDataFolder(), "bd.yml");

        if (!dbFile.exists()) {
            plugin.saveResource("bd.yml", false);
        }

        this.bdConfig = YamlConfiguration.loadConfiguration(dbFile);

        ConfigurationSection mysqlSection = bdConfig.getConfigurationSection("mysql");
        if (mysqlSection == null) {
            throw new IllegalStateException("Секция 'mysql' не найдена в bd.yml!");
        }

        loadMysql(mysqlSection);

    }


    public void parseGui(FileConfiguration config) {
        final ConfigurationSection section = config.getConfigurationSection("gui.task");
        isSectionIsNull(section);

        final ConfigurationSection warriorSection = section.getConfigurationSection("warrior");
        isSectionIsNull(warriorSection);
        parseTasks(warriorSection, warriorTask);

        for (GUI.TASK a : warriorTask) {
            getLogger().info("[Warrior] " + a.getName() + " — " + a.getTask());
        }


        final ConfigurationSection alchemistSection = section.getConfigurationSection("alchemist");
        isSectionIsNull(alchemistSection);
        parseTasks(alchemistSection, alchemistTask);
        for (GUI.TASK a : alchemistTask) {
            getLogger().info("[Alchemist] " + a.getName() + " — " + a.getTask());
        }


        final ConfigurationSection farmerSection = section.getConfigurationSection("farmer");
        isSectionIsNull(farmerSection);
        parseTasks(farmerSection, farmerTask);
        for (GUI.TASK a : farmerTask) {
            getLogger().info("[Farmer] " + a.getName() + " — " + a.getTask());
        }

    }

    private static void loadMysql(ConfigurationSection sqlSection){
        MySQL.HOST = sqlSection.getString("host");
        MySQL.PORT = sqlSection.getInt("port");
        MySQL.USER = sqlSection.getString("user");
        MySQL.PASSWORD = sqlSection.getString("password");
        MySQL.DATABASE = sqlSection.getString("database");
        log.info(sqlSection.getString("host"));
        log.info(sqlSection.getString("port"));
        log.info(sqlSection.getString("port"));
        log.info(sqlSection.getString("password"));
        log.info(sqlSection.getString("database"));
    }


    public static class MySQL{
        public static String HOST;
        public static int PORT;
        public static String USER;
        public static String PASSWORD;
        public static String DATABASE;
    }

    public static class GUI{
        @Getter
        @Setter
        @AllArgsConstructor
        public static class TASK{
            private String name;
            private String task;

        }

    }

    public static void isSectionIsNull(ConfigurationSection section){
        if(section == null) getLogger().info("Section is null");
    }

    public static void parseTasks(ConfigurationSection section, List<GUI.TASK> tasks) {

        for (String taskKey : section.getKeys(false)) {
            ConfigurationSection taskSection = section.getConfigurationSection(taskKey);

            if (taskSection == null) {
                getLogger().warning("Task section " + taskKey + " is null");
                continue;
            }
            String name = taskSection.getString( "name");
            String task = taskSection.getString("task");
            if (name != null && task != null) {
                tasks.add(new GUI.TASK(name, task));
            } else {
                getLogger().warning("Missing name or task for " + section.getName() + "." + taskKey);
            }
        }
    }
     public List<GUI.TASK> getTasks(Skill skill) {
        if (skill == null) return new ArrayList<>();

        return switch (skill.name().toLowerCase()) {
            case "warrior" -> warriorTask;
            case "alchemist" -> alchemistTask;
            case "farmer" -> farmerTask;
            default -> new ArrayList<>();
        };
    }
}



package ru.traiwy.skilltree.manager;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration challengeConfig;
    private FileConfiguration bdConfig;

    private final List<Challenge> challenges = new ArrayList<>();

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    public void load(FileConfiguration config) {
        try {
            loadMainConfig();
            loadDataBaseConfig();
            parseChallenges();
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка загрузки конфигурации", e);
        }
    }

    public void loadMainConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.challengeConfig = plugin.getConfig();

        if (challengeConfig == null) {
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

    private void parseChallenges() {
        List<Map<?, ?>> rawList = challengeConfig.getMapList("challenges");

        if (rawList == null || rawList.isEmpty()) return;

        for (Map map : rawList) {
            try {
                String type = (String) map.get("type");
                String id = (String) map.get("id");
                String displayName = (String) map.get("displayName");

                String nextChallengeId = (String) map.get("nextChallengeId");

                List<String> goal = (List<String>) map.get("goal");

                Map dataMap = (Map) map.get("data");
                int current = ((Number) dataMap.get("current")).intValue();
                int required = ((Number) dataMap.get("required")).intValue();

                Map settings = (Map) map.get("settings");
                Map metadata = (Map) map.get("metadata");

                Challenge challenge = new Challenge(type, id, goal,
                        new ChallengeData(current, required), displayName, nextChallengeId,
                        settings, metadata);

                challenges.add(challenge);
            } catch (Exception e) {
                log.error("Ошибка при парсинге испытания: {}", map, e);
            }
        }
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public Challenge getById(String id) {
        return challenges.stream()
                .filter(c -> c.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Data
    @AllArgsConstructor
    public static class Challenge {
        private String type;
        private String id;
        private List<String> goal;
        private ChallengeData data;
        private String displayName;
        private String nextChallengeId;
        private Map<String, Object> settings;
        private Map<String, Object> metadata;
    }

    @Getter
    @AllArgsConstructor
    public static class ChallengeData {
        private int current;
        private int required;
    }


    private static void loadMysql(ConfigurationSection sqlSection) {
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


    public static class MySQL {
        public static String HOST;
        public static int PORT;
        public static String USER;
        public static String PASSWORD;
        public static String DATABASE;
    }

    public void getAllInfo(Player player, ConfigManager.Challenge challenge) {
        if (challenge == null) {
            player.sendMessage("§c[SkillTree] Челлендж не найден!");
            return;
        }

        player.sendMessage("§6===== §eИнформация о челлендже §6=====§r");
        player.sendMessage("§eID: §f" + challenge.getId());
        player.sendMessage("§eТип: §f" + challenge.getType());
        player.sendMessage("§eНазвание: §f" + challenge.getDisplayName());
        player.sendMessage("§eСледующее задание: §f" +
                (challenge.getNextChallengeId() != null ? challenge.getNextChallengeId() : "§7Нет"));

        // Цели
        player.sendMessage("§eЦель:");
        if (challenge.getGoal() != null && !challenge.getGoal().isEmpty()) {
            for (String goal : challenge.getGoal()) {
                player.sendMessage("  §7- " + goal);
            }
        } else {
            player.sendMessage("  §7(Нет целей)");
        }

        // Прогресс
        if (challenge.getData() != null) {
            player.sendMessage("§eПрогресс: §f" + challenge.getData().getCurrent() + "§7/§f" + challenge.getData().getRequired());
        } else {
            player.sendMessage("§eПрогресс: §7Не задан");
        }

        // Настройки
        player.sendMessage("§eНастройки:");
        if (challenge.getSettings() != null && !challenge.getSettings().isEmpty()) {
            for (Map.Entry<String, Object> entry : challenge.getSettings().entrySet()) {
                player.sendMessage("  §f" + entry.getKey() + ": §7" + entry.getValue());
            }
        } else {
            player.sendMessage("  §7(Нет настроек)");
        }

        // Метаданные
        player.sendMessage("§eМетаданные:");
        if (challenge.getMetadata() != null && !challenge.getMetadata().isEmpty()) {
            for (Map.Entry<String, Object> entry : challenge.getMetadata().entrySet()) {
                player.sendMessage("  §f" + entry.getKey() + ": §7" + entry.getValue());
            }
        } else {
            player.sendMessage("  §7(Пусто)");
        }

        player.sendMessage("§6==============================");
    }
}
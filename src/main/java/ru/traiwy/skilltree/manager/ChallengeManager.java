package ru.traiwy.skilltree.manager;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.traiwy.skilltree.data.PlayerData;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChallengeManager {

    private final ConfigManager configManager;
    private final MySqlStorage mySqlStorage;

    public ChallengeManager(ConfigManager configManager, MySqlStorage mySqlStorage) {
        this.configManager = configManager;
        this.mySqlStorage = mySqlStorage;
    }


    public List<ConfigManager.Challenge> getAllChallenges() {
        return configManager.getChallenges();
    }


    public ConfigManager.Challenge getChallengeById(String id) {
        return configManager.getById(id);
    }

    public ConfigManager.Challenge getFirstChallengeForClass(String classPrefix) {
        for (ConfigManager.Challenge challenge : getAllChallenges()) {
            if (challenge.getId().startsWith(classPrefix)) {
                return challenge;
            }
        }
        return null;
    }


    public List<String> getAllId() {
        List<String> ids = new ArrayList<>();
        List<ConfigManager.Challenge> challenges = configManager.getChallenges();

        if (challenges == null || challenges.isEmpty()) {
            System.out.println("§cНет загруженных челленджей!");
            return ids;
        }

        for (ConfigManager.Challenge challenge : challenges) {
            if (challenge.getId() != null && !challenge.getId().isEmpty()) {
                ids.add(challenge.getId());
            }
        }

        return ids;
    }

    public void setNextChallenge(ConfigManager.Challenge challenge, Task task) {
        final String nextId = challenge.getNextChallengeId();

        if (nextId != null) {
            final ConfigManager.Challenge next = getChallengeById(nextId);
            System.out.println(next);
            if (next != null) {
                Task nextTask = new Task(
                        0,
                        task.getPlayerId(),
                        next.getDisplayName(),
                        nextId,
                        Status.IN_PROGRESS,
                        0
                );
                mySqlStorage.addTask(nextTask);
            }
        }
    }
    public void giveFirstChallengeToPlayer(Player player, String classPrefix, Skill skill) {
        mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
            if (playerData == null) {
                PlayerData newPlayer = new PlayerData(player.getName(), skill, 0);
                mySqlStorage.addPlayer(newPlayer);
                mySqlStorage.getPlayer(player.getName()).thenAccept(addedPlayer -> {
                    if (addedPlayer != null) {
                        giveTask(addedPlayer, classPrefix);
                    }
                });
            } else {
                giveTask(playerData, classPrefix);
            }
        });
    }
    private void giveTask(PlayerData playerData, String classPrefix) {
        ConfigManager.Challenge challenge = getFirstChallengeForClass(classPrefix);
        if (challenge == null) {
            System.out.println("challenge == null");
            return;
        }

        Task task = new Task(0, playerData.getId(), challenge.getDisplayName(), challenge.getId(), Status.IN_PROGRESS, 0);
        mySqlStorage.addTask(task);
    }
}

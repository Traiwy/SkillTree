package ru.traiwy.skilltree.manager;


import ru.traiwy.skilltree.data.Task;
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

    public ConfigManager.Challenge getNextChallenge(String currentChallengeId) {
        ConfigManager.Challenge current = getChallengeById(currentChallengeId);
        if (current == null || current.getNextChallengeId() == null) return null;
        return getChallengeById(current.getNextChallengeId());
    }

    public List<ConfigManager.Challenge> getChallengesByType(String type) {
        return getAllChallenges().stream()
                .filter(challenge -> type.equalsIgnoreCase(challenge.getType()))
                .collect(Collectors.toList());
    }


    public Map<String, Object> getSettings(String id) {
        ConfigManager.Challenge challenge = getChallengeById(id);
        return (challenge != null) ? challenge.getSettings() : null;
    }


    public Map<String, Object> getMetadata(String id) {
        ConfigManager.Challenge challenge = getChallengeById(id);
        return (challenge != null) ? challenge.getMetadata() : null;
    }


    public ConfigManager.ChallengeData getData(String id) {
        ConfigManager.Challenge challenge = getChallengeById(id);
        return (challenge != null) ? challenge.getData() : null;
    }


    public String getDisplayName(String id) {
        ConfigManager.Challenge challenge = getChallengeById(id);
        return (challenge != null) ? challenge.getDisplayName() : null;
    }

    public List<String> getGoal(String id) {
        ConfigManager.Challenge challenge = getChallengeById(id);
        return (challenge != null) ? challenge.getGoal() : null;
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
}

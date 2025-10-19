package ru.traiwy.skilltree.manager;


import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ChallengeManager {
    private final ConfigManager configManager;

    public List<ConfigManager.Challenge> getAllChallenges() {
        return configManager.getChallenges();
    }

    public ConfigManager.Challenge getChallengesById(String id) {
        return configManager.getById(id);
    }

    public String getType(String id) {
        ConfigManager.Challenge challenge = getChallengesById(id);
        return (challenge != null) ? challenge.getType() : null;
    }

    public List<String> getGoal(String id) {
        ConfigManager.Challenge challenge = getChallengesById(id);
        return (challenge != null) ? challenge.getGoal() : null;
    }

    public ConfigManager.ChallengeData getDataById(String id) {
        ConfigManager.Challenge challenge = getChallengesById(id);
        return (challenge != null) ? challenge.getData() : null;
    }

    public String getDisplayName(String id) {
        ConfigManager.Challenge challenge = getChallengesById(id);
        return (challenge != null) ? challenge.getDisplayName() : null;
    }

    public Map<String, Object> getSettingsById(String id) {
        ConfigManager.Challenge challenge = getChallengesById(id);
        return (challenge != null) ? challenge.getSettings() : null;
    }

    public Map<String, Object> getMetaDataById(String id) {
        ConfigManager.Challenge challenge = getChallengesById(id);
        return (challenge != null) ? challenge.getMetadata() : null;
    }

    public List<String> getAllId() {
        List<String> ids = new ArrayList<>();
        List<ConfigManager.Challenge> challenges = configManager.getChallenges();

        if (challenges == null || challenges.isEmpty()) {
            System.out.println("§cНет загруженных челленджей из конфига!");
            return ids;
        }

        for (ConfigManager.Challenge challenge : challenges) {
            String id = challenge.getId();
            if (id != null && !id.isEmpty()) {
                ids.add(id);
            }
        }

        return ids;
    }
}

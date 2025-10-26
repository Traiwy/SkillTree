package ru.traiwy.skilltree.manager;


import com.mysql.cj.exceptions.StreamingNotifiable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.traiwy.skilltree.data.PlayerData;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.session.PlayerSession;
import ru.traiwy.skilltree.session.TaskSession;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ChallengeManager {

    private final ConfigManager configManager;
    private final MySqlStorage mySqlStorage;
    private final PlayerSession playerSession;
    private final TaskSession taskSession;

    private final List<ConfigManager.Challenge> allChallenges;

    public ChallengeManager(ConfigManager configManager, MySqlStorage mySqlStorage, PlayerSession playerSession, TaskSession taskSession) {
        this.configManager = configManager;
        this.mySqlStorage = mySqlStorage;
        this.playerSession = playerSession;
        this.taskSession = taskSession;
        this.allChallenges = configManager.getChallenges();
    }


    public List<ConfigManager.Challenge> getAllChallenges() {
        return configManager.getChallenges();
    }


    public ConfigManager.Challenge getChallengeById(String id) {
        return configManager.getById(id);
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

    public void giveAllChallengesToPlayer(Player player, String classPrefix, Skill skill) {

        PlayerData playerData = getOrCreatePlayer(player, skill);


        List<ConfigManager.Challenge> classChallenges = getAllChallengesForClass(classPrefix);

        if (classChallenges.isEmpty()) {
            Bukkit.getLogger().warning("Нет челленджей для класса " + classPrefix);
            return;
        }


        boolean first = true;
        for (ConfigManager.Challenge challenge : classChallenges) {
            Status status = first ? Status.IN_PROGRESS : Status.NOT_STARTED;
            first = false;

            createTaskForPlayer(playerData, challenge, status);
        }
    }

    private List<ConfigManager.Challenge> getAllChallengesForClass(String classPrefix) {
        return getAllChallenges().stream()
                .filter(c -> c.getId().startsWith(classPrefix))
                .toList();
    }


    private void createTaskForPlayer(PlayerData playerData, ConfigManager.Challenge challenge, Status status) {
        Task task = new Task(
                0,
                playerData.getId(),
                challenge.getDisplayName(),
                challenge.getId(),
                status,
                0
        );

        mySqlStorage.addTask(task);
        taskSession.putTask(playerData.getPlayerName(), task);
    }


    private PlayerData getOrCreatePlayer(Player player, Skill skill) {
        PlayerData cachedPlayer = playerSession.getPlayerData(player.getName());
        if (cachedPlayer != null) return cachedPlayer;

        PlayerData newPlayer = new PlayerData(player.getName(), skill, 0);
        savePlayerAsync(newPlayer);
        return newPlayer;
    }


    private void savePlayerAsync(PlayerData playerData) {
        mySqlStorage.addPlayer(playerData);
        playerSession.updatePlayerData(playerData);
    }
}
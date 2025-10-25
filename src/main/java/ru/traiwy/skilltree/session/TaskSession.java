package ru.traiwy.skilltree.session;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.traiwy.skilltree.data.PlayerData;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class TaskSession {
    private final HashMap<String, HashMap<Task, Status>> cache = new HashMap<>();

    private final MySqlStorage mySqlStorage;

    public void load(PlayerData playerData) {
        mySqlStorage.getTasksByPlayer(playerData.getId()).thenAccept(tasks -> {
            for (Task task : tasks) {
                if (task.getStatus() == Status.IN_PROGRESS) {
                    putTask(playerData.getPlayerName(), task);

                    Bukkit.getLogger().info("Task " + task + "was add in the cache");
                }
            }
        });
    }

    public void putTask(String playerName, Task task) {
        HashMap<Task, Status> playerTasks = cache.computeIfAbsent(playerName, k -> new HashMap<>());
        playerTasks.put(task, task.getStatus());

        Bukkit.getLogger().info("Task " + task + "was update in the cache");
    }

    public HashMap<Task, Status> getTasks(String playerName) {
        return cache.getOrDefault(playerName, new HashMap<>());
    }

    public Task getActiveTask(String playerName) {
        HashMap<Task, Status> playerTasks = cache.get(playerName);
        if (playerTasks == null) return null;

        for (Map.Entry<Task, Status> entry : playerTasks.entrySet()) {
            if (entry.getValue() == Status.IN_PROGRESS) {
                return entry.getKey();
            }
        }

        return null;
    }

    public void saveAndRemove(PlayerData playerData) {
        HashMap<Task, Status> tasks = cache.remove(playerData.getPlayerName());
        if (tasks != null) {
            tasks.forEach((task, status) -> {
                task.setStatus(status);
                mySqlStorage.updateTask(task);
                Bukkit.getLogger().info("Task " + task + "was save in the cache");
            });
        }
    }
}

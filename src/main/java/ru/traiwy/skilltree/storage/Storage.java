package ru.traiwy.skilltree.storage;


import ru.traiwy.skilltree.data.PlayerData;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Storage {

    CompletableFuture<PlayerData> getPlayer(String playerName);
    CompletableFuture<PlayerData> getPlayer(int id);
    void addPlayer(PlayerData player);
    void removePlayer(int id);
    void updatePlayer(PlayerData player);



    CompletableFuture<Task> getTask(int id);
    CompletableFuture<List<Task>> getTasksByPlayer(int playerId);
    CompletableFuture<List<Task>> getTasksByStatus(int playerId, Status status);
    void addTask(Task task);
    void removeTask(int id);
    void updateTask(Task task);

    CompletableFuture<Integer> countTasksByStatus(String status);
    CompletableFuture<Integer> countPlayers();


}

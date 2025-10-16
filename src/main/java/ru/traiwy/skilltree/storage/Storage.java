package ru.traiwy.skilltree.storage;


import ru.traiwy.skilltree.data.Player;
import ru.traiwy.skilltree.data.Task;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Storage {

    CompletableFuture<Player> getPlayer(String playerName);
    CompletableFuture<Player> getPlayer(int id);
    void addPlayer(Player player);
    void removePlayer(int id);
    void updatePlayer(Player player);


    CompletableFuture<Task> getTask(int id);
    CompletableFuture<List<Task>> getTasksByPlayer(int playerId);
    CompletableFuture<List<Task>> getTasksByStatus(int playerId, String status);
    void addTask(Task task);
    void removeTask(int id);
    void updateTask(Task task);

    CompletableFuture<Integer> countTasksByStatus(String status);
    CompletableFuture<Integer> countPlayers();


}

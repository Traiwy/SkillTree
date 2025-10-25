package ru.traiwy.skilltree.session;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.traiwy.skilltree.data.PlayerData;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


@AllArgsConstructor
public class PlayerSession {
    private final Map<String, PlayerData> cache = new ConcurrentHashMap<>();

    private final MySqlStorage mySqlStorage;


    public void load(Player player) {
         mySqlStorage.getPlayer(player.getName()).thenApply(playerData -> {
            if (playerData == null) {
                playerData = new PlayerData(player.getName(), Skill.SOME_DEFAULT, 0);
                mySqlStorage.addPlayer(playerData);

            }

            cache.put(player.getName(), playerData);
            Bukkit.getLogger().info("Player " + player.getName() + " was added to cache");
            return playerData;
        });
    }

    public void remove(Player player){
        mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
            if(playerData != null){
                mySqlStorage.updatePlayer(playerData);
                cache.remove(player);
            }
        });

        Bukkit.getLogger().info("Player " + player.getName() + " was remove in the cache");
    }

    public PlayerData getPlayerData(String playerName){
        return cache.get(playerName);
    }

    public void updatePlayerData(String playerName){
        PlayerData playerData = getPlayerData(playerName);

        cache.put(playerName, playerData);
    }

    public void updatePlayerData(PlayerData playerData){
        String name = playerData.getPlayerName();

        cache.put(name, playerData);
    }
}

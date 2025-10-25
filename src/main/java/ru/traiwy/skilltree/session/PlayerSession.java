package ru.traiwy.skilltree.session;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.traiwy.skilltree.data.PlayerData;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
public class PlayerSession {
    private final Map<String, PlayerData> cache = new HashMap<>();

    private final MySqlStorage mySqlStorage;


    public void load(Player player){
        mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
            if(playerData == null){
                PlayerData data = new PlayerData(player.getName(), Skill.SOME_DEFAULT, 0);
                mySqlStorage.addPlayer(data);
            }

            cache.put(player.getName(), playerData);
        });

        Bukkit.getLogger().info("Player " + player.getName() + " was add in the cache");
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
}

package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.traiwy.skilltree.data.PlayerData;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.session.PlayerSession;
import ru.traiwy.skilltree.session.TaskSession;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.concurrent.CompletableFuture;


@AllArgsConstructor
public class PlayersSessionEvent implements Listener {
    private final MySqlStorage mySqlStorage;
    private final PlayerSession playerSession;


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        mySqlStorage.getPlayer(player.getName())
                .thenCompose(playerData -> {
                    if (playerData != null) {
                        playerSession.updatePlayerData(playerData);
                        return CompletableFuture.completedFuture(playerData);
                    } else {
                        PlayerData newPlayer = new PlayerData(player.getName(), Skill.SOME_DEFAULT, 0);
                        return mySqlStorage.addPlayerAsync(newPlayer)
                                .thenApply(pd -> {
                                    playerSession.updatePlayerData(pd);
                                    return pd;
                                });
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }



    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();


    }
}

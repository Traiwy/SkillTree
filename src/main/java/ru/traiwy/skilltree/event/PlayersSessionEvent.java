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
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.session.PlayerSession;
import ru.traiwy.skilltree.session.TaskSession;


@AllArgsConstructor
public class PlayersSessionEvent implements Listener {
    private final PlayerSession session;
    private final TaskSession taskSession;

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        session.load(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();


       PlayerData playerData = session.getPlayerData(player.getName());

       if(playerData == null){
           Bukkit.getLogger().warning("PlayerData is null event");

       }

        if(playerData != null) {
            taskSession.load(playerData);
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();

        PlayerData playerData = session.getPlayerData(player.getName());

         if(playerData != null){
             session.remove(player);
             taskSession.saveAndRemove(playerData);
         }

    }
}

package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.manager.EventManager;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
public class LavaDamageEvent implements Listener {
    private final MySqlStorage mySqlStorage;
    private final ChallengeManager challengeManager;
    private final JavaPlugin plugin;
    private final EventManager eventManager;

    private final Set<Player> lavaPlayer = new HashSet<>();

    @EventHandler
    public void onLavaDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player player)) return;

        if(event.getCause() != EntityDamageEvent.DamageCause.LAVA) return;
        lavaPlayer.add(player);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(!lavaPlayer.contains(player)) return;

            if(player.isDead()) return;

            handleLavaSurvive(player);
        }, 20L * 5);


    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        lavaPlayer.remove(event.getPlayer());
    }

    public void handleLavaSurvive(Player player){
        mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
            if (playerData == null) return;
            final int playerId = playerData.getId();

            mySqlStorage.getTasksByPlayer(playerId).thenAccept(tasks -> {
                for (Task task : tasks) {
                    if (!(eventManager.isApplicableTask(task, "survive"))) continue;

                    ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
                    Object condition = challenge.getSettings().get("condition");
                    if (condition == null || !condition.toString().contains("LAVA_SURVIVE")) continue;

                    eventManager.handleProgress(task, challenge, player);
                    if(task.getStatus() == Status.COMPLETED) {
                        challengeManager.setNextChallenge(challenge, task);
                    }
                    Bukkit.getScheduler().runTask(plugin, () ->
                            player.sendMessage("§aВы выжили после купания в лаве!")
                    );
                }
            });
        });
    }
}








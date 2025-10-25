package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.traiwy.skilltree.data.PlayerData;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.manager.EventManager;
import ru.traiwy.skilltree.session.PlayerSession;
import ru.traiwy.skilltree.session.TaskSession;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class MobKillEvent implements Listener {
    private final ChallengeManager challengeManager;
    private final EventManager eventManager;
    private final PlayerSession playerSession;
    private final TaskSession taskSession;

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        final Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        final String mobType = getMobType(event.getEntity());

        PlayerData playerData = playerSession.getPlayerData(killer.getName());

        if(playerData != null) {
            Task task = taskSession.getActiveTask(playerData.getPlayerName());
            if(task != null) {
                processTask(task, mobType, killer);
            }
        }
    }

    private void processTask(Task task, String mobType, Player player) {
        if (!eventManager.isApplicableTask(task, "kill-mob")) return;

        ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
        final Object rawEntities = challenge.getSettings().get("entityType");
        if (!(rawEntities instanceof List<?> targetMobs)) return;

        final String mobTypeUpper = mobType.toUpperCase();
        for (Object obj : targetMobs) {
            if (!(obj instanceof String entityName)) continue;

            if (entityName.equalsIgnoreCase(mobTypeUpper)) {
                eventManager.handleProgress(task, challenge, player);
                if(task.getStatus() == Status.COMPLETED) {
                    challengeManager.setNextChallenge(challenge, task);

                    CompletableFuture<Task> nextTask = challengeManager.getNextChallenge(player);

                    taskSession.putTask(player.getName(), nextTask);
                    return;
                }
            }
        }
    }


    private String getMobType(Entity entity) {
        return entity.getType().name();
    }
}

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
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.List;

@AllArgsConstructor
public class MobKillEvent implements Listener {
    private final MySqlStorage mySqlStorage;
    private final ConfigManager configManager;
    private final ChallengeManager challengeManager;
    private final EventManager eventManager;

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        final Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        final String mobType = getMobType(event.getEntity());

        mySqlStorage.getPlayer(killer.getName()).thenAccept(playerData -> {
            if (playerData == null) return;
            mySqlStorage.getTasksByPlayer(playerData.getId()).thenAccept(tasks -> {
                for (Task task : tasks) {
                    processTask(task, mobType, killer);
                }
            });
        });
    }

    private void processTask(Task task, String mobType, Player player) {
        eventManager.isApplicableTask(task, "kill-mob");

        ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
        final Object rawEntities = challenge.getSettings().get("entityType");
        if (!(rawEntities instanceof List<?> targetMobs)) return;

        final String mobTypeUpper = mobType.toUpperCase();
        for (Object obj : targetMobs) {
            if (!(obj instanceof String entityName)) continue;

            if (entityName.equalsIgnoreCase(mobTypeUpper)) {
                eventManager.handleProgress(task, challenge, player);
                challengeManager.setNextChallenge(challenge, task);
            }else{
                mySqlStorage.updateTask(task);
            }
        }
    }


    private String getMobType(Entity entity) {
        if (entity instanceof Zombie) return "zombie";
        if (entity instanceof Skeleton) return "skeleton";
        if (entity instanceof Spider) return "spider";
        if (entity instanceof Witch) return "witch";
        if (entity instanceof EnderDragon) return "ender_dragon";
        if (entity instanceof Creeper) return "creeper";
        if (entity instanceof Enderman) return "enderman";
        if (entity instanceof Blaze) return "blaze";
        return "other";
    }
}

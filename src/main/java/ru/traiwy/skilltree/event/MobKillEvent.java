package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.traiwy.skilltree.data.PlayerData;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class MobKillEvent implements Listener {
    private final MySqlStorage mySqlStorage;
    private final ConfigManager configManager;


    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        Entity killMob = event.getEntity();
        if (killer == null) return;

        String mobType = getMobType(event.getEntity());

        CompletableFuture.runAsync(() -> processMobKill(killer, mobType, killMob))
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    private void processMobKill(Player player, String mobType, Entity killMob) {
        PlayerData playerData = mySqlStorage.getPlayer(player.getName()).join();
        if (playerData == null) return;

        List<Task> tasks = mySqlStorage.getTasksByPlayer(playerData.getId()).join();

        for (Task task : tasks) {
            System.out.println("tasks: " + tasks);
            System.out.println("Status: " + task.getStatus());

            if (task.getStatus() != Status.IN_PROGRESS) continue;




            if(getMobType(killMob).equalsIgnoreCase("zombie")){

            }

        }
    }

    private boolean doesTaskMatchMob(Entity entity, String mobType) {
            String entityType = getMobType(entity);
            boolean match = entityType.equalsIgnoreCase(mobType);
            return match;
    }
    public void updateTaskProgress(Entity killMob, String mobType, PlayerData playerData, Task task){
        if (doesTaskMatchMob(killMob, mobType)) {
                int currentProgress = playerData.getProgress();
                int newProgress = currentProgress + 1;
                System.out.println("newProgress: " + newProgress);

                playerData.setProgress(newProgress);
                mySqlStorage.updatePlayer(playerData);

               // if (newProgress >= requiredKills) {
               //     task.setStatus(Status.COMPLETED);
               //     playerData.setProgress(0);
               //     mySqlStorage.updatePlayer(playerData);
               // }

                mySqlStorage.updateTask(task);
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

package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.storage.MySqlStorage;

@AllArgsConstructor
public class MobKillListener implements Listener {
    private final MySqlStorage mySqlStorage;

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        Entity entity = event.getEntity();

        if (killer == null) return;
        String mobType = getMobType(entity);


        if (mobType.equalsIgnoreCase("zombie")) {
            if (mySqlStorage.getStatus(killer.getName(), 1) == Status.IN_PROGRESS) {
                updateKillModCount(killer, 1, 5);
            }
            if (mySqlStorage.getProgress(killer.getName()) == 5 &&
                    mySqlStorage.getStatus(killer.getName(), 1) == Status.COMPLETED) {

                mySqlStorage.updateTask(killer.getName(), 2, Status.IN_PROGRESS);
                mySqlStorage.updateProgress(killer.getName(), 0);
            }

        }

        if (mobType.equalsIgnoreCase("skeleton")) {
            if (mySqlStorage.getStatus(killer.getName(), 2) == Status.IN_PROGRESS) {
                updateKillModCount(killer, 2, 3);
            }
            if(mySqlStorage.getProgress(killer.getName()) == 3 &&
                mySqlStorage.getStatus(killer.getName(), 2) == Status.COMPLETED){
                mySqlStorage.updateTask(killer.getName(), 3, Status.IN_PROGRESS);
                mySqlStorage.updateProgress(killer.getName(), 0);
            }
        }

        if (mobType.equalsIgnoreCase("spider")) {
            if (mySqlStorage.getStatus(killer.getName(), 3) == Status.IN_PROGRESS) {
                updateKillModCount(killer, 3, 2);
                mySqlStorage.updateTask(killer.getName(), 4, Status.IN_PROGRESS);
                mySqlStorage.updateProgress(killer.getName(), 0);
            }
        }

        if (mobType.equalsIgnoreCase("witch")) {
            if (mySqlStorage.getStatus(killer.getName(), 7) == Status.IN_PROGRESS) {
                updateKillModCount(killer, 7, 1);
            }
        }

        if (mobType.equalsIgnoreCase("ender_dragon")) {
            if (mySqlStorage.getStatus(killer.getName(), 1) == Status.IN_PROGRESS) {
                updateKillModCount(killer, 9, 1);
            }
        }

    }

    private String getMobType(Entity entity){
        if(entity instanceof Zombie) return  "zombie";
        else if (entity instanceof Skeleton) return "skeleton";
        else if (entity instanceof Spider) return "spider";
        else if (entity instanceof Witch) return "witch";
        else if( entity instanceof  EnderDragon) return "ender_dragon";
        else return "other";
    }

    private void updateKillModCount(Player player, int taskId, int kills){

        int currentCount = mySqlStorage.getProgress(player.getName());
        int newCount = currentCount + 1;
        mySqlStorage.updateProgress(player.getName(), newCount);

        int requiredKills  = getRequiredKills(kills);
        if(newCount >= requiredKills){
            mySqlStorage.updateTask(player.getName(), taskId, Status.COMPLETED);
        }
    }

    private int getRequiredKills(int kills){
        return kills;
    }
}

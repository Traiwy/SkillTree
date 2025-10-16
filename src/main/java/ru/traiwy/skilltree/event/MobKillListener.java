package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
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


    }

    private int getRequiredKills(int kills){
        return kills;
    }
}

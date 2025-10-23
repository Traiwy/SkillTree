package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.manager.EventManager;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class EntityPotionDamageEvent implements Listener {
    private final EventManager eventManager;
    private final JavaPlugin plugin;

    private final Set<Player> potionPlayer = new HashSet<>();

    @EventHandler
    public void onEntityPotionDamage(EntityPotionEffectEvent event) {
         if (!(event.getEntity() instanceof Player player)) return;
        if (event.getNewEffect() == null) return;

        String effectType = event.getNewEffect().getType().getName().toUpperCase();
        if (!effectType.equals("POISON")) return;

        potionPlayer.add(player);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!potionPlayer.contains(player)) return;
            if (player.isDead()) return;

            potionPlayer.remove(player);
            eventManager.updateChallengeProgress(player, "survive", "condition", "POISON");
        },20L * 5);

    }



}

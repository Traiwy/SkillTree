package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.manager.EventManager;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.lang.reflect.Method;


@AllArgsConstructor
public class BlockHitEvent implements Listener {
    private final ChallengeManager challengeManager;
    private final MySqlStorage mySqlStorage;
    private final JavaPlugin plugin;
    private final EventManager eventManager;

    @EventHandler
    public void onBlockHitEvent(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        ;

        final Block hitBlock = event.getHitBlock();

        if (hitBlock.getType() != Material.TARGET) return;
        mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
            if (playerData == null) return;

            mySqlStorage.getTasksByPlayer(playerData.getId()).thenAccept(tasks -> {
                for (Task task : tasks) {
                    eventManager.isApplicableTask(task, "bow-hit");
                    final ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());

                    final double playerDistance = player.getLocation().distance(hitBlock.getLocation());
                    final int distance = getDistance(challenge);

                    if (playerDistance >= distance) {
                        eventManager.handleProgress(task, challenge, player);
                        challengeManager.setNextChallenge(challenge, task);

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            player.sendMessage("Ты попал");
                        });

                    } else {
                        mySqlStorage.updateTask(task);
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            player.sendMessage("Не попал");
                        });
                    }
                }
            });
        });

    }{}

    public int getDistance(ConfigManager.Challenge challenge) {

        Object value = challenge.getSettings().get("distance");
        if (value instanceof Number num) {
            return num.intValue();
        }
        if (value instanceof String str) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException ignored) {
            }
        }
        Bukkit.getLogger().warning("§cНеверное значение distance в челлендже " + challenge.getId());
        return 0;
    }


}

package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.manager.EventManager;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.List;

@AllArgsConstructor
public class PotionDrinkEvent implements Listener {
    private final MySqlStorage mySqlStorage;
    private final EventManager eventManager;
    private final ChallengeManager challengeManager;

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        if (item == null || item.getType().name().contains("POTION") == false) return;

        if (!(item.getItemMeta() instanceof PotionMeta meta)) return;

        final PotionData data = meta.getBasePotionData();
        final PotionType type = data.getType();

        final int potionLevel = data.isUpgraded() ? 2 : 1;

        mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
            if (playerData == null) return;

            mySqlStorage.getTasksByPlayer(playerData.getId()).thenAccept(tasks -> {
                for (Task task : tasks) {
                    if (!eventManager.isApplicableTask(task, "brew-potion")) continue;

                    final ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
                    if (challenge == null) continue;

                    final Object potionObj = challenge.getSettings().get("potionType");
                    final Object levelObj = challenge.getSettings().get("level");
                    if (!(potionObj instanceof List<?> requiredPotions)) continue;
                    Integer requiredLevel = (levelObj instanceof Number) ? ((Number) levelObj).intValue() : 1;

                    if (requiredPotions.stream().anyMatch(p -> p.toString().equalsIgnoreCase(type.name()))) {
                        eventManager.handleProgress(task, challenge, player);

                        if (task.getStatus() == Status.COMPLETED) {
                            challengeManager.setNextChallenge(challenge, task);
                            return;
                        }
                    }
                    if (potionLevel == requiredLevel) {
                        eventManager.handleProgress(task, challenge, player);

                        if (task.getStatus() == Status.COMPLETED) {
                            challengeManager.setNextChallenge(challenge, task);
                            return;
                        }
                    }
                }
            });
        });
    }
}
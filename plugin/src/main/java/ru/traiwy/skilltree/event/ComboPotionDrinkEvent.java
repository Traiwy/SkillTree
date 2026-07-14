package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
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

import java.util.*;

@AllArgsConstructor
public class ComboPotionDrinkEvent implements Listener {
    private final MySqlStorage mySqlStorage;
    private final EventManager eventManager;
    private final ChallengeManager challengeManager;

    private final Map<Player, Set<String>> playerPotions = new HashMap<>();


    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        if (item == null || item.getType().name().contains("POTION") == false) return;
        if (!(item.getItemMeta() instanceof PotionMeta meta)) return;


        final PotionData data = meta.getBasePotionData();
        final String potionName = data.getType().name();
        final PotionType type = data.getType();

        Set<String> drankPotions = playerPotions.computeIfAbsent(player, k -> new HashSet<>());
        drankPotions.add(potionName);

        mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
            mySqlStorage.getTasksByPlayer(playerData.getId()).thenAccept(tasks -> {
                for (Task task : tasks) {
                    if (!(eventManager.isApplicableTask(task, "brew-combo-potion"))) continue;

                    final ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
                    if (challenge == null) continue;
                    final Object comboObj = challenge.getSettings().get("combo");
                    if (!(comboObj instanceof List<?> comboPotions)) continue;

                    boolean allDrank = comboPotions.stream()
                            .allMatch(p -> drankPotions.contains(p.toString()));
                    System.out.println(allDrank);

                    if (allDrank) {
                        eventManager.handleProgress(task, challenge, player);
                        if (task.getStatus() == Status.COMPLETED) {
                            challengeManager.setNextChallenge(challenge, task);
                        }
                        drankPotions.clear();
                    }
                }
            });
        });
    }
}

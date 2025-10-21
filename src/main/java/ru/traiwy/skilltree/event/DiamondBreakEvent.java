package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.manager.EventManager;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
public class DiamondBreakEvent implements Listener {
    private final ChallengeManager challengeManager;
    private final MySqlStorage mySqlStorage;
    private final EventManager eventManager;


    @EventHandler
    public void onDiamondBreakEvent(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();

        mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
            if (playerData == null) return;

            mySqlStorage.getTasksByPlayer(playerData.getId()).thenAccept(tasks -> {
                for (Task task : tasks) {
                    if (!eventManager.isApplicableTask(task, "find-item")) continue;

                    ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
                    if (challenge == null) continue;

                    List<Material> validBlocks = getAllowedBlocks(challenge);
                    if (validBlocks == null || !validBlocks.contains(block.getType())) continue;

                    if (!isValidYLevel(block, challenge)) continue;

                    handleProgress(task, challenge, player);
                }
            });
        });
    }


    private List<Material> getAllowedBlocks(ConfigManager.Challenge challenge) {
        Object rawBlockList = challenge.getSettings().get("item");
        if (!(rawBlockList instanceof List<?> blockList)) return null;

        List<Material> materials = new ArrayList<>();
        for (Object obj : blockList) {
            if (obj instanceof String blockName) {
                try {
                    materials.add(Material.valueOf(blockName.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("§cНеизвестный блок в конфиге: " + blockName);
                }
            }
        }
        return materials.isEmpty() ? null : materials;
    }

    private boolean isValidYLevel(Block block, ConfigManager.Challenge challenge) {
        Object yLevelObj = challenge.getSettings().get("yLevel");
        if (yLevelObj instanceof Number yLevel) {
            return block.getY() <= yLevel.intValue();
        }
        return true;
    }


    private void handleProgress(Task task, ConfigManager.Challenge challenge, Player player) {
        int required = challenge.getData().getRequired();
        int newProgress = Math.min(task.getProgress() + 1, required);

        task.setProgress(newProgress);
        if (newProgress >= required) {
            task.setStatus(Status.COMPLETED);
        }

        mySqlStorage.updateTask(task);
        player.sendMessage("§aТы сломал нужный блок!");
    }
}
package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.*;

@AllArgsConstructor
public class BlocksBreakEvent implements Listener {
    private final ChallengeManager challengeManager;
    private final MySqlStorage mySqlStorage;
    private final JavaPlugin plugin;

    private final Map<Player, Material> lastBreakBlock = new HashMap<>();

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Player player = event.getPlayer();

        if(isWood(block.getType())){
            lastBreakBlock.put(player, block.getType());
        }
    }

    @EventHandler
public void onPlayerItemBreakEvent(PlayerItemBreakEvent event) {
        final ItemStack item = event.getBrokenItem();
        final Player player = event.getPlayer();

        Material lastBlock = lastBreakBlock.get(player);
        if (lastBlock == null) return;

        mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
            if (playerData == null) return;

            mySqlStorage.getTasksByPlayer(playerData.getId()).thenAccept(tasks -> {
                for (Task task : tasks) {
                    if (task.getStatus() == Status.COMPLETED) continue;

                    final ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
                    if (challenge == null || !"break-sword-on-wood".equals(challenge.getType())) continue;

                    final List<Material> materialBlock = getMaterialBlock(task);
                    final List<Material> materialItem = getMaterialItem(task);
                    if (materialItem == null || materialBlock == null) continue;

                    if (materialItem.contains(item.getType()) && materialBlock.contains(lastBlock)) {
                        int newProgress = Math.min(task.getProgress() + 1, challenge.getData().getRequired());
                        task.setProgress(newProgress);

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            player.sendMessage("§aТы сломал меч об дерево. Задание выполнено!");
                        });

                        if (newProgress >= challenge.getData().getRequired()) {
                            task.setStatus(Status.COMPLETED);
                            mySqlStorage.updateTask(task);

                            String nextId = challenge.getNextChallengeId();
                            if (nextId != null) {
                                ConfigManager.Challenge next = challengeManager.getChallengeById(nextId);
                                if (next != null) {
                                    Task nextTask = new Task(
                                            0,
                                            task.getPlayerId(),
                                            next.getDisplayName(),
                                            nextId,
                                            Status.IN_PROGRESS,
                                            0
                                    );
                                    mySqlStorage.addTask(nextTask);
                                }
                            }
                        } else {
                            mySqlStorage.updateTask(task);
                        }
                        break;
                    }
                }
            });
        });
    }

    public List<Material> getMaterialBlock(Task task){
        if(task.getStatus() == Status.COMPLETED) return null;

        final ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
        if(challenge == null || !"break-sword-on-wood".equals(challenge.getType())) return null;

        final Object rawBlock = challenge.getSettings().get("block");
        if(!(rawBlock instanceof List<?> targetBlockList) ) return null;
         List<Material> materials = new ArrayList<>();

        for(Object obj : targetBlockList){
            if(!(obj instanceof String blockName)) continue;

            try{
                materials.add(Material.valueOf(blockName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                 System.out.println("§cНеизвестный блок в конфиге: " + blockName);
            }
        }
        return materials.isEmpty() ? null : materials;
    }

    public List<Material> getMaterialItem(Task task) {
        if (task.getStatus() == Status.COMPLETED) return null;

        final ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
        if (challenge == null || !"break-sword-on-wood".equals(challenge.getType())) return null;

        final Object rawItem = challenge.getSettings().get("item");
        if (!(rawItem instanceof List<?> targetItemList)) return null;

        List<Material> materials = new ArrayList<>();

        for (Object obj : targetItemList) {
            if (!(obj instanceof String itemName)) continue;

            try {
                Material mat = Material.valueOf(itemName.toUpperCase());
                materials.add(mat);
            } catch (IllegalArgumentException e) {
                System.out.println("§c[SkillTree] Неизвестный предмет в конфиге: " + itemName);
            }
        }

        return materials.isEmpty() ? null : materials;
    }

     private boolean isWood(Material mat) {
        return switch (mat) {
            case OAK_LOG, SPRUCE_LOG, BIRCH_LOG, JUNGLE_LOG, DARK_OAK_LOG, ACACIA_LOG, CHERRY_LOG -> true;
            default -> false;
        };
    }

}

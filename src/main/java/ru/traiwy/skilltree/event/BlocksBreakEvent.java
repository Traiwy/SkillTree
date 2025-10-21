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
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.manager.EventManager;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.*;

@AllArgsConstructor
public class BlocksBreakEvent implements Listener {
    private final ChallengeManager challengeManager;
    private final MySqlStorage mySqlStorage;
    private final JavaPlugin plugin;
    private final EventManager eventManager;

    private final Map<Player, Material> lastBreakBlock = new HashMap<>();

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Player player = event.getPlayer();

        player.sendMessage(block.getType() + " Высота: " + block.getY());

        if (isWood(block.getType())) {
            lastBreakBlock.put(player, block.getType());
        }

        mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
            if (playerData == null) return;

            mySqlStorage.getTasksByPlayer(playerData.getId()).thenAccept(tasks -> {
                for (Task task : tasks) {
                     Bukkit.getScheduler().runTask(plugin, () -> {
                         if (!eventManager.isApplicableTask(task, "find-item")) return;

                         ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
                         if (challenge == null) return;

                         List<Material> validBlocks = getAllowedBlocks(challenge);
                         if (validBlocks == null || !validBlocks.contains(block.getType())) return;

                         if (!isValidYLevel(block, challenge)) return;

                         eventManager.handleProgress(task, challenge, player);
                     });
                }
            });
        });
    }

    @EventHandler
    public void onPlayerItemBreakEvent(PlayerItemBreakEvent event) {
        final Material item = event.getBrokenItem().getType();
        final Player player = event.getPlayer();


        final Material lastBlock = lastBreakBlock.get(player);
        lastBreakBlock.remove(player);
        if (lastBlock == null) return;

        mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
            if (playerData == null) return;

            mySqlStorage.getTasksByPlayer(playerData.getId()).thenAccept(tasks -> {
                for (Task task : tasks) {
                    eventManager.isApplicableTask(task, "break-sword-on-wood");
                    final ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());

                    final List<Material> materialBlock = getMaterialBlock(challenge);
                    final List<Material> materialItem = getMaterialItem(challenge);

                    if (materialItem == null || materialBlock == null) continue;

                    for (Material materialB : materialBlock) {
                        for (Material materialI : materialItem) {
                            if (materialI == item && materialB == lastBlock) {
                                eventManager.handleProgress(task, challenge, player);
                                if(task.getStatus() == Status.COMPLETED) {
                                    challengeManager.setNextChallenge(challenge, task);
                                }

                                Bukkit.getScheduler().runTask(plugin, () -> {
                                    player.sendMessage("§aТы сломал меч об дерево. Задание выполнено!");
                                });

                            } else {
                                mySqlStorage.updateTask(task);
                            }
                            break;

                        }
                    }
                }
            });
        });
    }

    public List<Material> getMaterialBlock(ConfigManager.Challenge challenge) {

        final Object rawBlock = challenge.getSettings().get("block");
        if (!(rawBlock instanceof List<?> targetBlockList)) return null;
        List<Material> materials = new ArrayList<>();

        for (Object obj : targetBlockList) {
            if (!(obj instanceof String blockName)) continue;

            try {
                materials.add(Material.valueOf(blockName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("§cНеизвестный блок в конфиге: " + blockName);
            }
        }
        return materials.isEmpty() ? null : materials;
    }

    public List<Material> getMaterialItem(ConfigManager.Challenge challenge) {

        final Object rawItem = challenge.getSettings().get("item");
        if (!(rawItem instanceof List<?> targetItemList)) return null;

        List<Material> materials = new ArrayList<>();

        for (Object obj : targetItemList) {
            if (!(obj instanceof String itemName)) continue;

            try {
                Material mat = Material.valueOf(itemName.toUpperCase());
                materials.add(mat);
            } catch (IllegalArgumentException e) {
                System.out.println("§cНеизвестный предмет в конфиге: " + itemName);
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

    private List<Material> getAllowedBlocks(ConfigManager.Challenge challenge) {
        Object rawBlockList = challenge.getSettings().get("item");
        if (!(rawBlockList instanceof List<?> blockList)) {
            Bukkit.getLogger().info("[DEBUG] Challenge " + challenge.getId() + " не содержит список блоков.");
            return null;
        }

        List<Material> materials = new ArrayList<>();
        for (Object obj : blockList) {
            if (obj instanceof String blockName) {
                try {
                    Material mat = Material.valueOf(blockName.toUpperCase());
                    materials.add(mat);
                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().warning("[DEBUG] Неизвестный блок в конфиге: " + blockName);
                }
            }
        }

        Bukkit.getLogger().info("[DEBUG] getAllowedBlocks -> " + materials);
        return materials.isEmpty() ? null : materials;
    }

    private boolean isValidYLevel(Block block, ConfigManager.Challenge challenge) {
        Object yLevelObj = challenge.getSettings().get("yLevel");
        if (yLevelObj instanceof Number yLevel) {
            Bukkit.getLogger().info("[DEBUG] Проверка Y: блок " + block.getY() + ", лимит " + yLevel.intValue());
            return block.getY() <= yLevel.intValue();
        }
        Bukkit.getLogger().info("[DEBUG] В challenge нет yLevel, возвращаю true");
        return true;
    }

}

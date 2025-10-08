package ru.traiwy.skilltree.util;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.List;

@AllArgsConstructor
public class PanelManager {
    private final ConfigManager configManager;
    private final MySqlStorage mySqlStorage;

    private final int[] SLOTS_PANEL = {36, 37, 38, 39, 40, 41, 42, 43, 44};
     public void setPanels(Player player, Skill skill) {
        List<ConfigManager.GUI.TASK> tasks = configManager.getTasks(skill);

        for (int i = 0; i < SLOTS_PANEL.length; i++) {
            if (i < tasks.size()) {
                ConfigManager.GUI.TASK task = tasks.get(i);
                ItemStack panel = getTaskPanel(player, task, i + 1);
                player.getOpenInventory().setItem(SLOTS_PANEL[i], panel);
            } else {
                ItemStack emptyPanel = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMetaUtils.applyItemMeta(emptyPanel, "§7Пусто", List.of("§8Нет задачи"));
                player.getOpenInventory().setItem(SLOTS_PANEL[i], emptyPanel);
            }
        }
    }
    private ItemStack getTaskPanel(Player player, ConfigManager.GUI.TASK task, int taskId) {
        Status status = mySqlStorage.getStatus(player.getName(), taskId);

        Material panelMaterial;

        switch (status) {
            case COMPLETED:
                panelMaterial = Material.GREEN_STAINED_GLASS_PANE;
                break;
            case IN_PROGRESS:
                panelMaterial = Material.YELLOW_STAINED_GLASS_PANE;
                break;
            case NOT_STARTED:
            default:
                panelMaterial = Material.RED_STAINED_GLASS_PANE;
                break;
        }

        final ItemStack panel = new ItemStack(panelMaterial);
        ItemMetaUtils.applyItemMeta(panel, task.getName(), List.of(
                "§7" + task.getTask()
        ));

        return panel;
    }

}


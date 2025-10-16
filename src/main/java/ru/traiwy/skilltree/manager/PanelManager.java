package ru.traiwy.skilltree.manager;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.storage.MySqlStorage;
import ru.traiwy.skilltree.util.ItemMetaUtils;

import java.util.List;

@AllArgsConstructor
public class PanelManager {
    private final ConfigManager configManager;
    private final MySqlStorage mySqlStorage;

    private final int[] SLOTS_PANEL = {36, 37, 38, 39, 40, 41, 42, 43, 44};
    public static final int[] SLOTS_GRAY_PANEL = {
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            45, 46, 47, 48, 49, 50, 51, 52, 53,
    };

    public void setPanels(Player player, Skill skill, Inventory inventory) {
        List<ConfigManager.GUI.TASK> tasks = configManager.getTasks(skill);

        for (int i = 0; i < SLOTS_PANEL.length; i++) {
            if (i < tasks.size()) {
                ConfigManager.GUI.TASK task = tasks.get(i);
                ItemStack panel = getTaskPanel(player, task, i + 1);
                inventory.setItem(SLOTS_PANEL[i], panel);
            } else {
                ItemStack emptyPanel = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMetaUtils.applyItemMeta(emptyPanel, "§7Пусто", List.of("§8Нет задачи"));
                inventory.setItem(SLOTS_PANEL[i], emptyPanel);
            }
        }
    }

    private ItemStack getTaskPanel(Player player, ConfigManager.GUI.TASK task, int taskId) {
        Status taskStatus = mySqlStorage.getStatus(player.getName(), taskId);
        if (taskStatus == null) {
            taskStatus = Status.NOT_STARTED;
        }

        Material panelMaterial;

        switch (taskStatus) {
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

    public static void fillPanelSlots(Inventory inventory, ItemStack panel) {
        if (inventory == null || panel == null) return;
        for (int i = 0; i < SLOTS_GRAY_PANEL.length; i++) {
            inventory.setItem(SLOTS_GRAY_PANEL[i], panel);
        }
    }

}


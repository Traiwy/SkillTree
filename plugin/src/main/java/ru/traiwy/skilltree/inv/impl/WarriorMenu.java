package ru.traiwy.skilltree.inv.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.inv.AbstractMenu;
import ru.traiwy.skilltree.inv.context.WarriorMenuContext;
import ru.traiwy.skilltree.manager.ItemManager;
import ru.traiwy.skilltree.manager.PanelManager;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarriorMenu extends AbstractMenu<WarriorMenuContext> {
    PanelManager panelManager;
    ItemManager itemManager;
    JavaPlugin plugin;


    @Override
    protected int getSize() {
        return 54;
    }

    @Override
    protected Component getTitle(WarriorMenuContext context) {
        return Component.text("Путь война");
    }

    @Override
    protected void render(Player player, Inventory inventory, WarriorMenuContext context) {
        panelManager.setPanels(player, Skill.WARRIOR, inventory);

        panelManager.fillPanelSlots(
                inventory,
                new ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        );
        itemManager.parseHead(player).thenAccept(head ->
                Bukkit.getScheduler().runTask(plugin, () -> {
                    inventory.setItem(10, head);
                })
        );
    }

    @Override
    public void handleClick(Player player, InventoryClickEvent event, WarriorMenuContext context) {
        event.setCancelled(true);
        int slot = event.getRawSlot();

    }
}

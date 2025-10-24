package ru.traiwy.skilltree.service;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.inv.SkillMenu;
import ru.traiwy.skilltree.manager.ItemManager;
import ru.traiwy.skilltree.manager.PanelManager;


@AllArgsConstructor
public class GuiService implements Listener {
    private final PanelManager panelManager;
    private final ItemManager itemManager;

    @EventHandler
    public void on(InventoryClickEvent event) {
        if (event.getInventory().getHolder(false) instanceof SkillMenu skillMenu) {
            skillMenu.onClick(event);
        }

    }

    public void build(Player player, Skill skill, Inventory inventory){
        panelManager.setPanels(player, Skill.WARRIOR, inventory);
        panelManager.fillPanelSlots(inventory, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
         itemManager.parseHead(player).thenAccept(head -> {

             inventory.setItem(10, head);
             player.updateInventory();
         });
    }
}

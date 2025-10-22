package ru.traiwy.skilltree.inv;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.manager.ItemManager;
import ru.traiwy.skilltree.manager.PanelManager;
import ru.traiwy.skilltree.util.ItemMetaUtils;

import java.util.Collections;


@AllArgsConstructor
public class AlchemistMenu implements InventoryHolder, Listener {
    private final PanelManager panelManager;
    private final ItemManager itemManager;
    private final JavaPlugin plugin;

    private final Inventory inventory = Bukkit.createInventory(this, 54, "Путь алхимика");

    private void build(Player player) {
        panelManager.setPanels(player, Skill.ALCHEMIST, inventory);
        panelManager.fillPanelSlots(inventory, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemManager.parseHead(player).thenAccept(head -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventory.setItem(10, head);
                player.updateInventory();
            });
        });
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @EventHandler
    public void onClickInventoryPlayer(InventoryClickEvent event) {
        final Inventory inv = event.getClickedInventory();
        final ItemStack item = event.getCurrentItem();

        if (inv != null && item != null && inv.getHolder() instanceof WarriorMenu) {
          event.setCancelled(true);
      }

    }

    public void openInventory(Player player){
        build(player);
        player.openInventory(inventory);

    }
}

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
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.manager.ItemManager;
import ru.traiwy.skilltree.manager.PanelManager;


@AllArgsConstructor
public class WarriorMenu implements InventoryHolder, Listener {
    private final Inventory inventory = Bukkit.createInventory(this, 54, "Путь война");
    private PanelManager panelManager;
    private final ItemManager itemManager;
    private final JavaPlugin plugin;

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void build(Player player){
        panelManager.setPanels(player, Skill.WARRIOR, inventory);
        panelManager.fillPanelSlots(inventory, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
         itemManager.parseHead(player).thenAccept(head -> {
        Bukkit.getScheduler().runTask(plugin, () -> {
            inventory.setItem(10, head);
            player.updateInventory();
        });
    });
}



   @EventHandler
    public void onClickInventoryPlayer(InventoryClickEvent event) {
       final Player player = (Player) event.getWhoClicked();
       final Inventory inv = event.getInventory();
       final ItemStack item = event.getCurrentItem();


       if (inv == null &&
               inv.getHolder() == null &&
               !(inv.getHolder() instanceof WarriorMenu) &&
               (item == null || item.getType() == Material.AIR)) return;
       event.setCancelled(true);

   }

    public void openInventory(Player player) {
        build(player);
        player.openInventory(inventory);
    }
}

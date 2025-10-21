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
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.manager.PanelManager;

@AllArgsConstructor
public class FarmerMenu implements InventoryHolder, Listener {
    private final Inventory inventory = Bukkit.createInventory(this, 54, "Путь фермера");
    private PanelManager panelManager;
    private ConfigManager configManager;

    @Override
    public Inventory getInventory() {

        return inventory;
    }

    @EventHandler
    public void onClickInventoryPlayer(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final ItemStack item = event.getCurrentItem();

        if(inventory.getHolder() ==  this){
            event.setCancelled(true);
        }

        if(item.getType() ==  Material.GREEN_STAINED_GLASS_PANE){
            player.sendMessage("Вы получили награду");
        }


    }

    public void openInventory(Player player){
        player.openInventory(inventory);
        panelManager.setPanels(player, Skill.FARMER, inventory);
        panelManager.fillPanelSlots(inventory, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

    }
}

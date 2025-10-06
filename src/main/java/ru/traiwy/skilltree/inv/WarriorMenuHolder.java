package ru.traiwy.skilltree.inv;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import static ru.traiwy.skilltree.util.Utils.SLOTS_PANEL;

public class WarriorMenuHolder implements InventoryHolder, Listener {
    private final Inventory inventory = Bukkit.createInventory(this, 54, "Путь война");


    @Override
    public Inventory getInventory() {
        final ItemStack panel = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        for(int i = 0; i < SLOTS_PANEL.length; i++){
            inventory.setItem(SLOTS_PANEL[i], panel);
        }
        return inventory;
    }


    @EventHandler
    public void onClickInventoryPlayer(InventoryClickEvent event) {

    }
}

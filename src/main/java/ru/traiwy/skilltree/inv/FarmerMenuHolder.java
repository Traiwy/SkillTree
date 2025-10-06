package ru.traiwy.skilltree.inv;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.traiwy.skilltree.util.MenuManager;

public class FarmerMenuHolder implements MenuManager, InventoryHolder {

    @Override
    public Inventory getInventory() {
        final Inventory inventory = Bukkit.createInventory(this, 53, "Меню фермера");

        return inventory;
    }

    @Override
    public void onClickInventoryPlayer(InventoryClickEvent event) {

    }
}

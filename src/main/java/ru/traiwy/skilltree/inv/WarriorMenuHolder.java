package ru.traiwy.skilltree.inv;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.traiwy.skilltree.util.MenuManager;

public class WarriorMenuHolder implements MenuManager {
    @Override
    public Inventory getInventory() {
        final Inventory inventory = Bukkit.createInventory(new MenuManager(), 57, "Путь война");

        return inventory;
    }

    @Override
    public void onClickInventoryPlayer(InventoryClickEvent event) {

    }
}

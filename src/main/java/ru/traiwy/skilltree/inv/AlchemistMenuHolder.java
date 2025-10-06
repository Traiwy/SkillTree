package ru.traiwy.skilltree.inv;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.skilltree.util.MenuManager;

public class AlchemistMenuHolder implements MenuManager, InventoryHolder {
    @Override
    public Inventory getInventory() {
        return null;
    }

    @Override
    public void onClickInventoryPlayer(InventoryClickEvent event) {

    }
}

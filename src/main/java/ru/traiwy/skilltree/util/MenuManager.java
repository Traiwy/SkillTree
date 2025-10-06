package ru.traiwy.skilltree.util;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface MenuManager{
    Inventory getInventory();
    void onClickInventoryPlayer(InventoryClickEvent event);
}

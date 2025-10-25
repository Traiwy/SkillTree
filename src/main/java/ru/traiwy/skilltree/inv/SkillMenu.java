package ru.traiwy.skilltree.inv;


import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public interface SkillMenu extends InventoryHolder {
        void onClick(InventoryClickEvent event);
}

package ru.traiwy.skilltree.inv;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.traiwy.skilltree.enums.Skill;

public interface SkillMenu extends InventoryHolder {
        void onClick(InventoryClickEvent event);
}

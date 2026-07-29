package ru.traiwy.skilltree.inv;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.units.qual.C;

public interface Menu<C extends MenuContext> {
	void open(Player player, C context);
	void handleClick(Player player, InventoryClickEvent event, C context);

}

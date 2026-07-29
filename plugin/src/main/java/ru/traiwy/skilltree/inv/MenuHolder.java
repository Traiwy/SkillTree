package ru.traiwy.skilltree.inv;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class MenuHolder <C extends MenuContext> implements InventoryHolder {
	AbstractMenu<C> menu;
	C context;

	@Override
	public Inventory getInventory() {
		return null;
	}
}

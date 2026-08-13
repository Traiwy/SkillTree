package ru.traiwy.skilltree.inv;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;


@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class MenuHolder <C extends MenuContext> implements InventoryHolder {
	AbstractMenu<C> menu;
	C context;
	@Setter
	@NonFinal
	Inventory inventory;


	@Override
	public Inventory getInventory() {
		return inventory;
	}
}

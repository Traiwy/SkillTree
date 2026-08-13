package ru.traiwy.skilltree.inv;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class AbstractMenu<C extends MenuContext> implements Menu<C>{

	@Override
	public void open(Player player, C context) {
		MenuHolder<C> holder = new MenuHolder<>(this, context);

		Inventory inventory = Bukkit.createInventory(
				holder,
				getSize(),
				getTitle(context)
		);

		holder.setInventory(inventory);

		render(player, inventory, context);
		System.out.println(1);
		player.openInventory(inventory);
	}
	protected abstract int getSize();

	protected abstract String getTitle(C context);

	protected abstract void render(
			Player player,
			Inventory inventory,
			C context
	);

	@Override
	public abstract void handleClick(
			Player player,
			InventoryClickEvent event,
			C context
	);


}

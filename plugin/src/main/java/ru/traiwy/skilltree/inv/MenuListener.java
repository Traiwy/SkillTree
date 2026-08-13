package ru.traiwy.skilltree.inv;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public final class MenuListener implements Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player player)) {
			return;
		}

		if (!(event.getView().getTopInventory().getHolder() instanceof MenuHolder<?> holder)) {
			return;
		}

		callHandleClick(player, event, holder);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void callHandleClick(Player player, InventoryClickEvent event, MenuHolder holder) {
		holder.getMenu().handleClick(player, event, holder.getContext());
	}
}

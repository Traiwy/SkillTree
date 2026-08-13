package ru.traiwy.skilltree.inv.menu;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import ru.traiwy.skilltree.configuration.MenuConfiguration;
import ru.traiwy.skilltree.configuration.menu.Icon;
import ru.traiwy.skilltree.inv.AbstractMenu;
import ru.traiwy.skilltree.inv.MenuContext;

import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class MainMenu extends AbstractMenu {
	MenuConfiguration menuConfiguration;

	@Override
	protected int getSize() {
		return menuConfiguration.menus().getSize();
	}

	@Override
	protected String getTitle(MenuContext context) {
		return menuConfiguration.menus().getTitle();
	}

	@Override
	protected void render(Player player, Inventory inventory, MenuContext context) {
		Map<Integer, Icon> icons = menuConfiguration.menus().parserLayout();
		for(Map.Entry<Integer, Icon> icon : icons.entrySet()){
			inventory.setItem(icon.getKey(), icon.getValue().createItem());
		}

	}

	@Override
	public void handleClick(Player player, InventoryClickEvent event, MenuContext context) {
		event.setCancelled(true);

	}
}

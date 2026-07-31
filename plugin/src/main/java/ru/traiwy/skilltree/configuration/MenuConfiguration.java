package ru.traiwy.skilltree.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.traiwy.skilltree.configuration.menu.Menu;
import ru.traiwy.skilltree.configuration.menu.MenuSlot;


@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class MenuConfiguration implements MenuSlot {
	Menu menus = new Menu();
}

package ru.traiwy.skilltree.inv;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.traiwy.skilltree.configuration.MenuConfiguration;
import ru.traiwy.skilltree.inv.menu.MainMenu;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class MenuManager {
	MenuConfiguration configuration = new MenuConfiguration();
	MainMenu mainMenu = new MainMenu(configuration);
}

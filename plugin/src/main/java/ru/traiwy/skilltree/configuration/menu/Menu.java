package ru.traiwy.skilltree.configuration.menu;

import lombok.*;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Menu {
	@NonNull
	InventoryType type = InventoryType.CHEST;
	String title = "Меню";
	@NonNull List<String> layout = new ArrayList<>(List.of(
			"g_______g",
			"o_iiiii_o",
			"o__iii__o",
			"g_______g"
	));
	Map<String, Icon> icons = new HashMap<>(Map.of(
			"g", new Icon("minecraft:diamond")
	));


	public Map<Integer, Icon> parserLayout(){
		Map<Integer, Icon> result = new HashMap<>();

		for(int row = 0; row < layout.size(); row++){
			String line = layout.get(row);
			for(int colum = 0; colum < line.length(); colum++){
				char symbol = line.charAt(colum);
				if(symbol == '_') continue;

				String key = String.valueOf(symbol);
				if(!icons.containsKey(key)) continue;
				Icon icon = icons.get(key);
				if(icon == null) continue;
				int slot = row * 9 + colum;
				result.put(slot, icon);
			}
		}
		return result;
	}

	public int getSize(){
		int columns = layout.size();
		return columns * 9;
	}
}

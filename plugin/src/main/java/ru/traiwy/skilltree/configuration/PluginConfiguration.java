package ru.traiwy.skilltree.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class PluginConfiguration {
	@NonFinal
	MessageConfiguration message = new MessageConfiguration();
	@NonFinal
	MenuConfiguration menu = new MenuConfiguration();

	transient JavaPlugin plugin;
	transient JsonParser jsonParser;

	public PluginConfiguration(JavaPlugin plugin) {
		this.plugin = plugin;
		this.jsonParser = new JsonParser(plugin, this);
	}
}

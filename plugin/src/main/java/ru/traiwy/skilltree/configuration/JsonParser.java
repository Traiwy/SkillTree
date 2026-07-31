package ru.traiwy.skilltree.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class JsonParser {
	Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	JavaPlugin plugin;

	@NonNull
	PluginConfiguration configuration;

	@NonNull
	File file;

	public JsonParser(JavaPlugin plugin, @NonNull PluginConfiguration configuration) {
		this.plugin = plugin;
		this.configuration = configuration;
		this.file= new File(plugin.getDataFolder(), "config.json");
		createFile();
	}

	private void createFile() {
		File dataFolder = plugin.getDataFolder();

		if (!dataFolder.exists() && !dataFolder.mkdirs()) throw new IllegalStateException("Не удалось создать папку плагина: " + dataFolder.getAbsolutePath());


		if (file.exists()) {
			return;
		}

		try (FileWriter writer = new FileWriter(file)) {
			gson.toJson(configuration, writer);
		} catch (IOException exception) {
			throw new IllegalStateException(
					"Не удалось создать файл конфигурации: "
							+ file.getAbsolutePath(),
					exception
			);
		}
	}
}

package ru.traiwy.skilltree.configuration.menu;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public record Icon(@NonNull String type, Integer amount, Component name, Component[] lore, Boolean glow, String[] flags, Boolean unbreakable) {

	public Icon(@NonNull String type) {
		this(type, null, null, null, null, null, null);
	}

	public Material parseMaterial(){
		Material material = Material.matchMaterial(type);
		if(material == null)  throw new IllegalArgumentException("Not found material: " + type);

		return material;
	}

	public ItemStack createItem() {
		return new ItemStack(parseMaterial());
	}
}

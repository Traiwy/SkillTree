package ru.traiwy.skilltree.inv;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.manager.ItemManager;
import ru.traiwy.skilltree.manager.PanelManager;
import ru.traiwy.skilltree.util.ItemMetaUtils;

import java.util.Collections;



public class AlchemistMenu extends AbstractSkillMenu{

    public AlchemistMenu(PanelManager panelManager, ItemManager itemManager, JavaPlugin plugin) {
        super("Путь Алхимика", panelManager, itemManager, plugin);
    }
    public void open(Player player) {
        openInventory(player, Skill.ALCHEMIST);
    }
}

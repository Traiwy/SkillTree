package ru.traiwy.skilltree.inv;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.manager.ItemManager;
import ru.traiwy.skilltree.manager.PanelManager;

public abstract class AbstractSkillMenu implements SkillMenu {

    protected final Inventory inventory;
    protected final PanelManager panelManager;
    protected final ItemManager itemManager;
    protected final JavaPlugin plugin;

    public AbstractSkillMenu(String title, PanelManager panelManager, ItemManager itemManager, JavaPlugin plugin) {
        this.inventory = Bukkit.createInventory(this, 54, title);
        this.panelManager = panelManager;
        this.itemManager = itemManager;
        this.plugin = plugin;
    }

    protected void build(Player player, Skill skill) {
        panelManager.setPanels(player, skill, inventory);
        panelManager.fillPanelSlots(inventory, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        itemManager.parseHead(player).thenAccept(head -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventory.setItem(10, head);
                player.updateInventory();
            });
        });
    }

    public void openInventory(Player player, Skill skill) {
        build(player, skill);
        player.openInventory(inventory);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
package ru.traiwy.skilltree.inv;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import ru.traiwy.skilltree.util.Utils;

import static ru.traiwy.skilltree.util.Utils.SLOTS_PANEL;

public class AlchemistMenu implements InventoryHolder, Listener {
    private final Inventory inventory = Bukkit.createInventory(this, 54, "Путь алхимика");

    @Override
    public Inventory getInventory() {
        Utils.fillPanelSlots(inventory, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        return inventory;
    }


    @EventHandler
    public void onClickInventoryPlayer(InventoryClickEvent event) {

    }

}

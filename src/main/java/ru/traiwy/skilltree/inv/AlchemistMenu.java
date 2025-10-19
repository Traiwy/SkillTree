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
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.manager.PanelManager;
import ru.traiwy.skilltree.util.ItemMetaUtils;

import java.util.Collections;


@AllArgsConstructor
public class AlchemistMenu implements InventoryHolder, Listener {
    private final PanelManager panelManager;
    private final Inventory inventory = Bukkit.createInventory(this, 54, "Путь алхимика");

    private void setupInventory(Player player){
        inventory.close();

        

        final ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        final ItemStack paper = new ItemStack(Material.PAPER);

        ItemMetaUtils.applyItemMeta(
                playerHead,
                "Ваш никнейм: " + player.getName(),
                Collections.singletonList("Ваш класс: " + Skill.ALCHEMIST.name()));

        ItemMetaUtils.applyItemMeta(
                paper,
                "Ваши способности: ",
                Collections.singletonList("Создавать зелья")
        );

        inventory.setItem(10, playerHead);
        inventory.setItem(11, paper);

    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @EventHandler
    public void onClickInventoryPlayer(InventoryClickEvent event) {
         final Player player = (Player) event.getWhoClicked();
        final ItemStack item = event.getCurrentItem();

        if(inventory.getHolder() ==  this){
            event.setCancelled(true);
        }
        if(item.getType() ==  Material.GREEN_STAINED_GLASS_PANE){
            player.sendMessage("Вы получили награду");
        }
    }
    public void openInventory(Player player){
        setupInventory(player);
        panelManager.setPanels(player, Skill.ALCHEMIST, inventory);
        panelManager.fillPanelSlots(inventory, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        player.openInventory(inventory);


    }

}

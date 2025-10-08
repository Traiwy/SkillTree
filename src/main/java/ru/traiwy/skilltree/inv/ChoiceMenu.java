package ru.traiwy.skilltree.inv;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.traiwy.skilltree.storage.MySqlStorage;
import ru.traiwy.skilltree.util.ItemMetaUtils;
import ru.traiwy.skilltree.enums.Skill;

import java.util.Arrays;


@AllArgsConstructor
public class ChoiceMenu implements InventoryHolder, Listener {
    private WarriorMenu warriorMenu;
    private FarmerMenu farmerMenuHolder;
    private AlchemistMenu alchemistMenu;
    private MySqlStorage mySqlStorage;

    private static boolean isChoiceWarrior = false;
    private static boolean isChoiceAlchemist = false;
    private static boolean isChoiceFarmer = false;

    @Override
    public Inventory getInventory() {
        final Inventory inventory = Bukkit.createInventory(this, 27, ChatColor.DARK_BLUE +"Выберите класс");

        final ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, filler);
        }

        final ItemStack headWarrior = new ItemStack(Material.IRON_SWORD);
        final ItemStack headFarmer = new ItemStack(Material.WHEAT);
        final ItemStack headAlchemist = new ItemStack(Material.POTION);


        ItemMetaUtils.applyItemMeta(headWarrior, ChatColor.RED + "Путь воина", Arrays.asList(ChatColor.GRAY + "Стань мастером боя", ChatColor.GREEN + "Доступно"));
        ItemMetaUtils.applyItemMeta(headFarmer, ChatColor.GREEN + "Путь фермера", Arrays.asList(ChatColor.GRAY + "Выращивай урожай", ChatColor.GREEN + "Доступно"));
        ItemMetaUtils.applyItemMeta(headAlchemist,ChatColor.YELLOW + "Путь алхимика", Arrays.asList(ChatColor.GRAY + "Создавай зелья", ChatColor.RED + "Доступно"));

        inventory.setItem(11, headWarrior);
        inventory.setItem(13, headFarmer);
        inventory.setItem(15, headAlchemist);

        return inventory;
    }

    @EventHandler
    public void onClickInventoryPlayer(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inv = event.getClickedInventory();
        final ItemStack item = event.getCurrentItem();


        if(inv != null && item != null && inv.getHolder() instanceof ChoiceMenu){
            event.setCancelled(true);
            switch (item.getType()){
                case IRON_SWORD:
                        player.openInventory(warriorMenu.getInventory());
                        mySqlStorage.setSkill(player.getName(), Skill.WARRIOR);
                        isChoiceWarrior = true;
                        break;
                case WHEAT:
                    player.openInventory(farmerMenuHolder.getInventory());
                    mySqlStorage.setSkill(player.getName(), Skill.FARMER);
                    isChoiceWarrior = true;
                    break;

                case POTION:
                    player.openInventory(alchemistMenu.getInventory());
                    mySqlStorage.setSkill(player.getName(), Skill.ALCHEMIST);
                    isChoiceAlchemist = true;
                    break;
                default:
                    player.sendMessage("Выберите меню достижений");
            }
        }
    }

}

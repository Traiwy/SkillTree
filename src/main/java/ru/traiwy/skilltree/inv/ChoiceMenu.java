package ru.traiwy.skilltree.inv;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.data.PlayerData;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.storage.MySqlStorage;
import ru.traiwy.skilltree.util.ItemMetaUtils;
import ru.traiwy.skilltree.enums.Skill;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;


@AllArgsConstructor
public class ChoiceMenu implements InventoryHolder, Listener {
    final Inventory inventory = Bukkit.createInventory(this, 27, ChatColor.DARK_BLUE + "Выберите класс");

    private WarriorMenu warriorMenu;
    private FarmerMenu farmerMenuHolder;
    private AlchemistMenu alchemistMenu;
    private MySqlStorage mySqlStorage;
    private final ConfigManager configManager;
    private final JavaPlugin plugin;


    @Override
    public Inventory getInventory() {


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
        ItemMetaUtils.applyItemMeta(headAlchemist, ChatColor.YELLOW + "Путь алхимика", Arrays.asList(ChatColor.GRAY + "Создавай зелья", ChatColor.RED + "Доступно"));

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


        if (inv != null && item != null && inv.getHolder() instanceof ChoiceMenu) {
            event.setCancelled(true);
            Skill selectedClass = null;
            switch (item.getType()) {
                case IRON_SWORD:
                    player.openInventory(warriorMenu.getInventory());
                    selectedClass = Skill.WARRIOR;
                    break;
                case WHEAT:
                    player.openInventory(farmerMenuHolder.getInventory());
                    selectedClass = Skill.FARMER;
                    break;

                case POTION:
                    player.openInventory(alchemistMenu.getInventory());
                    selectedClass = Skill.ALCHEMIST;
                    break;
                default:
                    player.sendMessage("Выберите меню достижений");
            }
            if (selectedClass != null) {
                createPlayerWithTask(player, selectedClass);
            }
        }
    }

    private void createPlayerWithTask(Player player, Skill skill) {
        PlayerData newPlayer = new PlayerData(player.getName(), skill, 0);

        mySqlStorage.addPlayer(newPlayer);

        mySqlStorage.getPlayer(player.getName()).thenAccept(loadedPlayer -> {
            if (loadedPlayer != null) {
                ConfigManager.GUI.TASK taskConfig = configManager.getTasks(skill).get(0);
                String taskName = taskConfig.getName();
                Task task = new Task(0, loadedPlayer.getId(), taskName, Status.IN_PROGRESS);
                mySqlStorage.addTask(task);
            }
        });
    }

}

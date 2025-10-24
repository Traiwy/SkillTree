package ru.traiwy.skilltree.inv;

import lombok.NoArgsConstructor;
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
import ru.traiwy.skilltree.data.PlayerData;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.storage.MySqlStorage;
import ru.traiwy.skilltree.util.ItemMetaUtils;
import ru.traiwy.skilltree.enums.Skill;

import java.util.Arrays;


public class ChoiceMenu implements SkillMenu{
    final Inventory inventory = Bukkit.createInventory(this, 27, ChatColor.DARK_BLUE + "Выберите класс");

    private final WarriorMenu warriorMenu;
    private final FarmerMenu farmerMenuHolder;
    private final AlchemistMenu alchemistMenu;
    private final MySqlStorage mySqlStorage;
    private final ChallengeManager challengeManager;

    public ChoiceMenu(WarriorMenu warriorMenu, FarmerMenu farmerMenuHolder, AlchemistMenu alchemistMenu, MySqlStorage mySqlStorage, ChallengeManager challengeManager) {
        this.warriorMenu = warriorMenu;
        this.farmerMenuHolder = farmerMenuHolder;
        this.alchemistMenu = alchemistMenu;
        this.mySqlStorage = mySqlStorage;
        this.challengeManager = challengeManager;

        setupInventory();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setupInventory(){
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

    }

    public void openInventory(Player player){
        player.openInventory(inventory);
    }


    @Override
    public void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final ItemStack item = event.getCurrentItem();

        event.setCancelled(true);

        switch (item.getType()) {
            case IRON_SWORD -> {
                challengeManager.giveFirstChallengeToPlayer(player, "warrior", Skill.WARRIOR);
                mySqlStorage.updatePlayer(new PlayerData(player.getName(), Skill.WARRIOR, 0));
                warriorMenu.open(player);
            }
            case WHEAT -> {
                challengeManager.giveFirstChallengeToPlayer(player, "farmer", Skill.FARMER);
                mySqlStorage.updatePlayer(new PlayerData(player.getName(), Skill.FARMER, 0));
                farmerMenuHolder.open(player);
            }
            case POTION -> {
                challengeManager.giveFirstChallengeToPlayer(player, "alchemist", Skill.ALCHEMIST);
                mySqlStorage.updatePlayer(new PlayerData(player.getName(), Skill.ALCHEMIST, 0));
                alchemistMenu.open(player);
            }
        }
    }
}




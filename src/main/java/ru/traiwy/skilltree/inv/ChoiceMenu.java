package ru.traiwy.skilltree.inv;

import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import org.bukkit.entity.Item;
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
import java.util.List;
import java.util.Map;


public class ChoiceMenu implements SkillMenu{
    public record ClassData(String prefix, Skill skill, SkillMenu skillMenu, ItemStack icon) { }

    private final Map<Material, ClassData> classDataMap;

    final Inventory inventory = Bukkit.createInventory(this, 27, ChatColor.DARK_BLUE + "Выберите класс");

    private final WarriorMenu warriorMenu;
    private final FarmerMenu farmerMenuHolder;
    private final AlchemistMenu alchemistMenu;
    private final MySqlStorage mySqlStorage;
    private final ChallengeManager challengeManager;



    public ChoiceMenu(WarriorMenu warriorMenu,
                      FarmerMenu farmerMenuHolder,
                      AlchemistMenu alchemistMenu,
                      MySqlStorage mySqlStorage,
                      ChallengeManager challengeManager) {
        this.warriorMenu = warriorMenu;
        this.farmerMenuHolder = farmerMenuHolder;
        this.alchemistMenu = alchemistMenu;
        this.mySqlStorage = mySqlStorage;
        this.challengeManager = challengeManager;

        this.classDataMap = Map.of(
                Material.IRON_SWORD, new ClassData("warrior", Skill.WARRIOR, warriorMenu, new ItemStack(Material.IRON_SWORD)),
                Material.WHEAT, new ClassData("farmer", Skill.FARMER, farmerMenuHolder, new ItemStack(Material.WHEAT)),
                Material.POTION, new ClassData("alchemist", Skill.ALCHEMIST, alchemistMenu, new ItemStack(Material.POTION))
        );
         ItemStack warriorIcon = ItemMetaUtils.applyItemMeta(
                new ItemStack(Material.IRON_SWORD),
                ChatColor.RED + "Путь воина",
                List.of(ChatColor.GRAY + "Стань мастером боя", ChatColor.GREEN + "Доступно")
        );
         ItemStack farmerIcon = ItemMetaUtils.applyItemMeta(
                 new ItemStack(Material.WHEAT),
                 ChatColor.GREEN + "Путь фермера",
                 List.of(ChatColor.GRAY + "Выращивай урожай", ChatColor.GREEN + "Доступно"));

         ItemStack alchemistIcon = ItemMetaUtils.applyItemMeta(
                 new ItemStack(Material.POTION),
                 ChatColor.YELLOW + "Путь алхимика",
                 List.of(ChatColor.GRAY + "Создавай зелья", ChatColor.RED + "Доступно"));


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

        inventory.setItem(11, classDataMap.get(Material.IRON_SWORD).icon());
        inventory.setItem(13, classDataMap.get(Material.WHEAT).icon);
        inventory.setItem(15, classDataMap.get(Material.POTION).icon);

    }


    @Override
    public void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final ItemStack item = event.getCurrentItem();

        ClassData data = classDataMap.get(item.getType());
        if (data != null) {
            challengeManager.giveAllChallengesToPlayer(player, data.prefix, data.skill);
            mySqlStorage.updatePlayer(new PlayerData(player.getName(), data.skill(), 0));
            data.skillMenu.open(player);
        }
    }

    @Override
    public void open(Player player) {
        player.openInventory(inventory);
    }
}




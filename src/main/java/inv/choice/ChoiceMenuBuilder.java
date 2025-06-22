package inv.choice;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class ChoiceMenuBuilder {
     public Inventory getChoiceMenu(){
        var inv = Bukkit.createInventory(new ChoiceMenuInvholder(), 27, ChatColor.DARK_BLUE +"Выберите класс");

        //Стекла
       // Фоновое стекло
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(ChatColor.RESET + "");
        filler.setItemMeta(fillerMeta);
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, filler);
        }

        // Голова воина
        ItemStack headWarrior = new ItemStack(Material.IRON_SWORD);
        ItemMeta  metaWarrior = headWarrior.getItemMeta();
        metaWarrior.setDisplayName(ChatColor.RED + "Путь воина");
        metaWarrior.setLore(Arrays.asList(ChatColor.GRAY + "Стань мастером боя", ChatColor.GREEN + "Доступно"));
        headWarrior.setItemMeta(metaWarrior);
        inv.setItem(11, headWarrior);

        // Голова фермера
        ItemStack headFarmer = new ItemStack(Material.WHEAT);
        ItemMeta metaFarmer = headFarmer.getItemMeta();
        metaFarmer.setDisplayName(ChatColor.GREEN + "Путь фермера");
        metaFarmer.setLore(Arrays.asList(ChatColor.GRAY + "Выращивай урожай", ChatColor.GREEN + "Доступно"));
        headFarmer.setItemMeta(metaFarmer);
        inv.setItem(13, headFarmer);

        // Голова алхимика
        ItemStack headAlchemist = new ItemStack(Material.POTION);
        ItemMeta metaAlchemist = headAlchemist.getItemMeta();
        metaAlchemist.setDisplayName(ChatColor.YELLOW + "Путь алхимика");
        metaAlchemist.setLore(Arrays.asList(ChatColor.GRAY + "Создавай зелья", ChatColor.RED + "Заблокировано"));
        headAlchemist.setItemMeta(metaAlchemist);
        inv.setItem(15, headAlchemist);

         return inv;

    }
}

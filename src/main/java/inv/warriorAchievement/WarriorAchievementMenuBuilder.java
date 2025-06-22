package inv.warriorAchievement;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WarriorAchievementMenuBuilder {
    public static void getWarriorMenu(Player player){
        var inv = Bukkit.createInventory(new WarriorAchievementMenuInvholder(), 57, "Путь война");


    }
}

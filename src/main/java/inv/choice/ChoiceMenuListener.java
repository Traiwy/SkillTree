package inv.choice;

import inv.alchemistAchievement.AlchemistAchievementMenuBuilder;
import inv.farmerAchievement.FarmerAchievementMenuBuilder;
import inv.warriorAchievement.WarriorAchievementMenuBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ChoiceMenuListener implements Listener {
    @EventHandler
    public void onClickInventoryPlayer(InventoryClickEvent e){
        var player = (Player) e.getWhoClicked();
        var inv = e.getClickedInventory();
        var item = e.getCurrentItem();

        if(inv != null && item != null && inv.getHolder() instanceof ChoiceMenuInvholder){
            e.setCancelled(true);
            switch (item.getType()){
                case IRON_SWORD -> WarriorAchievementMenuBuilder.getWarriorMenu(player);
                case WHEAT -> FarmerAchievementMenuBuilder.getFarmerMenu(player);
                case POTION -> AlchemistAchievementMenuBuilder.getAlchemistMenu(player);
                default -> player.sendMessage("Выберите меню достижений");
            }
        }
    }
}

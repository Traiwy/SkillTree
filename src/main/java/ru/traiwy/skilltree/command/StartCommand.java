package ru.traiwy.skilltree.command;

import lombok.AllArgsConstructor;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.inv.AlchemistMenu;
import ru.traiwy.skilltree.inv.ChoiceMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.skilltree.inv.FarmerMenu;
import ru.traiwy.skilltree.inv.WarriorMenu;
import ru.traiwy.skilltree.storage.MySqlStorage;
import ru.traiwy.skilltree.enums.Skill;

@AllArgsConstructor
public class StartCommand implements CommandExecutor {
    private final MySqlStorage mySqlStorage;
    private final ChoiceMenu choiceMenu;
    private final WarriorMenu warriorMenu;
    private final FarmerMenu farmerMenuHolder;
    private final AlchemistMenu alchemistMenu;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
         if(!(sender instanceof Player player)) return true;

         if(mySqlStorage.isChecked(player.getName())){
             Skill skill = mySqlStorage.getSkill(player.getName());
             if(skill == Skill.WARRIOR){
                    warriorMenu.openInventory(player);
                    mySqlStorage.updateTask(player.getName(), 1, Status.IN_PROGRESS);
                } else if(skill == Skill.FARMER){
                    farmerMenuHolder.openInventory(player);
                    mySqlStorage.updateTask(player.getName(), 1, Status.IN_PROGRESS);
                } else if(skill == Skill.ALCHEMIST){
                    alchemistMenu.openInventory(player);
                    mySqlStorage.updateTask(player.getName(), 1, Status.IN_PROGRESS);
             }
         }else{
              player.openInventory(choiceMenu.getInventory());
         }

         return true;
    }
}

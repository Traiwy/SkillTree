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
        if (!(sender instanceof Player player)) return true;

        try {
            if (mySqlStorage.isChecked(player.getName())) {
                Skill skill = mySqlStorage.getSkill(player.getName());


                if (mySqlStorage.getStatus(player.getName(), 1) == Status.NOT_STARTED) {
                    mySqlStorage.updateTask(player.getName(), 1, Status.IN_PROGRESS);
                }
                activateNextTask(player, 1, 2);
                activateNextTask(player, 2, 3);
                activateNextTask(player, 3, 4);
                activateNextTask(player, 5, 6);
                activateNextTask(player, 6, 7);
                activateNextTask(player, 7, 8);
                activateNextTask(player, 8, 9);

                switch (skill) {
                    case WARRIOR -> warriorMenu.openInventory(player);
                    case FARMER -> farmerMenuHolder.openInventory(player);
                    case ALCHEMIST -> alchemistMenu.openInventory(player);
                }
            } else {
                player.openInventory(choiceMenu.getInventory());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private void activateNextTask(Player player, int lastTask, int nextTask) {
        Status lastTaskStatus = mySqlStorage.getStatus(player.getName(), lastTask);
        Status nextTaskStatus = mySqlStorage.getStatus(player.getName(), nextTask);


        if (lastTaskStatus == Status.COMPLETED && nextTaskStatus == Status.NOT_STARTED) {
            mySqlStorage.updateTask(player.getName(), nextTask, Status.IN_PROGRESS);
        }
    }
}

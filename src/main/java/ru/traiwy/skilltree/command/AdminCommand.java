package ru.traiwy.skilltree.command;

import lombok.AllArgsConstructor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.traiwy.skilltree.SkillTree;
import ru.traiwy.skilltree.inv.ChoiceMenu;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.List;

@AllArgsConstructor
public class AdminCommand implements CommandExecutor, TabCompleter {
    private final MySqlStorage mySqlStorage;
    private final ChoiceMenu choiceMenu;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //if(!(sender instanceof Player p && !sender.hasPermission("skilltree.admin"))) return false;
        Player p = (Player) sender;

        if(args.length == 0){
            choiceMenu.getInventory();
            p.sendMessage("§cИспользуйте: /skilltree <info|addtask>");
            return true;
        }

        String subcommand = args[0];
        switch (subcommand){
            case "info":
                showInfoPlayer(p);
                break;
            case "addtask":
                if(args.length != 3){
                    p.sendMessage("§cИспользуйте: /skilltree addtask <игрок> <номер задания>");
                    return true;
                }
                String targetName = args[1];
                int taskNumber = Integer.parseInt(args[2]);
                if(taskNumber > 9) {
                    p.sendMessage("§cНомер задания не может быть больше 9.");
                    return true;
                }
            case "start":
                choiceMenu.getInventory();
                p.openInventory(choiceMenu.getInventory());
                break;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

    public void showInfoPlayer(Player player){

    }
}

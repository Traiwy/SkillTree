package ru.traiwy.skilltree.command;

import lombok.AllArgsConstructor;
import ru.traiwy.skilltree.inv.ChoiceMenuHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class StartMenuCommand implements CommandExecutor {
    private final ChoiceMenuHolder choiceMenuHolder;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
         if(!(sender instanceof Player player)) return true;
         player.openInventory(choiceMenuHolder.getInventory());
         return true;
    }
}

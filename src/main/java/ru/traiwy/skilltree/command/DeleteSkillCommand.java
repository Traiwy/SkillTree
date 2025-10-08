package ru.traiwy.skilltree.command;

import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.skilltree.storage.MySqlStorage;

@AllArgsConstructor
public class DeleteSkillCommand implements CommandExecutor {
    private final MySqlStorage mySqlStorage;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) return false;

        mySqlStorage.deleteSkill(player.getName());
        return true;
    }
}

package command;

import inv.choice.ChoiceMenuBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartMenuCommand implements CommandExecutor {
    private final ChoiceMenuBuilder choiceMenuBuilder;
    public StartMenuCommand(ChoiceMenuBuilder choiceMenuBuilder){
        this.choiceMenuBuilder = choiceMenuBuilder;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
         if(!(sender instanceof Player)) return true;
         var player = (Player) sender;
         player.openInventory(choiceMenuBuilder.getChoiceMenu());
         return true;
    }
}

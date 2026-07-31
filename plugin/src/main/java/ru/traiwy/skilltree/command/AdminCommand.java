package ru.traiwy.skilltree.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.traiwy.skilltree.configuration.MessageConfiguration;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.inv.impl.AlchemistMenu;
import ru.traiwy.skilltree.inv.impl.ChoiceMenu;
import ru.traiwy.skilltree.inv.impl.FarmerMenu;
import ru.traiwy.skilltree.inv.impl.WarriorMenu;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class AdminCommand implements CommandExecutor, TabCompleter {
    JavaPlugin plugin;
    MySqlStorage mySqlStorage;
    ChoiceMenu choiceMenu;
    WarriorMenu warriorMenu;
    FarmerMenu farmerMenu;
    AlchemistMenu alchemistMenu;

    MessageConfiguration message;

    private final String[] SUBCOMMAND = {"info", "addtask", "start"};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Commands.literal("skill")
                .executes(ctx ->{
                    Player player = (Player)ctx.getSource();
                    player.sendMessage(message.commandNotFound());
                    return 1;
                })
                .then(Commands.literal("info")
                        .executes(ctx -> {
                            showInfoPlayer((Player)ctx.getSource());
                            return 1;
                        })
                ).then(Commands.literal("addtask")
                        .executes(ctx -> {
                            return 1;
                        })
                        .then(Commands.argument("numberTask", IntegerArgumentType.integer(1, 9))
                                .executes(ctx ->{
                                    int taskNumber = ctx.getArgument("numberTask", int.class);
                                    if(taskNumber > 9) {

                                    }
                                    return 1;
                                }))
                ).then(Commands.literal("start")
                        .executes(ctx -> {
                            menuCommandExecutor((Player) ctx.getSource());
                            return 1;
                        }));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 1){
           String current = args[0].toLowerCase();

           return Arrays.stream(SUBCOMMAND)
                   .filter(k -> k.startsWith(current))
                   .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public void showInfoPlayer(Player player) {

    }

    private void menuCommandExecutor(Player p) {
        mySqlStorage.getPlayer(p.getName()).thenAccept(playerData -> {
            if (playerData != null && playerData.getSkill() != null) {
                p.getScheduler().run(plugin, task -> {
                    Skill skill = playerData.getSkill();
                    switch (skill) {
                       //case WARRIOR -> warriorMenu.openInventory(p);
                       //case FARMER -> farmerMenu.openInventory(p);
                       //case ALCHEMIST -> alchemistMenu.openInventory(p);
                       //case SOME_DEFAULT -> choiceMenu.openInventory(p);
                        default -> p.sendMessage("Некорректно выбран класс.");
                    }
                }, null);
            } else {
                p.getScheduler().run(plugin, task -> {
                    p.openInventory(choiceMenu.getInventory());
                    p.sendMessage("§aВыберите свой класс!");
                }, null);
            }
        });
    }
}

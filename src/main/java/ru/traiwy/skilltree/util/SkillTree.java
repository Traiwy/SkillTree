package ru.traiwy.skilltree.util;

import ru.traiwy.skilltree.command.StartMenuCommand;
import ru.traiwy.skilltree.inv.AlchemistMenuHolder;
import ru.traiwy.skilltree.inv.ChoiceMenuHolder;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.inv.choice.ChoiceMenuListener;
import ru.traiwy.skilltree.inv.FarmerMenuHolder;
import ru.traiwy.skilltree.inv.WarriorMenuHolder;

public final class SkillTree extends JavaPlugin {

    @Override
    public void onEnable() {
        final ConfigManager configManager = new ConfigManager(this, "config.yml");
        final ChoiceMenuHolder choiceMenuHolder = new ChoiceMenuHolder();

        final WarriorMenuHolder warriorMenuHolder = new WarriorMenuHolder();
        final FarmerMenuHolder farmerMenuHolder = new FarmerMenuHolder();
        final AlchemistMenuHolder alchemistMenuHolder = new AlchemistMenuHolder();


        getCommand("start").setExecutor(new StartMenuCommand(choiceMenuHolder));
        getServer().getPluginManager().registerEvents(new ChoiceMenuListener(warriorMenuHolder, farmerMenuHolder, alchemistMenuHolder), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package ru.traiwy.skilltree.util;

import org.bukkit.event.Event;
import ru.traiwy.skilltree.command.StartMenuCommand;
import ru.traiwy.skilltree.inv.AlchemistMenuHolder;
import ru.traiwy.skilltree.inv.ChoiceMenuHolder;
import org.bukkit.plugin.java.JavaPlugin;

import ru.traiwy.skilltree.inv.FarmerMenuHolder;
import ru.traiwy.skilltree.inv.WarriorMenuHolder;

import javax.swing.event.MenuListener;

public final class SkillTree extends JavaPlugin {

    @Override
    public void onEnable() {
        final ConfigManager configManager = new ConfigManager(this, getConfig());
        configManager.load(getConfig());


        final WarriorMenuHolder warriorMenuHolder = new WarriorMenuHolder();
        final FarmerMenuHolder farmerMenuHolder = new FarmerMenuHolder();
        final AlchemistMenuHolder alchemistMenuHolder = new AlchemistMenuHolder();

        final ChoiceMenuHolder choiceMenu = new ChoiceMenuHolder(warriorMenuHolder, farmerMenuHolder, alchemistMenuHolder);
        getCommand("start").setExecutor(new StartMenuCommand(choiceMenu));
        getServer().getPluginManager().registerEvents(choiceMenu, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

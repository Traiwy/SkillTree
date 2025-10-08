package ru.traiwy.skilltree.util;

import ru.traiwy.skilltree.command.DeleteSkillCommand;
import ru.traiwy.skilltree.command.StartCommand;
import ru.traiwy.skilltree.inv.AlchemistMenu;
import ru.traiwy.skilltree.inv.ChoiceMenu;
import org.bukkit.plugin.java.JavaPlugin;

import ru.traiwy.skilltree.inv.FarmerMenu;
import ru.traiwy.skilltree.inv.WarriorMenu;
import ru.traiwy.skilltree.storage.MySqlStorage;

public final class SkillTree extends JavaPlugin {

    @Override
    public void onEnable() {
        final ConfigManager configManager = new ConfigManager(this, getConfig());
        configManager.load(getConfig());
        final MySqlStorage mySqlStorage = new MySqlStorage();


        final WarriorMenu warriorMenu = new WarriorMenu();
        final FarmerMenu farmerMenuHolder = new FarmerMenu();
        final AlchemistMenu alchemistMenu = new AlchemistMenu();

        final ChoiceMenu choiceMenu = new ChoiceMenu(
                warriorMenu,
                farmerMenuHolder,
                alchemistMenu,
                mySqlStorage);
        getCommand("start").setExecutor(new StartCommand(mySqlStorage, choiceMenu, warriorMenu, farmerMenuHolder, alchemistMenu));
        getCommand("delete").setExecutor(new DeleteSkillCommand(mySqlStorage));
        getServer().getPluginManager().registerEvents(choiceMenu, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

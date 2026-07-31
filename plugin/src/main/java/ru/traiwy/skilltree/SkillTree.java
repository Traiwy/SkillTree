package ru.traiwy.skilltree;

import ru.traiwy.skilltree.command.AdminCommand;
import ru.traiwy.skilltree.configuration.PluginConfiguration;
import ru.traiwy.skilltree.inv.impl.AlchemistMenu;
import ru.traiwy.skilltree.inv.impl.ChoiceMenu;
import org.bukkit.plugin.java.JavaPlugin;

import ru.traiwy.skilltree.inv.impl.FarmerMenu;
import ru.traiwy.skilltree.inv.impl.WarriorMenu;
import ru.traiwy.skilltree.manager.*;
import ru.traiwy.skilltree.storage.MySqlStorage;

public final class SkillTree extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        final MySqlStorage mySqlStorage = new MySqlStorage(this);
        mySqlStorage.initDatabase();
        final ItemManager itemManager = new ItemManager(mySqlStorage, this);
        final EventManager eventManager = new EventManager( mySqlStorage);

        final WarriorMenu warriorMenu = new WarriorMenu( itemManager, this);
        final FarmerMenu farmerMenuHolder = new FarmerMenu( itemManager, this);
        final AlchemistMenu alchemistMenu = new AlchemistMenu( itemManager, this);
        final PluginConfiguration pluginConfiguration = new PluginConfiguration(this);


        final ChoiceMenu choiceMenu = new ChoiceMenu(
                warriorMenu,
                farmerMenuHolder,
                alchemistMenu,
                mySqlStorage);
        getCommand("skilltree").setExecutor(new AdminCommand(this, mySqlStorage, choiceMenu, warriorMenu, farmerMenuHolder, alchemistMenu, pluginConfiguration.getMessage()));
        getServer().getPluginManager().registerEvents(choiceMenu, this);
    }

}

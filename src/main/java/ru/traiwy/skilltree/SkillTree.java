package ru.traiwy.skilltree;

import ru.traiwy.skilltree.command.DeleteCommand;
import ru.traiwy.skilltree.command.StartCommand;
import ru.traiwy.skilltree.event.MobKillListener;
import ru.traiwy.skilltree.event.RaidFinishListener;
import ru.traiwy.skilltree.inv.AlchemistMenu;
import ru.traiwy.skilltree.inv.ChoiceMenu;
import org.bukkit.plugin.java.JavaPlugin;

import ru.traiwy.skilltree.inv.FarmerMenu;
import ru.traiwy.skilltree.inv.WarriorMenu;
import ru.traiwy.skilltree.storage.MySqlStorage;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.manager.PanelManager;

public final class SkillTree extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("bd.yml", false);
        final ConfigManager configManager = new ConfigManager(this, getConfig());
        configManager.load(getConfig());
        final MySqlStorage mySqlStorage = new MySqlStorage();
        mySqlStorage.createTable();
        final PanelManager panelManager = new PanelManager(configManager, mySqlStorage);

        final WarriorMenu warriorMenu = new WarriorMenu(panelManager);
        final FarmerMenu farmerMenuHolder = new FarmerMenu(panelManager);
        final AlchemistMenu alchemistMenu = new AlchemistMenu(panelManager);


        final ChoiceMenu choiceMenu = new ChoiceMenu(
                warriorMenu,
                farmerMenuHolder,
                alchemistMenu,
                mySqlStorage);
        getCommand("start").setExecutor(new StartCommand(mySqlStorage, choiceMenu, warriorMenu, farmerMenuHolder, alchemistMenu));
        getCommand("delete").setExecutor(new DeleteCommand(mySqlStorage));
        getServer().getPluginManager().registerEvents(choiceMenu, this);
        getServer().getPluginManager().registerEvents(new MobKillListener(mySqlStorage), this);
        getServer().getPluginManager().registerEvents(new RaidFinishListener(mySqlStorage), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

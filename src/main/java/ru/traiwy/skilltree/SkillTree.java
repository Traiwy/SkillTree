package ru.traiwy.skilltree;

import org.bukkit.configuration.file.FileConfiguration;
import ru.traiwy.skilltree.command.AdminCommand;
import ru.traiwy.skilltree.event.*;
import ru.traiwy.skilltree.inv.AlchemistMenu;
import ru.traiwy.skilltree.inv.ChoiceMenu;
import org.bukkit.plugin.java.JavaPlugin;

import ru.traiwy.skilltree.inv.FarmerMenu;
import ru.traiwy.skilltree.inv.WarriorMenu;
import ru.traiwy.skilltree.manager.*;
import ru.traiwy.skilltree.storage.MySqlStorage;

public final class SkillTree extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        saveResource("bd.yml", false);
        final ConfigManager configManager = new ConfigManager(this);
        configManager.load(getConfig());
        final MySqlStorage mySqlStorage = new MySqlStorage();
        mySqlStorage.initDatabase();


        final ChallengeManager challengeManager = new ChallengeManager(configManager, mySqlStorage);
        final PanelManager panelManager = new PanelManager(configManager, mySqlStorage, challengeManager);
        final ItemManager itemManager = new ItemManager(mySqlStorage, configManager, this);
        final EventManager eventManager = new EventManager(challengeManager, mySqlStorage);
        panelManager.initializeSkillTasks();

        final WarriorMenu warriorMenu = new WarriorMenu(panelManager, itemManager, this);
        final FarmerMenu farmerMenuHolder = new FarmerMenu(panelManager, configManager);
        final AlchemistMenu alchemistMenu = new AlchemistMenu(panelManager);

        final ChoiceMenu choiceMenu = new ChoiceMenu(
                warriorMenu,
                farmerMenuHolder,
                alchemistMenu,
                mySqlStorage,
                challengeManager);
        getCommand("skilltree").setExecutor(new AdminCommand(this, mySqlStorage, choiceMenu, warriorMenu, farmerMenuHolder, alchemistMenu));
        getServer().getPluginManager().registerEvents(choiceMenu, this);
        getServer().getPluginManager().registerEvents(new MobKillEvent(mySqlStorage, configManager, challengeManager, eventManager), this);
        getServer().getPluginManager().registerEvents(new BlocksBreakEvent(challengeManager, mySqlStorage, this,eventManager), this);
        getServer().getPluginManager().registerEvents(new PlayersJoinEvent(mySqlStorage), this);
        getServer().getPluginManager().registerEvents(new BlockHitEvent(challengeManager, mySqlStorage, this, eventManager), this);
        getServer().getPluginManager().registerEvents(new LavaDamageEvent(mySqlStorage, challengeManager, this, eventManager), this);

    }



}

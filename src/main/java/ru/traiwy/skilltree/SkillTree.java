package ru.traiwy.skilltree;

import ru.traiwy.skilltree.command.AdminCommand;
import ru.traiwy.skilltree.event.*;
import ru.traiwy.skilltree.inv.AlchemistMenu;
import ru.traiwy.skilltree.inv.ChoiceMenu;
import org.bukkit.plugin.java.JavaPlugin;

import ru.traiwy.skilltree.inv.FarmerMenu;
import ru.traiwy.skilltree.inv.WarriorMenu;
import ru.traiwy.skilltree.manager.*;
import ru.traiwy.skilltree.service.GuiService;
import ru.traiwy.skilltree.session.PlayerSession;
import ru.traiwy.skilltree.storage.MySqlStorage;

public final class SkillTree extends JavaPlugin {
    private MySqlStorage mySqlStorage;
    private PlayerSession playerSession;

    private ConfigManager configManager;
    private ChallengeManager challengeManager;
    private PanelManager panelManager;
    private ItemManager itemManager;
    private EventManager eventManager;

    @Override
    public void onEnable() {
        setupConfig();


        mySqlStorage = new MySqlStorage(this);
        mySqlStorage.initDatabase();

        playerSession = new PlayerSession(mySqlStorage);


        challengeManager = new ChallengeManager(configManager, mySqlStorage);
        panelManager = new PanelManager(configManager, mySqlStorage, challengeManager);
        itemManager = new ItemManager(mySqlStorage, configManager, this);
        eventManager = new EventManager(challengeManager, mySqlStorage);

        panelManager.initializeSkillTasks();

        WarriorMenu warriorMenu = new WarriorMenu(panelManager, itemManager, this);
        FarmerMenu farmerMenu = new FarmerMenu(panelManager, itemManager, this);
        AlchemistMenu alchemistMenu = new AlchemistMenu(panelManager, itemManager, this);

        ChoiceMenu choiceMenu = new ChoiceMenu(
                warriorMenu,
                farmerMenu,
                alchemistMenu,
                mySqlStorage,
                challengeManager
        );

        registerCommands(choiceMenu, warriorMenu, farmerMenu, alchemistMenu);


        registerEvents(
                choiceMenu,
                warriorMenu,
                farmerMenu
        );
    }

    @Override
    public void onDisable() {
        if (mySqlStorage != null) {
            mySqlStorage.shutdown();
        }
    }

    private void setupConfig() {
        saveDefaultConfig();
        reloadConfig();
        saveResource("bd.yml", false);

        configManager = new ConfigManager(this);
        configManager.load(getConfig());
    }

    private void registerCommands(ChoiceMenu choiceMenu,
                                  WarriorMenu warriorMenu,
                                  FarmerMenu farmerMenu,
                                  AlchemistMenu alchemistMenu) {
        getCommand("skilltree").setExecutor(
                new AdminCommand(this, mySqlStorage, choiceMenu, warriorMenu, farmerMenu, alchemistMenu)
        );
    }

    private void registerEvents(ChoiceMenu choiceMenu,
                                WarriorMenu warriorMenu,
                                FarmerMenu farmerMenu) {

        getServer().getPluginManager().registerEvents(new MobKillEvent(mySqlStorage, configManager, challengeManager, eventManager), this);
        getServer().getPluginManager().registerEvents(new BlocksBreakEvent(challengeManager, mySqlStorage, this, eventManager, new ItemBreakEvent(challengeManager, mySqlStorage, this, eventManager)), this);
        getServer().getPluginManager().registerEvents(new PlayersSessionEvent(playerSession), this);

        getServer().getPluginManager().registerEvents(new BlockHitEvent(challengeManager, mySqlStorage, this, eventManager), this);
        getServer().getPluginManager().registerEvents(new LavaDamageEvent(mySqlStorage, challengeManager, this, eventManager), this);
        getServer().getPluginManager().registerEvents(new PotionDrinkEvent(mySqlStorage, eventManager, challengeManager), this);
        getServer().getPluginManager().registerEvents(new PotionDamageEvent(eventManager, this, mySqlStorage, challengeManager), this);
        getServer().getPluginManager().registerEvents(new ComboPotionDrinkEvent(mySqlStorage, eventManager, challengeManager), this);
        getServer().getPluginManager().registerEvents(new PotionDamageEntityEvent(mySqlStorage, eventManager, challengeManager), this);

        getServer().getPluginManager().registerEvents(new GuiService(panelManager, itemManager), this);
    }
}
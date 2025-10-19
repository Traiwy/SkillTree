package ru.traiwy.skilltree;

import ru.traiwy.skilltree.command.AdminCommand;
import ru.traiwy.skilltree.event.MobKillEvent;
import ru.traiwy.skilltree.event.PlayersJoinEvent;
import ru.traiwy.skilltree.event.RaidFinishListener;
import ru.traiwy.skilltree.inv.AlchemistMenu;
import ru.traiwy.skilltree.inv.ChoiceMenu;
import org.bukkit.plugin.java.JavaPlugin;

import ru.traiwy.skilltree.inv.FarmerMenu;
import ru.traiwy.skilltree.inv.WarriorMenu;
import ru.traiwy.skilltree.manager.ChallengeManager;
import ru.traiwy.skilltree.storage.MySqlStorage;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.manager.PanelManager;

public final class SkillTree extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("bd.yml", false);
        final ConfigManager configManager = new ConfigManager(this);
        configManager.load(getConfig());
        final MySqlStorage mySqlStorage = new MySqlStorage();
        mySqlStorage.initDatabase();
        final ChallengeManager challengeManager = new ChallengeManager(configManager);
        final PanelManager panelManager = new PanelManager(configManager, mySqlStorage, challengeManager);
        panelManager.initializeSkillTasks();

        final WarriorMenu warriorMenu = new WarriorMenu(panelManager);
        final FarmerMenu farmerMenuHolder = new FarmerMenu(panelManager);
        final AlchemistMenu alchemistMenu = new AlchemistMenu(panelManager);

        final ChoiceMenu choiceMenu = new ChoiceMenu(
                warriorMenu,
                farmerMenuHolder,
                alchemistMenu,
                mySqlStorage,
                challengeManager);
        getCommand("skilltree").setExecutor(new AdminCommand(this, mySqlStorage, choiceMenu, warriorMenu, farmerMenuHolder, alchemistMenu));
        getServer().getPluginManager().registerEvents(choiceMenu, this);
        getServer().getPluginManager().registerEvents(new MobKillEvent(mySqlStorage, configManager, challengeManager), this);
        getServer().getPluginManager().registerEvents(new RaidFinishListener(mySqlStorage), this);
        getServer().getPluginManager().registerEvents(new PlayersJoinEvent(mySqlStorage), this);

    }

}

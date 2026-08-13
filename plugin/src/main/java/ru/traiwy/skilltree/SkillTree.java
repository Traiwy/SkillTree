package ru.traiwy.skilltree;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.command.AdminCommand;
import ru.traiwy.skilltree.configuration.PluginConfiguration;
import ru.traiwy.skilltree.inv.MenuManager;
import ru.traiwy.skilltree.manager.EventManager;
import ru.traiwy.skilltree.manager.ItemManager;
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

        final PluginConfiguration pluginConfiguration = new PluginConfiguration(this);

        MenuManager menuManager = new MenuManager();
        AdminCommand adminCommand = new AdminCommand(
                this,
                mySqlStorage,
                pluginConfiguration.getMessage(),
                menuManager
        );

        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> event.registrar().register(
                        adminCommand.createCommand(),
                        "SkillTree command"
                )
        );
    }

}

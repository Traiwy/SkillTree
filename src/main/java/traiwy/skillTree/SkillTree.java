package traiwy.skillTree;

import command.StartMenuCommand;
import inv.choice.ChoiceMenuBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkillTree extends JavaPlugin {

    public ChoiceMenuBuilder choiceMenuBuilder;
    @Override
    public void onEnable() {
        this.choiceMenuBuilder = new ChoiceMenuBuilder();
        getCommand("start").setExecutor(new StartMenuCommand(choiceMenuBuilder));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

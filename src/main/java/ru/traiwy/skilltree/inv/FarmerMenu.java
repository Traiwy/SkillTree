package ru.traiwy.skilltree.inv;


import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.manager.ItemManager;
import ru.traiwy.skilltree.manager.PanelManager;


public class FarmerMenu extends AbstractSkillMenu{

    public FarmerMenu( PanelManager panelManager, ItemManager itemManager, JavaPlugin plugin) {
        super("Путь Фермера", panelManager, itemManager, plugin);
    }

     public void open(Player player) {
        openInventory(player, Skill.FARMER);
    }

}

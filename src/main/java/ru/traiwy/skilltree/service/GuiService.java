package ru.traiwy.skilltree.service;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.traiwy.skilltree.inv.AlchemistMenu;
import ru.traiwy.skilltree.inv.FarmerMenu;
import ru.traiwy.skilltree.inv.WarriorMenu;

public class GuiService implements Listener {

    @EventHandler
    public void on(InventoryClickEvent event){
        if(event.getInventory().getHolder(false) instanceof WarriorMenu){
            onClick(event);
        }

        if(event.getInventory().getHolder(false) instanceof FarmerMenu){
            onClick(event);
        }

        if(event.getInventory().getHolder(false) instanceof AlchemistMenu){
            onClick(event);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
       event.setCancelled(true);
   }



}

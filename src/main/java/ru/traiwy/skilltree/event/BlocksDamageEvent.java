package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@AllArgsConstructor
public class BlocksDamageEvent implements Listener {

    private final ItemBreakEvent itemBreakEvent;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Material brokenType = block.getType();

        if (isWood(brokenType)) {
            itemBreakEvent.addLastBrokenBlock(player, brokenType);
        }
    }

    private boolean isWood(Material mat) {
        return switch (mat) {
            case OAK_LOG, SPRUCE_LOG, BIRCH_LOG, JUNGLE_LOG, DARK_OAK_LOG, ACACIA_LOG, CHERRY_LOG -> true;
            default -> false;
        };
    }
}

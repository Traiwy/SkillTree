package ru.traiwy.skilltree.event;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.event.raid.RaidStopEvent;
import org.checkerframework.checker.units.qual.A;
import ru.traiwy.skilltree.command.StartCommand;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.storage.MySqlStorage;

@AllArgsConstructor
public class RaidFinishListener implements Listener {
    private final MySqlStorage mySqlStorage;

    private final int RAID_TASK = 5;

    @EventHandler
    public void onRaidFinish(RaidStopEvent event) {
        //if (event.getReason() == RaidStopEvent.Reason.FINISHED) {
        //for (Player player : event.getRaid().getHeroes()) {
        //    Status status = mySqlStorage.getStatus(player.getName(), RAID_TASK);
        //    if (status == Status.IN_PROGRESS) {
        //        mySqlStorage.updateTask(player.getName(), RAID_TASK, Status.COMPLETED);
        //        player.sendMessage("Вы завершили задание на прохождение рейда!");
        //    }
        //}
    }
    }


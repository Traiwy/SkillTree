package ru.traiwy.skilltree.manager;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.List;

@AllArgsConstructor
public class EventManager {
    private final MySqlStorage mySqlStorage;

    public boolean isApplicableTask(Task task, String typeConfig) {
        return false;
    }

    public void handleProgress(Task task, Player player) {
    }

}

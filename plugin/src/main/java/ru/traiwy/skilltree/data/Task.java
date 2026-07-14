package ru.traiwy.skilltree.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ConfigManager;


@Getter
@Setter
@AllArgsConstructor
public class Task {
    private int id;
    private int playerId;
    private String taskName;
    private String challengeId;
    private Status status;
    private int progress;
}

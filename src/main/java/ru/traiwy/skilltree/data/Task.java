package ru.traiwy.skilltree.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.traiwy.skilltree.enums.Status;


@Getter
@Setter
@AllArgsConstructor
public class Task {
    private int id;
    private int playerId;
    private String taskName;
    private Status status;
}

package ru.traiwy.skilltree.data;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Player {
    private final String playerName;
    private final String skill;
    private final int progress;


}

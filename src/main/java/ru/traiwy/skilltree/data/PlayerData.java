package ru.traiwy.skilltree.data;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.traiwy.skilltree.enums.Skill;

@Getter
@Setter
@AllArgsConstructor
public class PlayerData {
    private int id;
    private final String playerName;
    private final Skill skill;
    private final int progress;

    public PlayerData(String playerName, Skill skill, int progress) {
        this.playerName = playerName;
        this.skill = skill;
        this.progress = progress;
    }

}

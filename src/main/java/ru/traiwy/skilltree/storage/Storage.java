package ru.traiwy.skilltree.storage;

import ru.traiwy.skilltree.enums.Skill;
import ru.traiwy.skilltree.enums.Status;

public interface Storage {
    void createTable();

    void setSkill(String name, Skill characterSkill);
    Skill getSkill(String name);

    void updateTask(String name, int taskId, Status status);
    boolean isTaskCompleted(String name, int taskId);

    boolean isChecked(String name);
    int getCompletedTasksCount(String name);
    void deleteSkill(String name);
    Status getStatus(String name, int taskId);


     int getProgress(String name);
     void updateProgress(String name, int progress);

}

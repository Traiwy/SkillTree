package ru.traiwy.skilltree.storage;

public interface Storage {
    void setClass(String name, String characterClass);
    void getClass(String name);
    void getPlayerProgress(String name);
    void updateTask(String name, int taskId, boolean completed);
    boolean isTaskCompleted(String name, int taskId);
    int getCompletedTasksCount(String name);
}

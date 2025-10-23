package ru.traiwy.skilltree.manager;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import ru.traiwy.skilltree.data.Task;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.storage.MySqlStorage;

import java.util.List;

@AllArgsConstructor
public class EventManager {
    private final ChallengeManager challengeManager;
    private final MySqlStorage mySqlStorage;

    public boolean isApplicableTask(Task task, String typeConfig) {
        if (task.getStatus() == Status.COMPLETED) return false;
        ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
        if (challenge == null) return false;

        return typeConfig.equalsIgnoreCase(challenge.getType());
    }

    public void handleProgress(Task task, ConfigManager.Challenge challenge, Player player) {
        int required = challenge.getData().getRequired();
        int newProgress = Math.min(task.getProgress() + 1, required);

        task.setProgress(newProgress);
        if (newProgress >= required) {
            task.setStatus(Status.COMPLETED);
            player.sendMessage("Задание выполнено! Чтобы посмотреть следующее задание используй /skilltree start");
        }

        mySqlStorage.updateTask(task);
    }

     public void updateChallengeProgress(Player player, String typeConfig, String type, String typeSettings) {
         mySqlStorage.getPlayer(player.getName()).thenAccept(playerData -> {
             mySqlStorage.getTasksByPlayer(playerData.getId()).thenAccept(tasks -> {
                 for (Task task : tasks) {
                     if (!(isApplicableTask(task, typeConfig))) continue;

                     final ConfigManager.Challenge challenge = challengeManager.getChallengeById(task.getChallengeId());
                     if (challenge == null) continue;
                     final Object condition = challenge.getSettings().get(type);
                     boolean matches = false;

                     if (condition instanceof List<?> list) {
                         matches = list.stream().anyMatch(o ->
                                 o != null && o.toString().equalsIgnoreCase(typeSettings));
                     } else if (condition instanceof String str) {
                         matches = str.equalsIgnoreCase(typeSettings);
                     }

                     if (!matches) continue;

                     handleProgress(task, challenge, player);
                     if (task.getStatus() == Status.COMPLETED) {
                         challengeManager.setNextChallenge(challenge, task);
                     }

                 }
             });
         });
     }


}

package ru.traiwy.skilltree.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.persistence.PersistentDataType;
import ru.traiwy.skilltree.util.ConfigManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlStorage implements Storage{
    private HikariDataSource dataSource;
    private String url = "jdbc:mysql://" + ConfigManager.MySQL.HOST + ":" + ConfigManager.MySQL.PORT + "/" + ConfigManager.MySQL.DATABASE;

    public MySqlStorage(){
        final HikariConfig config = new HikariConfig();
        config.setUsername(ConfigManager.MySQL.USER);
        config.setPassword(ConfigManager.MySQL.PASSWORD);
        config.setJdbcUrl(url);
        config.setMaximumPoolSize(3);
        dataSource = new HikariDataSource(config);
    }

    @Override
    public void setClass(String name, String characterClass) {
        try(Connection connection = dataSource.getConnection()){
            try(final PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO skilltree (name, class) VALUES (?, ?)
                    """)){
                ps.setString(1, name);
                ps.setString(2, characterClass);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getClass(String name) {
        try (Connection connection = dataSource.getConnection()) {
            try (final PreparedStatement ps = connection.prepareStatement("""
                    SELECT class FROM skilltree WHERE name = ?
                    """)) {
                ps.setString(1, name);
                ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getPlayerProgress(String name) {

    }

    @Override
    public void updateTask(String name, int taskId, boolean completed) {

    }

    @Override
    public boolean isTaskCompleted(String name, int taskId) {
        return false;
    }

    @Override
    public int getCompletedTasksCount(String name) {
        return 0;
    }

}

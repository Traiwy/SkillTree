package ru.traiwy.skilltree.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.traiwy.skilltree.enums.Status;
import ru.traiwy.skilltree.manager.ConfigManager;
import ru.traiwy.skilltree.enums.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlStorage implements Storage {
    private HikariDataSource dataSource;

    public MySqlStorage() {
        setupDataSource();
    }

    private void setupDataSource() {
        String url = "jdbc:mysql://" + ConfigManager.MySQL.HOST + ":" +
                ConfigManager.MySQL.PORT + "/" + ConfigManager.MySQL.DATABASE;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(ConfigManager.MySQL.USER);
        config.setPassword(ConfigManager.MySQL.PASSWORD);
        config.setMaximumPoolSize(3);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    @Override
    public void setSkill(String name, Skill characterSkill) {
        try (Connection connection = dataSource.getConnection()) {
            try (final PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO skilltree (player_name, class) VALUES (?, ?)
                    """)) {
                ps.setString(1, name);
                ps.setString(2, characterSkill.name());
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Skill getSkill(String name) {
        try (Connection connection = dataSource.getConnection()) {
            try (final PreparedStatement ps = connection.prepareStatement("""
                    SELECT class FROM skilltree WHERE `player_name` = ?
                    """)) {
                ps.setString(1, name);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String className = rs.getString("class");
                        return Skill.valueOf(className);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateTask(String name, int taskId, Status status) {
        String columnName = "task" + taskId;
        try (Connection connection = dataSource.getConnection()) {
            try (final PreparedStatement ps = connection.prepareStatement(
                    "UPDATE skilltree SET " +
                            columnName + " = ? " +
                            "WHERE player_name = ?")) {
                ;
                ps.setString(1, status.name());
                ps.setString(2, name);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isTaskCompleted(String name, int taskId) {
        String sql = "SELECT task" + taskId + " FROM skilltree WHERE player_name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("task" + taskId);
                    return "COMPLETED".equals(status);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean isChecked(String name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("""
                     SELECT 1 FROM skilltree WHERE player_name = ?
                     """)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getCompletedTasksCount(String name) {
        return 0;
    }

    @Override
    public void deleteSkill(String name) {
        try (Connection connection = dataSource.getConnection()) {
            try (final PreparedStatement ps = connection.prepareStatement("""
                    DELETE FROM skilltree WHERE player_name = ?
                    """)) {
                ps.setString(1, name);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Status getStatus(String name, int taskId) {
        String sql = "SELECT task" + taskId + " FROM skilltree WHERE player_name = ?";
        try (Connection connection = dataSource.getConnection()) {
            String columnName = "task" + taskId;
            try (final PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, name);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String statusStr = rs.getString(columnName);
                        if (statusStr != null) {
                            return Status.valueOf(statusStr);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getProgress(String name, int taskId) {
        String sql = "SELECT progress FROM skilltree WHERE player_name = ? AND task" + taskId + " = ?";
        try (Connection connection = dataSource.getConnection()) {
            try (final PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setInt(2, taskId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("progress");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updateProgress(String name, int taskId, int progress, Skill skill) {
        String column = "task" + taskId;

        String sql = "UPDATE skilltree SET " + column + " = ?, progress = ? " +
                "WHERE player_name = ? AND class = ?";

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, progress);
                ps.setInt(2, progress);
                ps.setString(3, name);
                ps.setString(4, skill.name());

                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}








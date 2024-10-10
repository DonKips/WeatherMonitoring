package me.looks.weathermonitoring.database;

import me.looks.weathermonitoring.data.LogData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private Connection connection;
    private final String urlConnection, username, password;
    private boolean enable;

    public DatabaseService(String host, String database, int port, String username, String password, String rules) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + database + rules;
        this.username = username;
        this.password = password;

        try {
            connection = DriverManager.getConnection(urlConnection, username, password);
            enable = true;
        } catch (SQLException e) {
            System.out.println("База данных MySql не подключена: Неверно указаны данные!");
            enable = false;
            return;
        }
        executeUpdate(
                "CREATE TABLE IF NOT EXISTS logs (user_id VARCHAR(64), user_command VARCHAR(64), time LONG, bot_response VARCHAR(64));"
        );
    }

    public void newLog(long userId, String command, String botResponse) {
        if (!enable) return;

        executeUpdate("INSERT INTO logs VALUES ('" + String.join("', '",
                List.of(String.valueOf(userId), command, String.valueOf(System.currentTimeMillis()), botResponse)) + "');");
    }

    public List<LogData> getLogs() {
        if (!enable) return List.of();

        List<LogData> logs = new ArrayList<>();
        try (Statement statement = getConnection().createStatement()) {

            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM logs;")) {
                while (resultSet.next()) {

                    String userId = resultSet.getString("user_id");
                    String userCommand = resultSet.getString("user_command");
                    long time = resultSet.getLong("time");
                    String botResponse = resultSet.getString("bot_response");

                    logs.add(new LogData(userId, userCommand, time, botResponse));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return logs;
    }
    public List<LogData> getLogs(String userId) {
        if (!enable) return List.of();

        List<LogData> logs = new ArrayList<>();
        try (Statement statement = getConnection().createStatement()) {

            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM logs WHERE user_id IN ('" + userId + "');")) {
                while (resultSet.next()) {

                    String userCommand = resultSet.getString("user_command");
                    long time = resultSet.getLong("time");
                    String botResponse = resultSet.getString("bot_response");

                    logs.add(new LogData(userId, userCommand, time, botResponse));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return logs;
    }


    private void executeUpdate(String... strings) {
        if (!enable) return;

        executeUpdate(List.of(strings));
    }
    private void executeUpdate(List<String> strings) {
        if (!enable) return;

        try {
            Statement statement = getConnection().createStatement();
            for (String str : strings) {
                statement.executeUpdate(str);
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(urlConnection, username, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}

package me.looks.weathermonitoring.database;

import me.looks.weathermonitoring.data.LogData;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
                "CREATE TABLE IF NOT EXISTS logs (user_id VARCHAR(64), user_command VARCHAR(200), created_at LONG, bot_response VARCHAR(64));"
        );
    }

    public void newLog(long userId, String command, String botResponse) {
        if (!enable) return;

        executeUpdate("INSERT INTO logs VALUES ('" + String.join("', '",
                List.of(String.valueOf(userId), command, String.valueOf(System.currentTimeMillis()), botResponse)) + "');");
    }

    private String getFilter(String startWith, int afterId, int beforeId, long afterTime, long beforeTime) {
        StringBuilder filter = new StringBuilder(startWith);
        if (afterTime != -1) {
            if (!filter.isEmpty()) {
                filter.append(" AND ");
            } else {
                filter.append(" WHERE ");
            }
            filter.append("created_at >= ").append(afterTime);
        }
        if (beforeTime != -1) {
            if (!filter.isEmpty()) {
                filter.append(" AND ");
            } else {
                filter.append(" WHERE ");
            }
            filter.append("created_at <= ").append(afterTime);
        }
        filter.append(" ORDER BY created_at DESC ");
        if (beforeId != -1) {
            filter.append(" LIMIT ").append(beforeId);
            if (afterId != -1) {
                filter.append(" OFFSET ").append(afterId);
            }
        }
        return filter.toString();
    }

    public List<LogData> getLogs(int afterId, int beforeId, long afterTime, long beforeTime) {
        if (!enable) return List.of();

        String filter = getFilter("", afterId, beforeId, afterTime, beforeTime);

        return getLogs(filter);
    }
    public List<LogData> getLogs(String userId, int afterId, int beforeId, long afterTime, long beforeTime) {
        if (!enable) return List.of();

        String filter = getFilter(" WHERE user_id IN ('" + userId + "')", afterId, beforeId, afterTime, beforeTime);

        return getLogs(filter);
    }
    public List<LogData> getLogs(String filter) {
        List<LogData> logs = new ArrayList<>();
        try (Statement statement = getConnection().createStatement()) {

            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM logs" + filter + ";")) {
                while (resultSet.next()) {

                    String userId = resultSet.getString("user_id");
                    String userCommand = resultSet.getString("user_command");
                    long createdAt = resultSet.getLong("created_at");
                    String botResponse = resultSet.getString("bot_response");

                    Date date = new Date(createdAt);
                    SimpleDateFormat dateFormatData = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
                    dateFormatData.setTimeZone(TimeZone.getDefault());
                    dateFormatTime.setTimeZone(TimeZone.getDefault());

                    logs.add(new LogData(userId, userCommand, createdAt, botResponse, dateFormatData.format(date), dateFormatTime.format(date)));
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

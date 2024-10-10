package me.looks.weathermonitoring;

import me.looks.weathermonitoring.database.DatabaseService;
import me.looks.weathermonitoring.telegram.TelegramBotImpl;
import me.looks.weathermonitoring.weather.ReceiveWeatherStatus;
import me.looks.weathermonitoring.weather.ReceivingGeocoding;
import me.looks.weathermonitoring.weather.RequestsService;

public class Loader {
    private final RequestsService requestsService;
    private final ReceivingGeocoding receivingGeocoding;
    private final ReceiveWeatherStatus receiveWeatherStatus;
    private final TelegramBotImpl telegramBot;
    private final DatabaseService databaseService;

    public Loader(String telegramBotToken, String openWeatherMapKey,
                  String databaseServiceHost, String databaseServiceDatabase, int databaseServicePort,
                  String databaseServiceUsername, String databaseServicePassword, String databaseServiceRules) {
        telegramBot = new TelegramBotImpl(this, telegramBotToken);
        telegramBot.registerListener();

        requestsService = new RequestsService();
        receivingGeocoding = new ReceivingGeocoding(this, openWeatherMapKey);
        receiveWeatherStatus = new ReceiveWeatherStatus(this, openWeatherMapKey);
        databaseService = new DatabaseService(databaseServiceHost, databaseServiceDatabase, databaseServicePort,
                databaseServiceUsername, databaseServicePassword, databaseServiceRules);
    }

    public RequestsService getRequestsService() {
        return requestsService;
    }

    public ReceiveWeatherStatus getReceiveWeatherStatus() {
        return receiveWeatherStatus;
    }

    public ReceivingGeocoding getReceivingGeocoding() {
        return receivingGeocoding;
    }

    public TelegramBotImpl getTelegramBot() {
        return telegramBot;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }
}

package me.looks.weathermonitoring.telegram.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.looks.weathermonitoring.Loader;
import me.looks.weathermonitoring.telegram.Message;
import me.looks.weathermonitoring.utils.MapUtil;

public class WeatherCommand implements IBotCommand {
    @Override
    public String getCommandLabel() {
        return "/weather";
    }

    @Override
    public void executeCommand(Loader loader, long userId, long chatId, String command, String[] args) {
        if (args.length == 1) {
            loader.getTelegramBot().sendMessage(chatId, Message.COMMAND_WEATHER_NO_ARGS);
            loader.getDatabaseService().newLog(userId, command, "COMMAND_WEATHER_NO_ARGS");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            stringBuilder.append(args[i]);
            if (i < args.length - 1) {
                stringBuilder.append(" ");
            }
        }

        JsonElement geocodingElement = loader.getReceivingGeocoding().get(stringBuilder.toString());
        if (geocodingElement == null) {
            loader.getTelegramBot().sendMessage(chatId, Message.COMMAND_WEATHER_ERROR);
            loader.getDatabaseService().newLog(userId, command, "COMMAND_WEATHER_ERROR");
            return;
        }
        JsonArray geocodingArray = geocodingElement.getAsJsonArray();
        if (geocodingArray.isEmpty() || geocodingArray.asList().isEmpty()) {
            loader.getTelegramBot().sendMessage(chatId, Message.COMMAND_WEATHER_CITY_NOT_FOUND);
            loader.getDatabaseService().newLog(userId, command, "COMMAND_WEATHER_CITY_NOT_FOUND");
            return;
        }
        if (geocodingArray.asList().size() == 1) {
            JsonObject geocodingObject = geocodingArray.asList().get(0).getAsJsonObject();
            JsonObject weatherStatusObject = loader.getReceiveWeatherStatus()
                    .get(geocodingObject.get("lat").getAsDouble(), geocodingObject.get("lon").getAsDouble(), "ru")
                    .getAsJsonObject();

            JsonObject weatherStatusMainObject = weatherStatusObject.get("main").getAsJsonObject();
            JsonObject weatherStatusWeatherObject = weatherStatusObject.get("weather").getAsJsonArray().asList().get(0).getAsJsonObject();
            JsonObject weatherStatusWindObject = weatherStatusObject.get("wind").getAsJsonObject();

            loader.getTelegramBot().sendMessage(chatId, Message.COMMAND_WEATHER_SUCCESSFUL,
                    MapUtil.getEntry("%city%", weatherStatusObject.get("name").getAsString()),
                    MapUtil.getEntry("%temp%", String.format("%.2f", weatherStatusMainObject.get("temp").getAsDouble() - 273.15)),
                    MapUtil.getEntry("%feels_like%", String.format("%.2f", weatherStatusMainObject.get("feels_like").getAsDouble() - 273.15)),
                    MapUtil.getEntry("%weather%", weatherStatusWeatherObject.get("description").getAsString()),
                    MapUtil.getEntry("%humidity%", weatherStatusMainObject.get("humidity").getAsInt()),
                    MapUtil.getEntry("%wind_speed%", weatherStatusWindObject.get("speed").getAsString()));
            loader.getDatabaseService().newLog(userId, command, "COMMAND_WEATHER_SUCCESSFUL");
        } else {
            loader.getTelegramBot().sendMessage(chatId, Message.COMMAND_WEATHER_MANY_CITIES);
            loader.getDatabaseService().newLog(userId, command, "COMMAND_WEATHER_MANY_CITIES");
        }

    }
}

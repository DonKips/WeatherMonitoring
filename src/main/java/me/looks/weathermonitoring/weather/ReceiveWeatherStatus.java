package me.looks.weathermonitoring.weather;

import com.google.gson.JsonElement;
import me.looks.weathermonitoring.Loader;

public class ReceiveWeatherStatus {
    private final Loader loader;
    private final String apiKey;

    public ReceiveWeatherStatus(Loader loader, String apiKey) {
        this.loader = loader;
        this.apiKey = apiKey;
    }

    public JsonElement get(double lat, double lon, String lang) {
        String requestLink = "https://api.openweathermap.org/data/2.5/weather?lat=" + String.format("%.2f", lat) +
                "&lon=" + String.format("%.2f", lon) + "&exclude=minutely,hourly,daily,alerts&appid=" + this.apiKey + "&lang=" + lang;
        return loader.getRequestsService().request(requestLink);
    }

}

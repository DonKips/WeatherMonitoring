package me.looks.weathermonitoring.weather;

import com.google.gson.JsonElement;
import me.looks.weathermonitoring.Loader;

public class ReceivingGeocoding {
    private final Loader loader;
    private final String apiKey;

    public ReceivingGeocoding(Loader loader, String apiKey) {
        this.loader = loader;
        this.apiKey = apiKey;
    }

    public JsonElement get(String city) {
        String requestLink = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=5&appid=" + apiKey;
        return loader.getRequestsService().request(requestLink);
    }
}

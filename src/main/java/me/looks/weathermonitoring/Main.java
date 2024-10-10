package me.looks.weathermonitoring;

import me.looks.weathermonitoring.data.LogData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class Main {
    private final Loader loader;

    public Main() {
        String telegramBotToken = "8016332199:AAG6LupjIQYFYKxW-tC2fNY2DLTS0hdRqDI";
        String openWeatherMapKey = "6f98014f5088f4f164bc50aa07d66ae7";

        String databaseServiceHost = "localhost";
        String databaseServiceDatabase = "test";
        int databaseServicePort = 3306;
        String databaseServiceUsername = "root";
        String databaseServicePassword = "hadfgow219LDsqowe00Dasld";
        String databaseServiceRules = "?autoReconnect=true&useSSL=false";

        loader = new Loader(telegramBotToken, openWeatherMapKey, databaseServiceHost, databaseServiceDatabase, databaseServicePort,
                databaseServiceUsername, databaseServicePassword, databaseServiceRules);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }

    @GetMapping("/logs")
    public List<LogData> getLogs(@RequestParam(value = "afterId", defaultValue = "-1") int afterId,
                                 @RequestParam(value = "beforeId", defaultValue = "-1") int beforeId,
                                 @RequestParam(value = "afterTime", defaultValue = "-1") long afterTime,
                                 @RequestParam(value = "beforeTime", defaultValue = "-1") long beforeTime) {
        return loader.getDatabaseService().getLogs(afterId, beforeId, afterTime, beforeTime);
    }
    @GetMapping("/logs/{user_id}")
    public List<LogData> getLogs(@PathVariable String user_id,
                                 @RequestParam(value = "afterId", defaultValue = "-1") int afterId,
                                 @RequestParam(value = "beforeId", defaultValue = "-1") int beforeId,
                                 @RequestParam(value = "afterTime", defaultValue = "-1") long afterTime,
                                 @RequestParam(value = "beforeTime", defaultValue = "-1") long beforeTime) {
        return loader.getDatabaseService().getLogs(user_id, afterId, beforeId, afterTime, beforeTime);
    }
}
# WeatherMonitoring

Приложение WeatherMonitoring написано в качестве тестового задания для компании BobrAi.

**WeatherMonitoring** - приложение, содержащее телеграм-бота для получения текущего состояния погоды в указанном городе.

### Компиляция приложения:
```
git clone https://github.com/DonKips/WeatherMonitoring.git
mvn install 
mvn package
```

## WeatherMonitoring Logs API

### Получение логов

Метод позволяет получить историю ввода команд в телеграм боте.

```plaintext
METHOD /logs
METHOD /logs/{user_id}
METHOD /logs?beforeId=5&afterId=1&afterTime=1728591987242&beforeTime=1728591987242
METHOD /logs/{user_id}?beforeId=5&afterId=1&afterTime=1728591987242&beforeTime=1728591987242
```

Параметры:

| Attribute   | Type | Required | Description                                                                            |
|-------------|------|----------|----------------------------------------------------------------------------------------|
| `{user_id}` | long | No       | Айди пользователя, чьи логи нужно отобразить                                           |
| `beforeId`        | long | No       | Ограничение количества выведенных логов до указанного                                  |
| `afterId` | long | Yes      | Ограничение количества выведенных логов от указанного, указывается только совместно с beforeId |
| `beforeTime` | long | No       | Фильтрация по времени отправки команды до указанного                                   |
| `afterTime` | long | No       | Фильтрация по времени отправки команды от указанного                                   |

Пример запроса:

```shell
curl -X GET --url "http://localhost:8080/logs"
curl -X GET --url "http://localhost:8080/logs/123"
curl -X GET --url "http://localhost:8080/logs/123?beforeId=5&afterId=1&afterTime=1728591987242&beforeTime=1728591987242"
```

Пример ответа:

```json
[
  {
    "userId": 1,
    "userCommand": "/weather",
    "created_at": 192491294319,
    "botResponse": "COMMAND_WEATHER_SUCCESSFUL",
    "formatData": "10/10/2024",
    "formatTime": "23:23:29"
  }
]
```

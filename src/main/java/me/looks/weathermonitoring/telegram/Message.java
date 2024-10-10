package me.looks.weathermonitoring.telegram;

public enum Message {

    COMMAND_WEATHER_NO_ARGS("Введите город"),
    COMMAND_WEATHER_ERROR("Произошла ошибка с получением города"),
    COMMAND_WEATHER_CITY_NOT_FOUND("Город указан неверно"),
    COMMAND_WEATHER_SUCCESSFUL("Погода в городе %city%:\n Температура: %temp%°C\n Ощущаемая температура: %feels_like%°C\n " +
            "Описание погоды: %weather%\n Влажность: %humidity%%\n Скорость ветра: %wind_speed% м/с"),
    ;
    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

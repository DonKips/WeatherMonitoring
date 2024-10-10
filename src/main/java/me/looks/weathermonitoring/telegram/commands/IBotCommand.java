package me.looks.weathermonitoring.telegram.commands;

import me.looks.weathermonitoring.Loader;

public interface IBotCommand {

    String getCommandLabel();

    void executeCommand(Loader loader, long userId, long chatId, String command, String[] args);

}

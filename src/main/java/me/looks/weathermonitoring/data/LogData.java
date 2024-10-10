package me.looks.weathermonitoring.data;

public record LogData(String userId, String userCommand, long time, String botResponse) {}
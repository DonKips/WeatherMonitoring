package me.looks.weathermonitoring.data;

public record LogData(String userId, String userCommand, long createdAt, String botResponse, String formatData, String formatTime) {}
package me.looks.weathermonitoring.utils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

public class MapUtil {

    public static Map.Entry<String, String> getEntry(String str, Object object) {
        return new AbstractMap.SimpleEntry<>(str, Objects.requireNonNullElse(object, "none").toString());
    }

}

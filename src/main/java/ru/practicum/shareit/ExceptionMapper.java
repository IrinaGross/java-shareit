package ru.practicum.shareit;

import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class ExceptionMapper {

    private static final String ERROR_KEY = "error";

    public static Map<String, String> getCustomBody(RuntimeException e) {
        return Map.of(ERROR_KEY, e.getMessage());
    }
}

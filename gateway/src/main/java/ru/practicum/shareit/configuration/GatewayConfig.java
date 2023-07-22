package ru.practicum.shareit.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    @Value("${shareit-server.url}")
    private final String serverUrl;

    public String buildServerRoute(String path) {
        return serverUrl + path;
    }
}

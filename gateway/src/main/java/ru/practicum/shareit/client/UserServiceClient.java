package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.configuration.GatewayConfig;
import ru.practicum.shareit.user.dto.UserDto;

@Service
class UserServiceClient extends BaseClient implements UserClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserServiceClient(GatewayConfig config, RestTemplateBuilder builder) {
        super(builder, config.buildServerRoute(API_PREFIX));
    }

    @NonNull
    @Override
    public ResponseEntity<Object> findAll() {
        return get("");
    }

    @NonNull
    @Override
    public ResponseEntity<Object> getUser(@NonNull Long userId) {
        return get("/" + userId);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> create(@NonNull UserDto user) {
        return post("", user);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> updateUser(@NonNull Long userId, @NonNull UserDto user) {
        return patch("/" + userId, user);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> deleteUser(@NonNull Long userId) {
        return delete("/" + userId);
    }
}

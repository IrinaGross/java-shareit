package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.configuration.GatewayConfig;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

import static ru.practicum.shareit.Const.FROM_REQUEST_PARAM;
import static ru.practicum.shareit.Const.SIZE_REQUEST_PARAM;

@Service
class ItemRequestServiceClient extends BaseClient implements ItemRequestClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestServiceClient(GatewayConfig config, RestTemplateBuilder builder) {
        super(builder, config.buildServerRoute(API_PREFIX));
    }

    @NonNull
    @Override
    public ResponseEntity<Object> addNew(@NonNull Long userId, @NonNull ItemRequestDto request) {
        return post("", userId, request);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> getAllFor(@NonNull Long userId) {
        return get("", userId);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> getById(@NonNull Long userId, @NonNull Long requestId) {
        return get(
                String.format("/%d", requestId),
                userId
        );
    }

    @NonNull
    @Override
    public ResponseEntity<Object> getAll(@NonNull Long userId, @NonNull Integer from, @NonNull Integer size) {
        return get(
                "/all?from={from}&size={size}",
                userId,
                Map.of(FROM_REQUEST_PARAM, from,
                        SIZE_REQUEST_PARAM, size)
        );
    }
}

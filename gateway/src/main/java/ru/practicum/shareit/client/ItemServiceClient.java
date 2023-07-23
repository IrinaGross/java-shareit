package ru.practicum.shareit.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.configuration.GatewayConfig;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

import static ru.practicum.shareit.Const.*;

@Service
class ItemServiceClient extends BaseClient implements ItemClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemServiceClient(GatewayConfig config, RestTemplateBuilder builder) {
        super(builder, config.buildServerRoute(API_PREFIX));
    }

    @NonNull
    @Override
    public ResponseEntity<Object> getItems(@NonNull Long userId, @NonNull Integer from, @NonNull Integer size) {
        return get(
                "?from={from}&size={size}",
                userId,
                Map.of(FROM_REQUEST_PARAM, from,
                        SIZE_REQUEST_PARAM, size)
        );
    }

    @NonNull
    @Override
    public ResponseEntity<Object> getItem(@NonNull Long userId, @NonNull Long itemId) {
        return get("/" + itemId, userId);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> searchItems(@NonNull String text, @NonNull Integer from, @NonNull Integer size) {
        return get(
                "/search?text={text}&from={from}&size={size}",
                null,
                Map.of(SEARCH_REQUEST_PARAM, text,
                        FROM_REQUEST_PARAM, from,
                        SIZE_REQUEST_PARAM, size)
        );
    }

    @NonNull
    @Override
    public ResponseEntity<Object> addItem(@NonNull Long userId, @NonNull ItemDto item) {
        return post("", userId, item);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> updateItem(@NonNull Long userId, @NonNull Long itemId, @NonNull ItemDto item) {
        return patch("/" + itemId, userId, item);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> deleteItem(@NonNull Long userId, @NonNull Long itemId) {
        return delete("/" + itemId, userId);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> createComment(@NonNull Long userId, @NonNull Long itemId, @NonNull CommentDto comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }
}

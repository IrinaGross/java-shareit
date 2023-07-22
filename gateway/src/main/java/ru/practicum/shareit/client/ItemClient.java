package ru.practicum.shareit.client;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemClient {
    @NonNull
    ResponseEntity<Object> getItems(@NonNull Long userId, @NonNull Integer from, @NonNull Integer size);

    @NonNull
    ResponseEntity<Object> getItem(@NonNull Long userId, @NonNull Long itemId);

    @NonNull
    ResponseEntity<Object> searchItems(@NonNull String text, @NonNull Integer from, @NonNull Integer size);

    @NonNull
    ResponseEntity<Object> addItem(@NonNull Long userId, @NonNull ItemDto item);

    @NonNull
    ResponseEntity<Object> updateItem(@NonNull Long userId, @NonNull Long itemId, @NonNull ItemDto item);

    @NonNull
    ResponseEntity<Object> deleteItem(@NonNull Long userId, @NonNull Long itemId);

    @NonNull
    ResponseEntity<Object> createComment(@NonNull Long userId, @NonNull Long itemId, @NonNull CommentDto comment);
}

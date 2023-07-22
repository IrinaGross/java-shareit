package ru.practicum.shareit.client;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.request.dto.ItemRequestDto;

public interface ItemRequestClient {
    @NonNull
    ResponseEntity<Object> addNew(@NonNull Long userId, @NonNull ItemRequestDto request);

    @NonNull
    ResponseEntity<Object> getAllFor(@NonNull Long userId);

    @NonNull
    ResponseEntity<Object> getById(@NonNull Long userId, @NonNull Long requestId);

    @NonNull
    ResponseEntity<Object> getAll(@NonNull Long userId, @NonNull Integer from, @NonNull Integer size);
}

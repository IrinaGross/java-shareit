package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    @NonNull
    ItemRequest create(@NonNull Long userId, @NonNull ItemRequest request);

    @NonNull
    List<ItemRequest> getAll(@NonNull Long userId);

    @NonNull
    ItemRequest getById(@NonNull Long userId, @NonNull Long requestId);

    @NonNull
    List<ItemRequest> getAll(@NonNull Long userId, @NonNull Pageable pageable);
}

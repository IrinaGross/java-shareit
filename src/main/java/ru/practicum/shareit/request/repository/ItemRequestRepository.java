package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepository {
    @NonNull
    ItemRequest create(@NonNull User creator, @NonNull ItemRequest request);

    @NonNull
    List<ItemRequest> getAll(@NonNull Long userId);

    @NonNull
    ItemRequest getById(@NonNull Long requestId);

    @NonNull
    List<ItemRequest> getAll(@NonNull Long userId, @NonNull Pageable page);
}

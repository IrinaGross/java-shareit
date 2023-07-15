package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository {
    @NonNull
    List<Item> getItems(@NonNull Long userId, @NonNull Pageable page);

    @NonNull
    Item addNewItem(@NonNull User user, @NonNull Item item, @Nullable ItemRequest request);

    void deleteItem(@NonNull Long userId, @NonNull Long itemId);

    @NonNull
    Item update(@NonNull Long userId, @NonNull Item item);

    @NonNull
    Item getItem(@NonNull Long itemId);

    @NonNull
    List<Item> searchBy(@NonNull String text, @NonNull Pageable page);
}

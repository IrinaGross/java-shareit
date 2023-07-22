package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    @NonNull
    List<Item> getItems(@NonNull Long userId, @NonNull Pageable pageable);

    @NonNull
    Item addNewItem(@NonNull Long userId, @NonNull Item item);

    void deleteItem(@NonNull Long userId, @NonNull Long itemId);

    @NonNull
    Item updateItem(@NonNull Long userId, @NonNull Item item);

    @NonNull
    Item getItem(@NonNull Long userId, @NonNull Long itemId);

    @NonNull
    List<Item> searchBy(@NonNull String text, @NonNull Pageable pageable);

    @NonNull
    Comment createComment(@NonNull Long userId, @NonNull Long itemId, @NonNull Comment comment);
}

package ru.practicum.shareit.item.service;

import org.springframework.lang.NonNull;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<Item> getItems(@NonNull Long userId);

    Item addNewItem(@NonNull Long userId, @NonNull Item item);

    void deleteItem(@NonNull Long userId, @NonNull Long itemId);

    Item updateItem(@NonNull Long userId, @NonNull Item item);

    Item getItem(@NonNull Long itemId);

    List<Item> searchBy(@NonNull String text);
}

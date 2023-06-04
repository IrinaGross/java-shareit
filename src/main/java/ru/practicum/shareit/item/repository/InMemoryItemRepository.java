package ru.practicum.shareit.item.repository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
class InMemoryItemRepository implements ItemRepository {
    private Long lastId = 0L;
    private final HashMap<Long, HashMap<Long, Item>> items = new HashMap<>();

    @NonNull
    @Override
    public List<Item> getItems(@NonNull Long userId) {
        return new ArrayList<>(items.get(userId).values());
    }

    @NonNull
    @Override
    public Item addNewItem(@NonNull Long userId, @NonNull Item item) {
        Long id = ++lastId;
        Item newItem = item.toBuilder()
                .id(id)
                .idOwner(userId)
                .build();
        HashMap<Long, Item> set = getOrEmpty(items, userId);
        set.put(id, newItem);
        items.put(userId, set);
        return newItem;
    }

    @Override
    public void deleteItem(@NonNull Long userId, @NonNull Long itemId) {
        HashMap<Long, Item> set = getOrEmpty(items, userId);
        getOrThrow(set, itemId);
        set.remove(itemId);
    }

    @NonNull
    @Override
    public Item update(@NonNull Long userId, @NonNull Item item) {
        HashMap<Long, Item> set = getOrEmpty(items, userId);
        Item saved = getOrThrow(set, item.getId());
        Boolean possibleNewAvailable = item.getAvailable();
        String possibleNewName = item.getName();
        String possibleNewDescription = item.getDescription();
        Item updated = saved.toBuilder()
                .available(possibleNewAvailable == null ? saved.getAvailable() : possibleNewAvailable)
                .name(possibleNewName == null ? saved.getName() : possibleNewName)
                .description(possibleNewDescription == null ? saved.getDescription() : possibleNewDescription)
                .build();
        set.put(item.getId(), updated);
        items.put(userId, set);
        return updated;
    }

    @NonNull
    @Override
    public Item getItem(@NonNull Long itemId) {
        return items.values().stream()
                .flatMap(it -> it.values().stream())
                .filter(it -> Objects.equals(it.getId(), itemId))
                .findAny()
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с идентефикатором %1$s не найдена", itemId)));
    }

    @NonNull
    @Override
    public List<Item> searchBy(@NonNull String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return items.values().stream()
                .flatMap(it -> it.values().stream())
                .filter(it -> it.getName().toLowerCase().contains(text.toLowerCase()) ||
                        it.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    @NonNull
    private Item getOrThrow(@NonNull HashMap<Long, Item> map, @NonNull Long itemId) {
        Item item = map.get(itemId);
        if (item == null) {
            throw new NotFoundException(String.format("Вещь с идентефикатором %1$s не найдена", itemId));
        }
        return item;
    }

    private static HashMap<Long, Item> getOrEmpty(HashMap<Long, HashMap<Long, Item>> map, Long key) {
        HashMap<Long, Item> hashSet = map.get(key);
        if (hashSet == null) {
            return new HashMap<>();
        }
        return hashSet;
    }
}
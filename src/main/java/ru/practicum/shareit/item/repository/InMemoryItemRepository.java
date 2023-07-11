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
    private final HashMap<Long, List<Long>> userItemsIds = new HashMap<>();
    private final HashMap<Long, Item> allItems = new HashMap<>();

    @NonNull
    @Override
    public List<Item> getItems(@NonNull Long userId) {
        return userItemsIds.get(userId)
                .stream()
                .map(allItems::get)
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public Item addNewItem(@NonNull Long userId, @NonNull Item item) {
        Long id = ++lastId;
        Item newItem = item.toBuilder()
                .id(id)
                .idOwner(userId)
                .build();
        List<Long> list = getOrEmpty(userItemsIds, userId);
        list.add(id);
        userItemsIds.put(userId, list);
        allItems.put(id, newItem);
        return newItem;
    }

    @Override
    public void deleteItem(@NonNull Long userId, @NonNull Long itemId) {
        List<Long> list = getOrEmpty(userItemsIds, userId);
        getOrThrow(list, allItems, itemId);
        allItems.remove(itemId);
        list.remove(itemId);
        userItemsIds.put(userId, list);
    }

    @NonNull
    @Override
    public Item update(@NonNull Long userId, @NonNull Item item) {
        List<Long> userItems = getOrEmpty(this.userItemsIds, userId);
        Item saved = getOrThrow(userItems, allItems, item.getId());
        Boolean possibleNewAvailable = item.getAvailable();
        String possibleNewName = item.getName();
        String possibleNewDescription = item.getDescription();
        Item updated = saved.toBuilder()
                .available(possibleNewAvailable == null ? saved.getAvailable() : possibleNewAvailable)
                .name(possibleNewName == null ? saved.getName() : possibleNewName)
                .description(possibleNewDescription == null ? saved.getDescription() : possibleNewDescription)
                .build();
        allItems.put(item.getId(), updated);
        return updated;
    }

    @NonNull
    @Override
    public Item getItem(@NonNull Long itemId) {
        return allItems.values().stream()
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
        return allItems.values().stream()
                .filter(it -> it.getName().toLowerCase().contains(text.toLowerCase()) ||
                        it.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    @NonNull
    private static Item getOrThrow(@NonNull List<Long> userItems, @NonNull HashMap<Long, Item> allItems, @NonNull Long itemId) {
        boolean exist = userItems.contains(itemId);
        Item item = allItems.get(itemId);
        if (item == null || !exist) {
            throw new NotFoundException(String.format("Вещь с идентефикатором %1$s не найдена", itemId));
        }
        return item;
    }

    private static List<Long> getOrEmpty(HashMap<Long, List<Long>> map, Long key) {
        List<Long> list = map.get(key);
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }
}
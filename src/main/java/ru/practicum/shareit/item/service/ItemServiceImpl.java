package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<Item> getItems(@NonNull Long userId) {
        User user = userRepository.getById(userId);
        return itemRepository.getItems(user.getId());
    }

    @Override
    public Item addNewItem(@NonNull Long userId, @NonNull Item item) {
        User user = userRepository.getById(userId);
        return itemRepository.addNewItem(user.getId(), item);
    }

    @Override
    public void deleteItem(@NonNull Long userId, @NonNull Long itemId) {
        User user = userRepository.getById(userId);
        itemRepository.deleteItem(user.getId(), itemId);
    }

    @Override
    public Item updateItem(@NonNull Long userId, @NonNull Item item) {
        User user = userRepository.getById(userId);
        return itemRepository.update(user.getId(), item);
    }

    @Override
    public Item getItem(@NonNull Long itemId) {
        return itemRepository.getItem(itemId);
    }

    @Override
    public List<Item> searchBy(@NonNull String text) {
        return itemRepository.searchBy(text);
    }
}

package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.db.UserEntity;

public class ItemMapper {

    public static Item map(ItemDto dto, Long itemId) {
        return Item.builder()
                .id(itemId)
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .build();
    }

    public static ItemDto map(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item map(ItemEntity entity) {
        return Item.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .available(entity.getAvailable())
                .build();
    }

    public static ItemEntity map(Item model, Long itemId, Long userId) {
        return ItemEntity.builder()
                .id(itemId)
                .name(model.getName())
                .description(model.getDescription())
                .user(UserEntity.builder().id(userId).build())
                .available(model.getAvailable())
                .build();
    }

    public static ItemEntity merge(Item model, ItemEntity entity) {
        return ItemEntity.builder()
                .id(entity.getId())
                .user(entity.getUser())
                .name(model.getName() == null ? entity.getName() : model.getName())
                .description(model.getDescription() == null ? entity.getDescription() : model.getDescription())
                .available(model.getAvailable() == null ? entity.getAvailable() : model.getAvailable())
                .build();
    }
}

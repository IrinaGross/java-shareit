package ru.practicum.shareit.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ShortBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.db.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ItemMapper {

    public static Item map(ItemDto dto, Long itemId) {
        return Item.builder()
                .id(itemId)
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .build();
    }

    public static ItemDto mapToDto(Item item) {
        var builder = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable());

        var last = item.getLast();
        if (last != null) {
            builder.lastBooking(ShortBookingDto.builder()
                    .id(last.getId())
                    .bookerId(last.getBooker().getId())
                    .build());
        }

        var next = item.getNext();
        if (next != null) {
            builder.nextBooking(ShortBookingDto.builder()
                    .id(next.getId())
                    .bookerId(next.getBooker().getId())
                    .build());
        }

        List<Comment> comments = item.getComments();
        if (comments != null) {
            builder.comments(comments.stream()
                    .map(CommentMapper::mapToDto)
                    .collect(Collectors.toList())
            );
        }
        return builder.build();
    }

    public static Item map(ItemEntity entity) {
        return Item.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .available(entity.getAvailable())
                .owner(UserMapper.map(entity.getUser()))
                .build();
    }

    public static ItemEntity mapToEntity(Item model, Long itemId, UserEntity user) {
        return ItemEntity.builder()
                .id(itemId)
                .name(model.getName())
                .description(model.getDescription())
                .user(user)
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

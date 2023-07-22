package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.item.db.ItemEntity;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ShortBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.db.ItemRequestEntity;
import ru.practicum.shareit.user.db.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    @NonNull
    public static Item map(ItemDto dto, Long itemId) {
        return Item.builder()
                .id(itemId)
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .requestId(dto.getRequestId())
                .build();
    }

    @NonNull
    public static ItemDto mapToDto(Item item) {
        var builder = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId());

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

    @NonNull
    public static Item map(ItemEntity entity) {
        ItemRequestEntity requestEntity = entity.getRequest();
        return Item.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .available(entity.getAvailable())
                .owner(UserMapper.map(entity.getUser()))
                .requestId(requestEntity == null ? null : requestEntity.getId())
                .build();
    }

    @NonNull
    public static ItemEntity mapToEntity(@NonNull Item model, @Nullable Long itemId, @NonNull UserEntity user, @Nullable ItemRequestEntity request) {
        return ItemEntity.builder()
                .id(itemId)
                .name(model.getName())
                .description(model.getDescription())
                .user(user)
                .available(model.getAvailable())
                .request(request)
                .build();
    }

    @NonNull
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

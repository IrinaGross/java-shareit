package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.db.ItemRequestEntity;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    @NonNull
    public static ItemRequest map(@NonNull ItemRequestDto dto) {
        return ItemRequest.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .build();
    }

    @NonNull
    public static ItemRequestDto mapToDto(@NonNull ItemRequest request) {
        var builder = ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreateAt());

        var items = request.getItems();
        if (items != null) {
            builder.items(items.stream()
                    .map(ItemMapper::mapToDto)
                    .collect(Collectors.toList()));
        }
        return builder.build();
    }

    @Nullable
    public static ItemRequestEntity mapToEntity(@Nullable ItemRequest request) {
        if (request == null) {
            return null;
        }
        var builder = ItemRequestEntity.builder()
                .id(request.getId())
                .description(request.getDescription())
                .creator(UserMapper.mapToEntity(request.getCreator(), request.getCreator().getId()))
                .date(request.getCreateAt());

        var items = request.getItems();
        if (items != null) {
            builder.items(items.stream()
                    .map(it -> {
                        var userEntity = UserMapper.mapToEntity(it.getOwner(), it.getOwner().getId());
                        return ItemMapper.mapToEntity(it, it.getId(), userEntity, null);
                    })
                    .collect(Collectors.toList())
            );
        }
        return builder.build();
    }

    @NonNull
    public static ItemRequest map(@NonNull ItemRequestEntity entity) {
        var builder = ItemRequest.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .creator(UserMapper.map(entity.getCreator()))
                .createAt(entity.getDate());

        var items = entity.getItems();
        if (items != null) {
            builder.items(items.stream()
                    .map(ItemMapper::map)
                    .collect(Collectors.toList()));
        }

        return builder.build();
    }

    @NonNull
    public static ItemRequest from(@NonNull Long requestId) {
        return ItemRequest.builder()
                .id(requestId)
                .build();
    }
}

package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class ItemRequest {
    private final Long id;
    private final String description;
    private final User creator;
    private final LocalDateTime createAt;
    private final List<Item> items;
}

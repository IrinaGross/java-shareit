package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Item {
    private final Long id;
    private final String name;
    private final String description;
    private final Long idOwner;
    private final Boolean available;
}

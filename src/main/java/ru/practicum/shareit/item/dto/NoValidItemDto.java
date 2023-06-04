package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class NoValidItemDto implements ItemDto {
    @Builder.Default
    private final Long id = null;
    private final String name;
    private final String description;
    private final Boolean available;
}
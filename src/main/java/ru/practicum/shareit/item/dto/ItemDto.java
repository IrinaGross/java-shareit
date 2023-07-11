package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Jacksonized
public class ItemDto {
    @Builder.Default
    private final Long id = null;
    @NotNull(groups = {CreateItemGroup.class})
    @NotBlank(groups = {CreateItemGroup.class})
    private final String name;
    @NotNull(groups = {CreateItemGroup.class})
    @NotBlank(groups = {CreateItemGroup.class})
    private final String description;
    @NotNull(groups = {CreateItemGroup.class})
    private final Boolean available;
}

package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Jacksonized
public class ValidItemDto implements ItemDto {
    @Builder.Default
    private final Long id = null;
    @NonNull
    @NotBlank
    private final String name;
    @NonNull
    @NotBlank
    private final String description;
    @NonNull
    private final Boolean available;
}
package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Jacksonized
public class ItemDto {
    private final Long id;

    @NotNull(groups = {CreateItemGroup.class})
    @NotBlank(groups = {CreateItemGroup.class})
    private final String name;

    @NotNull(groups = {CreateItemGroup.class})
    @NotBlank(groups = {CreateItemGroup.class})
    private final String description;

    @NotNull(groups = {CreateItemGroup.class})
    private final Boolean available;

    private final ShortBookingDto lastBooking;
    private final ShortBookingDto nextBooking;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<CommentDto> comments;
}

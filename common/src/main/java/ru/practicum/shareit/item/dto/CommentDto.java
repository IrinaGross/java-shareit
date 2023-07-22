package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@Jacksonized
public class CommentDto {
    private final Long id;

    @NotNull
    @NotBlank
    private final String text;
    private final String authorName;
    private final LocalDateTime created;
}

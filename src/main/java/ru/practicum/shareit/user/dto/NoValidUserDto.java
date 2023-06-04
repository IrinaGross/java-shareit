package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class NoValidUserDto implements UserDto {
    @Builder.Default
    private final Long id = null;
    private final String name;
    private final String email;
}

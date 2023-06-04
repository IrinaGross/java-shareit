package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@Jacksonized
public class ValidUserDto implements UserDto {
    @Builder.Default
    private final Long id = null;
    @NonNull
    @NotEmpty
    private final String name;
    @NonNull
    @Email
    private final String email;
}

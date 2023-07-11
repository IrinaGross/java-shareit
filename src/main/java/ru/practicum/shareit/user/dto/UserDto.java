package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Jacksonized
public class UserDto {
    @Builder.Default
    private final Long id = null;
    @NotNull(groups = {CreateUserGroup.class})
    @NotEmpty(groups = {CreateUserGroup.class})
    private final String name;
    @NotNull(groups = {CreateUserGroup.class})
    @Email(groups = {CreateUserGroup.class})
    private final String email;
}

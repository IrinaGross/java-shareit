package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private final Long id;

    @NotNull(groups = {CreateUserGroup.class})
    @NotEmpty(groups = {CreateUserGroup.class})
    private final String name;

    @NotNull(groups = {CreateUserGroup.class})
    @Email(groups = {CreateUserGroup.class})
    @NotEmpty(groups = {CreateUserGroup.class})
    private final String email;
}

package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.ValidUserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static User map(UserDto dto, Long userId) {
        return User.builder()
                .id(userId)
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserDto map(User user) {
        return ValidUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
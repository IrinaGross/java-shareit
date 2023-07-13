package ru.practicum.shareit.user.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.db.UserEntity;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor
public class UserMapper {

    public static User map(UserDto dto, Long userId) {
        return User.builder()
                .id(userId)
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserEntity mapToEntity(User model, Long userId) {
        return UserEntity.builder()
                .id(userId)
                .name(model.getName())
                .email(model.getEmail())
                .build();
    }

    public static User map(UserEntity entity) {
        return User.builder()
                .name(entity.getName())
                .email(entity.getEmail())
                .id(entity.getId())
                .build();
    }

    public static UserEntity merge(User model, UserEntity entity) {
        return UserEntity.builder()
                .id(entity.getId())
                .name(model.getName() == null ? entity.getName() : model.getName())
                .email(model.getEmail() == null ? entity.getEmail() : model.getEmail())
                .build();
    }
}
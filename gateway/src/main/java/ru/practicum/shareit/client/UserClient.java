package ru.practicum.shareit.client;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserClient {
    @NonNull
    ResponseEntity<Object> findAll();

    @NonNull
    ResponseEntity<Object> getUser(@NonNull Long userId);

    @NonNull
    ResponseEntity<Object> create(@NonNull UserDto user);

    @NonNull
    ResponseEntity<Object> updateUser(@NonNull Long userId, @NonNull UserDto user);

    @NonNull
    ResponseEntity<Object> deleteUser(@NonNull Long userId);
}
package ru.practicum.shareit.user.repository;

import org.springframework.lang.NonNull;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    @NonNull
    User getById(@NonNull Long userId);

    @NonNull
    User addUser(@NonNull User user);

    @NonNull
    List<User> getAllUsers();

    @NonNull
    User updateUser(@NonNull User user);

    void deleteUser(@NonNull Long userId);
}
